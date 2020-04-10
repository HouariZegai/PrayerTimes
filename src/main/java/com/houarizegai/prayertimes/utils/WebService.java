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
                        .fajr("hh:mm")
                        .sunrise("hh:mm")
                        .dhuhr("hh:mm")
                        .asr("hh:mm")
                        .maghrib("hh:mm")
                        .isha("hh:mm")
                        .build();
                return prayerTimes;
            }

            JSONObject jsonDate = jsonRoot.getJSONObject("results")
                    .getJSONArray("datetime")
                    .getJSONObject(0)
                    .getJSONObject("times");

            /* Edit Times of prayer in UI */
            prayerTimes = new PrayerTimesBuilder()
                    .fajr(jsonDate.getString("Fajr"))
                    .sunrise(jsonDate.getString("Sunrise"))
                    .dhuhr(jsonDate.getString("Dhuhr"))
                    .asr(jsonDate.getString("Asr"))
                    .maghrib(jsonDate.getString("Maghrib"))
                    .isha(jsonDate.getString("Isha"))
                    .build();

            return prayerTimes;
        } catch (UnirestException e) {
            //e.printStackTrace();
            // Connection error > make empty prayer times
            prayerTimes = new PrayerTimesBuilder()
                    .fajr("hh:mm")
                    .sunrise("hh:mm")
                    .dhuhr("hh:mm")
                    .asr("hh:mm")
                    .maghrib("hh:mm")
                    .isha("hh:mm")
                    .build();
            return prayerTimes;
        }

    }
}
