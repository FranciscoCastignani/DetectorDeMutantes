package com.example.ExamenMercadoLibre.service;
import com.example.ExamenMercadoLibre.exception.DnaHashCalculationException;
import org.apache.commons.codec.digest.DigestUtils;
import com.example.ExamenMercadoLibre.dto.StatsResponse;
import com.example.ExamenMercadoLibre.entity.DnaRecord;
import com.example.ExamenMercadoLibre.repository.DnaRecordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MutantService {
    private final MutantDetector mutantDetector;
    private final DnaRecordRepository dnaRecordRepository;
    private final StatsService statsService;

    public boolean analyzeDna(String[] dna){
        String dnaSequence = String.join("", dna);
        String hash = DigestUtils.sha256Hex(dnaSequence);
        Optional<DnaRecord> existingDna = dnaRecordRepository.findByDna(hash);
        if (existingDna.isPresent()) {
            return existingDna.get().isMutant();
        }
        boolean isMutant = mutantDetector.isMutant(dna);
        DnaRecord dnaEntity = DnaRecord.builder()
                .dna(hash)
                .isMutant(isMutant)
                .build();
        dnaRecordRepository.save(dnaEntity);

        return isMutant;
    }

    public StatsResponse getStats(){
        return statsService.getStats();
    }

    private String calculateDnaHash(String dna) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(dna.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new DnaHashCalculationException("Error calculando hash", e);
        }
    }
}
