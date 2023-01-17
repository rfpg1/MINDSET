package com.application.MindSet.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.MindSet.R;
import com.application.MindSet.dto.Feed;

import java.util.ArrayList;

public class FeedRecyclerViewAdapter extends RecyclerView.Adapter<FeedRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<Feed> feedList;

    public FeedRecyclerViewAdapter(ArrayList<Feed> feedList){
        this.feedList = feedList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private TextView local;
        private TextView date;
        private TextView players;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_fill);
            local = itemView.findViewById(R.id.local_fill);
            date = itemView.findViewById(R.id.date_fill);
            players = itemView.findViewById(R.id.players_fill);
        }
    }

    @NonNull
    @Override
    public FeedRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_feed_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull FeedRecyclerViewAdapter.MyViewHolder holder, int position) {
        String username = feedList.get(position).getUsername();
        holder.username.setText(username);
        String local = feedList.get(position).getLocal();
        holder.local.setText(local);
        String date = feedList.get(position).getDate();
        holder.date.setText(date);
        String players = feedList.get(position).getNumberOfPlayer();
        holder.players.setText(players);
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }
}
