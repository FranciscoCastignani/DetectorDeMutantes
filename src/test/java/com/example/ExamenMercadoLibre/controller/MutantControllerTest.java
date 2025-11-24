package com.example.ExamenMercadoLibre.controller;

import com.example.ExamenMercadoLibre.dto.DnaRequest;
import com.example.ExamenMercadoLibre.dto.StatsResponse;
import com.example.ExamenMercadoLibre.service.MutantService;
import com.example.ExamenMercadoLibre.service.StatsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@WebMvcTest(MutantController.class)
public class MutantControllerTest {
    @Autowired
    private MockMvc mockMvc;  // Simula requests HTTP

    @Autowired
    private ObjectMapper objectMapper;  // Convierte objetos a JSON

    @MockitoBean  // Mock en contexto de Spring
    private MutantService mutantService;

    @MockitoBean
    private StatsService statsService;

    @Test
    @DisplayName("POST /mutant debe retornar 200 OK para ADN mutante")
    void testCheckMutantReturns200ForMutant() throws Exception {
        // ARRANGE
        String[] mutantDna = {
                "ATGCGA", "CAGTGC", "TTATGT",
                "AGAAGG", "CCCCTA", "TCACTG"
        };
        DnaRequest request = new DnaRequest(mutantDna);

        when(mutantService.analyzeDna(any(String[].class)))
                .thenReturn(true);  // Mock: es mutante

        // ACT & ASSERT
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());  // 200 OK
    }

    @Test
    @DisplayName("POST /mutant debe retornar 403 Forbidden para ADN humano")
    void testCheckMutantReturns403ForHuman() throws Exception {
        String[] humanDna = {
                "ATGCGA", "CAGTGC", "TTATTT",
                "AGACGG", "GCGTCA", "TCACTG"
        };
        DnaRequest request = new DnaRequest(humanDna);

        when(mutantService.analyzeDna(any(String[].class)))
                .thenReturn(false);  // Mock: es humano

        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isForbidden());  // 403 Forbidden
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para ADN nulo")
    void testCheckMutantReturns400ForNullDna() throws Exception {
        DnaRequest request = new DnaRequest(null);  // DNA nulo

        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());  // 400 Bad Request
    }

    @Test
    @DisplayName("POST /mutant debe retornar 400 Bad Request para ADN vacío")
    void testCheckMutantReturns400ForEmptyDna() throws Exception {
        DnaRequest request = new DnaRequest(new String[]{});  // Array vacío

        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isBadRequest());  // 400 Bad Request
    }

    @Test
    @DisplayName("GET /stats debe retornar estadísticas correctamente")
    void testGetStatsReturnsCorrectData() throws Exception {
        // ARRANGE
        StatsResponse statsResponse = new StatsResponse(40, 100, 0.4);
        when(mutantService.getStats()).thenReturn(statsResponse);

        // ACT & ASSERT
        mockMvc.perform(
                        get("/stats")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print()) // <--- ESTO TE MOSTRARÁ EL JSON EN CONSOLA
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countMutantDna").value(40))
                .andExpect(jsonPath("$.countHumanDna").value(100))
                .andExpect(jsonPath("$.ratio").value(0.4));
    }

    @Test
    @DisplayName("GET /stats debe retornar 200 OK incluso sin datos")
    void testGetStatsReturns200WithNoData() throws Exception {
        StatsResponse statsResponse = new StatsResponse(0, 0, 0.0);
        when(mutantService.getStats()).thenReturn(statsResponse);

        mockMvc.perform(
                        get("/stats")
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.countMutantDna").value(0))
                .andExpect(jsonPath("$.countHumanDna").value(0))
                .andExpect(jsonPath("$.ratio").value(0.0));
    }

    @Test
    @DisplayName("POST /mutant debe rechazar request sin body")
    void testCheckMutantRejectsEmptyBody() throws Exception {
        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)
                        // NO se incluye .content() → body vacío
                )
                .andExpect(status().isBadRequest());  // 400 Bad Request
    }

    @Test
    @DisplayName("POST /mutant debe aceptar Content-Type application/json")
    void testCheckMutantAcceptsJsonContentType() throws Exception {
        String[] mutantDna = {
                "ATGCGA", "CAGTGC", "TTATGT",
                "AGAAGG", "CCCCTA", "TCACTG"
        };
        DnaRequest request = new DnaRequest(mutantDna);

        when(mutantService.analyzeDna(any(String[].class)))
                .thenReturn(true);

        mockMvc.perform(
                        post("/mutant")
                                .contentType(MediaType.APPLICATION_JSON)  // ← Importante
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk());
    }

}
