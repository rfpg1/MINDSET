package com.application.MindSet.ui.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.application.MindSet.R;
import com.application.MindSet.ToolBar;
import com.application.MindSet.databinding.ActivityMainBinding;
import com.application.MindSet.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolBar.setToolBar(getSupportActionBar(), this);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageView profile = binding.profilePic;
        profile.setOnClickListener(view -> Log.i("Teste", "Test"));
        Button save = binding.save;
        save.setOnClickListener(view -> finish());
    }
}