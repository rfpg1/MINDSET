package com.application.MindSet.ui.sports.tennis;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.application.MindSet.CreateGameActivity;
import com.application.MindSet.GestureDetector.SimpleGestureListener;
import com.application.MindSet.R;
import com.application.MindSet.ui.sports.SportsActivity;

public class TennisActivity extends AppCompatActivity implements SportsActivity {

    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tennis);
        setToolBar();
        this.mDetector = new GestureDetectorCompat(getApplicationContext(), new SimpleGestureListener(this));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    private void setToolBar() {
        ActionBar mActionBar = getSupportActionBar();
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.toolbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    public void changeActivity() {
        startActivity(new Intent(TennisActivity.this, CreateGameActivity.class));
    }
}