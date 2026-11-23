package com.example.practica2.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.practica2.dto.RespuestaDTO;
import com.example.practica2.dto.SolicitudDTO;
import com.example.practica2.entity.Person;
import com.example.practica2.repository.PersonRepository;

@Service
@RestController
@RequestMapping("/api/noauth")
public class LoginService {

    private final PersonRepository pr;
    private final JwtService jwtService;

    public LoginService(PersonRepository pr, JwtService jwtService) {
        this.pr = pr;
        this.jwtService = jwtService;
    }

    public boolean validarCredenciales(SolicitudDTO solicitud) {
        if (solicitud == null || solicitud.getUserName() == null || solicitud.getPassword() == null) {
            return false;
        }

        Person person = pr
                .findByUserName(solicitud.getUserName())
                .orElse(null);

        if (person == null) {
            return false;
        }

        return person.getPassword().equals(solicitud.getPassword());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody SolicitudDTO solicitud) {
        if (!validarCredenciales(solicitud)) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body("Usuario o password invalido");
        }

        String token = jwtService.generarToken(solicitud.getUserName());
        return ResponseEntity.ok(new RespuestaDTO(token));
    }
}