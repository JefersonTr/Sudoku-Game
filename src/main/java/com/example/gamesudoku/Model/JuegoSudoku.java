package com.example.gamesudoku.Model;

import java.util.*;

public class JuegoSudoku {

    private static final int SIZE = 6;
    private static final int SUB_FILA = 2;
    private static final int SUB_Columna = 3;
    private int[][] juegoResuelto;
    private final Random random = new Random();

    public JuegoSudoku() {
        juegoResuelto = new int[SIZE][SIZE];
    }

    public boolean sudokuResuelto() {
        for (int[] row : juegoResuelto) {
            Arrays.fill(row, 0);
        }
        return resolver(0, 0);
    }

    private boolean resolver(int fila, int colum) {
        if (fila == SIZE) {
            return true;
        }

        int nextFila = (colum == SIZE - 1) ? fila + 1 : fila;
        int nextColum = (colum == SIZE - 1) ? 0 : colum + 1;

        if (juegoResuelto[fila][colum] != 0) {
            return resolver(nextFila, nextColum);
        }
        int[] numbers = {1, 2, 3, 4, 5, 6};
        mezclarLista(numbers);

        for (int num : numbers) {
            if (esValido(fila, colum,num)){
                juegoResuelto[fila][colum] = num;
                if (resolver(nextFila, nextColum)) {
                    return true;
                }
                juegoResuelto[fila][colum] = 0;
            }
        }
        return false;
    }
    private boolean esValido(int fila, int colum, int num) {
        for (int c = 0; c < SIZE; c++) {
            if (juegoResuelto[fila][c] == num)
                return false;
        }
        for (int f = 0; f < SIZE; f++) {
            if (juegoResuelto[f][colum] == num)
                return false;
        }
        int filaInicial = fila - fila % SUB_FILA;
        int columInicial = colum - colum % SUB_Columna;
        for (int f = 0; f < SUB_FILA; f++) {
            for (int c = 0; c < SUB_Columna; c++) {
                if (juegoResuelto[filaInicial + f][columInicial + c] == num)
                    return false;
            }
        }
        return true;
    }
    private void mezclarLista(int[] lista) {
        for (int i = lista.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = lista[index];
            lista[index] = lista[i];
            lista[i] = temp;
        }
    }

    public int[][] tableroInicial(){
        int[][] juegoinicial = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(juegoResuelto[i], 0, juegoinicial[i], 0, SIZE);
        }
        for (int bloqueFila = 0; bloqueFila < SIZE / SUB_FILA; bloqueFila++) {
            for (int bloquecolum = 0; bloquecolum < SIZE / SUB_Columna; bloquecolum++) {
                int filaInicial = bloqueFila * SUB_FILA;
                int columInicial = bloquecolum * SUB_Columna;

                java.util.List<int[]> bloqueceldas = new java.util.ArrayList<>();
                for (int f = 0; f < SUB_FILA; f++) {
                    for (int c = 0; c < SUB_Columna; c++) {
                        bloqueceldas.add(new int[]{filaInicial + f, columInicial + c});
                    }
                }

                java.util.Collections.shuffle(bloqueceldas);

                for (int i = 2; i < bloqueceldas.size(); i++) {
                    int f = bloqueceldas.get(i)[0];
                    int c = bloqueceldas.get(i)[1];
                    juegoinicial[f][c] = 0;
                }
            }
        }
        return juegoinicial;
    }

    public int[][] getJuegoResuelto() {
        int[][] copia = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(juegoResuelto[i], 0, copia[i], 0, SIZE);
        }
        return copia;
    }

    public int[][] getTableroInicial(){
    int[][] juegoinicial = getJuegoResuelto(); // Comienza con el tablero resuelto

        for (int bloqueFila = 0; bloqueFila < SIZE / SUB_FILA; bloqueFila++) {
            for (int bloquecolum = 0; bloquecolum < SIZE / SUB_Columna; bloquecolum++) {
                int filaInicial = bloqueFila * SUB_FILA;
                int columInicial = bloquecolum * SUB_Columna;

                java.util.List<int[]> bloqueceldas = new java.util.ArrayList<>();
                for (int f = 0; f < SUB_FILA; f++) {
                    for (int c = 0; c < SUB_Columna; c++) {
                        bloqueceldas.add(new int[]{filaInicial + f, columInicial + c});
                    }
                }

                java.util.Collections.shuffle(bloqueceldas);

                for (int i = 2; i < bloqueceldas.size(); i++) {
                    int f = bloqueceldas.get(i)[0];
                    int c = bloqueceldas.get(i)[1];
                    juegoinicial[f][c] = 0;
                }
            }
        }
        return juegoinicial;
    }
}


