package com.houarizegai.prayertimes.utils;

import com.houarizegai.prayertimes.models.PrayerTimes;
import com.houarizegai.prayertimes.models.PrayerTimesBuilder;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.json.JSONObject;

public class WebService {

    private static final String END_POINT = "https://api.pray.zone/v2/times/today.json";

    public static PrayerTimes getPrayerTimes(String city) { // Get prayer times from WebService
        PrayerTimes prayerTimes = null;

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(END_POINT)
                    .queryString("city", city)
                    .asJson();

            JSONObject jsonRoot = new JSONObject(jsonResponse.getBody().toString());

            if (!jsonRoot.has("results")) { // If city not found !
                // Make empty prayer times
                prayerTimes = new PrayerTimesBuilder()
                        .setFajr("hh:mm")
                        .setSunrise("hh:mm")
                        .setDhuhr("hh:mm")
                        .setAsr("hh:mm")
                        .setMaghrib("hh:mm")
                        .setIsha("hh:mm")
                        .build();
                return prayerTimes;
            }

            JSONObject jsonDate = jsonRoot.getJSONObject("results")
                    .getJSONArray("datetime")
                    .getJSONObject(0)
                    .getJSONObject("times");

            /* Edit Times of prayer in UI */
            prayerTimes = new PrayerTimesBuilder()
                    .setFajr(jsonDate.getString("Fajr"))
                    .setSunrise(jsonDate.getString("Sunrise"))
                    .setDhuhr(jsonDate.getString("Dhuhr"))
                    .setAsr(jsonDate.getString("Asr"))
                    .setMaghrib(jsonDate.getString("Maghrib"))
                    .setIsha(jsonDate.getString("Isha"))
                    .build();

            return prayerTimes;
        } catch (UnirestException e) {
            //e.printStackTrace();
            // Connection error > make empty prayer times
            prayerTimes = new PrayerTimesBuilder()
                    .setFajr("hh:mm")
                    .setSunrise("hh:mm")
                    .setDhuhr("hh:mm")
                    .setAsr("hh:mm")
                    .setMaghrib("hh:mm")
                    .setIsha("hh:mm")
                    .build();
            return prayerTimes;
        }

    }
}
