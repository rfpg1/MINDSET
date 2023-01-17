package com.application.MindSet.ui.message;

import static com.application.MindSet.MainActivity.getContext;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.application.MindSet.R;
import com.application.MindSet.databinding.ActivityMessageBinding;
import com.application.MindSet.dto.Message;
import com.application.MindSet.ui.profile.ProfileActivity;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MessageActivity extends AppCompatActivity {

    private ActivityMessageBinding binding;
    private List<Message> messages;
    private RecyclerView view;
    private String receiver;
    private String sender;
    private String receiverId;
    private EditText message;
    private ImageButton send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        binding = ActivityMessageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        receiver = intent.getStringExtra("receiver");
        receiverId = intent.getStringExtra("receiverId");
        sender = intent.getStringExtra("sender");
        setToolBar(getSupportActionBar(), this, receiver);
        view = binding.view;

        message = binding.message;
        send = binding.sendMessage;
        send.setOnClickListener(view -> {
            Message m = new Message(sender, receiverId, message.getText().toString());
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Messages").add(m).addOnSuccessListener(documentReference -> {
                message.setText("");
                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
            });
        });

        setMessages();
    }

    public static void setToolBar(ActionBar mActionBar, Activity activity, String partner){
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setDisplayShowTitleEnabled(false);
        LayoutInflater mInflater = LayoutInflater.from(activity);
        View mCustomView = mInflater.inflate(R.layout.toolbar_chat, null);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        ImageButton profile = mCustomView.findViewById(R.id.profile);

        profile.setOnClickListener(view ->{
            activity.startActivity(new Intent(activity.getApplicationContext(), ProfileActivity.class));
        });

        ImageButton message = mCustomView.findViewById(R.id.message);
        message.setOnClickListener(view -> {
            activity.startActivity(new Intent(activity.getApplicationContext(), AllMessagesActivity.class));
        });

        TextView receiver = mCustomView.findViewById(R.id.name_receiver);
        receiver.setText(partner);
    }

    private void setMessages() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Messages").addSnapshotListener((value, error) -> {
            messages = new ArrayList<>();
            for(DocumentSnapshot d : value.getDocuments()) {
                Message m = d.toObject(Message.class);
                if(m.getSender().equals(sender) && m.getReceiver().equals(receiverId)) {
                    messages.add(m);
                } else if(m.getSender().equals(receiverId) && m.getReceiver().equals(sender)){
                    messages.add(m);
                }
            }
            Collections.sort(messages, (message, t1) -> {
                if(message == null || t1 == null
                || message.getDate() == null || t1.getDate() == null)
                    return 0;
                return message.getDate().compareTo(t1.getDate());
            });
            setAdapter();
        });
        db.collection("Messages").get().addOnCompleteListener(task -> {
            messages = new ArrayList<>();
            for (QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()) {
                Message m = queryDocumentSnapshot.toObject(Message.class);
                if(m.getSender().equals(sender) && m.getReceiver().equals(receiverId)) {
                    messages.add(m);
                } else if(m.getSender().equals(receiverId) && m.getReceiver().equals(sender)){
                    messages.add(m);
                }
            }
            setAdapter();
        });
    }

    private void setAdapter() {
        MessageRecyclerView adapter = new MessageRecyclerView(messages, sender,receiverId);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        view.setLayoutManager(layoutManager);
        view.setItemAnimator(new DefaultItemAnimator());
        view.setAdapter(adapter);
    }
}