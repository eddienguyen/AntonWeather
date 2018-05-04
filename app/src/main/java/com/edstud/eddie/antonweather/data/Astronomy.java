package com.edstud.eddie.antonweather.data;

import org.json.JSONObject;

public class Astronomy implements JSONPopulator {
    private String sunrise, sunset;

    public String getSunrise() {
        return sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    @Override
    public void populate(JSONObject data) {
        sunrise = data.optString("sunrise");
        sunset = data.optString("sunset");
    }
}
