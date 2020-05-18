package com.example.messangerapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.PrivMess;

import java.util.ArrayList;

public class PrivateMessageAdapter extends RecyclerView.Adapter<ViewHolderPrivMess> {
    private ArrayList<PrivMess> privMesses;
    private LayoutInflater inflater;

    PrivateMessageAdapter(PrivateMessagesActivity context, ArrayList<PrivMess> privMesses) {
        this.privMesses = privMesses;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolderPrivMess onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_private_messages, parent, false);
        return new ViewHolderPrivMess(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderPrivMess holder, int position) {
        final PrivMess pm = privMesses.get(position);
        holder.nickname.setText(pm.getNickName());
        holder.letter.setText(pm.getNicknameLetter());
        holder.button.setOnClickListener(view -> {
            Intent i = new Intent(holder.button.getContext(), UserMessagesActivity.class);
            i.putExtra("ID", pm.getID());
            i.putExtra("UID",pm.getUID());
            holder.button.getContext().startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return privMesses.size();
    }
}
