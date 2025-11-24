package com.example.ExamenMercadoLibre.controller;

import com.example.ExamenMercadoLibre.dto.DnaRequest;
import com.example.ExamenMercadoLibre.dto.DnaResponse;
import com.example.ExamenMercadoLibre.dto.StatsResponse;
import com.example.ExamenMercadoLibre.service.MutantService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class MutantController {

    private final MutantService mutantService;

    @GetMapping("/stats")
    public StatsResponse getStats() {
        return mutantService.getStats();
    }

    @PostMapping("/mutant")
    public ResponseEntity<DnaResponse> postDNA(@RequestBody @Valid DnaRequest dnaRequest) {
        boolean isMutant = mutantService.analyzeDna(dnaRequest.dna());
        DnaResponse dnaResponse = new DnaResponse(isMutant);
        if (isMutant) {
            return ResponseEntity.ok(dnaResponse);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(dnaResponse);
        }
    }
}
