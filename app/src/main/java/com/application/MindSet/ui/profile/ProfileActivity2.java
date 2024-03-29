package com.application.MindSet.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.os.Bundle;


import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.application.MindSet.MainActivity;
import com.application.MindSet.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class ProfileActivity2 extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    private ImageView imageView;
    private Button changePictureButton;
    private Button uploadPictureButton;
    private Button takePictureButton;
    private Uri filePath;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        imageView = findViewById(R.id.image_view);
        changePictureButton = findViewById(R.id.change_picture_button);
        uploadPictureButton = findViewById(R.id.upload_picture_button);
        id = getIntent().getStringExtra("id");

        changePictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage();
            }
        });

        uploadPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (filePath != null) {
                    System.out.println(filePath);
                    StorageReference  ref = FirebaseStorageHelper.getInstance().uploadImage(id, filePath, new FirebaseStorageHelper.OnUploadImageListener() {
                        @Override
                        public void onSuccess() {

                            Toast.makeText(ProfileActivity2.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailure(Exception exception) {
                            Toast.makeText(ProfileActivity2.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                    db.collection("Profiles").document(id).update("image", ref.toString());

                } else {
                    Toast.makeText(ProfileActivity2.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }








    private void takePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }






}


