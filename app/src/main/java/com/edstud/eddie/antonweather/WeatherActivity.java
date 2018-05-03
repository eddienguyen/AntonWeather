package com.edstud.eddie.antonweather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edstud.eddie.antonweather.data.Channel;
import com.edstud.eddie.antonweather.data.Item;
import com.edstud.eddie.antonweather.data.Location;
import com.edstud.eddie.antonweather.service.WeatherServiceCallback;
import com.edstud.eddie.antonweather.service.YahooWeatherService;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback, LocationListener {

    private ImageView imgWeather, tempIcon, windIcon, humidityIcon, pressureIcon, visibilityIcon, sunIcon, moonIcon;
    private TextView temp, lineTxt, tempUnit, txtLocation, tempMin, tempMax, windDetailData, humidityDetailData, pressureDetailData,
            visibilityDetailData, sunriseDetailData, sunsetDetailData;

    private YahooWeatherService service;
    private ProgressDialog dialog;

    private LocationManager locationManager;
    private String provider;
    private double lat, lon;

    //add Runtime Permission Request for the app:
    private static final int MY_PERMISSION = 0;  //requestCode


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

//        Today's preview'
        imgWeather = (ImageView)findViewById(R.id.imgWeather);
        temp = (TextView) findViewById(R.id.temp);
        lineTxt = (TextView) findViewById(R.id.lineTxt);
        tempUnit = (TextView) findViewById(R.id.tempUnit);
        txtLocation = (TextView) findViewById(R.id.txtLocation);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.show();

        //get Coordinates:
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        //check for permissions:
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            //request permission(s):
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION

            }, MY_PERMISSION);
        }

        android.location.Location location = locationManager.getLastKnownLocation(provider);
        if (location == null) Log.e("TAG", "NO LOCATION!");
        else {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }


//        service.refreshWeather("(21.017929,105.823776)");
        service.refreshWeather("(" + lat + "," + lon + ")");
    }

    @Override
    protected void onResume() {
        //in AVD, change lat and lon and press "SEND"
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){

            //request permission(s):
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION

            }, MY_PERMISSION);
        }
        locationManager.requestLocationUpdates(provider, 400, 1, this);
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();
        Item item = channel.getItem();

        int resourceId = getResources().getIdentifier("drawable/icon_" + item.getCondition().getCode(), null, getPackageName());

        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        imgWeather.setImageDrawable(weatherIconDrawable);

        temp.setText(String.format(String.valueOf(item.getCondition().getTemperature())) );
        tempUnit.setText("\u00B0" + channel.getUnits().getTemperature());
        lineTxt.setText(String.format(item.getCondition().getDescription()));
        txtLocation.setText(channel.getLocation().getCity() + ", " + channel.getLocation().getCountry());
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(android.location.Location location) {
        lat = location.getLatitude();
        lon = location.getLongitude();

        service.refreshWeather("(" + lat + "," + lon + ")");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
