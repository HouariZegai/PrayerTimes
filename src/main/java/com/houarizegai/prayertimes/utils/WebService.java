package com.houarizegai.prayertimes.utils;

import com.houarizegai.prayertimes.models.PrayerTimes;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.json.JSONObject;

public class WebService {

    private static final String PRAYER_TIMES_END_POINT = "https://api.pray.zone/v2/times/today.json";

    public static PrayerTimes getPrayerTimes(String city) {

        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get(PRAYER_TIMES_END_POINT)
                    .queryString("city", city)
                    .asJson();

            JSONObject jsonRoot = new JSONObject(jsonResponse.getBody().toString());

            if (jsonRoot.has("results")) { // Is city founded?
                JSONObject jsonDate = jsonRoot.getJSONObject("results")
                        .getJSONArray("datetime")
                        .getJSONObject(0)
                        .getJSONObject("times");

                return PrayerTimes.builder()
                        .fajr(jsonDate.getString("Fajr"))
                        .sunrise(jsonDate.getString("Sunrise"))
                        .dhuhr(jsonDate.getString("Dhuhr"))
                        .asr(jsonDate.getString("Asr"))
                        .maghrib(jsonDate.getString("Maghrib"))
                        .isha(jsonDate.getString("Isha"))
                        .build();
            }
        } catch (UnirestException e) {
            //e.printStackTrace();
        }

        return PrayerTimes.builder()
                .fajr("--:--")
                .sunrise("--:--")
                .dhuhr("--:--")
                .asr("--:--")
                .maghrib("--:--")
                .isha("--:--")
                .build();
    }
}
