package com.example.messangerapplication;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class ViewHolderNote extends RecyclerView.ViewHolder {

    TextView note;
    ViewHolderNote(View itemView) {
        super(itemView);
        note = itemView.findViewById(R.id.note_item);
    }
}
