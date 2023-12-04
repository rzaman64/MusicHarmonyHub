package com.example.groupproject;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Random;

public class HelloController {
    String username;
    String password;

    @FXML
    private Label bottomlogindisplay;

    @FXML
    private Button loginButton;

    @FXML
    private Button passwordButton;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField userNameTextField;

    @FXML
    private Button closebtn;

    @FXML
    public void btnExitHandler(MouseEvent event) {
        Button selectedUIDesigner = (Button) event.getSource();
        selectedUIDesigner.setEffect(null);
    }

    @FXML
    void btnHoverHandler(MouseEvent event) {
        Button selectedUIDesigner = (Button) event.getSource();
        selectedUIDesigner.setEffect(new Glow());
    }

    public void closebtnHandler(ActionEvent actionEvent) {
        Platform.exit();
    }

    @FXML
    void loginButtonHandler(ActionEvent event) {
        username = userNameTextField.getText().replaceAll(" ", "");
        password = passwordTextField.getText();

        if (isValidInput(username, password)) {
            showWelcomeView(username);
        }
    }

    private boolean isValidInput(String username, String password) {
        if (username.isBlank() || password.isBlank()) {
            displayError("Invalid request");
            return false;
        }

        if (username.length() <= 3) {
            displayError("Username must be more than 3 characters");
            return false;
        }

        if (username.length() >= 30) {
            displayError("Username must be less than 30 characters");
            return false;
        }

        if (password.length() <= 6) {
            displayError("Password must be more than 6 characters");
            return false;
        }

        return true;
    }

    private void showWelcomeView(String username) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("welcome-user.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 602, 451);
            Stage currentStage = (Stage) (loginButton.getScene().getWindow());
            currentStage.setTitle("Welcome");
            currentStage.setScene(scene);
            currentStage.centerOnScreen();
            currentStage.show();

            WelcomeUserController welcomeUserController = fxmlLoader.getController();
            welcomeUserController.displayUsername(username);
        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
    }

    private void displayError(String message) {
        bottomlogindisplay.setVisible(true);
        bottomlogindisplay.setText(message);
        System.out.println(message);
    }

    @FXML
    void passwordButtonHandler(ActionEvent event) {
        String upper = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lower = "abcdefghijklmnopqrstuvwxyz";
        String num = "0123456789";
        String specialChars = "<>,.?/}]{[+_-)(*&^%$#@!=";
        String combination = upper + lower + specialChars + num;
        int len = 16;

        char[] password = new char[len];
        Random r = new Random();

        for(int i = 0; i<len; i++){
            password[i] = combination.charAt(r.nextInt(combination.length()));
        }
        System.out.println("generated password: " + new String(password));
        passwordTextField.setText(String.valueOf(password));
    }

}
