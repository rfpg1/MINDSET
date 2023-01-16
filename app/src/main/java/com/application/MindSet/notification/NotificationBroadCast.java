package com.application.MindSet.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.application.MindSet.R;

public class NotificationBroadCast extends BroadcastReceiver {

    public static final String CHANNEL_ID = "gameNotification";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("LocationUpdate", "Received notification");
        int notificationID = intent.getIntExtra("NotificationID", 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        Notification notification = builder.setContentTitle("Test")
                .setContentText("Foda-se")
                .setTicker("ALERTA RAMBOIA")
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher).build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID, notification);
    }
}
