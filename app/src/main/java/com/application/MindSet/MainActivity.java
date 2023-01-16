package com.application.MindSet;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GestureDetectorCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.application.MindSet.databinding.ActivityMainBinding;
import com.application.MindSet.gestureDetector.SimpleGestureListener;
import com.application.MindSet.notification.NotificationBroadCast;
import com.application.MindSet.ui.sports.Sports;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private GestureDetectorCompat mDetector;
    private NavController navController;
    private static Context context;
    //TODO ask for permissions
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolBar.setToolBar(getSupportActionBar(), this);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        this.navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        Sports sports = Sports.getInstance();
        sports.setManager(getSupportFragmentManager());
        sports.setContext(this);
        this.mDetector = new GestureDetectorCompat(getApplicationContext(), new SimpleGestureListener(sports));
        this.context = getApplicationContext();
        askForPermissions();
    }

    public static void createNotificationChannel() {
        Log.i("LocationUpdate", "Inside create channel");
        CharSequence name = "NotificationChannel";
        String description = "Channel for game notifications";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(NotificationBroadCast.CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager manager = context.getSystemService(NotificationManager.class);
        manager.createNotificationChannel(channel);
        Log.i("LocationUpdate", "Leaving create channel");
    }

    private void askForPermissions() {
        if(ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.SCHEDULE_EXACT_ALARM)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.SCHEDULE_EXACT_ALARM},
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    public static Context getContext(){
        return context;
    }

    public static void showNotification(int duration) {
        Log.i("LocationUpdate", "Sending notification");
        Intent intent = new Intent(context, NotificationBroadCast.class);
        intent.putExtra("duration", duration);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        long now = System.currentTimeMillis();

        long tenSecs = 1000 * 10;

        alarmManager.set(AlarmManager.RTC_WAKEUP,
                now + tenSecs,
                pendingIntent);
        Log.i("LocationUpdate", "Notification sent");
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }
}