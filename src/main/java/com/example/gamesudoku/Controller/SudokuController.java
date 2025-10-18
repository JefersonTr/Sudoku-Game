package com.example.gamesudoku.Controller;

import com.example.gamesudoku.Model.JuegoSudoku;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

import java.util.Optional;

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
    private JuegoSudoku sudoku;

    /**
     * Matriz que almacena la solución del juego del tablero actual.
     */
    private int[][] juegoResuelto;

    /**
     * Inicializa la lógica del juego y configura el tablero visualmente.
     */
    @FXML
    public void initialize() {
    sudoku = new JuegoSudoku();
    int[][] tableroInicial = sudoku.getTableroInicial();
    setupGridPane(tableroInicial);
    }

    /**
     * Inicia un nuevo juego
     * Genera un nuevo tablero resuelto, muestra una alerta de inicio
     * y configura la interfaz de usuario con el tablero inicial.
     */
    @FXML
    private void iniciarNuevoJuego(){

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmación");
        confirmacion.setHeaderText("¡Estas apunto de empezar!");
        confirmacion.setContentText("¡Presiona ACEPTAR si deseas iniciar!.");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK){
        sudoku = new JuegoSudoku();
        sudoku.sudokuResuelto();
        juegoResuelto = sudoku.getJuegoResuelto();
        int[][] tableroInicial = sudoku.getTableroInicial();
        setupGridPane(tableroInicial);

        }
    }

    /**
     * Reinicia el juego actual, generando un nuevo tablero y solución
     */
    @FXML
    private void botonReniciarJuego (){
        sudoku = new JuegoSudoku();
        sudoku.sudokuResuelto();
        juegoResuelto = sudoku.getJuegoResuelto();
        int[][] tableroInicial = sudoku.getTableroInicial();
        setupGridPane(tableroInicial);
    }

    /**
     * Configura el estado visual del GridPane en la interfaz.
     * Establece los números iniciales del juego y qué celdas están habilitadas para escribir
     * @param tableroDeInicio //Matriz que contiene los números iniciales.
     */
    private void setupGridPane(int[][] tableroDeInicio) {
        for (Node node : gridTablero.getChildren()) {
            if (node instanceof TextField) {
                TextField celda = (TextField) node;

                //obtiene las coordenadas de las celdas
                Integer f = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(celda);

                int fila = (f != null) ? f : 0;
                int columna = (c != null) ? c : 0;

                int valor = tableroDeInicio[fila][columna];

                if (valor != 0){
                    //fija el número inicial no editable
                    celda.setText(String.valueOf(valor));
                    celda.setEditable(false);
                    celda.getStyleClass().add("CeldaInicial");
                }else{
                    //genera las celdas vacía y editable
                    celda.setText("");
                    celda.setEditable(true);

                    celda.textProperty().addListener((observable, oldValue, newValue) -> {
                        comprobarNumero(celda, newValue);
                    });
                }
            }
        }
    }

    /**
     *
     * @param celda // es el textField que se está modificando
     * @param nuevoNumero // lo que ingresa el usuario
     */
    private void comprobarNumero(TextField celda, String nuevoNumero){
        // Elimina cualquier carácter que no sea 1-6
        String filtrado = nuevoNumero.replaceAll("[^1-6]", "");

        // No permite más de 1 carácter, corta al primero
        if (filtrado.length() > 1) {
            filtrado = filtrado.substring(0, 1);
        }

        // Actualiza el contenido de la celda
        celda.setText(filtrado);
    }

    /**
     * Verifica si el estado actual del tablero coincide con la solución.
     * @return boolean true si el tablero está completo y los numero coinciden con la solución, false en caso contrario
     */
    private boolean VerificarJuego(){
        for (Node node : gridTablero.getChildren()){
            if (node instanceof TextField){
                TextField celda = (TextField) node;
                Integer f = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);

                int fila = (f != null ? f : 0);
                int columna = (c != null ? c : 0);

                String text = celda.getText();
                // verifica si la celda está vacía (no completo)
                // verifica si el numero no coincide con la solución
                if (text.isEmpty() || Integer.parseInt(text) != juegoResuelto[fila][columna]){
                    return false;
                }
            }
        }
        return true;
    }
}

