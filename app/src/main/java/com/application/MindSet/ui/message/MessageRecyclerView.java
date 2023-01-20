package com.application.MindSet.ui.message;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.application.MindSet.R;
import com.application.MindSet.dto.Game;
import com.application.MindSet.dto.Message;
import com.application.MindSet.utils.Utils;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MessageRecyclerView extends RecyclerView.Adapter<MessageRecyclerView.MyViewHolder> {

    private final HashMap<String, Message> messages;

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private static final int MSG_TYPE_RECEIVED = 2;
    private final String senderID;
    private final String receiverID;

    public MessageRecyclerView(HashMap<String, Message> messages, String senderID, String receiverID) {
        this.messages = messages;
        this.senderID = senderID;
        this.receiverID = receiverID;
    }

    @NonNull
    @Override
    public MessageRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == MSG_TYPE_LEFT) {
            Log.i("MessageRecycler", "View if");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_left, parent, false);
        } else if(viewType == MSG_TYPE_RIGHT){
            Log.i("MessageRecycler", "View else if");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_right, parent, false);
        } else {
            Log.i("MessageRecycler", "View else");
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.invited_item, parent, false);
        }
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MessageRecyclerView.MyViewHolder holder, int position) {
        Message m = (Message) messages.values().toArray()[position];
        String id = (String) messages.keySet().toArray()[position];
        if(!m.getInvited()){
            holder.message.setText(m.getMessage());
        } else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Profiles").document(m.getSender()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    DocumentSnapshot d = task.getResult();
                    String ownerName = d.get("name", String.class);
                    holder.owner.setText(ownerName);
                    holder.date.setText(Utils.getDateDTO(m.getDate()));
                    holder.sport.setText(m.getSport());
                    holder.correct.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(senderID.equals(m.getReceiver())){
                                FirebaseFirestore db = FirebaseFirestore.getInstance();
                                db.collection("Games").document(m.getGameId()).get().addOnCompleteListener(task -> {
                                    Game g = task.getResult().toObject(Game.class);
                                    g.addParticipant(m.getInvitedPlayer());
                                    db.collection("Games").document(m.getGameId()).set(g);
                                    String mSender = m.getSender();
                                    String mReceiver = m.getReceiver();
                                    m.setReceiver(mSender);
                                    m.setSender(mReceiver);
                                    m.setInvited(false);
                                    m.setMessage("Invite Accepted");
                                    db.collection("Messages").document(id).set(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            notifyDataSetChanged();
                                        }
                                    });
                                });
                            }
                        }
                    });
                    holder.wrong.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                            m.setInvited(false);
                            if(user.getUid().equals(senderID)){
                                m.setMessage("Invite withdrawn");
                            } else {
                                m.setMessage("Invite refused");
                            }
                            db.collection("Messages").document(id).set(m).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    notifyDataSetChanged();
                                }
                            });
                        }
                    });
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView message;
        private CircleImageView send;
        private TextView sport;
        private TextView date;
        private TextView owner;
        private ImageView correct;
        private ImageView wrong;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            send = itemView.findViewById(R.id.profile_pic);
            sport = itemView.findViewById(R.id.sport);
            date = itemView.findViewById(R.id.date);
            owner = itemView.findViewById(R.id.owner);
            wrong = itemView.findViewById(R.id.wrong);
            correct = itemView.findViewById(R.id.correct);
        }
    }

    @Override
    public int getItemViewType(int position) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Message m = (Message) messages.values().toArray()[position];
        if(m.getInvited())
            return MSG_TYPE_RECEIVED;
        if(m.getSender().equals(user.getUid())) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }
}
