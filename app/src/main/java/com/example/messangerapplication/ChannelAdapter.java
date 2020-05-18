package com.example.messangerapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Channel;

import java.util.ArrayList;

public class ChannelAdapter extends RecyclerView.Adapter<ViewHolderChannel> {
    private ArrayList<Channel> channels;
    private LayoutInflater inflater;

    ChannelAdapter(ChannelActivity context, ArrayList<Channel> channels) {
        this.channels = channels;
        this.inflater = LayoutInflater.from(context);
    }


    @NonNull
    @Override
    public ViewHolderChannel onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_channel, parent, false);
        return new ViewHolderChannel(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderChannel holder, int position) {
        final Channel ch = channels.get(position);
        String a = "@" + ch.getID();
        holder.addres.setText(a);
        holder.name.setText(ch.getName());
        holder.button.setOnClickListener(view -> {
            Intent intent = new Intent(holder.button.getContext(), ChannelMeccagesActivity.class);
            intent.putExtra("UID",ch.getID());
            holder.button.getContext().startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return channels.size();
    }
}
