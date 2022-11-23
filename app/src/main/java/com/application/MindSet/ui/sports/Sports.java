package com.application.MindSet.ui.sports;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.application.MindSet.R;
import com.application.MindSet.ui.sports.tennis.TennisActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Sports extends BottomSheetDialogFragment {

    private final FragmentManager manager;
    private final Context context;

    public Sports(FragmentManager supportFragmentManager, Context context) {
        this.manager = supportFragmentManager;
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_sports, container, false);
        final Button tennisButton = root.findViewById(R.id.tennis_button);
        tennisButton.setOnClickListener(view -> {
            startActivity(new Intent(context, TennisActivity.class));
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void show() {
        this.show(manager, "TAG");
    }
}