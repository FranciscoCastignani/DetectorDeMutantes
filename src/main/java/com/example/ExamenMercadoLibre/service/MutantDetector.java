package com.example.ExamenMercadoLibre.service;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class MutantDetector {
    private static final int SEQUENCE_LENGTH = 4;
    private static final Set<Character> VALID_BASES = Set.of('A', 'T', 'C', 'G');

    public boolean isMutant(String[] dna) {
        if(!isValidDNA(dna)) return false;
        final int n = dna.length;
        int sequenceCounter = 0;

        char[][] matrix = new char[n][];
        for (int i = 0; i < n; i++) {
            matrix[i] = dna[i].toCharArray();
        }
        for (int row = 0; row < n; row++) {
            for (int col = 0; col < n; col++) {

                // Boundary Checking (Optimización #3)
                if (col <= n - SEQUENCE_LENGTH) {
                    if (checkHorizontal(matrix, row, col)) {
                        sequenceCounter++;
                        if (sequenceCounter > 1) return true;  // Early Termination!
                    }
                }

                if (row <= n - SEQUENCE_LENGTH) {
                    if (checkVertical(matrix, row, col)) {
                        sequenceCounter++;
                        if (sequenceCounter > 1) return true;  // Early Termination!
                    }
                }

                if (row >= SEQUENCE_LENGTH - 1 && col <= n - SEQUENCE_LENGTH) {
                    if (checkRightDiagonal(matrix, row, col)) {
                        sequenceCounter++;
                        if (sequenceCounter > 1) return true; // Early Termination!
                    }
                }

                if (row >= SEQUENCE_LENGTH - 1 && col >= SEQUENCE_LENGTH - 1) {
                    if (checkLeftDiagonal(matrix, row, col)) {
                        sequenceCounter++;
                        if (sequenceCounter > 1) return true; // Early Termination!
                    }
                }

            }
        }
        return false;
    }

    private boolean isValidDNA(String[] dna){
        //Validar que no sea un arreglo vacío
        if (dna == null || dna.length == 0) return false;

        // numero de filas
        final int n = dna.length;

        for(String row: dna){
            if (row == null) return false;
            //Validar que tenga la misma cantidad de columnas y filas
            if (row.length() != n) return false;

            //Validar que los caracteres dentro de la cadena sean caracteres válidos
            for(int i = 0; i < row.length(); i++){
                if(!VALID_BASES.contains(row.charAt(i))) return false;
            }
        }
        return true;
    }

    private boolean checkHorizontal(char[][] matrix, int row, int col){
        final char base = matrix[row][col];
        return matrix[row][col + 1] == base &&
                matrix[row][col + 2] == base &&
                matrix[row][col + 3] == base;
    }

    private boolean checkVertical(char[][] matrix, int row, int col){
        final char base = matrix[row][col];
        return matrix[row+1][col] == base &&
                matrix[row+2][col] == base &&
                matrix[row+3][col] == base;
    }

    private boolean checkRightDiagonal(char[][] matrix, int row, int col){
        final char base = matrix[row][col];
        return matrix[row-1][col+1] == base &&
                matrix[row-2][col+2] == base &&
                matrix[row-3][col+3] == base;
    }

    private boolean checkLeftDiagonal(char[][] matrix, int row, int col){
        final char base = matrix[row][col];
        return matrix[row-1][col-1] == base &&
                matrix[row-2][col-2] == base &&
                matrix[row-3][col-3] == base;
    }

}

