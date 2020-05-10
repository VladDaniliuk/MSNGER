package com.example.messangerapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class MyRequestsAdapter extends RecyclerView.Adapter<ViewHolderRequest> {

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    FirebaseUser U = FirebaseAuth.getInstance().getCurrentUser();
    private ArrayList<Request> requests;
    private LayoutInflater inflater;

    public MyRequestsAdapter(ShowRequestsActivity context, ArrayList<Request> requests) {
        this.requests = requests;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderRequest onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_request, parent, false);
        return new ViewHolderRequest(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderRequest holder, int position) {
        Request request = requests.get(position);
        holder.money.setText(request.getMoney());
        holder.mail.setText(request.getMail());
        db.child("User").child(request.getUID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                if (dataSnapshot.getKey().equals("name")) {
                    String mail = dataSnapshot.getValue(String.class);
                    holder.name.setText(mail);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    @Override
    public int getItemCount() {
        return requests.size();
    }
}
