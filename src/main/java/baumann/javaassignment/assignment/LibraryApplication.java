package baumann.javaassignment.assignment;

import baumann.javaassignment.assignment.controller.LoginController;
import baumann.javaassignment.assignment.data.Database;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;


public class LibraryApplication extends Application {
    private final Database database = new Database();

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource("login-view.fxml"));
        fxmlLoader.setController(new LoginController(database));
        Scene scene = new Scene(fxmlLoader.load(), 320, 280);
        stage.setTitle("Login");
        scene.getStylesheets().add(Objects.requireNonNull(LibraryApplication.class.getResource("css/style.css")).toExternalForm());
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}