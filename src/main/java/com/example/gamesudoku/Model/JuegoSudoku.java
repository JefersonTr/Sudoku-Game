package com.example.gamesudoku.Model;

import java.util.*;

/**
 * Clase encargada de manejar la lógica del juego, generación del tablero resuelto,
 * la validación de movimiento y la creación de tablero iniciales parcialmente vacios.
 *
 * @author Jeferson Stiven Trullott Rivas
 * @author Juan Carlos Fuentes
 * @version 1.0
 */
public class JuegoSudoku {

    /**
     * Define el tamaño del tablero 6x6
     */
    private static final int SIZE = 6;

    /**
     * Define el número de filas en los Sub bloque que es de 2
     */
    private static final int SUB_FILA = 2;

    /**
     * Define el número de columnas en los Sub bloque que es de 3
     */
    private static final int SUB_Columna = 3;

    /**
     * Almacena la matriz del juego resuelto.
     */
    private int[][] juegoResuelto;

    /**
     * Objeto utilizado para generar tableros aleatorios.
     */
    private final Random random = new Random();

    /**
     * Constructo de la clase
     * Inicializa la matriz del tablero resuelto.
     */
    public JuegoSudoku() {
        juegoResuelto = new int[SIZE][SIZE];
    }

    /**
     * Inicia el proceso de resolución de juego.
     * Limpia el tablero, lo llena de ceros y luego llama al método de backtracking para encontrar la solución.
     * @return boolean true si pudo resolver el juego y false si no tiene solución.
     */
    public boolean sudokuResuelto() {
        for (int[] row : juegoResuelto) {
            Arrays.fill(row, 0);
        }
        return resolver(0, 0);
    }

    /**
     *
     * @param fila la Fila que se está procesando
     * @param colum la Columna que se está procesando
     * @return true si se ha completado correctamente y false si no y quiere generar otro tablero
     */
    private boolean resolver(int fila, int colum) {
        // caso base: se ha completado la ultima fila
        if (fila == SIZE) {
            return true;
        }

        //determina la siguiente cela a procesar.
        int nextFila = (colum == SIZE - 1) ? fila + 1 : fila;
        int nextColum = (colum == SIZE - 1) ? 0 : colum + 1;

        //si la celda ya tiene valor, pasa a la siguiente.
        if (juegoResuelto[fila][colum] != 0) {
            return resolver(nextFila, nextColum);
        }

        //Genera números posibles, para mezclar aleatoriamente.
        int[] numbers = {1, 2, 3, 4, 5, 6};
        mezclarLista(numbers);

        //coloca los números mezclados
        for (int num : numbers) {
            if (esValido(fila, colum,num)){
                juegoResuelto[fila][colum] = num;
                if (resolver(nextFila, nextColum)) {
                    return true;
                }
                // si no se encuentra una solución se retrocede al backtrack
                juegoResuelto[fila][colum] = 0;
            }
        }
        return false;
    }

    /**
     * Verifica si es correcta la posición del número ingresado, validando
     * La fila, la columna y el sub bloque 2x3.
     *
     * @param fila la fila a validar
     * @param colum la columna a validar
     * @param num el numero que se coloca
     * @return true si el numero es valido, false si no.
     */
    private boolean esValido(int fila, int colum, int num) {
       // Comprueba la fial
        for (int c = 0; c < SIZE; c++) {
            if (juegoResuelto[fila][c] == num)
                return false;
        }
        //Comprueba la columna
        for (int f = 0; f < SIZE; f++) {
            if (juegoResuelto[f][colum] == num)
                return false;
        }
        //Comprueba el sub bloque 2x3
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

    /**
     * Mezcla aleatoriamente los elementos de la lista usando Fisher-Yates.
     *
     * @param lista el Array de enteros a mezclar
     */
    private void mezclarLista(int[] lista) {
        for (int i = lista.length - 1; i > 0; i--) {
            int index = random.nextInt(i + 1);
            int temp = lista[index];
            lista[index] = lista[i];
            lista[i] = temp;
        }
    }

    /**
     * Crea un tablero inicial de sudoku con 2 numero por cada sub bloque 2x3.
     *
     * @return int[][] Una matriz 6x6 con valores iniciales.
     */
    public int[][] tableroInicial(){
        int[][] juegoinicial = new int[SIZE][SIZE];

        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(juegoResuelto[i], 0, juegoinicial[i], 0, SIZE);
        }
        //itera sobre cada sub bloque
        for (int bloqueFila = 0; bloqueFila < SIZE / SUB_FILA; bloqueFila++) {
            for (int bloquecolum = 0; bloquecolum < SIZE / SUB_Columna; bloquecolum++) {
                int filaInicial = bloqueFila * SUB_FILA;
                int columInicial = bloquecolum * SUB_Columna;

                java.util.List<int[]> bloqueceldas = new java.util.ArrayList<>();
                //llena la lista con toda las coordenadas del bloque
                for (int f = 0; f < SUB_FILA; f++) {
                    for (int c = 0; c < SUB_Columna; c++) {
                        bloqueceldas.add(new int[]{filaInicial + f, columInicial + c});
                    }
                }

                java.util.Collections.shuffle(bloqueceldas);
                //oculta los números dejando ver solo 2 por bloque 2x3
                for (int i = 2; i < bloqueceldas.size(); i++) {
                    int f = bloqueceldas.get(i)[0];
                    int c = bloqueceldas.get(i)[1];
                    juegoinicial[f][c] = 0;
                }
            }
        }
        return juegoinicial;
    }

    /**
     * Obtiene una copia del tablero resuelto.
     * @return int[][] una copia del tablero resuelto.
     */
    public int[][] getJuegoResuelto() {
        int[][] copia = new int[SIZE][SIZE];
        for (int i = 0; i < SIZE; i++) {
            System.arraycopy(juegoResuelto[i], 0, copia[i], 0, SIZE);
        }
        return copia;
    }

    /**
     *
     * @return
     */

    public int[][] getTableroInicial(){
    int[][] juegoinicial = getJuegoResuelto(); // Comienza con una copia del tablero resuelto

        //itera sobre cada sub bloque 2x3
        for (int bloqueFila = 0; bloqueFila < SIZE / SUB_FILA; bloqueFila++) {
            for (int bloquecolum = 0; bloquecolum < SIZE / SUB_Columna; bloquecolum++) {
                int filaInicial = bloqueFila * SUB_FILA;
                int columInicial = bloquecolum * SUB_Columna;

                java.util.List<int[]> bloqueceldas = new java.util.ArrayList<>();
                //llena la lista con las coordenadas del bloque
                for (int f = 0; f < SUB_FILA; f++) {
                    for (int c = 0; c < SUB_Columna; c++) {
                        bloqueceldas.add(new int[]{filaInicial + f, columInicial + c});
                    }
                }

                java.util.Collections.shuffle(bloqueceldas);
                //oculta las celdas y deja solo 2 por cada sub bloque 2x3
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


