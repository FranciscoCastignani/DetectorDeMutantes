package com.example.ExamenMercadoLibre.service;

import com.example.ExamenMercadoLibre.dto.StatsResponse;
import com.example.ExamenMercadoLibre.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {
    @Mock
    private MutantDetector mutantDetector;  // Mock (simulado)

    @Mock
    private DnaRecordRepository dnaRecordRepository;  // Mock (simulado)

    @InjectMocks
    private StatsService statsService;  // Clase bajo prueba (recibe mocks)

    @Test
    @DisplayName("Debe calcular estadísticas correctamente")
    void testGetStatsWithData() {
        // ARRANGE
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        // ACT
        StatsResponse stats = statsService.getStats();

        // ASSERT
        assertEquals(40, stats.countMutantDna());
        assertEquals(100, stats.countHumanDna());
        assertEquals(0.4, stats.ratio(), 0.001);  // 40/100 = 0.4
    }

        @Test
        @DisplayName("Debe retornar ratio 0 cuando no hay humanos")
        void testGetStatsWithNoHumans() {
            when(dnaRecordRepository.countByIsMutant(true)).thenReturn(10L);
            when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

            StatsResponse stats = statsService.getStats();

            assertEquals(10, stats.countMutantDna());
            assertEquals(0, stats.countHumanDna());
            assertEquals(0, stats.ratio(), 0.001);  // Caso especial
        }

    @Test
    @DisplayName("Debe retornar ratio 0 cuando no hay datos")
    void testGetStatsWithNoData() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(0L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(0L);

        StatsResponse stats = statsService.getStats();

        assertEquals(0, stats.countMutantDna());
        assertEquals(0, stats.countHumanDna());
        assertEquals(0.0, stats.ratio(), 0.001);
    }

    @Test
    @DisplayName("Debe calcular ratio con decimales correctamente")
    void testGetStatsWithDecimalRatio() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(1L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(3L);

        StatsResponse stats = statsService.getStats();

        assertEquals(1, stats.countMutantDna());
        assertEquals(3, stats.countHumanDna());
        assertEquals(0.333, stats.ratio(), 0.001);  // 1/3 = 0.333...
    }

    @Test
    @DisplayName("Debe retornar ratio 1.0 cuando hay igual cantidad")
    void testGetStatsWithEqualCounts() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(50L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(50L);

        StatsResponse stats = statsService.getStats();

        assertEquals(50, stats.countMutantDna());
        assertEquals(50, stats.countHumanDna());
        assertEquals(1.0, stats.ratio(), 0.001);  // 50/50 = 1.0
    }

    @Test
    @DisplayName("Debe manejar grandes cantidades de datos")
    void testGetStatsWithLargeNumbers() {
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(1000000L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(2000000L);

        StatsResponse stats = statsService.getStats();

        assertEquals(1000000, stats.countMutantDna());
        assertEquals(2000000, stats.countHumanDna());
        assertEquals(0.5, stats.ratio(), 0.001);  // 1M / 2M = 0.5
    }

}
