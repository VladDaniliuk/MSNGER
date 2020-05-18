package com.example.messangerapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderPrivMess extends RecyclerView.ViewHolder {
    TextView letter;
    TextView nickname;
    ConstraintLayout button;

    public ViewHolderPrivMess(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.button);
        letter = itemView.findViewById(R.id.nickname_letter);
        nickname = itemView.findViewById(R.id.nickname);
    }
}
