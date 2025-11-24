package com.example.ExamenMercadoLibre.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "dna_records", indexes = {
        // Definimos el índice para la búsqueda rápida por hash
        @Index(name = "idx_dna_hash", columnList = "dna_hash"),
        // Definimos el índice para filtrar mutantes vs humanos rápidamente
        @Index(name = "idx_is_mutant", columnList = "is_mutant")
})
public class DnaRecord  implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;

    @Column(name = "dna_hash", unique = true, nullable = false)
    private String dna;

    @Column(name = "is_mutant")
    private boolean isMutant;

    @CreationTimestamp
    private Timestamp created_at;

    public DnaRecord (String dna, boolean isMutant) {
        this.dna = dna;
        this.isMutant = isMutant;
    }
}
