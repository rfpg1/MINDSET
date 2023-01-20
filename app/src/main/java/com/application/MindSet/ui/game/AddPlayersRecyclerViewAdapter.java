package com.application.MindSet.ui.game;

import android.graphics.Color;
import android.view.LayoutInflater;
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
import com.application.MindSet.dto.User;

import java.util.ArrayList;
import java.util.List;

public class AddPlayersRecyclerViewAdapter extends RecyclerView.Adapter<AddPlayersRecyclerViewAdapter.MyViewHolder> {

    //players that can be invited
    private List<String> playersIDs;
    private LinearLayout hsv;
    //names of players that can be invited
    private List<String> names;
    //ids of players that are invited
    private List<String> invitedPlayersIDs;

    public AddPlayersRecyclerViewAdapter(List<String> playersList, LinearLayout hsv, List<String> playersIDs) {
        this.names = playersList;
        this.hsv = hsv;
        this.playersIDs = playersIDs;
        invitedPlayersIDs = new ArrayList<>();
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
        holder.profilePic.setOnClickListener(v -> {
            RelativeLayout rl = new RelativeLayout(hsv.getContext());

            ImageView img = new ImageView(rl.getContext());
            final float scale = hsv.getContext().getResources().getDisplayMetrics().density;
            int oneHunDP = (int) (100 * scale);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(oneHunDP, oneHunDP);
            int fiveDP = (int) (5 * scale);
            params.setMargins(fiveDP,fiveDP,fiveDP,fiveDP);
            img.setImageResource(R.drawable.ic_outline_person_24);
            img.setBackgroundResource(R.drawable.round_corners_orange);
            img.setLayoutParams(params);
            img.setId(R.id.imageView);
            rl.addView(img);

            TextView txt = new TextView(rl.getContext());
            txt.setText(names.get(position));
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
            params.addRule(RelativeLayout.ALIGN_RIGHT,img.getId());
            x.setLayoutParams(params);
            x.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    RelativeLayout parent =(RelativeLayout) view.getParent();
                    hsv.removeView(parent);
                    invitedPlayersIDs.remove(parent.getTag(R.string.playerId));
                    notifyDataSetChanged();
                }
            });

            rl.addView(x);
            rl.setTag(R.string.playerId, playersIDs.get(position));
            rl.setTag(R.string.playerName, names.get(position));

            hsv.addView(rl);
            invitedPlayersIDs.add(playersIDs.get(position));
            playersIDs.remove(position);
            names.remove(position);
            ((ViewManager) holder.playerLayout.getParent()).removeView(holder.playerLayout);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public List<String> getInvitedPlayersIDs() {
        return invitedPlayersIDs;
    }

    public void filterList(List<String> filteredList) {
        this.names = filteredList;
        notifyDataSetChanged();
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
