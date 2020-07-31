package com.example.messangerapplication;

import android.content.Intent;
import android.database.Cursor;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Share;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ShareAdapter extends RecyclerView.Adapter<ViewHolderShare> {
    FirebaseUser U = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<Share> shares;
    private LayoutInflater inflater;

    ShareAdapter(ShareActivity context, ArrayList<Share> shares) {
        this.shares = shares;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderShare onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_share, parent, false);
        return new ViewHolderShare(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolderShare holder, int position) {
        final Share sh = shares.get(position);
        holder.mail.setText(sh.getMail());
        holder.name.setText(sh.getName());
        holder.button.setOnClickListener(view -> {
            DatabaseReference db = FirebaseDatabase.getInstance().getReference().
                    child("Wallet");
            db.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (!dataSnapshot.child(sh.getUID()).exists()) {
                        Toast.makeText(holder.button.getContext(),"Этот пользователь не" +
                                        " имеет кошелька", Toast.LENGTH_SHORT).show();
                    } else if (sh.getUID().equals(U.getUid())) {
                        Toast.makeText(holder.button.getContext(),"Это ваш аккаунт",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(holder.button.getContext(), SendMoneyActivity.class);
                        intent.putExtra("UID", sh.getUID());
                        holder.button.getContext().startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        });
    }



    @Override
    public int getItemCount() {
        return shares.size();
    }


}
