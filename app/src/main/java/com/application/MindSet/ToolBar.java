package com.application.MindSet;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.ActionBar;

import com.application.MindSet.ui.message.MessageActivity;
import com.application.MindSet.ui.profile.ProfileActivity;

public class ToolBar {

    public static void setToolBar(ActionBar mActionBar, Activity activity){
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(activity);
        View mCustomView = mInflater.inflate(R.layout.toolbar, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        ImageButton profile = mCustomView.findViewById(R.id.profile);
        profile.setOnClickListener(view ->{
            if(!(activity instanceof ProfileActivity))
                activity.startActivity(new Intent(activity.getApplicationContext(), ProfileActivity.class));
        });
        ImageButton message = mCustomView.findViewById(R.id.message);
        message.setOnClickListener(view -> {
            if(!(activity instanceof MessageActivity)){
                activity.startActivity(new Intent(activity.getApplicationContext(), MessageActivity.class));
            }
        });
    }
}
