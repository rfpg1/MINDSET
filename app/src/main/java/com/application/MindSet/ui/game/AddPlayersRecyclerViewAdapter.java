package com.application.MindSet.ui.game;

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

import java.util.ArrayList;
import java.util.List;

public class AddPlayersRecyclerViewAdapter extends RecyclerView.Adapter<AddPlayersRecyclerViewAdapter.MyViewHolder> {

    private List<String> playersIDs;
    private LinearLayout hsv;
    private List<String> names;
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
        return new AddPlayersRecyclerViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.username.setText(names.get(position));
        holder.profilePic.setImageResource(R.drawable.border_round_corners);
        holder.profilePic.setOnClickListener(v -> {
            //Remover o nome e a imagem dos seus pais para os meter noutros pais
            ViewManager parent = (ViewManager) holder.playerLayout.getParent();
            parent.removeView(holder.playerLayout);
            //Adicionar uma tag que vai ser o ID do jogador adicionar
            holder.playerLayout.setTag(playersIDs.get(position));
            //Meter nos novos mais
            hsv.addView(holder.playerLayout);
            //Remover para não continuar a aparecer no recycler view
            names.remove(position);
            //Adicionar como jogador já no jogo
            playersInGameIDs.add(playersIDs.get(position));
        });
    }

    @Override
    public int getItemCount() {
        return names.size();
    }

    public List<String> getPlayersInGameIDs() {
        return playersInGameIDs;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout playerLayout;
        private ImageView profilePic;
        private TextView username;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_add);
            profilePic = itemView.findViewById(R.id.user_profile_pic);
            playerLayout = itemView.findViewById(R.id.layoutPlayer);
        }
    }
}
