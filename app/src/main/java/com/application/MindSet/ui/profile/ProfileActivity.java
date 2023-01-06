package com.application.MindSet.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.application.MindSet.MainActivity;
import com.application.MindSet.R;
import com.application.MindSet.SignUpActivity;
import com.application.MindSet.ToolBar;
import com.application.MindSet.databinding.ActivityMainBinding;
import com.application.MindSet.databinding.ActivityProfileBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";

    private ActivityProfileBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String id = mUser.getUid();

    private EditText nameET, surnameET;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolBar.setToolBar(getSupportActionBar(), this);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

        nameET = binding.nameET;
        surnameET = binding.surnameET;
        saveBtn = binding.saveBTN;

        db.collection("Profiles").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            nameET.setText(documentSnapshot.getString(KEY_NAME));
                            surnameET.setText(documentSnapshot.getString(KEY_SURNAME));
                        }
                    }
                });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                String surname = surnameET.getText().toString();

                Map<String, Object> profile = new HashMap<>();
                profile.put(KEY_NAME, name);
                profile.put(KEY_SURNAME, surname);

                db.collection("Profiles").document(id).set(profile)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ProfileActivity.this, "Information saved", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Could not save", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}