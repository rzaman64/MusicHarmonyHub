module com.example.groupproject {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires jaudiotagger;


    opens com.example.groupproject to javafx.fxml;
    exports com.example.groupproject;
}