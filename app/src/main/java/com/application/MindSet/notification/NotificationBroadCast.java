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
        int notificationID = intent.getIntExtra("NotificationID", 0);
        int duration = intent.getIntExtra("duration", 0) + 15;
        Log.i("NotificationGame", duration + "");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID);

        Notification notification = builder.setContentTitle("Game Notification")
                .setContentText("You have a game in less than " + duration + " minutes you have to start to move")
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.logotipo).build();

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(notificationID, notification);
    }
}
