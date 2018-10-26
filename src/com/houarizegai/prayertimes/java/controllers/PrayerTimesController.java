package com.houarizegai.prayertimes.java.controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrayerTimesController implements Initializable {

    @FXML
    private Label lblDate, lblTime;

    @FXML
    private JFXComboBox<String> comboWilaya;

    @FXML
    private JFXListView<StackPane> listTimes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initComboWilaya();
        initPrayerTime();
    }

    private void initComboWilaya() {
        // Add wilaya name to Comnobox
        comboWilaya.getItems().clear();
        comboWilaya.getItems().add("Adrar");
        comboWilaya.getItems().add("Chlef");
        comboWilaya.getItems().add("Alger");
        comboWilaya.getItems().add("Tiaret");

        // Add Event to ComboBox
        comboWilaya.setOnAction(e -> {

        });
    }

    private void initPrayerTime() {
        listTimes.getItems().clear();
        listTimes.getItems().add(getItemList("الفجر", "05:50"));
        listTimes.getItems().add(getItemList("الشروق", "07:05"));
        listTimes.getItems().add(getItemList("الظهر", "12:40"));
        listTimes.getItems().add(getItemList("العصر", "16:00"));
        listTimes.getItems().add(getItemList("المغرب", "19:16"));
        listTimes.getItems().add(getItemList("العشاء", "20:30"));
    }

    private StackPane getItemList(String prayerName, String prayerTime) {
        StackPane prayerItem = null;
        try {
            prayerItem = FXMLLoader.load(getClass().getResource("/com/houarizegai/prayertimes/resources/views/PrayerItem.fxml"));
            ((Label) prayerItem.getChildren().get(0)).setText(prayerName);
            ((Label) prayerItem.getChildren().get(1)).setText(prayerTime);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
        return prayerItem;
    }
}
