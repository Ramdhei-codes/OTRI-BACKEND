package com.ucaldas.otri.api.controllers;

import com.ucaldas.otri.application.shared.exceptions.ApplicationException;
import com.ucaldas.otri.application.technologies.models.RegisterTechnologyRequest;
import com.ucaldas.otri.application.technologies.models.TechnologySummaryResponse;
import com.ucaldas.otri.application.technologies.models.ViewLevelAnswersResponse;
import com.ucaldas.otri.application.technologies.models.ViewTechnologyResponse;
import com.ucaldas.otri.application.technologies.services.TechnologiesService;
import com.ucaldas.otri.domain.technologies.entities.Answer;
import com.ucaldas.otri.domain.technologies.enums.ReadinessType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;
import java.util.List;

@RestController
@RequestMapping("/api/technologies")
@RequiredArgsConstructor

public class TechnologiesController {
    private final TechnologiesService service;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterTechnologyRequest request) {
        return ResponseEntity.ok(service.register(request));
    }

    @GetMapping("/view")
    public ResponseEntity<ViewTechnologyResponse> view(@RequestParam UUID id){
        return ResponseEntity.ok(service.view(id));
    }

    @GetMapping("/list")
    public ResponseEntity<List<TechnologySummaryResponse>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    @PostMapping("/update")
    public ResponseEntity<Map<String, String>> update(@RequestParam UUID id, @RequestBody RegisterTechnologyRequest request) {
        try {
            service.updateTechnology(id, request);
            return ResponseEntity.ok(Map.of("message", "Tecnología actualizada exitosamente"));
        } catch (ApplicationException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "No se pudo actualizar la tecnología: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Error inesperado al actualizar la tecnología"));
        }
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, String>> delete(@RequestParam UUID id) {
        try {
            service.deleteTechnology(id);
            return ResponseEntity.ok(Map.of("message", "Tecnología eliminada exitosamente"));
        } catch (ApplicationException e) {
            return ResponseEntity.badRequest().body(Map.of("message", "No se pudo eliminar la tecnología: " + e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(Map.of("message", "Error inesperado al eliminar la tecnología"));
        }
    }
    @GetMapping("/rate_level")
    public ResponseEntity<List<ViewLevelAnswersResponse>> rateLevel(
            @RequestParam UUID technologyId,
            @RequestParam int level,
            @RequestParam ReadinessType type
    ){
        return ResponseEntity.ok(service.getLevelAnswers(technologyId, level, type));
    }

    @PutMapping("/save_answers")
    public ResponseEntity<Void> saveAnswers(@RequestBody List<ViewLevelAnswersResponse> answers){
        service.updateTechnologyAnswers(answers);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/view_answers")
    public ResponseEntity<Map<Integer, List<ViewLevelAnswersResponse>>> viewTechnologyAnswers
            (
                    @RequestParam UUID technologyId,
                    @RequestParam ReadinessType type
            ){
        return ResponseEntity.ok(service.viewTechnologyAnswers(technologyId, type));
    }
}
