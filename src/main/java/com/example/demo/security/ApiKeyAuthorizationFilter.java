package com.example.demo.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.demo.entities.Usuario;
import com.example.demo.repository.UsuarioRepository;

import java.io.IOException;

import static com.example.demo.security.Constans.HEADER_APIKEY;
import static com.example.demo.security.Constans.LOGIN_URL;

// Se ejecuta DESPUES del JWTAuthorizationFilter: valida que el APIKey del header
// corresponda al usuario que ya quedo autenticado por el token.
@Component
public class ApiKeyAuthorizationFilter extends OncePerRequestFilter {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // No aplica a /authenticate ni a los servicios publicos
        if (path.startsWith(LOGIN_URL) || path.startsWith("/public")
                || path.startsWith("/swagger-ui") || path.startsWith("/api-docs") || path.startsWith("/v3/api-docs")) {
            filterChain.doFilter(request, response);
            return;
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String apiKeyHeader = request.getHeader(HEADER_APIKEY);
            String login = authentication.getName();

            Usuario usuario = usuarioRepository.findByLogin(login).orElse(null);

            if (usuario == null || apiKeyHeader == null || !apiKeyHeader.equals(usuario.getApikey())) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "APIKey invalido o ausente");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
