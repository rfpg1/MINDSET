package com.application.MindSet.ui.sports;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;

import com.application.MindSet.MainActivity;
import com.application.MindSet.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class Sports extends BottomSheetDialogFragment {

    private FragmentManager manager;
    private MainActivity context;
    private static final Sports INSTANCE = new Sports(null, null);

    private Sports(FragmentManager supportFragmentManager, MainActivity context) {
        this.manager = supportFragmentManager;
        this.context = context;
    }

    public static Sports getInstance(){
        return INSTANCE;
    }

    public void setManager(FragmentManager manager){
        this.manager = manager;
    }

    public void setContext(MainActivity context){
        this.context = context;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root= inflater.inflate(R.layout.fragment_sports, container, false);
        final Button tennisButton = root.findViewById(R.id.tennis_button);
        tennisButton.setOnClickListener(view -> {
            //TODO: Aplicar filtro
        });

        return root;
    }


    public Context getAppContext(){
        return context;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void show() {
        this.show(manager, "TAG");
    }
}