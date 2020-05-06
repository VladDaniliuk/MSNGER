package com.example.messangerapplication;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

class ViewHolderNote extends RecyclerView.ViewHolder {
    LinearLayout noteAll;
    TextView note;
    Button delete;
    RelativeLayout RL;
    ViewHolderNote(View itemView) {
        super(itemView);
        RL = itemView.findViewById(R.id.note_item_with_button);
        delete = itemView.findViewById(R.id.delete);
        note = itemView.findViewById(R.id.note_item);
        noteAll = itemView.findViewById(R.id.lL);
    }
}
