package com.example.messangerapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<ViewHolderNote> {

    private ArrayList<Note> notes;
    private LayoutInflater inflater;

    private FirebaseUser U = FirebaseAuth.getInstance().getCurrentUser();

    NoteAdapter(Notes context, ArrayList<Note> notes) {
        this.notes = notes;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ViewHolderNote onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_note, parent, false);
        return new ViewHolderNote(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderNote holder, final int position) {
        final Note nt = notes.get(position);
        holder.note.setText(nt.getNot());
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

}
