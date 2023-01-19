package com.application.MindSet.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.application.MindSet.databinding.FragmentMyGamesBinding;
import com.application.MindSet.dto.Game;
import com.application.MindSet.utils.Pair;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.HashMap;
import java.util.List;

public class MyGamesFragment extends Fragment {

    private HashMap<Game, Pair<Boolean, String>> gamesList;

    private FragmentMyGamesBinding binding;
    private RecyclerView view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentMyGamesBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        view = binding.view;
        gamesList = new HashMap<>();
        setGames();

        return root;
    }

    private void setAdapter() {
        MyGamesRecyclerView adapter = new MyGamesRecyclerView(gamesList);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }

    private void setGames() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Games").get().addOnCompleteListener(task -> {
            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                Game g = queryDocumentSnapshot.toObject(Game.class);

                FirebaseAuth mAuth = FirebaseAuth.getInstance();
                FirebaseUser mUser = mAuth.getCurrentUser();
                String uID = mUser.getUid();

                if(uID.equals(g.getOwnerID())) {
                    gamesList.put(g, new Pair<>(true, queryDocumentSnapshot.getId()));
                } else {
                    List<String> participants = g.getParticipantsID();
                    if(participants.contains(uID)) {
                        gamesList.put(g, new Pair<>(false, queryDocumentSnapshot.getId()));
                    }
                }
            }
            setAdapter();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}