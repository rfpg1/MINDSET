package com.application.MindSet.ui.profile;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FirebaseStorageHelper {

    private static FirebaseStorageHelper instance;
    private FirebaseStorage storage;
    private StorageReference storageRef;

    private FirebaseStorageHelper() {
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();
    }

    public static FirebaseStorageHelper getInstance() {
        if (instance == null) {
            instance = new FirebaseStorageHelper();
        }
        return instance;
    }

    public void uploadImage(String userEmail, Uri filePath, final OnUploadImageListener listener) {
        // Create a reference to the user's profile picture in Firebase Storage
        StorageReference profilePicRef = storageRef.child("images/users/" + userEmail + "/profile_pic.jpg");

        // Upload the image to Firebase Storage
        UploadTask uploadTask = profilePicRef.putFile(filePath);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                listener.onFailure(exception);
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                listener.onSuccess();
            }
        });
    }

    public interface OnUploadImageListener {
        void onSuccess();
        void onFailure(Exception exception);
    }



}