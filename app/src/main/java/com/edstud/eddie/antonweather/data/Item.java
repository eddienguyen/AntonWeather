package com.edstud.eddie.antonweather.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Item implements JSONPopulator {

    private Condition condition;
    private Forecast[] forecast;

    public Condition getCondition() {
        return condition;
    }

    public Forecast[] getForecast() {return forecast;}

    @Override
    public void populate(JSONObject data) {
        condition = new Condition();
        condition.populate(data.optJSONObject("condition"));

        JSONArray forecastData = data.optJSONArray("forecast");
        forecast = new Forecast[forecastData.length()];

        for (int i = 0; i < forecastData.length(); i++){
            forecast[i] = new Forecast();
            try {
                forecast[i].populate(forecastData.getJSONObject(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
