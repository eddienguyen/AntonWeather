package com.edstud.eddie.antonweather.data;

import org.json.JSONObject;

public class Forecast implements JSONPopulator{
    private int code;
    private String date, day;
    private int high, low;
    private String text;

    public int getCode() {return code;}

    public String getDate() {return date;}

    public String getDay() {return day;}

    public int getHigh() {return high;}

    public int getLow() {return low;}

    public String getText() {return text;}


    @Override
    public void populate(JSONObject data) {
        code = data.optInt("code");
        date = data.optString("date");
        day = data.optString("day");
        high = data.optInt("high");
        low = data.optInt("low");
        text = data.optString("text");
    }

    @Override
    public String toString() {
        return "Forecast{" +
                "code=" + code +
                ", date='" + date + '\'' +
                ", day='" + day + '\'' +
                ", high=" + high +
                ", low=" + low +
                ", text='" + text + '\'' +
                '}';
    }
}
