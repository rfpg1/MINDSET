package com.application.MindSet.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.MindSet.R;
import com.application.MindSet.dto.Feed;
import com.application.MindSet.ui.game.MapsActivity;
import com.application.MindSet.ui.gameInfo.GameInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.MyViewHolder> {

    private List<Feed> feedList;

    public FeedRecyclerViewAdapter(List<Feed> feedList){
        this.feedList = feedList;
    }

    public void filterList(List<Feed> filteredList) {
        this.feedList = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private TextView local;
        private TextView date;
        private TextView players;
        private ImageView image;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_fill);
            local = itemView.findViewById(R.id.local_fill);
            date = itemView.findViewById(R.id.date_fill);
            players = itemView.findViewById(R.id.players_fill);
            image = itemView.findViewById(R.id.icon);
        }
    }

    @NonNull
    @Override
    public FeedRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_feed_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedRecyclerViewAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        String username = feedList.get(position).getUsername();
        holder.username.setText(username);
        String local = feedList.get(position).getLocal();
        holder.local.setText(local);
        String date = feedList.get(position).getDate().toString();
        holder.date.setText(date);
        String players = feedList.get(position).getNumberOfPlayer();
        holder.players.setText(players);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.itemView.getContext(), GameInfo.class);
                i.putExtra("id", feedList.get(position).getGameID());
                holder.itemView.getContext().startActivity(i);
            }
        });

        switch (feedList.get(position).getSport()){
            case "Football":
                holder.image.setBackgroundResource(R.drawable.ic_baseline_sports_soccer_24);
                break;
            case "Basketball":
                holder.image.setBackgroundResource(R.drawable.ic_baseline_sports_basketball_24);
                break;
            case "Rugby":
                holder.image.setBackgroundResource(R.drawable.ic_baseline_sports_rugby_24);
                break;
            case "Volleyball":
                holder.image.setBackgroundResource(R.drawable.ic_baseline_sports_volleyball_24);
                break;
            case "Tennis":
                holder.image.setBackgroundResource(R.drawable.ic_baseline_sports_tennis_24);
                break;
            case "Boxing":
                holder.image.setBackgroundResource(R.drawable.ic_baseline_sports_mma_24);
                break;
        }

    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}
