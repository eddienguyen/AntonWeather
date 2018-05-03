package com.edstud.eddie.antonweather.service;

import android.net.Uri;
import android.os.AsyncTask;

import com.edstud.eddie.antonweather.data.Channel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class YahooWeatherService {
    private WeatherServiceCallback callback;
    private String location;

    private Exception error;

    public YahooWeatherService(WeatherServiceCallback callback) {
        this.callback = callback;
    }

    public String getLocation() {
        return location;
    }

    public void refreshWeather(String loc) {
        this.location = loc;
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {

                //parse custom location into YQL:
                String YQL = String.format("select * from weather.forecast where woeid in (select woeid from geo.places(1) where text=\"%s\") and u='c'", strings[0]);

                //parse cusomized YQL into query URL:
                String endpoint = String.format("https://query.yahooapis.com/v1/public/yql?q=%s&format=json", Uri.encode(YQL));

                try {
                    URL url = new URL(endpoint);

                    URLConnection connection = url.openConnection();

                    InputStream inputStream = connection.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                    StringBuilder result = new StringBuilder();
                    String line;

                    while ((line = reader.readLine()) != null) {
                        result.append(line);
                    }

                    return result.toString();

                } catch (Exception e) {
                    error = e;
                    //TODO: handling error
                }

                return null;
            }

            @Override
            protected void onPostExecute(String s) {

                if (s == null && error != null) {
                    //doInBackground catched an exception && returned null
                    callback.serviceFailure(error);
                    return;
                }

                try {
                    JSONObject data = new JSONObject(s);

                    //if location doesnt exist -> return query with count = 0 (fail)
                    //if location exist        -> return query with count = 1 (success)
                    JSONObject queryResult = data.optJSONObject("query");

                    int count = queryResult.optInt("count");

                    if (count == 0){
                        //city doesnt exist
                        callback.serviceFailure(new LocationWeatherException("no weather data found for" + location));
                        return;
                    }

                    Channel channel = new Channel();
                    channel.populate(queryResult.optJSONObject("results").optJSONObject("channel"));
                    callback.serviceSuccess(channel);
                } catch (JSONException e) {
                    callback.serviceFailure(e);
                }
            }
        }.execute(location);
    }

    public class LocationWeatherException extends Exception{
        public LocationWeatherException(String message) {
            super(message);
        }
    }
}
