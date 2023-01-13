package com.application.MindSet.ui.game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.MindSet.databinding.FragmentFeedBinding;

import java.util.ArrayList;
import java.util.List;

public class AddPlayersFragment extends DialogFragment {

    private List<String> playersList = new ArrayList<>();

    private FragmentFeedBinding binding;
    private RecyclerView view;

    public AddPlayersFragment(List<String> names) {
        this.playersList = names;
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        view = binding.view;
        setAdapter();

        return root;
    }

    private void setAdapter() {
        AddPlayersRecyclerViewAdapter adapter = new AddPlayersRecyclerViewAdapter(this.playersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}