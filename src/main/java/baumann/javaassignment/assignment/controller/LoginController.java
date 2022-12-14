package baumann.javaassignment.assignment.controller;

import baumann.javaassignment.assignment.LibraryApplication;

import baumann.javaassignment.assignment.data.Database;
import baumann.javaassignment.assignment.model.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Logger;

public class LoginController {

    private final Logger log = Logger.getLogger(this.getClass().getName());


    @FXML
    public TextField username;

    @FXML
    public PasswordField password;

    @FXML
    public VBox vBox;

    @FXML
    public Button btnLogin;

    @FXML
    public Label feedback;


    private final Database database;

    public LoginController(Database database) {
        this.database = database;
    }

    public void onLoginButtonClick() {

        String name = username.getText();

        User user = database.getUserByName(name);

        if (user == null) {
            feedback.setText("User not found.");
            return;
        }

        if (!user.validatePassword(password.getText())) {
            feedback.setText("Invalid password.");
            return;
        }

        loadScene("main-view.fxml", new MainViewController(user, database));
    }

    private void loadScene(String sceneName, IController controller) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(LibraryApplication.class.getResource(sceneName));
            fxmlLoader.setController(controller);
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            scene.getStylesheets().add(Objects.requireNonNull(LibraryApplication.class.getResource("css/style.css")).toExternalForm());
            Stage window = (Stage) vBox.getScene().getWindow();
            window.setOnCloseRequest(windowEvent -> controller.shutdown());
            window.setTitle("Library system");
            window.setScene(scene);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void onInputChanged() {
        feedback.setText("");

        // Disable login button if username or password is empty
        btnLogin.setDisable(username.getText().isEmpty() || password.getText().isEmpty());
    }
}
