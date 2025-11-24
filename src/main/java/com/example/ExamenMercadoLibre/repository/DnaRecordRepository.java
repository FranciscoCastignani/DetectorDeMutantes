package com.example.ExamenMercadoLibre.repository;

import com.example.ExamenMercadoLibre.entity.DnaRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DnaRecordRepository extends JpaRepository<DnaRecord, Long>{
    Optional<DnaRecord> findByDna(String dna);
    long countByIsMutant(boolean isMutant);
}
