package com.houarizegai.prayertimes.data.network;

import com.houarizegai.prayertimes.data.model.PrayerTimes;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.UnirestException;
import org.json.JSONObject;

import java.util.logging.Logger;

public class WebService {

  private static final String PRAYER_TIMES_END_POINT = "https://api.pray.zone/v2/times/today.json";
  private static final Logger LOG = Logger.getLogger(WebService.class.getName());

  public PrayerTimes getPrayerTimes(String city) {
    try {
      HttpResponse<JsonNode> jsonResponse = Unirest.get(PRAYER_TIMES_END_POINT)
        .queryString("city", city)
        .asJson();

      JSONObject jsonRoot = new JSONObject(jsonResponse.getBody().toString());

      if (jsonRoot.has("results")) { // Is city founded?
        JSONObject prayerTimes = jsonRoot.getJSONObject("results")
          .getJSONArray("datetime")
          .getJSONObject(0)
          .getJSONObject("times");

        return PrayerTimes.builder()
          .fajr(prayerTimes.getString("Fajr"))
          .sunrise(prayerTimes.getString("Sunrise"))
          .dhuhr(prayerTimes.getString("Dhuhr"))
          .asr(prayerTimes.getString("Asr"))
          .maghrib(prayerTimes.getString("Maghrib"))
          .isha(prayerTimes.getString("Isha"))
          .build();
      }
    } catch (UnirestException e) {
      LOG.warning(e.getMessage());
    }

    return getDefaultPrayerTimes();
  }

  private PrayerTimes getDefaultPrayerTimes() {
    String defaultTime = "--:--";
    return PrayerTimes.builder()
      .fajr(defaultTime)
      .sunrise(defaultTime)
      .dhuhr(defaultTime)
      .asr(defaultTime)
      .maghrib(defaultTime)
      .isha(defaultTime)
      .build();
  }
}
