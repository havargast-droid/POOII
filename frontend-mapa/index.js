// Backend local donde corre tu proyecto Spring Boot (ajusta el puerto si es distinto)
const API = "http://localhost:9001";

// Servicios gratuitos de rutas por carretera (OSRM, datos de OpenStreetMap).
// Se intenta el primero y, si falla, el segundo.
const OSRM_SERVIDORES = [
  "https://routing.openstreetmap.de/routed-car/route/v1/driving/",
  "https://router.project-osrm.org/route/v1/driving/",
];

let token = null;
let apiKey = null;

// ---------- Mapa (Leaflet + OpenStreetMap, sin API Key) ----------
const centroDefault = [4.4279668, -75.2134661]; // Ibague, Tolima
const map = L.map("map").setView(centroDefault, 13);
L.tileLayer("https://tile.openstreetmap.org/{z}/{x}/{y}.png", {
  maxZoom: 19,
  attribution: "&copy; OpenStreetMap contributors",
}).addTo(map);
const capaRuta = L.layerGroup().addTo(map);

function mostrar(idElemento, texto, clase) {
  const el = document.getElementById(idElemento);
  el.textContent = texto;
  el.className = "msg " + (clase || "");
}

// ---------- 1. Iniciar sesión ----------
async function iniciarSesion() {
  const username = document.getElementById("usuario").value.trim();
  const password = document.getElementById("clave").value;
  apiKey = document.getElementById("apikey").value.trim();

  if (!username || !password || !apiKey) {
    mostrar("msgLogin", "Escribe usuario, contraseña y APIKey.", "error");
    return;
  }
  try {
    const resp = await fetch(API + "/authenticate", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ username, password }),
    });
    if (!resp.ok) {
      token = null;
      mostrar("msgLogin", "Usuario o contraseña incorrectos.", "error");
      return;
    }
    const datos = await resp.json();
    token = datos.token;
    mostrar("msgLogin", "Sesión iniciada correctamente.", "ok");
  } catch (e) {
    console.error(e);
    mostrar("msgLogin", "No se pudo conectar con el servidor (¿está corriendo la aplicación?).", "error");
  }
}

// ---------- 2. Consultar una ruta ----------
async function cargarRuta() {
  const codigoRuta = document.getElementById("codigoRuta").value.trim();
  if (!token) {
    mostrar("msgRuta", "Primero inicia sesión.", "error");
    return;
  }
  if (!codigoRuta) {
    mostrar("msgRuta", "Escribe un código de ruta.", "error");
    return;
  }

  let trayectos;
  try {
    const resp = await fetch(API + "/trayecto/ruta/" + encodeURIComponent(codigoRuta), {
      headers: { "Authorization": "Bearer " + token, "APIKey": apiKey, "Accept": "application/json" },
    });
    if (resp.status === 403 || resp.status === 401) {
      mostrar("msgRuta", "Sin permiso: revisa el APIKey o vuelve a iniciar sesión.", "error");
      return;
    }
    if (!resp.ok) {
      mostrar("msgRuta", "Error consultando la ruta (HTTP " + resp.status + ").", "error");
      return;
    }
    trayectos = await resp.json();
  } catch (e) {
    console.error(e);
    mostrar("msgRuta", "No se pudo consultar la ruta. Revisa la consola.", "error");
    return;
  }

  capaRuta.clearLayers();
  document.getElementById("paradas").innerHTML = "";
  document.getElementById("info").textContent = "";

  if (!trayectos || trayectos.length === 0) {
    mostrar("msgRuta", "No se encontraron trayectos para ese código de ruta.", "error");
    return;
  }

  const paradas = trayectos
    .filter(t => t.latitud != null && t.longitud != null)
    .sort((a, b) => a.ordenParada - b.ordenParada);

  if (paradas.length === 0) {
    mostrar("msgRuta", "Las paradas aún no tienen coordenadas. Espera a que la tarea programada las calcule (cada 90 segundos).", "error");
    return;
  }

  mostrar("msgRuta", "Ruta encontrada: " + paradas.length + " paradas.", "ok");

  // Marcadores numerados + lista de paradas debajo del mapa
  const puntos = paradas.map(t => [t.latitud, t.longitud]);
  const lista = document.getElementById("paradas");
  paradas.forEach((t, i) => {
    const rol = i === 0 ? "Inicio" : (i === paradas.length - 1 ? "Final" : "Intermedia");
    const icono = L.divIcon({
      className: "",
      html: '<div class="pin">' + t.ordenParada + "</div>",
      iconSize: [28, 28],
      iconAnchor: [14, 14],
    });
    const marcador = L.marker([t.latitud, t.longitud], { icon: icono }).addTo(capaRuta);
    const popup = document.createElement("div");
    popup.textContent = "Parada " + t.ordenParada + " (" + rol + "): " + t.ubicacion;
    marcador.bindPopup(popup);

    const li = document.createElement("li");
    li.textContent = (i + 1) + ". Parada " + t.ordenParada + " (" + rol + "): " + t.ubicacion +
      " (" + Number(t.latitud).toFixed(4) + ", " + Number(t.longitud).toFixed(4) + ")";
    lista.appendChild(li);
  });

  map.fitBounds(L.latLngBounds(puntos).pad(0.2));
  await dibujarPorCarretera(puntos);
}

// Dibuja la ruta siguiendo las calles reales. Si el servicio de rutas falla,
// se dibuja una linea punteada recta como respaldo.
async function dibujarPorCarretera(puntos) {
  if (puntos.length < 2) {
    return;
  }
  const coordenadas = puntos.map(p => p[1] + "," + p[0]).join(";"); // OSRM usa lon,lat
  for (const servidor of OSRM_SERVIDORES) {
    try {
      const resp = await fetch(servidor + coordenadas + "?overview=full&geometries=geojson");
      const datos = await resp.json();
      if (datos.code !== "Ok" || !datos.routes || datos.routes.length === 0) {
        throw new Error("Respuesta: " + datos.code);
      }
      const ruta = datos.routes[0];
      const linea = ruta.geometry.coordinates.map(c => [c[1], c[0]]); // a lat,lon
      L.polyline(linea, { color: "#1a3a6b", weight: 5, opacity: 0.85 }).addTo(capaRuta);
      map.fitBounds(L.latLngBounds(linea).pad(0.1));
      const km = (ruta.distance / 1000).toFixed(1);
      const min = Math.round(ruta.duration / 60);
      document.getElementById("info").textContent =
        "Recorrido por carretera: " + km + " km, aprox. " + min + " min en carro.";
      return;
    } catch (e) {
      console.error("Fallo el servidor de rutas " + servidor, e);
    }
  }
  L.polyline(puntos, { color: "#c0392b", weight: 3, dashArray: "8 8" }).addTo(capaRuta);
  document.getElementById("info").textContent =
    "No se pudo calcular el recorrido por carretera (revisa tu conexión a internet); se muestra una línea recta punteada entre las paradas.";
}

window.iniciarSesion = iniciarSesion;
window.cargarRuta = cargarRuta;