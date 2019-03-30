package com.houarizegai.prayertimes.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Launcher extends Application {
    public static Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/houarizegai/prayertimes/resources/views/PrayerTimes.fxml"));
        stage.setScene(new Scene(root));
        // Make icon to the app
        stage.getIcons().add(new Image("/com/houarizegai/prayertimes/resources/images/icon-app-64px.png"));
        stage.initStyle(StageStyle.UNDECORATED);
        Launcher.stage = stage;
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
