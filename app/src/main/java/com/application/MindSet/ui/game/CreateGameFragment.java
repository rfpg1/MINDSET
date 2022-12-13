package com.application.MindSet.ui.game;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.application.MindSet.R;
import com.application.MindSet.databinding.FragmentCreateGameBinding;
import com.application.MindSet.ui.game.ChooseLocalActivity;

import java.util.ArrayList;
import java.util.List;

public class CreateGameFragment extends Fragment {

    private FragmentCreateGameBinding binding;
    private ImageButton lastClicked;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentCreateGameBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        Button go = binding.go;
        go.setBackgroundColor(Color.rgb(255, 100, 0));
        go.setOnClickListener(view -> {
            if(lastClicked != null)
                startActivity(new Intent(getActivity(), ChooseLocalActivity.class));
        });

        List<ImageButton> buttons = new ArrayList<>();
        buttons.add(binding.football);
        buttons.add(binding.tennis);
        buttons.add(binding.golf);
        buttons.add(binding.volleyball);
        buttons.add(binding.basketball);
        buttons.add(binding.bowling);

        for(ImageButton b : buttons){
            b.setBackgroundColor(0x000000);
            b.setOnClickListener(view -> {
                if(lastClicked != null){
                    lastClicked.setImageResource(R.drawable.futebol);
                }
                b.setImageResource(R.drawable.futebol_selected);
                lastClicked = b;
            });
        }

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}