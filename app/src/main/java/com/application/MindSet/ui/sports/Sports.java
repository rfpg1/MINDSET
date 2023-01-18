package com.application.MindSet.ui.sports;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.Navigation;

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
            action("Tennis");
        });
        final Button rugbyButton = root.findViewById(R.id.rugby_button);
        rugbyButton.setOnClickListener(view -> {
            action("Rugby");
        });
        final Button footballButton = root.findViewById(R.id.football_button);
        footballButton.setOnClickListener(view -> {
            action("Football");
        });
        final Button basketballButton = root.findViewById(R.id.basketball_button);
        basketballButton.setOnClickListener(view -> {
            action("Basketball");
        });
        final Button volleyballButton = root.findViewById(R.id.volleyball_button);
        volleyballButton.setOnClickListener(view -> {
            action("Volleyball");
        });
        final Button boxingButton = root.findViewById(R.id.boxing_button);
        boxingButton.setOnClickListener(view -> {
            action("Boxing");
        });

        final Button allButton = root.findViewById(R.id.all_button);
        allButton.setOnClickListener(view -> {
            action(null);
        });

        return root;
    }

    private void action(String sport) {
        Bundle bundle = new Bundle();
        bundle.putString("Sport", sport);
        Navigation.findNavController(getActivity(), R.id.nav_host_fragment_activity_main).navigate(R.id.navigation_home, bundle);
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