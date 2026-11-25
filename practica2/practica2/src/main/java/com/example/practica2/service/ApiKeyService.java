package com.example.practica2.service;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.practica2.entity.ApiKey;
import com.example.practica2.repository.ApiKeyRepository;

@Service
public class ApiKeyService {
	
	private static final long EXPIRATION_TIME = 1;
	private final ApiKeyRepository aKr;
	
	public ApiKeyService(ApiKeyRepository aKr) {
		this.aKr = aKr;
	}

	public String generarApiKey(String userName) {
		if (userName == null || userName.isBlank()) {
            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
        }
		
		LocalDateTime ahora = LocalDateTime.now();
        String valorKey = UUID.randomUUID().toString();

        ApiKey ak = new ApiKey();
        ak.setApiKey(valorKey);
        ak.setUserName(userName);
        ak.setFechaEmision(ahora);
        ak.setFechaExpiracion(ahora.plusMinutes(EXPIRATION_TIME));

        aKr.save(ak);
		return valorKey;
	}
	
	public boolean validarApiKey(String apiKey) {

        if (apiKey == null || apiKey.isBlank()) {
            return false;
        }

        ApiKey llave = aKr.findByapiKey(apiKey).orElse(null);

        if (llave == null) {
            return false;
        }
        
        LocalDateTime ahora = LocalDateTime.now();

        if (llave.getFechaEmision() == null) {
            return false;
        }

        return llave.getFechaEmision()
                .plusMinutes(1)
                .isAfter(ahora);
    }
}