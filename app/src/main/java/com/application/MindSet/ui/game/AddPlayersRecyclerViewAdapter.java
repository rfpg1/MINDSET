package com.application.MindSet.ui.game;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.application.MindSet.R;
import java.util.List;

public class AddPlayersRecyclerViewAdapter extends RecyclerView.Adapter<AddPlayersRecyclerViewAdapter.MyViewHolder> {

    private List<String> names;

    public AddPlayersRecyclerViewAdapter(List<String> playersList) {
        this.names = playersList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_add_players_item, parent, false);
        return new AddPlayersRecyclerViewAdapter.MyViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(names.get(position));
        holder.profilePic.setImageResource(R.drawable.border_round_corners);
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView profilePic;
        private TextView username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_add);
            profilePic = itemView.findViewById(R.id.user_profile_pic);
        }
    }
}
