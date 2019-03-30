package com.houarizegai.prayertimes.java.controllers;

import com.houarizegai.prayertimes.java.models.PrayerTimes;
import com.houarizegai.prayertimes.java.models.PrayerTimesBuilder;
import com.houarizegai.prayertimes.java.utils.Constants;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Scale;
import javafx.util.Duration;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

public class PrayerTimesController implements Initializable {

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTimeH;

    @FXML
    private Label lblTimeSeparator;

    @FXML
    private Label lblTimeM;

    @FXML
    private Label lblTimeSeparator2;

    @FXML
    private Label lblTimeS;

    @FXML
    private Label lblPrayerFajr;

    @FXML
    private Label lblPrayerSunrise;

    @FXML
    private Label lblPrayerDhuhr;

    @FXML
    private Label lblPrayerAsr;

    @FXML
    private Label lblPrayerMaghrib;

    @FXML
    private Label lblPrayerIsha;

    @FXML
    private JFXHamburger hamburgerMenu;

    @FXML
    private JFXComboBox<String> comboCity;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initDateAndClock();
        initComboCities();

        // Make Tiaret city as default
        comboCity.getSelectionModel().select("Tiaret");
        getJsonPrayerTimes(comboCity.getSelectionModel().getSelectedItem());
    }

    @FXML
    private void onClose() {
        Platform.exit();
    }

    @FXML
    private void onHide() {

    }

    private void initDateAndClock() {
        /* initialize clock (date & time) of prayer times */
        KeyFrame clockKeyFrame = new KeyFrame(Duration.ZERO, e -> {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            lblDate.setText(dateFormat.format(date));

            dateFormat = new SimpleDateFormat("HH:mm:ss");
            String[] time = dateFormat.format(date).split(":");
            lblTimeS.setText(time[2]);
            lblTimeM.setText(time[1]);
            lblTimeH.setText(time[0]);

            // If new day change the prayer times
            if(dateFormat.equals("00:00:00") && comboCity.getSelectionModel() != null) {
                getJsonPrayerTimes(comboCity.getSelectionModel().getSelectedItem());
            }
        });

        Timeline clock = new Timeline(clockKeyFrame, new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        /* Show and hide animation for separetor of time */
        KeyFrame clockSeparatorKeyFrame = new KeyFrame(Duration.ZERO, e -> {
            if(lblTimeSeparator.isVisible()) {
                lblTimeSeparator.setVisible(false);
                lblTimeSeparator2.setVisible(false);
            } else {
                lblTimeSeparator.setVisible(true);
                lblTimeSeparator2.setVisible(true);
            }
        });

        Timeline clockSeparator = new Timeline(clockSeparatorKeyFrame, new KeyFrame(Duration.millis(500)));
        clockSeparator.setCycleCount(Animation.INDEFINITE);
        clockSeparator.play();

    }

    private void initComboCities() {
        // Add cities names to ComboBox
        comboCity.getItems().clear();
        comboCity.getItems().addAll(Constants.DZ_CITIES);

        // Add Event to ComboBox
        comboCity.setOnAction(e -> {
            getJsonPrayerTimes(comboCity.getSelectionModel().getSelectedItem());
        });
    }

    private void getJsonPrayerTimes(String city) { // Get prayer times from WebService
        city = city // format City
                .replaceAll(" ", "-")
                .replaceAll("é", "e")
                .replaceAll("è", "e")
                .replaceAll("â", "a")
                .replaceAll("'", "-")
                .replaceAll("ï", "i");

        try {
            HttpResponse<JsonNode> jsonResponse
                    = Unirest.get("https://api.pray.zone/v2/times/today.json")
                    .queryString("city", city)
                    .asJson();

            JSONObject jsonRoot = new JSONObject(jsonResponse.getBody().toString());

            if(!jsonRoot.has("results")) {
                System.out.println("City not found !");
                /* Make Empty prayer times */
                PrayerTimes prayerTimes = new PrayerTimesBuilder()
                        .setFajr("hh:mm")
                        .setSunrise("hh:mm")
                        .setDhuhr("hh:mm")
                        .setAsr("hh:mm")
                        .setMaghrib("hh:mm")
                        .setIsha("hh:mm")
                        .build();
                setPrayerTimes(prayerTimes);
                return;
            }

            JSONObject jsonDate = jsonRoot.getJSONObject("results")
                    .getJSONArray("datetime")
                    .getJSONObject(0)
                    .getJSONObject("times");

            /* Edit Times of prayer in UI */
            PrayerTimes prayerTimes = new PrayerTimesBuilder()
                    .setFajr(jsonDate.getString("Fajr"))
                    .setSunrise(jsonDate.getString("Sunrise"))
                    .setDhuhr(jsonDate.getString("Dhuhr"))
                    .setAsr(jsonDate.getString("Asr"))
                    .setMaghrib(jsonDate.getString("Maghrib"))
                    .setIsha(jsonDate.getString("Isha"))
                    .build();
            setPrayerTimes(prayerTimes);

        } catch (UnirestException e) {
            e.printStackTrace();
        }

    }

    private void setPrayerTimes(PrayerTimes prayerTimes) {
        lblPrayerFajr.setText(prayerTimes.getFajr());
        lblPrayerSunrise.setText(prayerTimes.getSunrise());
        lblPrayerDhuhr.setText(prayerTimes.getDhuhr());
        lblPrayerAsr.setText(prayerTimes.getAsr());
        lblPrayerMaghrib.setText(prayerTimes.getMaghrib());
        lblPrayerIsha.setText(prayerTimes.getIsha());
    }

}
