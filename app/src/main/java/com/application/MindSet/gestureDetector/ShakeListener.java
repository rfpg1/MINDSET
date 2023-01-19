package com.application.MindSet.gestureDetector;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import com.application.MindSet.ui.sports.Sports;

public class ShakeListener implements SensorEventListener {

    private float shakeValue;
    private static final float DELTA_FOR_SHAKE = 15f;
    private float accelerationValue = SensorManager.GRAVITY_EARTH;
    private Sports sports;

    public ShakeListener(Sports sports, Context context) {
        this.sports = sports;
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);

        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);

        shakeValue = 0.00f;
    }
    
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            float accelerationLast = accelerationValue;
            accelerationValue = (float) Math.sqrt((double) (x * x) + (y * y) + (z * z));
            float delta = accelerationValue - accelerationLast;
            shakeValue = shakeValue * 0.9f + delta;

            if (shakeValue > DELTA_FOR_SHAKE){
                if(!sports.isAdded()){
                    sports.show();
                } else {
                    sports.dismiss();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
