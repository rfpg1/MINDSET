package com.application.MindSet.ui.sports;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.application.MindSet.R;
import com.application.MindSet.databinding.FragmentSportsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Sports extends BottomSheetDialogFragment {

    private final FragmentManager manager;
    private FragmentSportsBinding binding;
    private View root;

    public Sports(FragmentManager supportFragmentManager) {
        this.manager = supportFragmentManager;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentSportsBinding.inflate(inflater, container, false);
        root= inflater.inflate(R.layout.fragment_sports, container, false);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void show() {
        this.show(manager, "TAG");
    }
}