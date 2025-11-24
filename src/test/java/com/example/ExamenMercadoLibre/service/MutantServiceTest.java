package com.example.ExamenMercadoLibre.service;

import com.example.ExamenMercadoLibre.entity.DnaRecord;
import com.example.ExamenMercadoLibre.repository.DnaRecordRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {
    @Mock
    private MutantDetector mutantDetector;  // Mock (simulado)

    @Mock
    private DnaRecordRepository dnaRecordRepository;  // Mock (simulado)

    @InjectMocks
    private MutantService mutantService;  // Clase bajo prueba (recibe mocks)

    private final String[] mutantDna = new String[] {"ATGCGA", "CAGTGC", "TTATGT", "AGAAGG", "CCCCTA", "TCACTG"};
    private final String[] humanDna =  new String[] {"ATGCGA", "CCGTGC", "TTATGT", "AGAAGG", "CACCTA", "TCACTG"};

    @Test
    @DisplayName("Debe analizar ADN mutante y guardarlo en DB")
    void testAnalyzeMutantDnaAndSave() {
        // ARRANGE (Preparar)
        when(dnaRecordRepository.findByDna(anyString()))
                .thenReturn(Optional.empty());  // No existe en BD
        when(mutantDetector.isMutant(mutantDna))
                .thenReturn(true);  // Es mutante
        when(dnaRecordRepository.save(any(DnaRecord.class)))
                .thenReturn(new DnaRecord());  // Guardado exitoso

        // ACT (Actuar)
        boolean result = mutantService.analyzeDna(mutantDna);

        // ASSERT (Afirmar)
        assertTrue(result);

        // VERIFY (Verificar interacciones)
        verify(mutantDetector, times(1)).isMutant(mutantDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe analizar ADN humano y guardarlo en DB")
    void testAnalyzeHumanDnaAndSave() {
        when(dnaRecordRepository.findByDna(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(humanDna))
                .thenReturn(false);  // Es humano
        when(dnaRecordRepository.save(any(DnaRecord.class)))
                .thenReturn(new DnaRecord());

        boolean result = mutantService.analyzeDna(humanDna);

        assertFalse(result);
        verify(mutantDetector, times(1)).isMutant(humanDna);
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    @DisplayName("Debe retornar resultado cacheado si el ADN ya fue analizado")
    void testReturnCachedResultForAnalyzedDna() {
        // ARRANGE
        DnaRecord cachedRecord = new DnaRecord("somehash", true);
        when(dnaRecordRepository.findByDna(anyString()))
                .thenReturn(Optional.of(cachedRecord));  // YA existe en BD

        // ACT
        boolean result = mutantService.analyzeDna(mutantDna);

        // ASSERT
        assertTrue(result);

        // VERIFY - NO debe llamar al detector ni guardar
        verify(mutantDetector, never()).isMutant(any());
        verify(dnaRecordRepository, never()).save(any());
    }

    @Test
    @DisplayName("Debe generar hash consistente para el mismo ADN")
    void testConsistentHashGeneration() {
        when(dnaRecordRepository.findByDna(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(any()))
                .thenReturn(true);

        mutantService.analyzeDna(mutantDna);
        mutantService.analyzeDna(mutantDna);  // Mismo DNA otra vez

        // Debe buscar por el mismo hash ambas veces
        verify(dnaRecordRepository, times(2)).findByDna(anyString());
    }

    @Test
    @DisplayName("Debe guardar registro con hash correcto")
    void testSavesRecordWithCorrectHash() {
        when(dnaRecordRepository.findByDna(anyString()))
                .thenReturn(Optional.empty());
        when(mutantDetector.isMutant(mutantDna))
                .thenReturn(true);

        mutantService.analyzeDna(mutantDna);

        verify(dnaRecordRepository).save(argThat(record ->
                record.getDna() != null &&
                        record.getDna().length() == 64 &&  // SHA-256 = 64 chars hex
                        record.isMutant()
        ));
    }


}
