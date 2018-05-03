package com.edstud.eddie.antonweather.data;

import org.json.JSONObject;

public class Location implements JSONPopulator {
    //this class is a json parsed object from yahoo api
    //not a class for lat and long in WeatherActivity

    private String city, country, region;           //region is not used at the moment

    public String getCity() {return city;}

    public String getCountry() {return country;}

    @Override
    public void populate(JSONObject data) {
        city = data.optString("city");
        country = data.optString("country");
    }
}
