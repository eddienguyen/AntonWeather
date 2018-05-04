package com.edstud.eddie.antonweather.data;

import org.json.JSONObject;

public class Atmosphere implements JSONPopulator {
    private int humidity;
    private double pressure, visibility;

    public int getHumidity() {
        return humidity;
    }

    public double getPressure() {
        return pressure;
    }

    public double getVisibility() {
        return visibility;
    }

    @Override
    public void populate(JSONObject data) {
        humidity = data.optInt("humidity");
        pressure = data.optDouble("pressure");
        visibility = data.optDouble("visibility");
    }
}
