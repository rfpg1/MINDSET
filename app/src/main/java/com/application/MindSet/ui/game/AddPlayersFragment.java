package com.application.MindSet.ui.game;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.application.MindSet.R;
import com.application.MindSet.databinding.FragmentFeedBinding;
import com.application.MindSet.ui.home.Feed;
import com.application.MindSet.ui.home.FeedRecyclerViewAdapter;

import java.util.ArrayList;

public class AddPlayersFragment extends Fragment {

    private ArrayList<String> playersList = new ArrayList<>();

    private FragmentFeedBinding binding;
    private RecyclerView view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        view = binding.view;
        setFeed();
        setAdapter();

        return root;
    }

    private void setAdapter() {
        /*FeedRecyclerViewAdapter adapter = new FeedRecyclerViewAdapter(playersList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);*/
    }

    private void setFeed() {
        
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}