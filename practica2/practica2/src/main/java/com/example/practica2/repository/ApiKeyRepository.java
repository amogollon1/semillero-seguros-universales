package com.example.practica2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.practica2.entity.ApiKey;

@Repository("apiKeyRepository")
public interface ApiKeyRepository extends JpaRepository<ApiKey, Long>{
	Optional<ApiKey> findByapiKey(String apiKey);
}