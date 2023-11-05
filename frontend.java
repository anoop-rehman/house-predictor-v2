package com.example.t;

import static java.lang.Math.round;

import androidx.fragment.app.FragmentActivity;

Expand
message.txt
10 KB
﻿
package com.example.t;

import static java.lang.Math.round;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.service.autofill.UserData;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.t.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private  LatLng latLng = new LatLng(0.0, 0.0);
    private Integer radius = 500;
    private ActivityMapsBinding binding;
    private SearchView mapSearchView;

    public class Data {
        public double list_price;
        public int bathrooms;
        public double sqft;
        public double lat;
        public double lon;

        public double mean_district_income;
        public int bedrooms_ag;
        public int bedrooms_bg;


    }

    public class Price{
        private int predicted_price;
    }
    public interface ApiService {

        @GET("predit")
        Call<Price> getUser(@Path("userId") int userId);

        @POST("predit")
        Call<Price> createUser(@Body Data userData);
    }
    private Button nextButton;

    private ArrayList rawUserData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        nextButton = (Button) findViewById(R.id.next_button);

        nextButton.setOnClickListener(this::onClick);

        nextButton.setVisibility(View.INVISIBLE);
        mapSearchView = findViewById(R.id.mapSearch);
        mapSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                String location = mapSearchView.getQuery().toString();
                List<Address> addressList = null;

                if (location != null){
                    Geocoder geocoder = new Geocoder(MapsActivity.this);

                    try{
                        addressList = geocoder.getFromLocationName(location, 1);

                    } catch (IOException e){
                        e.printStackTrace();
                    }

                    Address address = addressList.get(0);

                    latLng = new LatLng(address.getLatitude(), address.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(latLng).title(location));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    CircleOptions circly = new CircleOptions().center(latLng).radius(500);
                    Circle circle = mMap.addCircle(circly);
                    circle.setFillColor(Color.argb(50, 200, 10, 10));
                    circle.setStrokeColor(Color.RED);
                    nextButton.setVisibility(View.VISIBLE);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        mapFragment.getMapAsync(this);


        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.address_and_spinner_LinearLayout);
        linearLayout.bringToFront();
        Spinner spinner = findViewById(R.id.radius_spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.radius_array,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (latLng.latitude != 0.0 && latLng.longitude != 0.0) {
                    TextView radiusSelected = findViewById(view.getId());
                    String radiusValue = radiusSelected.getText().toString();
                    radius = Integer.parseInt(radiusValue.substring(0, radiusValue.length()-1));
                    mMap.clear();
                    mMap.addMarker(new MarkerOptions().position(latLng));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                    CircleOptions circly = new CircleOptions().center(latLng).radius(radius);
                    Circle circle = mMap.addCircle(circly);
                    circle.setFillColor(Color.argb(50, 200, 10, 10));
                    circle.setStrokeColor(Color.RED);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    public void onClick(View v)
    {
        if (v == nextButton)
        {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://d3a6-138-51-68-255.ngrok-free.app/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            ApiService apiService = retrofit.create(ApiService.class);

            Data userData = new Data();
            EditText price = (EditText) findViewById(R.id.price);
            EditText washrooms = (EditText) findViewById(R.id.num_washrooms);
            EditText bathrooms = (EditText) findViewById(R.id.bathrooms);
            EditText price = (EditText) findViewById(R.id.price);
            EditText price = (EditText) findViewById(R.id.price);
            userData.list_price = Integer.parseInt(String.valueOf(price.getText()));
            userData.bathrooms = Integer.parseInt(String.valueOf(washrooms.getText()));
            userData.sqft = 2250;
            userData.lat = 43.905626;
            userData.lon = -79.449659;
            userData.mean_district_income = 70600;
            userData.bedrooms_ag = 2;
            userData.bedrooms_bg = 1;

            Call<Price> call = apiService.createUser(userData);

            call.enqueue(new Callback<Price>() {
                @Override
                public void onResponse(Call<Price> call, Response<Price> response) {
                    if (response.isSuccessful()) {
                        Price userResponse = response.body();
                        // Handle the received data
                        int id = userResponse.predicted_price;
                        String returnedPrice = Integer.toString(id);
                    } else {
                        // Handle the error. You can also extract more info from the 'response' object if needed.
                    }
                }

                @Override
                public void onFailure(Call<Price> call, Throwable t) {
                    // Handle the failure
                }
            });

        }
        return;

    }



}
message.txt
10 KB
