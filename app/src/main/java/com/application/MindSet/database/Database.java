package com.application.MindSet.database;

import android.util.Log;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    static List<String> getUsers() {
        List<String> names = new ArrayList<>();
        db.collection("Profiles").get().addOnSuccessListener(queryDocumentSnapshots ->
                queryDocumentSnapshots.getDocuments().forEach(user -> {
                            Log.i("USER", (String) user.get("name"));
                            names.add((String) user.get("name"));
                        }
                ));
        names.add("Teste");
        return names;
    }
}
