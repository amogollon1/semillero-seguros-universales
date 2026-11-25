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
    private final ApiKeyService apiKeyService;

    public HolaMundoService(JwtService jwtService, ApiKeyService apiKeyService) {
        this.jwtService = jwtService;
        this.apiKeyService = apiKeyService;
    }

    @GetMapping("/hola")
    public ResponseEntity<String> hola(@RequestHeader(value = "Authorization", required = false) String headerToken,
    		@RequestHeader(value = "X-API-KEY", required = false) String headerApiKey) {

    	boolean tokenValido = false;
        boolean apiKeyValida = false;
        
        if (headerToken != null && !headerToken.isBlank()) {

            if (headerToken.startsWith("Bearer ")) {
                String token = headerToken.substring(7).trim();
                tokenValido = jwtService.validarToken(token);
            }
        }
        
        if (headerApiKey != null && !headerApiKey.isBlank()) {
            String apiKey = headerApiKey.trim();
            apiKeyValida = apiKeyService.validarApiKey(apiKey);
        }
        
        if ((headerToken == null || headerToken.isBlank()) &&
                (headerApiKey == null || headerApiKey.isBlank())) {

                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Token o API Key no enviado");
            }
        
        if (!tokenValido && !apiKeyValida) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("Token o API Key invalido");
        }

        return ResponseEntity.ok("Hola mundo");
    }
}