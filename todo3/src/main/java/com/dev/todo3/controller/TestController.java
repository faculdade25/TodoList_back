package com.dev.todo3.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

    @PostMapping("/public")
    public ResponseEntity<String> publicEndpoint() {
        return ResponseEntity.ok("Public endpoint works!");
    }

    @PostMapping("/register-simulado")
    public ResponseEntity<String> registerSimulado(@RequestBody Map<String, String> request) {
        return ResponseEntity.ok("Registro simulado: " + request.get("email"));
    }
}