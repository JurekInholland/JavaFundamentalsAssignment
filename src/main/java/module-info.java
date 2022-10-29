module baumann.javaassignment.assignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens baumann.javaassignment.assignment to javafx.fxml;

    exports baumann.javaassignment.assignment;
    exports baumann.javaassignment.assignment.controller;
    exports baumann.javaassignment.assignment.model;
    exports baumann.javaassignment.assignment.data;


}