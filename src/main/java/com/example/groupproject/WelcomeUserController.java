package com.example.groupproject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.Glow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;

public class WelcomeUserController {

    @FXML
    private ImageView chillgenreimg;
    @FXML
    private Button continueBtn;
    @FXML
    private ImageView countrygenreimg;
    @FXML
    private ImageView hiphogenreimg;
    @FXML
    private ImageView moodgenreimg;
    @FXML
    private ImageView partygenreimg;
    @FXML
    private Label usernamelabel;
    @FXML
    private ImageView wktgenreimg;

    @FXML
    void imgHoverHandler(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        applyGlowEffect(imageView);
        imageView.getStyleClass().add("image-hover");
    }

    @FXML
    void imgExitHandler(MouseEvent event) {
        ImageView imageView = (ImageView) event.getSource();
        removeGlowEffect(imageView);
        imageView.getStyleClass().remove("image-hover");
    }

    @FXML
    public void btnHoverHandler(MouseEvent event) {
        Button selectedUIDesigner = (Button) event.getSource();
        selectedUIDesigner.setEffect(new Glow());
    }

    @FXML
    public void btnExitHandler(MouseEvent event) {
        Button selectedUIDesigner = (Button) event.getSource();
        selectedUIDesigner.setEffect(null);
    }

    @FXML
    void imgSelectHandler(MouseEvent event) {

    }

    private void applyGlowEffect(ImageView imageView) {
        Glow glow = new Glow();
        glow.setLevel(0.7); // Adjust the level to control the glow intensity
        imageView.setEffect(glow);
    }

    private void removeGlowEffect(ImageView imageView) {
        imageView.setEffect(null); // Remove the glow effect
    }

    public void displayUsername(String username){
        usernamelabel.setText(username);
    }


    @FXML
    void continueBtnHandler(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("music-library.fxml"));
            Scene scene = null;
            scene = new Scene(fxmlLoader.load());
            Stage currStge = (Stage) (continueBtn.getScene().getWindow());
            currStge.setTitle("Music.io");
            currStge.setScene(scene);
            currStge.centerOnScreen();
            currStge.show();
            System.out.println("Current Working Directory: " + System.getProperty("user.dir"));

        } catch (IOException e) {
            System.out.println(e);
            throw new RuntimeException(e);
        }
        catch (RuntimeException e){
            System.out.println(e);
        }

    }

}