module kent.co871.a2calculator {
    requires javafx.controls;
    requires javafx.fxml;


    opens jamie.javafx.calculator to javafx.fxml;
    exports jamie.javafx.calculator;
}