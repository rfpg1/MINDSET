package com.application.MindSet.ui.game;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.application.MindSet.MainActivity;
import com.application.MindSet.R;
import com.application.MindSet.databinding.ActivityInviteFriendsBinding;

public class InviteFriendsActivity extends AppCompatActivity {

    private ActivityInviteFriendsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInviteFriendsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Button back = binding.back;
        back.setOnClickListener(view -> finish());
        Button forward = binding.forward;
        forward.setOnClickListener(view -> {
            startActivity(new Intent(InviteFriendsActivity.this, MainActivity.class));
            finish();
        });
    }
}