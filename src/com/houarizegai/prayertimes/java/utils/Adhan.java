package com.houarizegai.prayertimes.java.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

import java.io.File;

public class Adhan {
    private static MediaPlayer player; // Adhan player

    private static boolean canPlay = true;

    public static void setAdhan(String adhanName) {
        Media hit = new Media(new File(Constants.ADHAN_PATH + adhanName + ".mp3").toURI().toString());
        player = new MediaPlayer(hit);
    }

    public static void play() {
        player.play();
    }

    public static void pause() {
        player.pause();
        player.seek(player.getStartTime());
    }

    public static boolean isPlaying() {
        return player.getStatus().equals(MediaPlayer.Status.PLAYING);
    }

    public static boolean canPlay() {
        return canPlay;
    }

    public static void setCanPlay(boolean canPlay) {
        Adhan.canPlay = canPlay;
    }

    public static void launchPeriodStop() { // if i stop adhan not play again until next prayer
        canPlay = false;
        (new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep((long) Duration.minutes(1).toMillis());
                } catch(InterruptedException ie) {
                    ie.printStackTrace();
                }
                canPlay = true;
            }
        }).start();
    }

}
