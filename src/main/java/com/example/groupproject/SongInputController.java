package com.example.groupproject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

// In SongInputController.java
public class SongInputController{

    @FXML
    private Label filePathLabel;

    @FXML
    private Button filechoosebtn;

    FileChooser filechooser = new FileChooser();

    @FXML
    void filechooserbtnHandler(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("MP3 Files", "*.mp3")
        );
        fileChooser.setInitialDirectory(new File("mp3File/")); //opens mp3File folder initially
        File selectedFile = fileChooser.showOpenDialog(filechoosebtn.getScene().getWindow());

        if (selectedFile != null) {
            filePathLabel.setText(selectedFile.getPath());
        }

    }

    private MusicLibraryController musicLibraryController;

    public void setMusicLibraryController(MusicLibraryController musicLibraryController, ArrayList<File> targetList) {
        this.musicLibraryController = musicLibraryController;
    }

    @FXML
    private TextField songNameTextField;

    @FXML
    private TextField artistNameTextField;

    @FXML
    void submitButtonHandler(ActionEvent event) {
        String songName = songNameTextField.getText();
        String artistName = artistNameTextField.getText();
        File selectedFile = new File(filePathLabel.getText());

        // Print debug information
        System.out.println("Selected File Path: " + selectedFile.getAbsolutePath());

        // Pass the songName and artistName to the parent controller
        if (musicLibraryController != null) {
            ArrayList<File> playlist = null;
            ArrayList<File> targetList = null;
            musicLibraryController.addSongToLibrary(songName, artistName, selectedFile.getAbsolutePath(), targetList);
        }

        // Close the dialog
        closeDialog();
    }

    private void closeDialog() {
        Stage stage = (Stage) songNameTextField.getScene().getWindow();
        stage.close();
    }
}
