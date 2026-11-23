package com.example.practica2.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class HolaMundoService {

    private final JwtService jwtService;

    public HolaMundoService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping("/hola")
    public ResponseEntity<String> hola(
            @RequestHeader(value = "Authorization", required = false) String authHeader) {

        if (authHeader == null || authHeader.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Token no enviado");
        }

        if (!authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalido");
        }

        String token = authHeader.substring(7).trim();

        if (!jwtService.validarToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token invalido");
        }

        return ResponseEntity.ok("Hola mundo");
    }
}