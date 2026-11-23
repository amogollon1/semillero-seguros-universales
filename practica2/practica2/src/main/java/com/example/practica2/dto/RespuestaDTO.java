package com.example.practica2.dto;

public class RespuestaDTO {

	private String token;

    public RespuestaDTO(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}