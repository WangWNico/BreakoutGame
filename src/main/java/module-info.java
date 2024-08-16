module edu.rpi.cs.csci4963.java.finalproject {
    requires javafx.controls;
    requires javafx.fxml;


    opens edu.rpi.cs.csci4963.finalproject to javafx.fxml;
    exports edu.rpi.cs.csci4963.finalproject;
    exports edu.rpi.cs.csci4963.finalproject.model;
    exports edu.rpi.cs.chane5.networking.connection;
    exports edu.rpi.cs.chane5.networking.commands;
    exports edu.rpi.cs.chane5;
    opens edu.rpi.cs.csci4963.finalproject.model to javafx.fxml;
    exports edu.rpi.cs.csci4963.finalproject.commands;
    opens edu.rpi.cs.csci4963.finalproject.commands to javafx.fxml;
}