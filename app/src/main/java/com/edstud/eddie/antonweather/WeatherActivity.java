package com.edstud.eddie.antonweather;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.edstud.eddie.antonweather.adapter.CustomAdapter;
import com.edstud.eddie.antonweather.data.Channel;
import com.edstud.eddie.antonweather.viewadapter.Detail;
import com.edstud.eddie.antonweather.data.Forecast;
import com.edstud.eddie.antonweather.data.Item;
import com.edstud.eddie.antonweather.service.WeatherServiceCallback;
import com.edstud.eddie.antonweather.service.YahooWeatherService;

import java.util.ArrayList;
import java.util.List;

public class WeatherActivity extends AppCompatActivity implements WeatherServiceCallback, LocationListener,
        NavigationView.OnNavigationItemSelectedListener {
    private List<Object> dataList = new ArrayList<>();
    private RecyclerView recyclerView;
    private CustomAdapter mAdapter;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ImageView imgWeather, tempIcon, windIcon, humidityIcon, pressureIcon, visibilityIcon, sunIcon, moonIcon;
    private TextView temp, txtLocation, lineTxt, tempUnit;

    private YahooWeatherService service;
    private ProgressDialog dialog;

    private LocationManager locationManager;
    private String provider;
    private double lat, lon;

    Toolbar toolbar;

    //add Runtime Permission Request for the app:
    static final int MY_PERMISSION = 0;  //requestCode


    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        //Today's preview'
        imgWeather = (ImageView) findViewById(R.id.imgWeather);
        txtLocation = findViewById(R.id.txtLocation);

//        temp = (TextView) findViewById(R.id.temp);
//        lineTxt = (TextView) findViewById(R.id.lineTxt);
//        tempUnit = (TextView) findViewById(R.id.tempUnit);

        drawerLayout = (DrawerLayout) findViewById(R.id.activity_main_drawer);
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);

        setNavigationViewListener();

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //recyclerView
        recyclerView = findViewById(R.id.tempDetails);

        mAdapter = new CustomAdapter(this, dataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setHasFixedSize(true);
        //end recyclerView

        //get Coordinates:
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(WeatherActivity.this, new String[]{
                    Manifest.permission.INTERNET,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE,
                    Manifest.permission.SYSTEM_ALERT_WINDOW,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            }, MY_PERMISSION);
        }
        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

        if (location == null){
            Log.e("TAG", "NO LOCATION!");
        } else {
            lat = location.getLatitude();
            lon = location.getLongitude();
        }


        service = new YahooWeatherService(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading ...");
        dialog.show();


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
        locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 400, 1, this);
    }

    @Override
    public void serviceSuccess(Channel channel) {
        dialog.hide();
        Item item = channel.getItem();

        int code = item.getCondition().getCode();
        int resourceId = getResources().getIdentifier("drawable/icon_" + code, null, getPackageName());

        Drawable weatherIconDrawable = getResources().getDrawable(resourceId);
        imgWeather.setImageDrawable(weatherIconDrawable);
        txtLocation.setText(channel.getLocation().getCity() + ", " + channel.getLocation().getCountry());

        String title = item.getCondition().getTemperature() + "\u00B0" + channel.getUnits().getTemperature();
        toolbar.setTitle(title);

        setRootLayoutBackgroundColor(item.getCondition().getTemperature());

//        temp.setText(String.format(String.valueOf(item.getCondition().getTemperature())));
//        tempUnit.setText("\u00B0" + channel.getUnits().getTemperature());
//        lineTxt.setText(String.format(item.getCondition().getDescription()));

        prepareDataDetailsFromChannel(channel);
    }

    @Override
    public void serviceFailure(Exception exception) {
        dialog.hide();
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onLocationChanged(Location location) {
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

    private void setRootLayoutBackgroundColor(int temp) {
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

        drawerLayout.setBackgroundColor(color);
//        toolbar.setBackgroundColor(color);

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

    //prepare data for recyclerView
    public void prepareDataDetailsFromChannel(Channel channel){
        dataList.clear();

        Detail detail = new Detail(
                String.valueOf(channel.getWind().getSpeed() + " " + channel.getUnits().getSpeed()),
                String.valueOf(channel.getAtmosphere().getHumidity() + " %" ),
                String.valueOf(channel.getAtmosphere().getPressure() + " " + channel.getUnits().getPressure()),
                String.valueOf(channel.getAtmosphere().getVisibility()),
                channel.getAstronomy().getSunrise(),
                channel.getAstronomy().getSunset()
        );
        dataList.add(detail);

        //TODO: forecast

        Forecast[] forecasts = channel.getItem().getForecast();
        for (Forecast forecast : forecasts){
            dataList.add(new com.edstud.eddie.antonweather.viewadapter.Forecast(
                    forecast.getDay(),
                    forecast.getDate(),
                    forecast.getText(),
                    forecast.getHigh(),
                    forecast.getLow(),
                    forecast.getCode()
            ));
        }

        mAdapter.notifyDataSetChanged();
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
