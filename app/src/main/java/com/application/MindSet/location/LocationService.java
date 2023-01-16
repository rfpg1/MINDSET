package com.application.MindSet.location;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.application.MindSet.direction.Direction;
import com.application.MindSet.notification.NotificationBroadCast;
import com.application.MindSet.utils.Pair;
import com.application.MindSet.utils.Utils;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;

public class LocationService extends Service {

    private static final String TAG = "SERVICE";

    private FusedLocationProviderClient fused;
    private LatLng gameLocation;
    private Date gameDate;

    private final LocationCallback callBack = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Log.i("LocationUpdate", locationResult.getLastLocation().getLongitude() +
                    "," + locationResult.getLastLocation().getLatitude());
            LatLng currentLocation = new LatLng(locationResult.getLastLocation().getLatitude(),
                    locationResult.getLastLocation().getLongitude());

            Direction l = Direction.getInstance();
            Direction.distanceBetweenTwoCoordinates(gameLocation, currentLocation, "walking", 15);
            Log.i("LocationUpdate", "Walking");

            while(!l.isDone()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.i("LocationUpdate", "Lock is done");
            Pair<String, String> response = l.getResponseBody();
            double distance = Double.parseDouble(response.getFirst().split("km")[0].trim());
            Log.i("LocationUpdate", response.getSecond());
            int duration = Integer.parseInt(response.getSecond().split("mins")[0].trim());
            if(distance >= 2.5) {
                Direction.distanceBetweenTwoCoordinates(gameLocation, currentLocation, "driving", 15);
                while(!l.isDone()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                response = l.getResponseBody();
                distance = Double.parseDouble(response.getFirst().split("km")[0].trim());
                //duration =
            }

            if(Utils.isGameIn(gameDate, duration)) {
                Log.i("LocationUpdate", "Inside if");
                showNotification(duration);
            }
        }
    };


    private void showNotification(int duration) {
        Log.i("LocationUpdate", "Sending notification");

        Intent intent = new Intent(this, NotificationBroadCast.class);
        intent.putExtra("duration", duration);
        sendBroadcast(intent);
        stopSelf();
        fused.removeLocationUpdates(callBack);
        Log.i("LocationUpdate", "Notification sent");
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        this.gameDate = (Date) intent.getExtras().get("Date");
        this.gameLocation = (LatLng) intent.getExtras().get("Location");
        this.fused = LocationServices.getFusedLocationProviderClient(this);
        locationInitialize();
        return START_STICKY;
    }

    @SuppressLint("MissingPermission")
    public void locationInitialize(){
        Log.i("LocationUpdate", "Trying to fetch location");

        LocationRequest request = LocationRequest.create();
        request.setInterval(500);

        request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        fused.requestLocationUpdates(request,
                callBack, Looper.getMainLooper());
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
    }
}
