package com.example.messangerapplication;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
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
        holder.user.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final androidx.appcompat.app.AlertDialog.Builder builder = new AlertDialog.
                        Builder(holder.user.getContext());
                builder.setTitle(holder.name.getText());
                LayoutInflater inflater = LayoutInflater.from(holder.user.getContext());
                View settingsWindow = inflater.inflate(R.layout.write_message_window,null);
                builder.setView(settingsWindow);
                builder.setCancelable(true);
                AlertDialog alertDialog = builder.create();// создать отдельное активити
                alertDialog.show();
                FirebaseDatabase.getInstance().getReference().child("UserMessageList").
                        child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        child(us.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            Button button = settingsWindow.findViewById(R.id.create_dialog);
                            button.setVisibility(View.GONE);
                            button = settingsWindow.findViewById(R.id.open_dialog);
                            button.setVisibility(View.VISIBLE);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(holder.user.getContext(),
                                            UserMessagesActivity.class);
                                    FirebaseDatabase.getInstance().getReference().child(
                                            "UserMessageList").child(FirebaseAuth.getInstance().
                                            getCurrentUser().getUid()).child(us.getUID()).
                                            addListenerForSingleValueEvent(
                                            new ValueEventListener() {
                                                @Override
                                                public void onDataChange(
                                                        @NonNull DataSnapshot dataSnapshot) {
                                                    String uid = dataSnapshot.getValue(String.
                                                            class);
                                                    intent.putExtra("ID", uid);
                                                    intent.putExtra("UID",us.getUID());
                                                    holder.user.getContext().startActivity(intent);
                                                }

                                                @Override
                                                public void onCancelled(
                                                        @NonNull DatabaseError databaseError) { }
                                            });
                                }
                            });
                            button = settingsWindow.findViewById(R.id.delete_dialog);
                            button.setVisibility(View.VISIBLE);
                            button.invalidate();
                        } else {
                            Button button = settingsWindow.findViewById(R.id.create_dialog);
                            button.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Mess message = new Mess();
                                    message.setMes("Теперь вы можете переписываться");
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

                                    Toast.makeText(holder.user.getContext(),"Диалог был " +
                                                    "создан",
                                            Toast.LENGTH_SHORT).show();
                                    alertDialog.dismiss();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });
                return true;
            }
        });
        holder.user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.user.getContext(),
                        UserPageActivity.class);
                intent.putExtra("UID",us.getUID());
                holder.user.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}