package com.application.MindSet.ui.profile;


import static com.application.MindSet.MainActivity.getContext;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.application.MindSet.MainActivity;
import com.application.MindSet.R;
import com.application.MindSet.SignUpActivity;
import com.application.MindSet.ToolBar;
import com.application.MindSet.databinding.ActivityProfileBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private static final String KEY_NAME = "name";
    private static final String KEY_SURNAME = "surname";
    //  private static final String KEY_EMAIL = "email";
    // private static final String KEY_PASSWORD = "password";


    private ActivityProfileBinding binding;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private FirebaseUser mUser = mAuth.getCurrentUser();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String id = mUser.getUid();

    private EditText nameET, surnameET;
    private EditText emailET, passwordET;
    private ImageView imageView;
    private Button saveBtn;
    private Button cancelBTN;
    private Button editProfile;

    // Dialog myDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ToolBar.setToolBar(getSupportActionBar(), this);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        //setContentView(R.layout.activity_main);
        //  myDialog = new Dialog(this);

        setContentView(binding.getRoot());


        nameET = binding.nameET;
        surnameET = binding.surnameET;
        emailET = binding.emailET;
        // passwordET = binding.passwordET;

        imageView = binding.profileImg;

        saveBtn = binding.saveBTN;
        cancelBTN = binding.cancelBTN;

        // editProfile = findViewById(R.id.editButton);


        db.collection("Profiles").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(documentSnapshot.getString("image"));
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Glide.with(getContext()).load(uri).into(imageView);
                                }
                            });


                            nameET.setText(documentSnapshot.getString(KEY_NAME));
                            surnameET.setText(documentSnapshot.getString(KEY_SURNAME));
                            emailET.setText(documentSnapshot.getString(mUser.getEmail()));
                            //  passwordET.setText(documentSnapshot.getString(KEY_PASSWORD));

                          /*  EditText text = (EditText)findViewById(R.id.surnameET);
                            String value = text.getText().toString();
                            TextView myTextView = findViewById(R.id.surnameETv);
                            myTextView.setText(value);

                           */
                        }
                    }
                });

        Button profileButton = findViewById(R.id.profile_button);
        profileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, ProfileActivity2.class);
                intent.putExtra("id", id);
                startActivity(intent);
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameET.getText().toString();
                String surname = surnameET.getText().toString();
                String email = emailET.getText().toString();
                //    String password = passwordET.getText().toString();

                Map<String, Object> profile = new HashMap<>();
                profile.put(KEY_NAME, name);
                profile.put(KEY_SURNAME, surname);
                //    profile.put(KEY_EMAIL, email);
                //    profile.put(KEY_PASSWORD, password);

                db.collection("Profiles").document(id).set(profile)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(ProfileActivity.this, "Information saved", Toast.LENGTH_SHORT).show();
                                //finish(); //Para voltar para a atividade anterior e não ir parar ao feed sempre
                                Intent i = new Intent(ProfileActivity.this, MainActivity.class);
                                startActivity(i);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProfileActivity.this, "Could not save", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        cancelBTN.setOnClickListener(view -> finish());


    }


    public void logout(View view) {
        mAuth.signOut();
        Intent intent = new Intent(ProfileActivity.this, SignUpActivity.class);
        startActivity(intent);
        finish();
        Toast.makeText(ProfileActivity.this, "Logout Successful", Toast.LENGTH_SHORT).show();
    }


}