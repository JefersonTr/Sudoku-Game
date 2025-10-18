package com.example.gamesudoku.Controller;

import com.example.gamesudoku.Model.JuegoSudoku;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

/**
 * Clase controladora para la interfaz de usuario del juego.
 * Gestiona los eventos del usuario, el inicio del juego y el reinicio,
 * y válida las entradas que se hagan en el tablero
 *
 * @author Jeferson Stiven Trullott Rivas
 * @author Juan Carlos Fuentes
 * @version 1.0
 *
 */
public class SudokuController {
    private static final int SIZE = 6; //define el tamaño del tablero 6x6
    //Estilos
    private static final String ESTILO_DEFAULT = "";
    private static final String ESTILO_ERROR = "-fx-background-color: red; -fx-text-fill: black; -fx-border-width: 1; -fx-border-color: #A9A9A9";
    private static final String ESTILO_SUGERENCIA = "-fx-background-color: yellow; -fx-font-weight: bold; -fx-border-width: 1; -fx-border-color: #A9A9A9";
    private static final String ESTILO_FIJO = "-fx-background-color: #E0E0E0; -fx-font-weight: bold; -fx-text-fill: #333333; -fx-border-width: 1; -fx-border-color: #A9A9A9";

    @FXML
    private Button botonAyuda;

    @FXML
    private Button botonIniciarJuego;

    @FXML
    private Button botonReiniciarJuego;

    @FXML
    private GridPane gridTablero;
    /**
     * Instancia que maneja la lógica interna del juego.
     */

    @FXML
    private Label labelMensaje;

    private JuegoSudoku sudoku;

    private int ayudasUsadas = 0;

    private static final int MAX_AYUDAS = 5;

    /**
     * Matriz que almacena la solución del juego del tablero actual.
     */
    private int[][] juegoResuelto;
    private int[][] tableroActualJuego;

    /**
     * Inicializa la lógica del juego y configura el tablero visualmente.
     */
    @FXML
    public void initialize() {
        sudoku = new JuegoSudoku();

        // Estado inicial: Tablero completamente vacío.
        limpiarTablero();

        // Configuración inicial de botones
        seleccionarEstadoBotones();

        if (labelMensaje != null) {
            labelMensaje.setText("¡Bienvenido! Haz clic en JUGAR.");
        }
    }

    private void seleccionarEstadoBotones() {
        // Al inicio, solo el botón de Jugar está activo.
        botonIniciarJuego.setDisable(false);
        botonReiniciarJuego.setDisable(true);
        botonAyuda.setDisable(true);
    }

    // Limpia el tablero
    private void limpiarTablero() {
        // Se utiliza una matriz vacía para establecer el estado del juego
        tableroActualJuego = new int[SIZE][SIZE];

        for (Node node : gridTablero.getChildren()) {
            if (node instanceof TextField) {
                TextField celda = (TextField) node;
                // Limpiar texto y estilos
                celda.setText("");
                celda.setStyle(ESTILO_DEFAULT);
                // Deshabilitar edición
                celda.setEditable(false);
            }
        }
    }

    /**
     * Inicia un nuevo juego
     * Genera un nuevo tablero resuelto, muestra una alerta de inicio
     * y configura la interfaz de usuario con el tablero inicial.
     */
    @FXML
    private void iniciarNuevoJuego() {

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación");
        confirmacion.setHeaderText("¡Estas apunto de empezar!");
        confirmacion.setContentText("¡Presiona ACEPTAR si deseas iniciar!.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();

        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            sudoku = new JuegoSudoku();
            tableroActualJuego = sudoku.generarNuevoTablero(); // Genera tablero jugable con solución
            juegoResuelto = sudoku.getJuegoResuelto(); // Guarda la solución correcta

            configuracionTablero(tableroActualJuego);

            // Desactivar botón de Jugar y activar el de Reiniciar/Ayuda
            botonIniciarJuego.setDisable(true);
            botonReiniciarJuego.setDisable(false);
            ayudasUsadas = 0;
            botonAyuda.setDisable(false);

            if (labelMensaje != null) {
                labelMensaje.setText("¡A jugar! Ingresa un número (1-6).");
            }

        }
    }

    /**
     * Reinicia el juego actual, y dando la opción de generar otro juego.
     */
    @FXML
    private void reiniciarJuego() {

        Alert alertaReinicio = new Alert(Alert.AlertType.CONFIRMATION);
        alertaReinicio.setTitle("Confirmación de Reinicio");
        alertaReinicio.setHeaderText(null);
        alertaReinicio.setContentText("¿Estás seguro de que deseas limpiar el tablero y reiniciar el juego?");

        Optional<ButtonType> resultado = alertaReinicio.showAndWait();

        // Solo ejecuta la lógica de reinicio si el resultado es ACEPTAR (ButtonType.OK)
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {

            limpiarTablero(); // Tablero vacío
            seleccionarEstadoBotones(); // Activa Jugar, desactiva Reiniciar/Ayuda
            ayudasUsadas = 0;

            if (labelMensaje != null) {
                labelMensaje.setText("Tablero vacío. Haz clic en JUGAR para empezar.");
            }
        } else {
            // Si cancela, se informa al usuario.
            if (labelMensaje != null) {
                labelMensaje.setText("Reinicio cancelado. ¡Sigue jugando!");
            }
        }
    }

    /**
     * Genera una sugerencia a jugador de un número correcto.
     * Busca una celda vacía para y coloca el número correcto,
     * Solo deja pedir 5 ayudas.
     */
    @FXML
    private void pedirAyuda() {
        if (juegoResuelto == null) {
            if (labelMensaje != null)
                labelMensaje.setText("No hay juego cargado. Presiona JUGAR primero.");
            return;
        }

        // Verificar si alcanzó el máximo de ayudas
        if (ayudasUsadas >= MAX_AYUDAS) {
            if (labelMensaje != null) {
                labelMensaje.setStyle("-fx-text-fill: red");
                labelMensaje.setText("🚫 Límite de ayudas alcanzado (" + MAX_AYUDAS + "). ¡Intenta resolverlo!");
            }
            botonAyuda.setDisable(true);
            return;
        }

        // --- Recolectar todas las celdas vacías y editables ---
        List<TextField> celdasDisponibles = new ArrayList<>();

        for (Node node : gridTablero.getChildren()) {
            if (node instanceof TextField) {
                TextField celda = (TextField) node;
                Integer f = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(celda);
                int fila = (f != null) ? f : 0;
                int columna = (c != null) ? c : 0;

                if (celda.isEditable() && tableroActualJuego[fila][columna] == 0) {
                    celdasDisponibles.add(celda);
                }
            }
        }

        if (celdasDisponibles.isEmpty()) {
            if (labelMensaje != null) {
                labelMensaje.setStyle("-fx-text-fill: green");
                labelMensaje.setText("El tablero está lleno o no hay movimientos válidos para sugerir.");
            }
            return;
        }

        // --- Escoger una celda aleatoria ---
        Random random = new Random();
        TextField celdaSeleccionada = celdasDisponibles.get(random.nextInt(celdasDisponibles.size()));

        Integer f = GridPane.getRowIndex(celdaSeleccionada);
        Integer c = GridPane.getColumnIndex(celdaSeleccionada);
        int fila = (f != null) ? f : 0;
        int columna = (c != null) ? c : 0;

        // --- Colocar el número correcto según la solución ---
        int numSugerido = juegoResuelto[fila][columna];
        celdaSeleccionada.setText(String.valueOf(numSugerido));
        celdaSeleccionada.setStyle(ESTILO_SUGERENCIA);
        celdaSeleccionada.setEditable(false);

        tableroActualJuego[fila][columna] = numSugerido;
        ayudasUsadas++;

        if (labelMensaje != null) {
            labelMensaje.setStyle("-fx-text-fill: green");
            labelMensaje.setText("💡 Pista #" + ayudasUsadas + " aplicada (máx " + MAX_AYUDAS + ").");
        }

        // Si llega al máximo, desactivar botón
        if (ayudasUsadas >= MAX_AYUDAS) {
            botonAyuda.setDisable(true);
            if (labelMensaje != null) {
                labelMensaje.setStyle("-fx-text-fill: red");
                labelMensaje.setText("🚫 Límite de ayudas alcanzado. ¡Resuelve el resto tú!");
            }
        }
    }

    /**
     * Bandera para indicar si el juego termino.
     */
    private boolean juegoTerminado = false;

    /**
     * Configura el estado visual del GridPane en la interfaz.
     * Establece los números iniciales del juego y qué celdas están habilitadas para escribir
     *
     * @param tableroDeInicio //Matriz que contiene los números iniciales.
     */
    private void configuracionTablero(int[][] tableroDeInicio) {

        juegoTerminado = false;

        for (Node node : gridTablero.getChildren()) {
            if (node instanceof TextField) {
                TextField celda = (TextField) node;

                Integer f = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(celda);

                int fila = (f != null) ? f : 0;
                int columna = (c != null) ? c : 0;

                int valor = tableroDeInicio[fila][columna];

                // Limpiar estilos antes de reasignar
                celda.setStyle(ESTILO_DEFAULT);

                if (valor != 0) {
                    celda.setText(String.valueOf(valor));
                    celda.setEditable(false);
                    celda.setStyle(ESTILO_FIJO);
                } else {
                    celda.setText("");
                    celda.setEditable(true);

                    // Añadir el Change Listener para la validación en tiempo real
                    if (celda.getProperties().get("listenerAgregado") == null) {

                        celda.textProperty().addListener((observable, oldValue, newValue) -> {
                            // Restricción de entrada (solo 1-6 o vacío)
                            if (!newValue.matches("[1-6]?")) {
                                celda.setText(oldValue);
                                if (labelMensaje != null) {
                                    labelMensaje.setStyle("-fx-text-fill: red");
                                    labelMensaje.setText("❌ Error: Solo se permiten números del 1 al 6.");
                                }
                                return;
                            }

                            // Actualizar el estado del modelo local
                            int numIngresado = newValue.isEmpty() ? 0 : Integer.parseInt(newValue);
                            tableroActualJuego[fila][columna] = numIngresado;

                            if (numIngresado != 0) {
                                // Llamar a la validación de reglas del Sudoku
                                if (!sudoku.esMovimientoValido(tableroActualJuego, fila, columna, numIngresado)) {
                                    celda.setStyle(ESTILO_ERROR);
                                    if (labelMensaje != null) {
                                        labelMensaje.setStyle("-fx-text-fill: red");
                                        labelMensaje.setText("❌ Error: El número " + numIngresado + " está repetido.");
                                    }
                                } else {
                                    celda.setStyle(ESTILO_DEFAULT);
                                    if (labelMensaje != null) {
                                        labelMensaje.setStyle("-fx-text-fill: green");
                                        labelMensaje.setText("✅ Número válido. ¡Sigue así!");
                                    }

                                    // Verificar si el juego terminó (solo una vez)
                                    if (!juegoTerminado && verificarJuego()) {
                                        juegoTerminado = true;
                                        Alert finJuego = new Alert(Alert.AlertType.INFORMATION);
                                        finJuego.setTitle("¡Juego Terminado!");
                                        finJuego.setHeaderText(null);
                                        finJuego.setContentText("🎉 ¡Felicidades! Has resuelto el Sudoku correctamente.");
                                        finJuego.showAndWait();
                                        if (labelMensaje != null) {
                                            labelMensaje.setText("🎉 ¡Sudoku Resuelto!");
                                        }
                                    }
                                }
                            } else {
                                // El campo se vació (borrado)
                                celda.setStyle(ESTILO_DEFAULT);
                                if (labelMensaje != null) {
                                    labelMensaje.setText("Sigue jugando...");
                                }
                            }
                        });

                        // 🔹 Marcar que ya se agregó el listener
                        celda.getProperties().put("listenerAgregado", true);
                    }
                }
            }
        }
    }



    /**
     * Verifica si el estado actual del tablero coincide con la solución.
     *
     * @return boolean true si el tablero está completo y los numero coinciden con la solución, false en caso contrario
     */
    private boolean verificarJuego() {
        for (Node node : gridTablero.getChildren()) {
            if (node instanceof TextField) {
                TextField celda = (TextField) node;
                Integer f = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);

                int fila = (f != null ? f : 0);
                int columna = (c != null ? c : 0);

                String text = celda.getText();
                // erifica que no esté vacío
                if (text.isEmpty()) {
                    return false;
                }
                // Verifica que sea la solución final correcta (coincida con juegoResuelto)
                if (Integer.parseInt(text) != juegoResuelto[fila][columna]) {
                    return false;
                }
            }
        }
        return true;
    }
}
