package com.edstud.eddie.antonweather.viewadapter;

public class Forecast {
    private String day, date, descriptionText;
    private int code, highTemp, lowTemp;

    public Forecast() {
    }

    public Forecast(String day, String date, String descriptionText, int highTemp, int lowTemp, int code) {
        this.day = day;
        this.date = date;
        this.descriptionText = descriptionText;
        this.highTemp = highTemp;
        this.lowTemp = lowTemp;
        this.code = code;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public void setDescriptionText(String descriptionText) {
        this.descriptionText = descriptionText;
    }

    public int getHighTemp() {
        return highTemp;
    }

    public void setHighTemp(int highTemp) {
        this.highTemp = highTemp;
    }

    public int getLowTemp() {
        return lowTemp;
    }

    public void setLowTemp(int lowTemp) {
        this.lowTemp = lowTemp;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
