package com.example.gamesudoku.Controller;

import com.example.gamesudoku.Model.JuegoSudoku;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;

public class SudokuController {
    private static final int SIZE = 6;

    @FXML
    private Button botonAyuda;

    @FXML
    private Button botonIniciarJuego;

    @FXML
    private Button botonReiniciarJuego;

    @FXML
    private GridPane gridTablero;

    private JuegoSudoku sudoku;
    private int[][] juegoResuelto;

    @FXML
    public void initialize() {
    sudoku = new JuegoSudoku();
    int[][] tableroInicial = sudoku.getTableroInicial();
    setupGridPane(tableroInicial);
    }

    @FXML
    private void iniciarNuevoJuego(){
        sudoku = new JuegoSudoku();
        sudoku.sudokuResuelto();
        juegoResuelto = sudoku.getJuegoResuelto();
        int[][] tableroInicial = sudoku.getTableroInicial();
        Alert alerta = new Alert(Alert.AlertType.INFORMATION);
        alerta.setTitle("¡Estas a punto de empezar!");
        alerta.setHeaderText(null);
        alerta.setContentText("¡Si desea iniciar da clic en aceptar.");
        alerta.showAndWait();
        setupGridPane(tableroInicial);

    }

    @FXML
    private void botonReniciarJuego (){
        sudoku = new JuegoSudoku();
        sudoku.sudokuResuelto();
        juegoResuelto = sudoku.getJuegoResuelto();
        int[][] tableroInicial = sudoku.getTableroInicial();
        setupGridPane(tableroInicial);
    }

    private void setupGridPane(int[][] tableroDeInicio) {
        for (Node node : gridTablero.getChildren()) {
            if (node instanceof TextField) {
                TextField celda = (TextField) node;

                Integer f = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(celda);

                int fila = (f != null) ? f : 0;
                int columna = (c != null) ? c : 0;

                int valor = tableroDeInicio[fila][columna];

                if (valor != 0){
                    celda.setText(String.valueOf(valor));
                    celda.setEditable(false);
                    celda.getStyleClass().add("CeldaInicial");
                }else{
                    celda.setText("");
                    celda.setEditable(true);
                }
            }
        }

    }

    private void comprobarNumero(TextField celda, String nuevoNumero){
        if (!nuevoNumero.matches("[1-6]?")){
            celda.setText(nuevoNumero.replaceAll("[^1-6]",""));
        }
    }

    private boolean VerificarJuego(){
        for (Node node : gridTablero.getChildren()){
            if (node instanceof TextField){
                TextField celda = (TextField) node;
                Integer f = GridPane.getRowIndex(node);
                Integer c = GridPane.getColumnIndex(node);

                int fila = (f != null ? f : 0);
                int columna = (c != null ? c : 0);

                String text = celda.getText();
                if (text.isEmpty() || Integer.parseInt(text) != juegoResuelto[fila][columna]){
                    return false;
                }
            }
        }
        return true;
    }
}

