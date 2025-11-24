package com.example.ExamenMercadoLibre.dto;

import com.example.ExamenMercadoLibre.validation.ValidDnaSequence;


public record DnaRequest(
        @ValidDnaSequence
        String[] dna
) { }
