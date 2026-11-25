
package com.example.practica2.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private static final long EXPIRATION_TIME = 60_000;

    private final SecretKey key;

    public JwtService(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarToken(String userName) {
    	if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
    	
        Instant ahora = Instant.now();
        Instant expiracion = ahora.plusMillis(EXPIRATION_TIME);

        return Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(expiracion))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public boolean validarToken(String token) {

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
