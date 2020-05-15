package com.example.messangerapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

class ViewHolderUser extends RecyclerView.ViewHolder {
    TextView name;
    TextView mail;
    ConstraintLayout user;
    ViewHolderUser(@NonNull View itemView) {
        super(itemView);
        user = itemView.findViewById(R.id.user);
        name = itemView.findViewById(R.id.name);
        mail = itemView.findViewById(R.id.mail);
    }
}
