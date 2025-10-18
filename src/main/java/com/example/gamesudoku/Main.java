package com.example.gamesudoku;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;


/**
 * Clase principal que inicializa y lanza la aplicación
 *
 * @author Jeferson Stiven Trullott
 * @author Juan Carlos Fuentes
 * @version 1.0
 * @see javafx.application.Application
 * @see javafx.stage.Stage
 */
public class Main extends Application {
    /**
     *
     * @param stage the primary stage for this application, onto which
     * the application scene can be set.
     * Applications may create other stages, if needed, but they will not be
     * primary stages.
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/com/example/gamesudoku/Sudoku.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Juego Sudoku 6X6");
        stage.show();
    }

    /**
     * llama el metodo launch() para iniciar el ciclo de JavaFX.
     * @param args
     */
    public static void main(String[] args) {launch();}
}
