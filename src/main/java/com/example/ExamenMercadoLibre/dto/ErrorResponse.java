package com.example.ExamenMercadoLibre.dto;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

public record ErrorResponse(
    Timestamp timestamp,
    int status,
    String message,
    List<String> details
) {
    public static ErrorResponse of(int status, String error, List<String> detalles) {
        return new ErrorResponse(Timestamp.from(Instant.now()), status, error, detalles);
    }

    public static ErrorResponse simple(int status, String error, String detalle) {
        return new ErrorResponse(Timestamp.from(Instant.now()), status, error, List.of(detalle));
    }
}
