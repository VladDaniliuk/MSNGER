package com.example.messangerapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<ViewHolderUser> {
    private ArrayList<User> users;
    private LayoutInflater inflater;

    UserAdapter(UsersActivity context, ArrayList<User> users) {
        this.users = users;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderUser onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_user,parent,false);
        return new ViewHolderUser(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderUser holder, int position) {
        final User us = users.get(position);
        holder.mail.setText(us.getEmail());
        holder.name.setText(us.getName());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
