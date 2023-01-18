package com.application.MindSet.ui.message;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.application.MindSet.R;
import com.application.MindSet.dto.User;

import java.util.List;

public class UserRecyclerView extends RecyclerView.Adapter<UserRecyclerView.MyViewHolder> {


    private List<User> users;
    private final Context context;
    private final String sender;

    public UserRecyclerView(List<User> users, Context context, String sender) {
        this.users = users;
        this.context = context;
        this.sender = sender;
    }

    public void filterList(List<User> filteredList) {
        this.users = filteredList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView username;
        private RelativeLayout layoutUser;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username_add);
            layoutUser = itemView.findViewById(R.id.layoutPlayer);
        }
    }

    @NonNull
    @Override
    public UserRecyclerView.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_add_players_item, parent, false);
        return new UserRecyclerView.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        User user = users.get(position);
        holder.username.setText(user.getName());
        holder.layoutUser.setOnClickListener(view -> {
            Intent i = new Intent(context, MessageActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.putExtra("receiver", user.getName());
            i.putExtra("receiverId", user.getId());
            i.putExtra("sender", sender);
            context.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

}