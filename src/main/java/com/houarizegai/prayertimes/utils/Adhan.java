package com.houarizegai.prayertimes.utils;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;

public class Adhan {
    private static MediaPlayer player; // Adhan player

    private static boolean canPlay = true;

    public static void setAdhan(String adhanName) {
        Media media = new Media(new File(Constants.RESOURCES_PATH + "adhan\\" + adhanName).toURI().toString());
        player = new MediaPlayer(media);
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

    public static void launchPeriodStop() { // If I stop Adhan don't play it again until the next prayer
        canPlay = false;
        new Thread(() -> {
            try {
                Thread.sleep(60000L); // Sleep 1 min
            } catch(InterruptedException ie) {
                ie.printStackTrace();
            }
            canPlay = true;
        }).start();
    }

}
