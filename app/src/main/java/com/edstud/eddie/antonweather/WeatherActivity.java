package com.edstud.eddie.antonweather;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edstud.eddie.antonweather.data.Channel;
import com.edstud.eddie.antonweather.data.Item;
import com.edstud.eddie.antonweather.service.WeatherServiceCallback;
import com.edstud.eddie.antonweather.service.YahooWeatherService;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback, LocationListener,
        NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

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

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        setNavigationViewListener();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        Today's preview'
        imgWeather = (ImageView) findViewById(R.id.imgWeather);
        temp = (TextView) findViewById(R.id.temp);
        lineTxt = (TextView) findViewById(R.id.lineTxt);
        tempUnit = (TextView) findViewById(R.id.tempUnit);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        windDetailData = findViewById(R.id.windDetailData);
        humidityDetailData = findViewById(R.id.humidityDetailData);
        pressureDetailData = findViewById(R.id.pressureDetailData);
        visibilityDetailData = findViewById(R.id.visibilityDetailData);
        sunriseDetailData = findViewById(R.id.sunriseDetailData);
        sunsetDetailData = findViewById(R.id.sunsetDetailData);

        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.show();

        //get Coordinates:
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(new Criteria(), false);
        //check for permissions:
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //request permission(s):
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION

            }, MY_PERMISSION);
        }

//        if ( (Intent i = getIntent()) == null) => not get location from cityfinder yet
                android.location.Location location = locationManager.getLastKnownLocation(provider);
                if (location == null) Log.e("TAG", "NO LOCATION!");
                else {
                    lat = location.getLatitude();
                    lon = location.getLongitude();
                }

        //        service.refreshWeather("(21.017929,105.823776)");
                service.refreshWeather("(" + lat + "," + lon + ")");
//        else if Intent i = getIntent() != null => this activity just get intent(new location) from cityFinder
        // lat = i.getExtra(lat)
        // lon = i.getExtra(lon)
        //service.refreshWeather(lat + lon)
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNavigationViewListener() {
        NavigationView mNavigationView = (NavigationView) findViewById(R.id.navigation_header_container);
        mNavigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    protected void onResume() {
        //in AVD, change lat and lon and press "SEND"
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

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

        int code = item.getCondition().getCode();
        //right now, drawable icons are only 37(icons), so if the code is >37, resources error will appear, so:
        if (code > 37){
            code = 0;
        }

        int resourceId = getResources().getIdentifier("drawable/icon_" + code, null, getPackageName());


        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        imgWeather.setImageDrawable(weatherIconDrawable);

        temp.setText(String.format(String.valueOf(item.getCondition().getTemperature())));
        tempUnit.setText("\u00B0" + channel.getUnits().getTemperature());
        lineTxt.setText(String.format(item.getCondition().getDescription()));

        getSupportActionBar().setTitle(channel.getLocation().getCity() + ", " + channel.getLocation().getCountry());


        setActionBarColor(item.getCondition().getTemperature());

        windDetailData.setText(String.valueOf(channel.getWind().getSpeed()));
        humidityDetailData.setText(String.valueOf(channel.getAtmosphere().getHumidity()));
        pressureDetailData.setText(String.valueOf(channel.getAtmosphere().getPressure()));
        visibilityDetailData.setText(String.valueOf(channel.getAtmosphere().getVisibility()));
        sunriseDetailData.setText(channel.getAstronomy().getSunrise());
        sunsetDetailData.setText(channel.getAstronomy().getSunset());

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

    private void setActionBarColor(int temp) {
        int color = -1;

        if (temp < -10) {
            color = getResources().getColor(R.color.primary_indigo);
        } else if (temp >= -10 && temp <= -5) {
            color = getResources().getColor(R.color.primary_blue);
        } else if (temp > -5 && temp < 5) {
            color = getResources().getColor(R.color.primary_light_blue);
        } else if (temp >= 5 && temp < 10) {
            color = getResources().getColor(R.color.primary_teal);
        } else if (temp >= 10 && temp < 15) {
            color = getResources().getColor(R.color.primary_light_green);
        } else if (temp >= 15 && temp < 20) {
            color = getResources().getColor(R.color.primary_green);
        } else if (temp >= 20 && temp < 25) {
            color = getResources().getColor(R.color.primary_lime);
        } else if (temp >= 25 && temp < 28) {
            color = getResources().getColor(R.color.primary_yellow);
        } else if (temp >= 28 && temp < 32) {
            color = getResources().getColor(R.color.primary_amber);
        } else if (temp >= 32 && temp < 35) {
            color = getResources().getColor(R.color.primary_orange);
        } else if (temp >= 35) {
            color = getResources().getColor(R.color.primary_red);
        }

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(color));
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_find){
            Intent intent = new Intent(this, CityFinderActivity.class);
            startActivity(intent);
            return true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }


}
