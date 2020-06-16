package com.example.messangerapplication;

import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.messangerapplication.Models.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<ViewHolderNote> {
    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
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
    public void onBindViewHolder(@NonNull final ViewHolderNote holder, final int position) {
        final Note nt = notes.get(position);
        holder.note.setText(nt.getNot());
        holder.noteAll.setOnLongClickListener((View.OnLongClickListener) view -> {
            if(holder.delete.getVisibility() == View.GONE) {
                holder.delete.setVisibility(View.VISIBLE);
                int calc = (int)holder.RL.getBottom();
                 int calc2 = (int)holder.RL.getTop();
                int calc3 = calc - calc2 - 5;
                holder.delete.setHeight(calc3);
            } else {
                holder.delete.setVisibility(View.GONE);
            }
            return true;
        });
        holder.delete.setOnClickListener(view -> {
            holder.delete.setVisibility(View.GONE);
            FirebaseDatabase.getInstance().getReference().child("Notes").child(User.getUid()).child(nt.getMesuid()).removeValue();
            notes.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }
}
