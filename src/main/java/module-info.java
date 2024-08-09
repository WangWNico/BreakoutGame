module edu.rpi.cs.csci4963.java.finalproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.rpi.cs.csci4963.finalproject to javafx.fxml;
    exports edu.rpi.cs.csci4963.finalproject;
    exports edu.rpi.cs.csci4963.finalproject.model;
    opens edu.rpi.cs.csci4963.finalproject.model to javafx.fxml;
}