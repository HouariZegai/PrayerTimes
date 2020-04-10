package com.houarizegai.prayertimes.models;

public class PrayerTimesBuilder {
    private String fajr;
    private String sunrise;
    private String dhuhr;
    private String asr;
    private String maghrib;
    private String isha;

    public PrayerTimesBuilder() {
    }

    public PrayerTimesBuilder setFajr(String fajr) {
        this.fajr = fajr;
        return this;
    }

    public PrayerTimesBuilder setSunrise(String sunrise) {
        this.sunrise = sunrise;
        return this;
    }

    public PrayerTimesBuilder setDhuhr(String dhuhr) {
        this.dhuhr = dhuhr;
        return this;
    }

    public PrayerTimesBuilder setAsr(String asr) {
        this.asr = asr;
        return this;
    }

    public PrayerTimesBuilder setMaghrib(String maghrib) {
        this.maghrib = maghrib;
        return this;
    }

    public PrayerTimesBuilder setIsha(String isha) {
        this.isha = isha;
        return this;
    }

    public PrayerTimes build() {
        return new PrayerTimes(fajr, sunrise, dhuhr, asr, maghrib, isha);
    }
}
