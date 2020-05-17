package com.example.messangerapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderPrivMess extends RecyclerView.ViewHolder {
    TextView letter;
    TextView nickname;
    TextView you;
    TextView message;

    public ViewHolderPrivMess(@NonNull View itemView) {
        super(itemView);
        letter = itemView.findViewById(R.id.nickname_letter);
        nickname = itemView.findViewById(R.id.nickname);
        you = itemView.findViewById(R.id.you);
        message = itemView.findViewById(R.id.text);
    }
}
