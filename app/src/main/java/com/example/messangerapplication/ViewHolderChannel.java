package com.example.messangerapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolderChannel extends RecyclerView.ViewHolder {
    TextView name;
    TextView addres;
    ConstraintLayout button;
    ViewHolderChannel(@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.channel_name);
        addres = itemView.findViewById(R.id.channel_addres);
        button = itemView.findViewById(R.id.channel_item);
    }
}
