package com.application.MindSet.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.MindSet.databinding.FragmentFeedBinding;
import com.application.MindSet.dto.Feed;

import java.util.ArrayList;

public class FeedFragment extends Fragment {

    private ArrayList<Feed> feedList = new ArrayList<>();

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
        FeedRecyclerViewAdapter adapter = new FeedRecyclerViewAdapter(feedList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    private void setFeed() {
        feedList = new ArrayList<>();
        feedList.add(new Feed("Ricardo", "CG", "10/11/2022", "2/3"));
        feedList.add(new Feed("Gonçalo", "CG", "11/11/2022", "2/3"));
        feedList.add(new Feed("Rafael", "CG", "12/11/2022", "2/3"));
        feedList.add(new Feed("Madalena", "CG", "13/11/2022", "2/3"));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}