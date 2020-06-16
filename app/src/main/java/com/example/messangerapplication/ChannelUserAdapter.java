package com.example.messangerapplication;

import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.ChannelUser;
import com.example.messangerapplication.Models.Mess;
import com.example.messangerapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChannelUserAdapter extends RecyclerView.Adapter<ViewHolderUser> {
    private ArrayList<User> users;
    private LayoutInflater inflater;

    ChannelUserAdapter(ChannelUsersActivity context, ArrayList<User> users) {
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
        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("UserMessageList").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        child(us.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (!dataSnapshot.exists()) {
                            Mess message = new Mess();
                            message.setMes("Теперь вы можете переписываться");
                            message.setType("mess");
                            message.setUid("0");
                            message.setUs("Admin");

                            DatabaseReference mR = FirebaseDatabase.getInstance().
                                    getReference().child("UserMessageList").
                                    child(FirebaseAuth.getInstance().getCurrentUser().
                                            getUid()).child(us.getUID()).push();

                            String uid = mR.getKey();

                            FirebaseDatabase.getInstance().getReference()
                                    .child("UserMessageList").child(FirebaseAuth.
                                    getInstance().getCurrentUser().getUid()).child(us.
                                    getUID()).setValue(uid);

                            FirebaseDatabase.getInstance().getReference().
                                    child("UserMessageList").child(us.getUID()).
                                    child(FirebaseAuth.getInstance().getCurrentUser().
                                            getUid()).setValue(uid);

                            mR = FirebaseDatabase.getInstance().
                                    getReference().child("UserMessages").child(uid).push();

                            message.setMesuid(uid);

                            mR.setValue(message);

                            Toast.makeText(holder.user.getContext(),"Нажмите еще " +
                                            "раз, для открытия соообщений",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Intent intent = new Intent(holder.user.getContext(),
                                    UserMessagesActivity.class);
                            FirebaseDatabase.getInstance().getReference().
                                    child("UserMessageList").child(FirebaseAuth.
                                    getInstance().getCurrentUser().getUid()).
                                    child(us.getUID()).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(
                                                @NonNull DataSnapshot dataSnapshot) {
                                            String uid = dataSnapshot.getValue(String.class);
                                            intent.putExtra("ID", uid);
                                            intent.putExtra("UID",us.getUID());
                                            holder.user.getContext().startActivity(intent);
                                        }

                                        @Override
                                        public void onCancelled(
                                                @NonNull DatabaseError databaseError) { }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}