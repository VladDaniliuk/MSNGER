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

    public ViewHolderShare(@NonNull View itemView) {
        super(itemView);
        button = itemView.findViewById(R.id.share_item);
        name = itemView.findViewById(R.id.name);
        mail = itemView.findViewById(R.id.mail);
    }
}
