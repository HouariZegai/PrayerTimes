package com.houarizegai.prayertimes.data.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PrayerTimes {
  private String fajr;
  private String sunrise;
  private String dhuhr;
  private String asr;
  private String maghrib;
  private String isha;
}
