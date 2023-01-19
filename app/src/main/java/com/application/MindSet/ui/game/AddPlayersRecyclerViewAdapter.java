package com.application.MindSet.ui.game;

import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.MindSet.R;

import java.util.ArrayList;
import java.util.List;

public class AddPlayersRecyclerViewAdapter extends RecyclerView.Adapter<AddPlayersRecyclerViewAdapter.MyViewHolder> {

    //players that can be invited
    private List<String> playersIDs;
    private LinearLayout hsv;
    //names of players that can be invited
    private List<String> names;
    //ids of players that are invited
    private List<String> playersInGameIDs;

    public AddPlayersRecyclerViewAdapter(List<String> playersList, LinearLayout hsv, List<String> playersIDs) {
        this.names = playersList;
        this.hsv = hsv;
        this.playersIDs = playersIDs;
        playersInGameIDs = new ArrayList<>();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_add_players_item, parent, false);
        return new MyViewHolder(itemView).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(names.get(position));
        holder.profilePic.setImageResource(R.drawable.border_round_corners);
        holder.profilePic.setOnClickListener(v -> {
            LinearLayout rl = new LinearLayout(hsv.getContext());
            rl.setOrientation(LinearLayout.VERTICAL);

            ImageView img = new ImageView(rl.getContext());
            final float scale = hsv.getContext().getResources().getDisplayMetrics().density;
            int oneHunDP = (int) (100 * scale);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(oneHunDP, oneHunDP);
            int fiveDP = (int) (5 * scale);
            params.setMargins(fiveDP,fiveDP,fiveDP,fiveDP);
            img.setImageResource(R.drawable.ic_outline_person_24);
            img.setBackgroundResource(R.drawable.round_corners_orange);
            img.setLayoutParams(params);
            rl.addView(img);

            TextView txt = new TextView(rl.getContext());
            txt.setText(names.get(position));
            txt.setTextAlignment(TextView.TEXT_ALIGNMENT_CENTER);
            rl.addView(txt);

            rl.setTag(R.string.playerId, playersIDs.get(position));
            rl.setTag(R.string.playerName, names.get(position));
            rl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    hsv.removeView(view);
                    playersInGameIDs.remove(view.getTag(R.string.playerId));
                    notifyDataSetChanged();
                }
            });
            hsv.addView(rl);
            playersInGameIDs.add(playersIDs.get(position));
            playersIDs.remove(position);
            names.remove(position);
            ((ViewManager) holder.playerLayout.getParent()).removeView(holder.playerLayout);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return playersIDs.size();
    }

    public List<String> getPlayersInGameIDs() {
        return playersInGameIDs;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout playerLayout;
        private ImageView profilePic;
        private TextView username;
        private AddPlayersRecyclerViewAdapter adapter;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_add);
            profilePic = itemView.findViewById(R.id.user_profile_pic);
            playerLayout = itemView.findViewById(R.id.layoutPlayer);
        }

        private MyViewHolder linkAdapter(AddPlayersRecyclerViewAdapter adapter) {
            this.adapter = adapter;
            return this;
        }
    }
}
