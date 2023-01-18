package com.application.MindSet.ui.message;

import static com.application.MindSet.MainActivity.getContext;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

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
import java.util.Locale;

public class AllMessagesActivity extends AppCompatActivity {

    private ActivityAllMessagesBinding binding;
    private List<User> users;
    private RecyclerView view;
    private String sender;
    private UserRecyclerView uAdapter;

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

        Log.i("Name", sender);
        setUsers();

    }

    private void filter(String text) {
        List<User> filteredList = new ArrayList<>();
        for(User u : users) {
            if(u.getName().toLowerCase().contains(text)){
                filteredList.add(u);
            }
        }
        uAdapter.filterList(filteredList);
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
        uAdapter = new UserRecyclerView(users, getContext(), sender);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(uAdapter);
    }
}