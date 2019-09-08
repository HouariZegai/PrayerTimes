package com.houarizegai.prayertimes.java.controllers;

import com.houarizegai.prayertimes.java.Launcher;
import com.houarizegai.prayertimes.java.models.PrayerTimes;
import com.houarizegai.prayertimes.java.models.PrayerTimesBuilder;
import com.houarizegai.prayertimes.java.utils.Adhan;
import com.houarizegai.prayertimes.java.utils.Constants;
import com.houarizegai.prayertimes.java.utils.WebService;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXToggleButton;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.ResourceBundle;

public class PrayerTimesController implements Initializable {

    @FXML
    private StackPane menuBar;

    @FXML
    private JFXHamburger hamburgerMenu;

    @FXML
    private JFXComboBox<String> comboCities;

    @FXML
    private Label lblDate;

    @FXML
    private Label lblTimeH, lblTimeSeparator, lblTimeM, lblTimeSeparator2, lblTimeS;

    @FXML
    private Label lblPrayerFajr, lblPrayerSunrise, lblPrayerDhuhr, lblPrayerAsr, lblPrayerMaghrib, lblPrayerIsha;

    /* Start adhan part */

    @FXML
    private StackPane alarmView;

    @FXML
    private Text txtAlarmPrayer, txtAlarmCity;

    /* End adhan part */

    /* Start settings part */

    @FXML
    private AnchorPane settingsView;

    @FXML
    private JFXToggleButton tglRunAdhan;

    @FXML
    private JFXComboBox<String> comboAdhan;

    @FXML
    private FontAwesomeIconView iconPlayAdhan;

    HamburgerBasicCloseTransition hamburgerTransition;

    /* End settings part */

    // For Make Stage Drageable
    private double xOffset = 0;
    private double yOffset = 0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initMenu();
        initDateAndClock();
        initComboCities();
        loadSettingsLog(); // load saved app stat
        initAdhan();

        // make stage drageable
        menuBar.setOnMousePressed(event -> {
            xOffset = event.getSceneX();
            yOffset = event.getSceneY();
        });
        menuBar.setOnMouseDragged(event -> {
            Launcher.stage.setX(event.getScreenX() - xOffset);
            Launcher.stage.setY(event.getScreenY() - yOffset);
            Launcher.stage.setOpacity(0.7f);
        });
        menuBar.setOnDragDone(e -> {
            Launcher.stage.setOpacity(1.0f);
        });
        menuBar.setOnMouseReleased(e -> {
            Launcher.stage.setOpacity(1.0f);
        });

    }

    // Just for testing: change prayer times
    private void localTestPrayer() {
        PrayerTimes prayerTimes = new PrayerTimesBuilder()
                .setFajr("00:00")
                .setDhuhr("18:35")
                .setAsr("18:24")
                .setMaghrib("18:42")
                .setIsha("18:43")
                .build();

        lblPrayerFajr.setText(prayerTimes.getFajr());
        lblPrayerSunrise.setText(prayerTimes.getSunrise());
        lblPrayerDhuhr.setText(prayerTimes.getDhuhr());
        lblPrayerAsr.setText(prayerTimes.getAsr());
        lblPrayerMaghrib.setText(prayerTimes.getMaghrib());
        lblPrayerIsha.setText(prayerTimes.getIsha());
    }

    private void initDateAndClock() {
        /* initialize clock (date & time) of prayer times */
        KeyFrame clockKeyFrame = new KeyFrame(Duration.ZERO, e -> {
            Date date = new Date();
            DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
            lblDate.setText(dateFormat.format(date));

            dateFormat = new SimpleDateFormat("HH:mm:ss");
            String[] time = dateFormat.format(date).split(":");
            lblTimeS.setText(time[2]);
            lblTimeM.setText(time[1]);
            lblTimeH.setText(time[0]);

            // If new day change the prayer times
            if (dateFormat.equals("00:00:00") && comboCities.getSelectionModel() != null) {
                setPrayerTimes(comboCities.getSelectionModel().getSelectedItem());
            }

            checkAdhanTime();
        });

        Timeline clock = new Timeline(clockKeyFrame, new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();

        /* Show and hide animation for separetor of time */
        KeyFrame clockSeparatorKeyFrame = new KeyFrame(Duration.ZERO, e -> {
            if (lblTimeSeparator.isVisible()) {
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
        comboCities.getItems().clear();
        comboCities.getItems().addAll(Constants.DZ_CITIES);

        // Add Event to ComboBox
        comboCities.setOnAction(e -> {
            setPrayerTimes(comboCities.getSelectionModel().getSelectedItem());
        });
    }

    private void initAdhan() {
        Adhan.setAdhan("adan" + (comboAdhan.getSelectionModel().getSelectedIndex() + 1));
    }

    private void setPrayerTimes(String city) {
        city = city // format City
                .replaceAll(" ", "-")
                .replaceAll("é", "e")
                .replaceAll("è", "e")
                .replaceAll("â", "a")
                .replaceAll("'", "-")
                .replaceAll("ï", "i");
        // Get prayer times from web service
        PrayerTimes prayerTimes = WebService.getPrayerTimes(city);

        lblPrayerFajr.setText(prayerTimes.getFajr());
        lblPrayerSunrise.setText(prayerTimes.getSunrise());
        lblPrayerDhuhr.setText(prayerTimes.getDhuhr());
        lblPrayerAsr.setText(prayerTimes.getAsr());
        lblPrayerMaghrib.setText(prayerTimes.getMaghrib());
        lblPrayerIsha.setText(prayerTimes.getIsha());
    }

    @FXML
    private void onClose() {
        saveSettingsLog();
        Platform.exit();
    }

    @FXML
    private void onHide() {
        Launcher.stage.setIconified(true);
    }

    /* Start alarm (adhan) part */

    private void checkAdhanTime() {
        // Check prayer times with actual time
        String timeNow = lblTimeH.getText() + ":" + lblTimeM.getText();
        System.out.println("time now: " + timeNow);
        checkTimeWithPrayer(timeNow, lblPrayerFajr, "الفجر");
        checkTimeWithPrayer(timeNow, lblPrayerDhuhr, "الظهر");
        checkTimeWithPrayer(timeNow, lblPrayerAsr, "العصر");
        checkTimeWithPrayer(timeNow, lblPrayerMaghrib, "المغرب");
        checkTimeWithPrayer(timeNow, lblPrayerIsha, "العشاء");
    }

    private void checkTimeWithPrayer(String time, Label lblPrayerTime, String prayerName) { // Check if it's the time of prayer
        if(time.equals(lblPrayerTime.getText())) {
            if(Adhan.canPlay() && !Adhan.isPlaying() && tglRunAdhan.isSelected()) {
                Adhan.setCanPlay(false);
                txtAlarmCity.setText(comboCities.getSelectionModel().getSelectedItem());
                txtAlarmPrayer.setText(prayerName);
                setShowView(true, alarmView);
                Adhan.play();
            }
        }
    }

    @FXML
    public void onCloseAlarm() {
        setShowView(false, alarmView);
        Adhan.pause();
        Adhan.launchPeriodStop();
    }

    /* End alarm (adhan) part */

    /* Start settings part */

    private void initMenu() { // Init settings
        /* Init show/hide menu */
        hamburgerTransition = new HamburgerBasicCloseTransition(hamburgerMenu);
        hamburgerTransition.setRate(-1);
        hamburgerMenu.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (hamburgerTransition.getRate() == -1) {
                hamburgerTransition.setRate(1);
                setShowView(true, settingsView);
            } else {
                hamburgerTransition.setRate(-1);
                setShowView(false, settingsView);
                Adhan.pause();
            }
            hamburgerTransition.play();
        });

        // Init combo Adan
        comboAdhan.getItems().addAll("أذان 1", "أذان 2", "أذان 3", "أذان 4", "أذان 5");
        comboAdhan.setOnAction(e -> {
            initAdhan();
            iconPlayAdhan.setGlyphName("PLAY");
        });

        // Init play/pause test adan
        iconPlayAdhan.setOnMouseClicked(e -> {
            if (iconPlayAdhan.getGlyphName().equals("PLAY")) {
                Adhan.play();
                iconPlayAdhan.setGlyphName("PAUSE");
            } else {
                Adhan.pause();
                iconPlayAdhan.setGlyphName("PLAY");
            }
        });
    }

    @FXML
    private void onCloseMenu() {
        setShowView(false, settingsView);

        hamburgerTransition.setRate(-1);
        hamburgerTransition.play();

        Adhan.pause();
    }

    private void setShowView(boolean show, Parent view) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(500), view);
        if (show) {
            view.setVisible(true);
            scaleTransition.setFromX(0);
            scaleTransition.setFromY(0);
            scaleTransition.setToX(1);
            scaleTransition.setToY(1);
        } else {
            scaleTransition.setFromX(1);
            scaleTransition.setFromY(1);
            scaleTransition.setToX(0);
            scaleTransition.setToY(0);
            scaleTransition.setOnFinished(e -> {
                view.setVisible(false);
            });
        }
        scaleTransition.play();
    }

    private void loadSettingsLog() {
        ResourceBundle bundle = ResourceBundle.getBundle("com.houarizegai.prayertimes.resources.config.settings");

        // Make Tiaret city as default
        comboCities.getSelectionModel().select(Integer.parseInt(toUTF(bundle.getString("city"))));
        setPrayerTimes(comboCities.getSelectionModel().getSelectedItem());
        //localTestPrayer();

        tglRunAdhan.setSelected(Boolean.valueOf(toUTF((bundle.getString("enableAdan")))));
        comboAdhan.getSelectionModel().select(Integer.parseInt(toUTF((bundle.getString("adan")))));
    }

    private void saveSettingsLog() {
        Properties prop = new Properties();
        OutputStream output = null;
        try {
            output = new FileOutputStream("src/com/houarizegai/prayertimes/resources/config/settings.properties");

            // Set the properties value
            prop.setProperty("city", String.valueOf(comboCities.getSelectionModel().getSelectedIndex()));
            prop.setProperty("enableAdan", String.valueOf(tglRunAdhan.isSelected()));
            prop.setProperty("adan", String.valueOf(comboAdhan.getSelectionModel().getSelectedIndex()));

            // Save properties to project root folder
            prop.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String toUTF(String val) {
        try {
            return new String(val.getBytes("ISO-8859-1"), "UTF-8");
        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /* End settings part */

}
