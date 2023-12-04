package com.example.groupproject;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

public class SongModel {
    private String songName;
    private String artistName;
    private String songIcon;

    public String getSongName() {
        return songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public String getSongIcon() {
        return songIcon;
    }

    @FXML
    private ImageView imgIcon;

    @FXML
    private Label lblArtistName;

    @FXML
    private Label lblSongName;

    public class SongModuleController {
        @FXML
        private BorderPane songModuleBorderPane;

        private static SongModuleController selectedSongModule = null;

        @FXML
        void initialize() {
            songModuleBorderPane.setOnMouseClicked(event -> handleSongModuleSelection());
        }

        private void handleSongModuleSelection() {
            if (selectedSongModule != null) {
                selectedSongModule.songModuleBorderPane.setStyle("-fx-background-color: #1E1E1E;");
            }

            songModuleBorderPane.setStyle("-fx-background-color: #165DDB;");
            selectedSongModule = this;
        }

        // Other methods for setting song and artist names, if needed
    }

    public void setSongName(String songName) {
        lblSongName.setText(songName);
    }

    public void setArtistName(String artistName) {
        lblArtistName.setText(artistName);
    }

}
