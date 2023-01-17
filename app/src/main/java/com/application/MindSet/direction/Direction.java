package com.application.MindSet.direction;

import android.util.Log;
import androidx.annotation.NonNull;
import com.application.MindSet.utils.Pair;
import com.google.android.gms.maps.model.LatLng;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Direction {

    private final static String GOOGLE_MAPS_API_URL = "https://maps.googleapis.com/maps/api/directions/json?";
    private final static String API_KEY = "AIzaSyDDhzxUawUVuUfURUqs1zPUSOBjccoefIY"; //AIzaSyCXdFI8jO7r-7juPehMkDn9s0wu04OQtjU
    private final static String MOCK_URL = "https://raw.github.com/square/okhttp/master/README.md";
    private static Direction INSTANCE;
    private static boolean done;
    private static Pair<String, String> responseBody;

    public static Direction getInstance() {
        if(INSTANCE == null){
            INSTANCE = new Direction();
        }

        return INSTANCE;
    }

    public static void distanceBetweenTwoCoordinates(LatLng c1, LatLng c2, String mode) {

        String url = getUrl(c1, c2, mode);

        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.i("ResponseDirection" , e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String s = response.body().string();
                try {
                    JSONObject jObject = new JSONObject(s);
                    String distance = (String) jObject.getJSONArray("routes").getJSONObject(0)
                            .getJSONArray("legs").getJSONObject(0)
                            .getJSONObject("distance").get("text");

                    String duration = (String) jObject.getJSONArray("routes").getJSONObject(0)
                            .getJSONArray("legs").getJSONObject(0)
                            .getJSONObject("duration").get("text");

                    responseBody = new Pair<>(distance, duration);
                    done = true;
                } catch (JSONException e) {
                    Log.i("ResponseDirection" , e.getMessage());
                }
            }
        });
    }

    private static String getUrl(LatLng c1, LatLng c2, String mode) {
        String bob = GOOGLE_MAPS_API_URL + "origin=" + c1.latitude + "," + c1.longitude +
                "&destination=" + c2.latitude + "," + c2.longitude +
                "&mode=" + mode +
                "&key=" + API_KEY;
        Log.i("LocationUpdate", bob);
        return bob;
    }

    public boolean isDone() {
        return done;
    }

    public Pair<String, String> getResponseBody() {
        return responseBody;
    }

    public void setDone(boolean done) {
        this.done = done;
    }
}
