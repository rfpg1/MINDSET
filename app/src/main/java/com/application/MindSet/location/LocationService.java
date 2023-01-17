package com.application.MindSet.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.util.Log;
import androidx.annotation.NonNull;
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
import java.util.Date;

public class LocationService {

    private Context context;
    private FusedLocationProviderClient fused;
    private LatLng gameLocation;
    private Date gameDate;
    private int notificationID;


    public LocationService(Intent intent, Context context) {
        this.gameDate = (Date) intent.getExtras().get("Date");
        this.gameLocation = (LatLng) intent.getExtras().get("Location");
        this.fused = LocationServices.getFusedLocationProviderClient(context);
        this.notificationID = 0;
        this.context = context;
        locationInitialize();
    }

    private final LocationCallback callBack = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            LatLng currentLocation = new LatLng(locationResult.getLastLocation().getLatitude(),
                    locationResult.getLastLocation().getLongitude());

            Direction l = Direction.getInstance();

            Direction.distanceBetweenTwoCoordinates(gameLocation, currentLocation, "walking");

            while(!l.isDone()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            l.setDone(false);

            Pair<String, String> response = l.getResponseBody();

            double distance = Double.parseDouble(response.getFirst().split("km")[0].trim());

            if(distance >= 2.5) {
                Direction.distanceBetweenTwoCoordinates(gameLocation, currentLocation,"driving");
                while(!l.isDone()) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                l.setDone(false);
            }
            response = l.getResponseBody();

            int duration = Utils.getDuration(response.getSecond());
            if(Utils.isGameIn(gameDate, duration)) {
                showNotification(duration);
            }
        }
    };

    private void showNotification(int duration) {
        Log.i("LocationUpdate", "Sending notification");

        Intent intent = new Intent(context, NotificationBroadCast.class);
        intent.putExtra("duration", duration);
        intent.putExtra("NotificationID", notificationID);
        this.notificationID++;
        context.sendBroadcast(intent);
        fused.removeLocationUpdates(callBack);
        Log.i("LocationUpdate", "Notification sent");
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
}
