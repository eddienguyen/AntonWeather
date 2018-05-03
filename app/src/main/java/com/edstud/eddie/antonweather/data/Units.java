package com.edstud.eddie.antonweather.data;

import org.json.JSONObject;

public class Units implements JSONPopulator{
//    get unit temperature only (now)
    private String temperature;

    public String getTemperature() {
        return temperature;
    }

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
    }
}
