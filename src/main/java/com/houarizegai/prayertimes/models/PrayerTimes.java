package com.houarizegai.prayertimes.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PrayerTimes {
    private String fajr;
    private String sunrise;
    private String dhuhr;
    private String asr;
    private String maghrib;
    private String isha;

    public static PrayerTimesBuilder builder() {
        return new PrayerTimesBuilder();
    }
}
