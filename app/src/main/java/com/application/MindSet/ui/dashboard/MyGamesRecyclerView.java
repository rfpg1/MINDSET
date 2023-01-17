package com.application.MindSet.ui.dashboard;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.MindSet.R;
import com.application.MindSet.dto.Feed;
import com.application.MindSet.dto.Game;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class MyGamesRecylerView extends RecyclerView.Adapter<MyGamesRecylerView.MyViewHolder> {

    private ArrayList<Game> myGamesList;

    public MyGamesRecylerView() {

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView sport;
        private TextView date;
        private TextView local;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            local = itemView.findViewById(R.id.local_fill);
            date = itemView.findViewById(R.id.date_fill);
        }
    }

    @NonNull
    @Override
    public MyGamesRecylerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_feed_list_item, parent, false);
        return new MyGamesRecylerView.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

}