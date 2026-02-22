package com.filespark.server.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JWTService {

    private final SecretKey secretKey;

    public JWTService(@Value("${spring.data.secret}") String sessionSecret) {

        this.secretKey = Keys.hmacShaKeyFor(
                sessionSecret.getBytes(StandardCharsets.UTF_8)
        );
    }

    public String issueSessionToken(String userId) {

        long now = System.currentTimeMillis();
        long expiry = now + (7L * 24 * 60 * 60 * 1000);

        return Jwts.builder()
                .subject(userId)
                .issuedAt(new Date(now))
                .expiration(new Date(expiry))
                .claim("typ", "session")
                .signWith(secretKey)
                .compact();
    }

    public String extractUserId(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }
}