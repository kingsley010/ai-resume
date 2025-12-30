package com.example.airesume.security;

import io.github.cdimascio.dotenv.Dotenv;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final Key key;
    private final long expirationMs;

    // Inject the Dotenv bean created in AppConfig
    public JwtTokenProvider(Dotenv dotenv) {
        // Retrieve values from .env
        String secret = dotenv.get("JWT_SECRET");
        String expirationStr = dotenv.get("JWT_EXPIRATION_MS");

        // Validation to prevent startup if security config is missing
        if (secret == null || secret.length() < 32) {
            throw new IllegalStateException("JWT_SECRET must be at least 32 characters long and present in .env");
        }

        this.key = Keys.hmacShaKeyFor(secret.getBytes());
        this.expirationMs = (expirationStr != null) ? Long.parseLong(expirationStr) : 3600000; // Default 1 hour
    }

    public String generateToken(String subject) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }
}
