package com.application.MindSet.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.MindSet.R;
import com.application.MindSet.dto.Game;

import java.util.ArrayList;

public class MyGamesRecyclerView extends RecyclerView.Adapter<MyGamesRecyclerView.MyViewHolder> {

    private ArrayList<Game> myGamesList;

    public MyGamesRecyclerView(ArrayList<Game> gamesList) {
        this.myGamesList = gamesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView sport;
        private TextView date;
        private TextView longitude;
        private TextView latitude;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            date = itemView.findViewById(R.id.date);
            sport = itemView.findViewById(R.id.sport);
        }
    }

    @NonNull
    @Override
    public MyGamesRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_my_games_list_item, parent, false);
        return new MyGamesRecyclerView.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        double latitude = myGamesList.get(position).getLatitude();
        holder.latitude.setText(latitude + "");

        double longitude = myGamesList.get(position).getLongitude();
        holder.longitude.setText(longitude + "");

        String date = myGamesList.get(position).getDate().toString();
        holder.date.setText(date);

        String sport = myGamesList.get(position).getSport();
        holder.sport.setText(sport);
    }

    @Override
    public int getItemCount() {
        return myGamesList.size();
    }

}