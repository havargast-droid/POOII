package com.example.demo.security;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class Constans {

    // Spring Security
    public static final String LOGIN_URL = "/authenticate";
    public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
    public static final String TOKEN_BEARER_PREFIX = "Bearer ";
    public static final String HEADER_APIKEY = "APIKey";

    // JWT
    // IMPORTANTE: en un proyecto real esta llave debe ir en application.properties, no quemada en el codigo
    public static final String SUPER_SECRET_KEY =
        "LZTsNtWJ/MQUOJyWtODgFf4QL/dpipGeBDsnil65j+58aNfvPF2ESt/eo850lvP83k33nqdMZCSuCqTNKNQcmw==";
    public static final long TOKEN_EXPIRATION_TIME = 864_000_000; // 10 dias

    public static Key getSigningKey(String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
