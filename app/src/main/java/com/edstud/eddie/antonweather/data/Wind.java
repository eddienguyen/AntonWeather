package com.edstud.eddie.antonweather.data;

import org.json.JSONObject;

public class Wind implements JSONPopulator {

    private double speed;

    public double getSpeed() {return speed;}

    @Override
    public void populate(JSONObject data) {
        speed = data.optDouble("speed");
    }
}
