package com.application.MindSet.ui.message;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.application.MindSet.MainActivity;
import com.application.MindSet.R;
import com.application.MindSet.ToolBar;
import com.application.MindSet.databinding.ActivityChooseLocalBinding;
import com.application.MindSet.databinding.ActivityMessageBinding;

import java.util.zip.Inflater;

public class MessageActivity extends AppCompatActivity {

    private ActivityMessageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ToolBar.setToolBar(getSupportActionBar(), this);

        Button back = binding.back;
        back.setOnClickListener(view -> startActivity(new Intent(MessageActivity.this,
                MainActivity.class)));
    }
}