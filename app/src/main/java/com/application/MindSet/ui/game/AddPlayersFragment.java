package com.application.MindSet.ui.game;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.MindSet.databinding.FragmentFeedBinding;
import java.util.List;

public class AddPlayersFragment extends DialogFragment {

    private final List<String> playersIDs;
    private LinearLayout playersInGame;
    private List<String> playersList;

    private FragmentFeedBinding binding;
    private RecyclerView view;
    private AddPlayersRecyclerViewAdapter adapter;

    public AddPlayersFragment(List<String> names, LinearLayout playerInGame, List<String> playersIDs) {
        this.playersList = names;
        this.playersInGame = playerInGame;
        this.playersIDs = playersIDs;
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
        this.adapter = new AddPlayersRecyclerViewAdapter(this.playersList, this.playersInGame, playersIDs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    public List<String> getPlayersInGameIDs() {
        return adapter.getPlayersInGameIDs();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}