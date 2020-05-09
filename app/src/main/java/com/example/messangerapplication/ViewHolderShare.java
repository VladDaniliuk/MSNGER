package com.example.messangerapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderShare extends RecyclerView.ViewHolder {
    TextView name;
    TextView mail;
    ConstraintLayout button;
    ConstraintLayout redButton;
    ConstraintLayout YourProf;

    public ViewHolderShare(@NonNull View itemView) {
        super(itemView);
        YourProf = itemView.findViewById(R.id.your_prof);
        redButton = itemView.findViewById(R.id.red_b);
        button = itemView.findViewById(R.id.share_item);
        name = itemView.findViewById(R.id.name);
        mail = itemView.findViewById(R.id.mail);
    }
}
