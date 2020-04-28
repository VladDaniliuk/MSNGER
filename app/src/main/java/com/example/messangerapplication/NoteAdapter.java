package com.example.messangerapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Note;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<ViewHolderNote> {

    private ArrayList<Note> notes;
    private LayoutInflater inflater;

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
