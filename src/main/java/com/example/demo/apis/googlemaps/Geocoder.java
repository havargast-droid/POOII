package com.example.demo.apis.googlemaps;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Geocodificador basado en Nominatim (OpenStreetMap). No requiere API Key.
 * Reglas de uso de Nominatim: maximo 1 consulta por segundo y enviar un User-Agent propio.
 * (El nombre del paquete se deja como "googlemaps" para no romper los imports existentes.)
 */
public class Geocoder {

    private static final String GEOCODING_RESOURCE = "https://nominatim.openstreetmap.org/search";

    // Nominatim exige identificar la aplicacion en el User-Agent
    private static final String USER_AGENT = "proyecto-vehiculos-poo2/1.0 (proyecto academico)";

    public Geocoder() {
    }

    public String geocodeSync(String query) throws IOException, InterruptedException {
        HttpClient httpClient = HttpClient.newHttpClient();
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String requestUri = GEOCODING_RESOURCE + "?format=json&limit=1&countrycodes=co&q=" + encodedQuery;
        HttpRequest geocodingRequest = HttpRequest.newBuilder()
            .GET()
            .uri(URI.create(requestUri))
            .header("User-Agent", USER_AGENT)
            .header("Accept", "application/json")
            .timeout(Duration.ofSeconds(8))
            .build();
        HttpResponse<String> geocodingResponse = httpClient.send(geocodingRequest,
            HttpResponse.BodyHandlers.ofString());
        if (geocodingResponse.statusCode() != 200) {
            throw new IOException("Nominatim respondio HTTP " + geocodingResponse.statusCode());
        }
        return geocodingResponse.body();
    }

    // Devuelve "latitud,longitud" como String
    public String getLatLng(String query) throws IOException, InterruptedException {
        ObjectMapper mapper = new ObjectMapper();
        String response = this.geocodeSync(query);
        JsonNode resultados = mapper.readTree(response);

        if (resultados == null || !resultados.isArray() || resultados.isEmpty()) {
            throw new IOException("Nominatim no encontro resultados para: " + query);
        }

        String latitud = resultados.get(0).get("lat").asText();
        String longitud = resultados.get(0).get("lon").asText();
        return latitud + "," + longitud;
    }
}
