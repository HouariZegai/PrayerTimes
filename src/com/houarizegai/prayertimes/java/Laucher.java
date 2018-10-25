package com.houarizegai.prayertimes.java;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Laucher extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/com/houarizegai/prayertimes/resources/views/PrayerTimes.fxml"));
        stage.setScene(new Scene(root));
        stage.setTitle("Prayer Times");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
