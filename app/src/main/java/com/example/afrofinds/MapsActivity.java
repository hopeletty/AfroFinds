package com.example.afrofinds;

import static com.example.afrofinds.LoginActivity.databaseReference;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.afrofinds.Models.Service;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, TaskLoadedCallback {
    GoogleMap map;
    MarkerOptions place1, place2;
    TextView placeName;
    ArrayList<Service> list;
    Polyline currentPolyline;
    String id =  "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        placeName = findViewById(R.id.placeName);
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapNearBy);
        mapFragment.getMapAsync(this);

        //37.2781391, 127.1346745
        //27.667491,85.3208583
      //  place1 = new MarkerOptions().position(new LatLng(37.2781391, 127.1346745)).title("Current");
      //  place2 = new MarkerOptions().position(new LatLng(37.55438, 126.90926)).title("Pie Republic Korea");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        id = extras.getString("id");

        double lat = extras.getDouble("latitude");
        double lng = extras.getDouble("longitude");

        map = googleMap;

        String name = getIntent().getStringExtra("name");
        LatLng location = new LatLng(lat, lng);
        //latLng = getLatLongFromAddress(MapsActivity.this, address);

        map.addMarker(new MarkerOptions().position(location).title(name));
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
        placeName.setText(name);
        // map.addMarker(place2);

        /*
        String url = "https://afrofinds-93455-default-rtdb.asia-southeast1.firebasedatabase.app/";
        DatabaseReference databaseReference = FirebaseDatabase.getInstance(url).getReference("Restaurants");
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = getIntent().getStringExtra("name");
                Double latitude = dataSnapshot.child("latitude").getValue(Double.class);
                Double longitude = dataSnapshot.child("longitude").getValue(Double.class);
                LatLng location = new LatLng(latitude, longitude);
                map.addMarker(new MarkerOptions().position(location).title("Restaurants"));
                //latLng = getLatLongFromAddress(MapsActivity.this, address);
                map.addMarker(new MarkerOptions().position(location).title(name));
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
                // map.addMarker(place2);
            }

            @Override
            public void onCancelled (@NonNull DatabaseError error){

            }
        });

       /* public LatLng getLatLongFromAddress(Context context, String address){
        Geocoder geocoder = new Geocoder(context);
        LatLng latLng = null;
        try {
            list = geocoder.getFromLocationName(, 2);
            if (address==null){ return null;}
            Service location = list.get(0);
            Double lat = location.getLatitude();
            Double lng = location.getLongitude();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  latLng;*/
    }

    private String getUrl(LatLng origin, LatLng dest, String directionMode) {
        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;
        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;
        // Mode
        String mode = "mode=" + directionMode;
        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + mode;
        // Output format
        String output = "json";
        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters + "&key=" + getString(R.string.google_maps_key);
        return url;
    }

    @Override
    public void onTaskDone(Object... values) {
        if (currentPolyline != null)
            currentPolyline.remove();
        currentPolyline = map.addPolyline((PolylineOptions) values[0]);
    }
}
