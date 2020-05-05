package com.example.messangerapplication;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class ViewHolderNote extends RecyclerView.ViewHolder {
    LinearLayout noteAll;
    TextView note;
    ViewHolderNote(View itemView) {
        super(itemView);
        note = itemView.findViewById(R.id.note_item);
        noteAll = itemView.findViewById(R.id.note_item_with_button);
    }
}
