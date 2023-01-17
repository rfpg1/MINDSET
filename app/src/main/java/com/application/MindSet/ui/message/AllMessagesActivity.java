package com.application.MindSet.ui.message;

import static com.application.MindSet.MainActivity.getContext;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.application.MindSet.ToolBar;
import com.application.MindSet.databinding.ActivityAllMessagesBinding;
import com.application.MindSet.dto.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class AllMessagesActivity extends AppCompatActivity {

    private ActivityAllMessagesBinding binding;
    private List<User> users;
    private RecyclerView view;
    private String sender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllMessagesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ToolBar.setToolBar(getSupportActionBar(), this);
        users = new ArrayList<>();
        view = binding.view;
        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        sender = mUser.getUid();

        Log.i("Name", sender);
        setUsers();

    }

    private void setUsers() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profiles").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                    String id = queryDocumentSnapshot.getId();
                    if(!id.equals(senderID))
                        users.add(new User((String) queryDocumentSnapshot.get("name"), id));
                }
                setAdapter();
            }
        });
    }

    private void setAdapter() {
        UserRecyclerView adapter = new UserRecyclerView(users, getContext(), sender);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }
}