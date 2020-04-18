package com.example.messangerapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Mess;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DataAdapter extends RecyclerView.Adapter<ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private ArrayList<Mess> messages;
    private LayoutInflater inflater;

    DatabaseReference users = FirebaseDatabase.getInstance().getReference();

    FirebaseUser U = FirebaseAuth.getInstance().getCurrentUser();

    DataAdapter(Messages context, ArrayList<Mess> messages){
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == MSG_TYPE_RIGHT) {
            View view = inflater.inflate(R.layout.item_my_message, parent, false);
            return new ViewHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_message, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Mess msg = messages.get(position);
        holder.message.setText(msg.getMes());
        holder.sender.setText(msg.getUs());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
/*
    @Override
    public int getItemViewType(int position) {

        if(messages.get(position).getUs() == users.child("User").child(U.getUid()).child("name").toString()) {
            return MSG_TYPE_RIGHT;
        } else {
            return MSG_TYPE_LEFT;
        }
    }*/
}
