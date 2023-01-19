package com.application.MindSet.ui.gameInfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.application.MindSet.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GameInfo extends AppCompatActivity {


    private String ownerID;
    private String sport;
    private String local;
    private Date date;
    private ArrayList<String> participants;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_info);

        String id = getIntent().getExtras().get("id").toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("Games").document(id).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()){
                            ownerID = documentSnapshot.get("ownerID", String.class);
                            sport = documentSnapshot.get("sport", String.class);
                            local = documentSnapshot.get("localName", String.class);
                            date = documentSnapshot.get("date", Date.class);
                            participants = (ArrayList<String>) documentSnapshot.get("participantsID");
                            participants.add(ownerID);

                            switch (sport){
                                case "Football":
                                    findViewById(R.id.gameImage).setBackgroundResource(R.drawable.ic_baseline_sports_soccer_24);
                                    break;
                                case "Basketball":
                                    findViewById(R.id.gameImage).setBackgroundResource(R.drawable.ic_baseline_sports_basketball_24);
                                    break;
                                case "Rugby":
                                    findViewById(R.id.gameImage).setBackgroundResource(R.drawable.ic_baseline_sports_rugby_24);
                                    break;
                                case "Volleyball":
                                    findViewById(R.id.gameImage).setBackgroundResource(R.drawable.ic_baseline_sports_volleyball_24);
                                    break;
                                case "Tennis":
                                    findViewById(R.id.gameImage).setBackgroundResource(R.drawable.ic_baseline_sports_tennis_24);
                                    break;
                                case "Boxing":
                                    findViewById(R.id.gameImage).setBackgroundResource(R.drawable.ic_baseline_sports_mma_24);
                                    break;
                            }

                            TextView dateTV = findViewById(R.id.dateBTN);
                            dateTV.setText(date.toString());

                            TextView localTV = findViewById(R.id.localBTN);
                            localTV.setText(local);


                            LinearLayout hsv = findViewById(R.id.playersLL);

                            for(String s :participants){
                                ImageView img = new ImageView(hsv.getContext());
                                final float scale = hsv.getContext().getResources().getDisplayMetrics().density;
                                int oneHunDP = (int) (100 * scale);
                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(oneHunDP, oneHunDP);
                                int fiveDP = (int) (5 * scale);
                                params.setMargins(fiveDP,fiveDP,fiveDP,fiveDP);
                                img.setImageResource(R.drawable.ic_outline_person_24);
                                img.setBackgroundResource(R.drawable.round_corners_orange);
                                img.setLayoutParams(params);
                                hsv.addView(img);
                            }
                        }
                    }
                });
    }
}