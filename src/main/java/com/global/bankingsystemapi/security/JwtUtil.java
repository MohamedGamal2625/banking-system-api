package com.global.bankingsystemapi.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")                          // Line 1
    private String SECRET;

    @Value("${jwt.expiration}")                      // Line 2
    private long EXPIRY;

    // ─────────────────────────────
    // CREATE token
    // ─────────────────────────────
    public String generateToken(String username, String role) {
        return Jwts.builder()
                .subject(username)
                .claims(Map.of("role", role))
                .issuedAt(new Date())
                .expiration(new Date(
                        System.currentTimeMillis()
                                + EXPIRY))
                .signWith(getKey())
                .compact();
    }

    // ─────────────────────────────
    // READ username FROM token
    // ─────────────────────────────
    public String extractUsername(String token) {
        return getClaims(token).getSubject();
    }

    // ─────────────────────────────
    // VALIDATE token
    // ─────────────────────────────
    public boolean isTokenValid(String token) {
        try {
            getClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // ─────────────────────────────
    // PRIVATE helpers
    // ─────────────────────────────
    private Claims getClaims(String token) {             // Line 12
        return Jwts.parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    private SecretKey getKey() {                         // Line 13
        return Keys.hmacShaKeyFor(
                SECRET.getBytes()
        );
    }
}
