
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
        byte[] keyBytes = Decoders.BASE64.decode(secret); //convierte secret a bytes
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String generarToken(String userName) {

        Instant ahora = Instant.now();
        Instant expiracion = ahora.plusMillis(EXPIRATION_TIME);

        return Jwts.builder()
                .subject(userName)
                .issuedAt(Date.from(ahora))
                .expiration(Date.from(expiracion))
                .signWith(key, Jwts.SIG.HS256)
                .compact(); //lo construye como una cadena de texto
    }

    public String obtenerUsername(String token) {

        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validarToken(String token) {

        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token); //comprueba automáticamente la fecha de expiración

            return true;

        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }
}
