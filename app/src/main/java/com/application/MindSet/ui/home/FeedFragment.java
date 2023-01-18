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
import com.application.MindSet.dto.Game;
import com.application.MindSet.utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;

public class FeedFragment extends Fragment {

    private ArrayList<Feed> feedList = new ArrayList<>();

    private FragmentFeedBinding binding;
    private RecyclerView view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentFeedBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        view = binding.view;
        Bundle bundle = getArguments();
        String sport = null;
        if(bundle != null) {
            sport = bundle.getString("Sport");
        }
        setFeed(sport);

        return root;
    }

    private void setAdapter() {
        Collections.sort(feedList, (f, f1) -> {
            if(f == null || f1 == null
                    || f.getDate() == null || f1.getDate() == null)
                return 0;
            return f.getDate().compareTo(f1.getDate());
        });
        FeedRecyclerViewAdapter adapter = new FeedRecyclerViewAdapter(feedList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    private void setFeed(String sport) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Games").get().addOnCompleteListener(task -> {
            feedList = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                Game g = queryDocumentSnapshot.toObject(Game.class);
                if(sport == null || g.getSport().equals(sport)){
                    db.collection("Profiles").document(g.getOwnerID()).get().addOnCompleteListener(task1 -> {
                        DocumentSnapshot documentSnapshot = task1.getResult();
                        String name = documentSnapshot.get("name", String.class);
                        Feed f = new Feed(name, new LatLng(g.getLatitude(), g.getLongitude()).toString(),
                                g.getDate(), g.getParticipantsID().size() + 1 + "/" +
                                Utils.MAX_OF_EACH_GAME.get(g.getSport()));
                        feedList.add(f);
                        setAdapter();
                    });
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}