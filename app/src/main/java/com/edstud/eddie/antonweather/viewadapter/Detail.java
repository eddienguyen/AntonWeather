package com.edstud.eddie.antonweather.viewadapter;

public class Detail {
    private String windSpeed, humidity, pressure, visibility, sunrise, sunset;

    public Detail() {
    }

    public Detail(String windSpeed, String humidity, String pressure, String visibility, String sunrise, String sunset) {
        this.windSpeed = windSpeed;
        this.humidity = humidity;
        this.pressure = pressure;
        this.visibility = visibility;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getPressure() {
        return pressure;
    }

    public void setPressure(String pressure) {
        this.pressure = pressure;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getSunset() {
        return sunset;
    }

    public void setSunset(String sunset) {
        this.sunset = sunset;
    }
}
