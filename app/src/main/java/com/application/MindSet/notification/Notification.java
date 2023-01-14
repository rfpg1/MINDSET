package com.application.MindSet.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.application.MindSet.R;

public class Notification extends BroadcastReceiver {

    public static final String CHANNEL_ID = "gameNotification";
    private final int NOTIFICATION_ID = 200;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.message_icon)
                .setContentTitle("Game Reminder")
                .setContentText("You have a game in an hour")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat manager = NotificationManagerCompat.from(context);

        manager.notify(NOTIFICATION_ID, builder.build());
    }
}
