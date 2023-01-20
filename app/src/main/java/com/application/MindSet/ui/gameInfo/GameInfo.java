package com.application.MindSet.ui.gameInfo;

import static com.application.MindSet.MainActivity.getContext;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.application.MindSet.R;
import com.application.MindSet.SignInActivity;
import com.application.MindSet.dto.Message;
import com.application.MindSet.ui.game.AddPlayersFragment;
import com.application.MindSet.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser mUser = mAuth.getCurrentUser();
        String uID = mUser.getUid();


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

                            LinearLayout hsv = findViewById(R.id.playersLL);

                            FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
                            String myId = mUser.getUid();

                            ImageView addButton = findViewById(R.id.addPlayerBTN);
                            final AddPlayersFragment[] addPFrag = new AddPlayersFragment[1];

                            Button addNewPlayers = findViewById(R.id.add_new_players);
                            if(!myId.equals(ownerID)) {
                                addNewPlayers.setVisibility(View.INVISIBLE);
                            } else {
                                addNewPlayers.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(addPFrag[0] != null) {
                                            List<String> newPlayers = addPFrag[0].getInvitedPlayersIDs();
                                            for(String invitedPlayer : newPlayers) {
                                                Message m = new Message(myId, invitedPlayer, "Invited for game",
                                                        true, documentSnapshot.getId(), sport);
                                                db.collection("Messages").add(m).addOnSuccessListener(documentReference1 -> {
                                                    Log.i("InvitePlayer", "Message created");
                                                }).addOnFailureListener(fail -> {
                                                    Log.i("InvitePlayer", "Message failed");
                                                });
                                            }
                                            finish();
                                        } else {
                                            Toast.makeText(GameInfo.this, "Select new players to add", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }

                            if(Utils.MAX_OF_EACH_GAME.get(sport) == participants.size()){
                                hsv.removeView(addButton);
                            } else {
                                addButton.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if(myId.equals(ownerID)) {
                                            //<ID, Name>
                                            HashMap<String, String> usersToInvite = new HashMap<>();
                                            db.collection("Profiles").get().addOnCompleteListener(task -> {
                                                for(QueryDocumentSnapshot document : task.getResult()) {
                                                    if(!participants.contains(document.getId())) {
                                                        usersToInvite.put(document.getId(), document.get("name", String.class));
                                                    }
                                                }
                                                addPFrag[0] = new AddPlayersFragment(new ArrayList<>(usersToInvite.values()),
                                                                        //IDs dos jogadores a convidar
                                                        hsv, new ArrayList<>(usersToInvite.keySet()));
                                                addPFrag[0].show(getSupportFragmentManager(), "ADD PLAYER");
                                            });
                                        } else {
                                            if(!participants.contains(myId)){
                                                Message m = new Message(myId, ownerID, myId, "Can I join your game?",
                                                        true, documentSnapshot.getId(), sport);
                                                db.collection("Messages").add(m).addOnSuccessListener(documentReference1 -> {
                                                    Log.i("InvitePlayer", "Message created");
                                                    finish();
                                                }).addOnFailureListener(fail -> {
                                                    Log.i("InvitePlayer", "Message failed");
                                                });
                                            } else {
                                                Toast.makeText(GameInfo.this, "Already in this game", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }

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

                            for(String s :participants){

                                db.collection("Profiles").document(s).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                    @Override
                                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                                        if(ownerID.equals(myId)) {
                                            if(!documentSnapshot.getId().equals(myId)) {
                                                RelativeLayout rl = new RelativeLayout(hsv.getContext());
                                                ImageView img = new ImageView(rl.getContext());

                                                final float scale = hsv.getContext().getResources().getDisplayMetrics().density;
                                                int oneHunDP = (int) (100 * scale);
                                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(oneHunDP, oneHunDP);
                                                int fiveDP = (int) (5 * scale);
                                                params.setMargins(fiveDP, fiveDP, fiveDP, fiveDP);
                                                img.setBackgroundResource(R.drawable.round_corners_orange);
                                                img.setLayoutParams(params);
                                                img.setId(R.id.imageView);
                                                String location = documentSnapshot.getString("image");
                                                System.out.println(location);
                                                if(location != null && !location.isEmpty()){
                                                    StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(location);
                                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            Glide.with(getContext()).load(uri).into(img);
                                                        }
                                                    });
                                                }else{
                                                    img.setImageResource(R.drawable.ic_outline_person_24);
                                                }
                                                rl.addView(img);
                                                TextView txt = new TextView(rl.getContext());
                                                txt.setText(documentSnapshot.get("name", String.class));
                                                txt.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
                                                params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                                                params.addRule(RelativeLayout.BELOW, img.getId());
                                                params.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                                txt.setLayoutParams(params);
                                                rl.addView(txt);
                                                ImageView x = new ImageView(rl.getContext());
                                                x.setImageResource(R.drawable.ic_baseline_cancel_24);
                                                x.setBackgroundResource(R.drawable.ic_baseline_close_24);
                                                params = new RelativeLayout.LayoutParams((int) (25 * scale), (int) (25 * scale));
                                                params.addRule(RelativeLayout.ALIGN_RIGHT, img.getId());
                                                x.setLayoutParams(params);
                                                x.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View view) {
                                                        RelativeLayout parent = (RelativeLayout) view.getParent();
                                                        hsv.removeView(parent);
                                                        participants.remove(s);
                                                        participants.remove(ownerID);
                                                        db.collection("Games").document(id).update("participantsID", participants);
                                                        participants.add(ownerID);
                                                    }
                                                });
                                                rl.addView(x);
                                                hsv.addView(rl);
                                            }else{
                                                LinearLayout rl = new LinearLayout(hsv.getContext());

                                                rl.setOrientation(LinearLayout.VERTICAL);

                                                TextView txt = new TextView(rl.getContext());
                                                txt.setText("You");
                                                txt.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

                                                ImageView img = new ImageView(hsv.getContext());
                                                final float scale = hsv.getContext().getResources().getDisplayMetrics().density;
                                                int oneHunDP = (int) (100 * scale);
                                                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(oneHunDP, oneHunDP);
                                                int fiveDP = (int) (5 * scale);
                                                params.setMargins(fiveDP, fiveDP, fiveDP, fiveDP);
                                                img.setBackgroundResource(R.drawable.round_corners_orange);
                                                String location = documentSnapshot.getString("image");
                                                System.out.println(location);
                                                if(location != null && !location.isEmpty()){
                                                    StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(location);
                                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                        @Override
                                                        public void onSuccess(Uri uri) {
                                                            Glide.with(getContext()).load(uri).into(img);
                                                        }
                                                    });
                                                }else{
                                                    img.setImageResource(R.drawable.ic_outline_person_24);
                                                }
                                                img.setLayoutParams(params);

                                                rl.addView(img);
                                                rl.addView(txt);

                                                hsv.addView(rl);

                                            }
                                        }else {
                                            LinearLayout rl = new LinearLayout(hsv.getContext());

                                            rl.setOrientation(LinearLayout.VERTICAL);

                                            TextView txt = new TextView(rl.getContext());
                                            txt.setText(documentSnapshot.get("name", String.class));
                                            txt.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);

                                            ImageView img = new ImageView(hsv.getContext());
                                            final float scale = hsv.getContext().getResources().getDisplayMetrics().density;
                                            int oneHunDP = (int) (100 * scale);
                                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(oneHunDP, oneHunDP);
                                            int fiveDP = (int) (5 * scale);
                                            params.setMargins(fiveDP, fiveDP, fiveDP, fiveDP);
                                            img.setBackgroundResource(R.drawable.round_corners_orange);
                                            String location = documentSnapshot.getString("image");
                                            System.out.println(location);
                                            if(location != null && !location.isEmpty()){
                                                StorageReference ref = FirebaseStorage.getInstance().getReferenceFromUrl(location);
                                                ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        Glide.with(getContext()).load(uri).into(img);
                                                    }
                                                });
                                            }else{
                                                img.setImageResource(R.drawable.ic_outline_person_24);
                                            }
                                            img.setLayoutParams(params);

                                            rl.addView(img);
                                            rl.addView(txt);

                                            hsv.addView(rl);
                                        }
                                    }
                                });
                            }
                        }
                    }
                });
    }
}