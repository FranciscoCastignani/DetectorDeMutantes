package com.example.ExamenMercadoLibre.dto;

public record StatsResponse(
        long countMutantDna,
        long countHumanDna,
        double ratio
) {
}
