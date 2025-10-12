module com.example.gamesudoku {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.gamesudoku to javafx.fxml;
    exports com.example.gamesudoku;
    exports com.example.gamesudoku.Controller;
    opens com.example.gamesudoku.Controller to javafx.fxml;
}