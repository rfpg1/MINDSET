package com.application.MindSet.ui.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.application.MindSet.ToolBar;
import com.application.MindSet.databinding.ActivityChooseLocalBinding;

public class ChooseLocalActivity extends AppCompatActivity {

    private ActivityChooseLocalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChooseLocalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ToolBar.setToolBar(getSupportActionBar(), this);
        Button back = binding.back;
        back.setOnClickListener(view -> finish());
        Button forwrd = binding.forward;
        forwrd.setOnClickListener(view -> startActivity(
                new Intent(ChooseLocalActivity.this, InviteFriendsActivity.class)
        ));

    }
}