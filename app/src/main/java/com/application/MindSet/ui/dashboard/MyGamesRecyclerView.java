package com.application.MindSet.ui.dashboard;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.MindSet.R;
import com.application.MindSet.dto.Game;
import com.application.MindSet.ui.gameInfo.GameInfo;
import com.application.MindSet.utils.Pair;

import java.util.ArrayList;
import java.util.HashMap;

public class MyGamesRecyclerView extends RecyclerView.Adapter<MyGamesRecyclerView.MyViewHolder> {

    private HashMap<Game, Pair<Boolean, String>> myGamesList;

    public MyGamesRecyclerView(HashMap<Game, Pair<Boolean, String>> gamesList) {
        this.myGamesList = gamesList;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView sport;
        private TextView date;
        private TextView longitude;
        private TextView latitude;
        private ImageView star;
        private ImageView icon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            latitude = itemView.findViewById(R.id.latitude);
            longitude = itemView.findViewById(R.id.longitude);
            date = itemView.findViewById(R.id.date);
            sport = itemView.findViewById(R.id.sport);
            star = itemView.findViewById(R.id.star);
            icon = itemView.findViewById(R.id.icon);
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
        Game g = (Game) myGamesList.keySet().toArray()[position];
        double latitude = g.getLatitude();
        holder.latitude.setText(latitude + "");

        double longitude = g.getLongitude();
        holder.longitude.setText(longitude + "");

        String date = g.getDate().toString();
        holder.date.setText(date);

        String sport = g.getSport();
        holder.sport.setText(sport);

       if(myGamesList.get(g).getFirst()){
           holder.star.setVisibility(View.VISIBLE);
       } else {
           holder.star.setVisibility(View.INVISIBLE);
       }

        switch (sport){
            case "Football":
                holder.icon.setBackgroundResource(R.drawable.ic_baseline_sports_soccer_24);
                break;
            case "Basketball":
                holder.icon.setBackgroundResource(R.drawable.ic_baseline_sports_basketball_24);
                break;
            case "Rugby":
                holder.icon.setBackgroundResource(R.drawable.ic_baseline_sports_rugby_24);
                break;
            case "Volleyball":
                holder.icon.setBackgroundResource(R.drawable.ic_baseline_sports_volleyball_24);
                break;
            case "Tennis":
                holder.icon.setBackgroundResource(R.drawable.ic_baseline_sports_tennis_24);
                break;
            case "Boxing":
                holder.icon.setBackgroundResource(R.drawable.ic_baseline_sports_mma_24);
                break;
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(holder.itemView.getContext(), GameInfo.class);
                i.putExtra("id", myGamesList.get(g).getSecond());
                holder.itemView.getContext().startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return myGamesList.size();
    }

}