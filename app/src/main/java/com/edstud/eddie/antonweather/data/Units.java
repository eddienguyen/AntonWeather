package com.edstud.eddie.antonweather.data;

import org.json.JSONObject;

public class Units implements JSONPopulator{
//    get unit temperature only (now)
    private String temperature, distance, pressure, speed;

    public String getTemperature() {return temperature;}

    public String getDistance() {return distance;}

    public String getPressure() {return pressure;}

    public String getSpeed() {return speed;}

    @Override
    public void populate(JSONObject data) {
        temperature = data.optString("temperature");
        distance = data.optString("distance");
        pressure = data.optString("pressure");
        speed = data.optString("speed");
    }
}
