package com.application.MindSet.ui.game;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;
import com.application.MindSet.R;
import com.application.MindSet.SignInActivity;
import com.application.MindSet.databinding.ActivityMapsBinding;
import com.application.MindSet.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private LatLng local;
    private static final int REQUEST_CODE = 101;
    private AutoCompleteTextView searchBar;
    private static final float DEFAULT_ZOOM = 15f;
    private Marker selectedMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.application.MindSet.databinding.ActivityMapsBinding binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getApplicationContext());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        searchBar = binding.searchBar;
        init();

        Button cancelBtn = binding.cancelBTN;
        cancelBtn.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        Button chooseBtn = binding.chooseBTN;
        chooseBtn.setOnClickListener(view -> {
            if(selectedMarker != null){
                Intent backToCreate = new Intent();
                backToCreate.putExtra("location", local);
                backToCreate.putExtra("localName", selectedMarker.getTitle());
                setResult(RESULT_OK, backToCreate);
                finish();
            } else {
                Toast.makeText(MapsActivity.this, "Need to select a marker", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void init() {

        searchBar.setOnEditorActionListener((textView, actionId, keyEvent) -> {
            if(actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE
                || keyEvent.getAction() == KeyEvent.ACTION_DOWN || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {
                geoLocate();
            }
            return false;
        });
        hideSoftKeyboard();
    }

    private void geoLocate() {
        String search = searchBar.getText().toString();
        Log.i("AddressMaps", "GeoLocate");
        Geocoder geoCoder = new Geocoder(MapsActivity.this);
        List<Address> addressList = new ArrayList<>();
        try{
            addressList = geoCoder.getFromLocationName(search, 1);
        } catch (IOException e){
            Log.e("ERROR", e.getMessage());
        }
        if(addressList.size() > 0) {
            Address address = addressList.get(0);
            Log.i("AddressMaps", address.toString());
            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private void moveCamera(LatLng currLL, float defaultZoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLL,defaultZoom));

        MarkerOptions options = new MarkerOptions()
                .position(currLL)
                .title(title);
        mMap.addMarker(options);

        hideSoftKeyboard();
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMyLocationEnabled(true);
        goToCurrentLocation();

        for(Map.Entry<String, LatLng> entry : Utils.PLACES.entrySet()) {
            MarkerOptions options = new MarkerOptions().title(entry.getKey()).position(entry.getValue());
            mMap.addMarker(options);
        }

        mMap.setOnMarkerClickListener(marker -> {
            selectedMarker = marker;
            local = marker.getPosition();
            if(marker.isInfoWindowShown()) {
                marker.hideInfoWindow();
            } else {
                marker.showInfoWindow();
            }
            return false;
        });
        init();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    goToCurrentLocation();
                }
        }
    }

    @SuppressLint("MissingPermission")
    private void goToCurrentLocation(){
        if(ActivityCompat.checkSelfPermission(
    this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
        this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                Location curr = locationResult.getLastLocation();
                LatLng currLL = new LatLng(curr.getLatitude(), curr.getLongitude());
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currLL,17));
            }
        };

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);
        fusedLocationProviderClient.getLastLocation();
    }

    private void hideSoftKeyboard() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}