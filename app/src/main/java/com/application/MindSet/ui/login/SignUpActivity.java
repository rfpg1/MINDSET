package com.application.MindSet.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.application.MindSet.MainActivity;
import com.application.MindSet.R;
import com.application.MindSet.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setToolBar();
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        final EditText usernameEditText = binding.username;
        final EditText passwordEditText = binding.password;
        final Button signUpButton = binding.signUp;
        final TextView login = binding.login;
        login.setOnClickListener(view -> {
            startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
        });

        signUpButton.setOnClickListener(view -> {
            if(usernameEditText.getText().length() > 0 && passwordEditText.getText().length() > 0) {
                //TODO: Database call
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
            } else {
                Toast.makeText(getApplicationContext(),"Username or Password not filled",Toast.LENGTH_SHORT).show();
            }
        });

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
}