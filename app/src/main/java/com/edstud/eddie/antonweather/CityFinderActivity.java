package com.edstud.eddie.antonweather;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.model.LatLng;

public class CityFinderActivity extends FragmentActivity {
    public static final String TAG = "AutoCompleteActivity";

    protected GeoDataClient mGeoDataClient;
    protected PlaceDetectionClient mPlaceDetectionClient;

    LatLng latLng;

    //TODO: set button for going back to main activity with some result intent

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_finder);

        //Construct a GeoDataClient
        mGeoDataClient = Places.getGeoDataClient(this, null);

        //Construct a PlaceDetectionClient
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);

        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment) getFragmentManager()
                .findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                latLng = place.getLatLng();
                double lat = latLng.latitude;
                double lon = latLng.longitude;

                //declare bundle and intent
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                //put data to bundle
                bundle.putDouble("latitude", lat);
                bundle.putDouble("longitude", lon);
                //put bundle to intent
                resultIntent.putExtra("latLngBundle", bundle);
                //return data back to parent, which is WeatherActivity
                setResult(RESULT_OK, resultIntent);
                //finish current activity
                finish();
            }

            @Override
            public void onError(Status status) {
                //TODO: Handle the error
                Log.i(TAG, "An error ocurred" + status);
            }
        });

        AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                .build();
        autocompleteFragment.setFilter(typeFilter);

        //TODO: get latLng by pick a location on Map
    }

}

