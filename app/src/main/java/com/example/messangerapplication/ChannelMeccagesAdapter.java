package com.example.messangerapplication;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Mess;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChannelMeccagesAdapter extends RecyclerView.Adapter<ViewHolder> {

    private static final int MSG_TYPE_LEFT = 0;
    private static final int MSG_TYPE_RIGHT = 1;
    private static final int IMG_TYPE_LEFT = 2;
    private static final int IMG_TYPE_RIGHT = 3;

    private ArrayList<Mess> messages;
    private LayoutInflater inflater;

    private FirebaseUser U = FirebaseAuth.getInstance().getCurrentUser();

    ChannelMeccagesAdapter(ChannelMeccagesActivity context, ArrayList<Mess> messages) {
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = inflater.inflate(R.layout.item_my_message, parent, false);
            return new ViewHolder(view,1);
        } else if (viewType == MSG_TYPE_LEFT) {
            View view = inflater.inflate(R.layout.item_message, parent, false);
            return new ViewHolder(view,1);
        } else if (viewType == IMG_TYPE_LEFT) {
            View view = inflater.inflate(R.layout.image_message, parent, false);
            return new ViewHolder(view,2);
        } else {
            View view = inflater.inflate(R.layout.image_my_message, parent, false);
            return new ViewHolder(view,2);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mess msg = messages.get(position);
        if(!messages.get(position).getUid().equals(U.getUid())) {
            holder.sender.setText(msg.getUs());
        }
        if(messages.get(position).getType().equals("image")) {
            FirebaseStorage firebaseStorage = FirebaseStorage.getInstance().getReference().getStorage();
            Picasso.with(holder.imageView.getContext())
                    .load(msg.getMes()).resize(500,500)
                    .into(holder.imageView);
            holder.imageView.setVisibility(View.VISIBLE);
            holder.imageView.setOnClickListener(view -> {
                Intent intent = new Intent(holder.imageView.getContext(), ImageActivity.class);
                intent.putExtra("image_id", msg.getMes());
                holder.imageView.getContext().startActivity(intent);
            });
        } else {
            holder.message.setText(msg.getMes());
        }
        holder.time.setText(msg.getTime());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(messages.get(position).getUid().equals(U.getUid())) {
            if (messages.get(position).getType().equals("image")) {
                return IMG_TYPE_RIGHT;
            } else {
                return MSG_TYPE_RIGHT;
            }
        } else {
            if (messages.get(position).getType().equals("image")) {
                return IMG_TYPE_LEFT;
            } else {
                return MSG_TYPE_LEFT;
            }
        }
    }
}
