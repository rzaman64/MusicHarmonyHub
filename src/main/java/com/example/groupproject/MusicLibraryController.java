package com.example.groupproject;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;
import org.jaudiotagger.tag.images.Artwork;

import java.io.ByteArrayInputStream;
import java.text.DecimalFormat;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MusicLibraryController implements Initializable{

    @FXML
    private Button addSongBtn;
    @FXML
    private Button allSongBtn;
    @FXML
    private Label artistNameLabel;
    @FXML
    private Label songNameLabel;
    @FXML
    private VBox vItems;
    @FXML
    private ProgressBar songProgressBar;
    @FXML
    private ComboBox<String> speedBox;
    @FXML
    private Slider volumeSlider;
    @FXML
    private Pane draggablePane;
    @FXML
    private TextField searchSongTextField;
    @FXML
    private ImageView displayImgIcon;


// For playing music
    private Media media;
    private File directory;
    private File[] files;

    private ArrayList<File> songs;

    private int songNumber;
    private int[] speeds = {25, 50, 75, 100, 125, 150, 175, 200};

    private Timer timer;
    private TimerTask task;
    private boolean running;

    @FXML
    private Label startTimeLabel;
    @FXML
    private Label endTimeLabel;


// *************************************

    // a Map to store playlists
    private Map<String, ArrayList<File>> playlists = new HashMap<>();
    private ArrayList<File> allSongs = new ArrayList<>();
    private ArrayList<File> favoriteSongs = new ArrayList<>();

    @FXML
    void searchSongTextFieldHandler(ActionEvent event) {
        String searchText = searchSongTextField.getText().toLowerCase();

        if (!searchText.isEmpty()) {
            // Perform the search and rearrange songs
            rearrangeSongs(searchText);
        } else {
            // If the search text is empty, reset the order to the original
            loadSongsIntoVBox(songs);
        }
    }

    private void rearrangeSongs(String searchText) {
        // Calculate relevance scores for each song based on your criteria
        // For simplicity, let's assume higher relevance scores come first
        songs.sort(Comparator.comparingDouble(song -> calculateRelevance((File) song, searchText)).reversed());

        // Load the rearranged songs into the VBox
        loadSongsIntoVBox(songs);
    }

    private double calculateRelevance(File song, String searchText) {
        // Implement your relevance calculation logic here
        // For simplicity, let's assume higher relevance scores for partial matches
        String songName = song.getName().toLowerCase();
        return songName.contains(searchText) ? 1.0 : 0.0;
    }

    @FXML
    void allSongBtnHandler(ActionEvent event) {
        // Load all songs initially
        loadSongsIntoVBox(songs);
        //initialize other components..
    }

    @FXML
    public void closeBtnHandler(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    void editBtnHandler(ActionEvent event) {

    }

    @FXML
    void favoriteSongBtnHandler(ActionEvent event) {
        // Clear the VBox
        //vItems.getChildren().clear(); //try deleting
        loadSongsIntoVBox(favoriteSongs);

    }

    @FXML
    void personalBtnHandler(ActionEvent event) {

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
    void addSongBtnHandler(ActionEvent event) {
        openSongInputDialog(songs);
    }
    private Node selectedSongModule = null;
    private MediaPlayer mediaPlayer; // MediaPlayer for audio playback

    @FXML
    void playBtnHandler(ActionEvent event) {
        beginTimer();
        changeSpeed(null);
        mediaPlayer.setVolume(volumeSlider.getValue() *0.01);

        mediaPlayer.play();
    }

    @FXML
    void backBtnHandler(ActionEvent event) {
        if(songNumber > 0){

            songNumber--;

            mediaPlayer.stop();

            if(running){
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songNameLabel.setText(songs.get(songNumber).getName());

            // Highlight the song module
            if (selectedSongModule != null) {
                selectedSongModule.setStyle("-fx-background-color: #1E1E1E;");
            }
            selectedSongModule = vItems.getChildren().get(songNumber);
            selectedSongModule.setStyle("-fx-background-color: #165DDB");

            playBtnHandler();
        }
        else{

            songNumber = songs.size() - 1;
            mediaPlayer.stop();

            if(running){
                cancelTimer();
            }

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songNameLabel.setText(songs.get(songNumber).getName());

            // Highlight the song module
            if (selectedSongModule != null) {
                selectedSongModule.setStyle("-fx-background-color: #1E1E1E;");
            }
            selectedSongModule = vItems.getChildren().get(songNumber);
            selectedSongModule.setStyle("-fx-background-color: #165DDB");

            mediaPlayer.setVolume(volumeSlider.getValue() *0.01);
            mediaPlayer.play();
        }
    }

    @FXML
    void skipBtnHandler(ActionEvent event) {
        if(songNumber < songs.size() - 1){

            songNumber++;
            mediaPlayer.stop();
            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songNameLabel.setText(songs.get(songNumber).getName());

            mediaPlayer.setVolume(volumeSlider.getValue() *0.01);

            // Highlight the song module
            if (selectedSongModule != null) {
                selectedSongModule.setStyle("-fx-background-color: #1E1E1E;");
            }
            selectedSongModule = vItems.getChildren().get(songNumber);
            selectedSongModule.setStyle("-fx-background-color: #165DDB");

            mediaPlayer.play();
            playBtnHandler();

        }
        else{

            songNumber = 0;
            mediaPlayer.stop();

            media = new Media(songs.get(songNumber).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
            songNameLabel.setText(songs.get(songNumber).getName());

            mediaPlayer.setVolume(volumeSlider.getValue() *0.01);

            // Highlight the song module
            if (selectedSongModule != null) {
                selectedSongModule.setStyle("-fx-background-color: #1E1E1E;");
            }
            selectedSongModule = vItems.getChildren().get(songNumber);
            selectedSongModule.setStyle("-fx-background-color: #165DDB");

            mediaPlayer.play();
        }
    }

    @FXML
    void pauseBtnHandler(ActionEvent event) {
        cancelTimer();
        changeSpeed(null);
        mediaPlayer.pause();
    }


    public void beginTimer() {
        timer = new Timer();

        task = new TimerTask() {
            public void run() {
                running = true;
                double current = mediaPlayer.getCurrentTime().toSeconds();
                double end = media.getDuration().toSeconds();

                // Convert seconds to minutes and seconds
                long currentMinutes = (long) current / 60;
                long currentSeconds = (long) current % 60;

                long endMinutes = (long) end / 60;
                long endSeconds = (long) end % 60;

                // Format the time in "0:00" format
                String currentTime = String.format("%d:%02d", currentMinutes, currentSeconds);
                String endTime = String.format("%d:%02d", endMinutes, endSeconds);

                System.out.println("[Song Progress]: " + currentTime + " / " + endTime);

                // Update UI on the JavaFX Application Thread
                Platform.runLater(() -> {
                    startTimeLabel.setText(currentTime);
                    endTimeLabel.setText(endTime);
                });

                songProgressBar.setProgress(current / end);

                if (current == end) {
                    cancelTimer();
                }
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
    }

    public void cancelTimer(){
        running = false;
        timer.cancel();
    }

    private void playBtnHandler() {
        beginTimer();
        changeSpeed(null);
        mediaPlayer.setVolume(volumeSlider.getValue() *0.01);
        mediaPlayer.play();
    }

    // Opens song-add dialog which allows the user to add their own songs, but also naming the song and artist
    private void openSongInputDialog(ArrayList<File> targetList) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("song-input.fxml"));
            Parent root = loader.load();

            // Pass the reference of this MusicLibraryController to the SongInputController
            SongInputController songInputController = loader.getController();
            songInputController.setMusicLibraryController(this, targetList);

            Stage dialogStage = new Stage();
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(addSongBtn.getScene().getWindow());
            dialogStage.setScene(new Scene(root));
            dialogStage.setTitle("Add Song");
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Loads added songs into VBOX (from userinput)
    public void addSongToLibrary(String songName, String artistName, String filePath, ArrayList<File> targetList) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("song-module.fxml"));
            Node songModule = fxmlLoader.load();

            // Set your song and artist names into your song module's labels
            // You should adjust these IDs based on your actual FXML structure
            Label lblSongName = (Label) songModule.lookup("#lblSongName");
            Label lblArtistName = (Label) songModule.lookup("#lblArtistName");
            lblSongName.setText(songName);
            lblArtistName.setText(artistName);

            // Set the click event for song module
            songModule.setOnMouseClicked(event -> handleSongModuleClick(songModule, songName, artistName, filePath));

            // Set the click event for song module
            songModule.setOnMouseClicked(event -> {
                if (selectedSongModule != null) {
                    // Deselect the previously selected song module
                    selectedSongModule.setStyle("-fx-background-color: #1E1E1E;");
                }

                // Select the new song module, and edit its features
                songModule.setStyle("-fx-background-color: #165DDB;"); //color when selected

                songNameLabel.setText(songName);     //sets the third collumns songname
                artistNameLabel.setText(artistName); //sets the third collumns artistname
                selectedSongModule = songModule;
            });

            //Add the new song to the songs list (via the add button)
            File newSongFile = new File(filePath);
            songs.add(newSongFile);

            // Update media and mediaPlayer to reflect the new song added by the user
            media = new Media(newSongFile.toURI().toString());
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = new MediaPlayer(media);
            songNameLabel.setText(newSongFile.getName());
            lblSongName.setText(newSongFile.getName());

            System.out.println("song name: " + newSongFile.getName()); //gets name of song file (explorer file name)

            // Add the song module to your VBox
            vItems.getChildren().add(songModule);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createSongModulesFromMp3Files();
        ArrayList<File> allSongs = new ArrayList<>();
        loadSongsFromFolderIntoVBox(new File("mp3File"),allSongs);

        songs = new ArrayList<File>();

        directory = new File("mp3File");

        files = directory.listFiles();

        if(files != null){
            for(File file : files) {
                songs.add(file);
                System.out.println(file);
            }
        }

        media = new Media(songs.get(songNumber).toURI().toString());
        mediaPlayer = new MediaPlayer(media);
        songNameLabel.setText(songs.get(songNumber).getName());
        System.out.println(songNumber +"\n" + songs);

        // Initialize songNumber
        songNumber = 0;

        //Displaying speed
        for (int i = 0; i < speeds.length; i++) {
            speedBox.getItems().add(Integer.toString(speeds[i])+"%");
        }
        speedBox.setOnAction(this::changeSpeed);

        //volume initializer (go to fxml and change the maxvalue to 100, and value to 50)
        volumeSlider.valueProperty().addListener(new ChangeListener<Number>() {

            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number t1) {
                mediaPlayer.setVolume(volumeSlider.getValue() *0.01);
            }
        });

        songProgressBar.setStyle("-fx-accent: #1E1E1E"); //progress color "black"
    }

    @FXML
    public void changeSpeed(ActionEvent event) {
        if(speedBox.getValue()==null){
            mediaPlayer.setRate(1);
        }
        else {
            mediaPlayer.setRate(Integer.parseInt(speedBox.getValue().substring(0, speedBox.getValue().length() - 1)) * 0.01);
        }
    }


    private Image loadAlbumArt(String mp3FileName) {
        try {
            // Assuming the mp3 files are in the "mp3File" directory
            String mp3FilePath = "mp3File/" + mp3FileName;
            File mp3File = new File(mp3FilePath);

            if (mp3File.exists()) {
                AudioFile audioFile = AudioFileIO.read(mp3File);

                if (audioFile != null) {
                    Tag tag = audioFile.getTag();

                    if (tag instanceof AbstractID3v2Tag) {
                        AbstractID3v2Tag id3v2Tag = (AbstractID3v2Tag) tag;

                        // Find the first artwork
                        Artwork artwork = id3v2Tag.getFirstArtwork();

                        if (artwork != null) {
                            byte[] imageData = artwork.getBinaryData();
                            return new Image(new ByteArrayInputStream(imageData));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return a default image if no album art is found
        return new Image(getClass().getResourceAsStream("/icons/defaultimage3.png"));
    }

    private Node createSongModule(String songName, String artistName, String mp3File) {
        try {
            System.out.println("Current Working Directory: " + System.getProperty("user.dir"));

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("song-module.fxml"));
            Node songModule = fxmlLoader.load();

            // Set your song and artist names into your song module's labels
            // You should adjust these IDs based on your actual FXML structure
            Label lblSongName = (Label) songModule.lookup("#lblSongName");
            Label lblArtistName = (Label) songModule.lookup("#lblArtistName");

            // Set the correct song and artist names
            lblSongName.setText(songName);
            lblArtistName.setText(artistName);

            // Set the click event for song module
            songModule.setOnMouseClicked(event -> handleSongModuleClick(songModule, songName, artistName, mp3File));

            // Load album art
            ImageView imgIcon = (ImageView) songModule.lookup("#imgIcon");
            Image albumArt = loadAlbumArt(mp3File);
            if (albumArt != null) {
                imgIcon.setImage(albumArt);
            }

            return songModule;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Automatically makes the "song modules" within the vbox
    // by just adding songs into the mp3File folder
    private void createSongModulesFromMp3Files() {
        loadSongsIntoVBox(allSongs);  // Load all songs initially
    }

    private void loadSongsIntoVBox(ArrayList<File> songList) {
        vItems.getChildren().clear();  // Clear existing song modules

        if (songList != null) {
            for (File mp3File : songList) {
                if (mp3File.isFile() && mp3File.getName().toLowerCase().endsWith(".mp3")) {
                    String fileName = mp3File.getName();
                    String songName = fileName.substring(0, fileName.lastIndexOf("."));

                    String artistName = songName;
                    System.out.println("test 1");

                    Node songModule = createSongModule(songName, artistName, fileName);
                    vItems.getChildren().add(songModule);
                }
            }
        }
    }

    private void loadSongsFromFolderIntoVBox(File folder, ArrayList<File> songList) {
        songList.clear();

        File[] mp3Files = folder.listFiles();
        if (mp3Files != null) {
            for (File mp3File : mp3Files) {
                if (mp3File.isFile() && mp3File.getName().toLowerCase().endsWith(".mp3")) {
                    songList.add(mp3File);
                }
            }
        }

        loadSongsIntoVBox(songList);
    }

    //song module selection
    private void handleSongModuleClick(Node songModule, String songName, String artistName, String mp3File) {
        System.out.println("clicked on song module: " + songName + " - " + artistName);
        if (selectedSongModule != null) {
            selectedSongModule.setStyle("-fx-background-color: #1E1E1E;");
        }

        songModule.setStyle("-fx-background-color: #165DDB");
        songNameLabel.setText(songName);
        artistNameLabel.setText(artistName);
        selectedSongModule = songModule;

        // Update songNumber based on the clicked song module
        songNumber = vItems.getChildren().indexOf(songModule);

        // Load album art and update the displayImgIcon
        Image albumArt = loadAlbumArt(mp3File);
        if (albumArt != null) {
            displayImgIcon.setImage(albumArt);
        } else {
            // Set a default image if no album art is found
            displayImgIcon.setImage(new Image(getClass().getResourceAsStream("/icons/defaultimage3.png")));
        }

        String songFilePath = "mp3File/" + mp3File;
        System.out.println("Song file path: " + songFilePath);
        songNameLabel.setText(mp3File);

        // Check if the song is currently playing
        boolean isPlaying = mediaPlayer != null && mediaPlayer.getStatus() == MediaPlayer.Status.PLAYING;

        if (mediaPlayer == null) {
            media = new Media(new File(songFilePath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        } else {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            media = new Media(new File(songFilePath).toURI().toString());
            mediaPlayer = new MediaPlayer(media);
        }

        // Set the volume (adjust as needed)
        mediaPlayer.setVolume(volumeSlider.getValue() *0.01);

        // Set a listener to handle the end of the song
        mediaPlayer.setOnEndOfMedia(() -> {
            // Auto-play the next song when the current song ends (if you want)
            // You can add logic here to handle song transitions.
            mediaPlayer.play(); //added
            System.out.println("Playing: " + mp3File); //added
        });

        // Play the selected song
        beginTimer();               //starts the timer on the progress bar
        changeSpeed(null);    //resets speed back to normal (little buggy)
        mediaPlayer.play();
        System.out.println("Playing: " + mp3File);
    }
}
