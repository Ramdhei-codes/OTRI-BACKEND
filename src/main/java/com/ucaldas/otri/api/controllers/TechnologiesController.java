package com.ucaldas.otri.api.controllers;

import com.ucaldas.otri.application.technologies.models.RegisterTechnologyRequest;
import com.ucaldas.otri.application.technologies.models.ViewTechnologyResponse;
import com.ucaldas.otri.application.technologies.services.TechnologiesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("api/technologies")
@RequiredArgsConstructor
public class TechnologiesController {
    private final TechnologiesService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(RegisterTechnologyRequest request){
        return ResponseEntity.ok(service.register(request));
    }

    @GetMapping
    public ResponseEntity<ViewTechnologyResponse> view(UUID id){
        return ResponseEntity.ok(service.view(id));
    }
}
