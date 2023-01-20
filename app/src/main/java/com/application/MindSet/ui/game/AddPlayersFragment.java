package com.application.MindSet.ui.game;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.MindSet.databinding.FragmentFeedBinding;
import com.application.MindSet.dto.User;

import java.util.ArrayList;
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

        EditText search = binding.searchBar;
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });

        return root;
    }

    private void filter(String text) {
        List<String> filteredList = new ArrayList<>();
        for(String u : playersList) {
            if(u.contains(text)){
                filteredList.add(u);
            }
        }
        adapter.filterList(filteredList);
    }
    private void setAdapter() {
        this.adapter = new AddPlayersRecyclerViewAdapter(this.playersList, this.playersInGame, playersIDs);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    public List<String> getInvitedPlayersIDs() {
        return adapter.getInvitedPlayersIDs();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}