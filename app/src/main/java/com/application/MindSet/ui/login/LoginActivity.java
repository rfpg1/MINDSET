package com.application.MindSet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GestureDetectorCompat;

import com.application.MindSet.MainActivity;
import com.application.MindSet.R;
import com.application.MindSet.ToolBar;
import com.application.MindSet.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    private static final String DEBUG_TAG = "Gestures";
    private GestureDetectorCompat mDetector;
    private ActivityLoginBinding binding;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ToolBar.setToolBar(getSupportActionBar(), this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button loginButton = binding.login;
        final TextView signup = binding.signUp;
        signup.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
        });

        loginButton.setOnClickListener(view -> {
            if(usernameEditText.getText().length() > 0 && passwordEditText.getText().length() > 0) {
                //TODO: Database call
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            } else {
                Toast.makeText(getApplicationContext(),"Username or Password not filled",Toast.LENGTH_SHORT).show();
            }
        });
    }
}