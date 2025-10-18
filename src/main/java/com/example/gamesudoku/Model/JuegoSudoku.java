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

    public boolean esMovimientoValido(int[][] tableroActual, int fila, int colum, int num) {

        // Verificar Fila (ignorando la celda actual)
        for (int c = 0; c < SIZE; c++) {
            if (c != colum && tableroActual[fila][c] == num) {
                return false;
            }
        }

        // Verificar Columna (ignorando la celda actual)
        for (int f = 0; f < SIZE; f++) {
            if (f != fila && tableroActual[f][colum] == num) {
                return false;
            }
        }

        // Verificar Subcuadrícula (Bloque 2x3, ignorando la celda actual)
        int filaInicial = fila - fila % SUB_FILA;
        int columInicial = colum - colum % SUB_Columna;
        for (int f = 0; f < SUB_FILA; f++) {
            for (int c = 0; c < SUB_Columna; c++) {
                int currentFila = filaInicial + f;
                int currentColum = columInicial + c;

                if (currentFila != fila || currentColum != colum) {
                    if (tableroActual[currentFila][currentColum] == num) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public Optional<Integer> sugerirNumero(int[][] tableroActual, int fila, int colum) {
        int[] numbers = {1, 2, 3, 4, 5, 6};

        mezclarLista(numbers);

        // Guardar el valor actual (debería ser 0)
        int valorOriginal = tableroActual[fila][colum];

        for (int num : numbers) {
            // Se simula la colocación del número para poder usar la validación
            tableroActual[fila][colum] = num;

            //Validar
            if (esMovimientoValido(tableroActual, fila, colum, num)) {
                tableroActual[fila][colum] = valorOriginal; // Restaurar la celda antes de salir
                return Optional.of(num);
            }
            tableroActual[fila][colum] = valorOriginal; // Restaurar la celda antes de probar el siguiente
        }

        return Optional.empty();
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

    public int[][] getTableroInicial() {
        // Intentos máximos para encontrar una configuración con solución única
        final int MAX_ATTEMPTS = 2000;

        // Obtenemos la solución completa (copia)
        // getJuegoResuelto() ya te devuelve una copia del juegoResuelto
        for (int attempt = 0; attempt < MAX_ATTEMPTS; attempt++) {
            int[][] juegoinicial = getJuegoResuelto();

            // Para cada bloque 2x3, barajamos sus celdas y mantenemos solo 2
            for (int bloqueFila = 0; bloqueFila < SIZE / SUB_FILA; bloqueFila++) {
                for (int bloquecolum = 0; bloquecolum < SIZE / SUB_Columna; bloquecolum++) {
                    int filaInicial = bloqueFila * SUB_FILA;
                    int columInicial = bloquecolum * SUB_Columna;

                    List<int[]> bloqueceldas = new ArrayList<>();
                    for (int f = 0; f < SUB_FILA; f++) {
                        for (int c = 0; c < SUB_Columna; c++) {
                            bloqueceldas.add(new int[]{filaInicial + f, columInicial + c});
                        }
                    }

                    // Mezclar usando tu Random existente
                    Collections.shuffle(bloqueceldas, random);

                    // Mantener solo las dos primeras posiciones del bloque; el resto se pone en 0
                    for (int i = 2; i < bloqueceldas.size(); i++) {
                        int f = bloqueceldas.get(i)[0];
                        int c = bloqueceldas.get(i)[1];
                        juegoinicial[f][c] = 0;
                    }
                }
            }

            // Comprobar unicidad de la solución para la configuración completa
            // (tieneUnicaSolucion ya existe en tu clase y recibe un tablero)
            if (tieneUnicaSolucion(juegoinicial)) {
                return juegoinicial; // perfecto: cumple 2 por bloque y solución única
            }

            // Si no es única, volvemos a intentar con otra baraja (sigue el loop)
        }

        // Si agotamos intentos, como fallback devolvemos la última configuración generada
        // (aunque no tenga unicidad garantizada). Si prefieres, puedes lanzar excepción.
        // Aquí devolvemos un tablero con 2 por bloque (último intento).
        int[][] fallback = getJuegoResuelto();
        for (int bloqueFila = 0; bloqueFila < SIZE / SUB_FILA; bloqueFila++) {
            for (int bloquecolum = 0; bloquecolum < SIZE / SUB_Columna; bloquecolum++) {
                int filaInicial = bloqueFila * SUB_FILA;
                int columInicial = bloquecolum * SUB_Columna;

                List<int[]> bloqueceldas = new ArrayList<>();
                for (int f = 0; f < SUB_FILA; f++) {
                    for (int c = 0; c < SUB_Columna; c++) {
                        bloqueceldas.add(new int[]{filaInicial + f, columInicial + c});
                    }
                }
                Collections.shuffle(bloqueceldas, random);
                for (int i = 2; i < bloqueceldas.size(); i++) {
                    int f = bloqueceldas.get(i)[0];
                    int c = bloqueceldas.get(i)[1];
                    fallback[f][c] = 0;
                }
            }
        }
        return fallback;
    }

    private boolean tieneUnicaSolucion(int[][] tablero) {
        return contarSoluciones(tablero, 0, 0, 0) == 1;
    }

    private int contarSoluciones(int[][] tablero, int fila, int colum, int contador) {
        if (contador > 1) return contador; // más de una solución → salir
        if (fila == SIZE) return contador + 1;

        int nextFila = (colum == SIZE - 1) ? fila + 1 : fila;
        int nextColum = (colum == SIZE - 1) ? 0 : colum + 1;

        if (tablero[fila][colum] != 0) {
            return contarSoluciones(tablero, nextFila, nextColum, contador);
        }

        for (int num = 1; num <= SIZE; num++) {
            if (esValidoEnTablero(tablero, fila, colum, num)) {
                tablero[fila][colum] = num;
                contador = contarSoluciones(tablero, nextFila, nextColum, contador);
                tablero[fila][colum] = 0;
            }
        }
        return contador;
    }

    private boolean esValidoEnTablero(int[][] tablero, int fila, int colum, int num) {
        for (int c = 0; c < SIZE; c++) {
            if (tablero[fila][c] == num) return false;
        }
        for (int f = 0; f < SIZE; f++) {
            if (tablero[f][colum] == num) return false;
        }
        int filaInicial = fila - fila % SUB_FILA;
        int columInicial = colum - colum % SUB_Columna;
        for (int f = 0; f < SUB_FILA; f++) {
            for (int c = 0; c < SUB_Columna; c++) {
                if (tablero[filaInicial + f][columInicial + c] == num)
                    return false;
            }
        }
        return true;
    }

    /**
     * Genera un nuevo tablero resuelto y retorna un tablero inicial con celdas ocultas.
     * Se usa para garantizar que cada juego tiene solución válida.
     */
    public int[][] generarNuevoTablero() {
        sudokuResuelto(); // Genera un nuevo tablero resuelto con backtracking
        return getTableroInicial(); // Crea el tablero inicial con espacios vacíos
    }
}


