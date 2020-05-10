package com.example.messangerapplication;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ViewHolderRequest extends RecyclerView.ViewHolder {
    TextView name;
    TextView money;
    TextView mail;

    public ViewHolderRequest (@NonNull View itemView) {
        super(itemView);
        name = itemView.findViewById(R.id.name);
        money = itemView.findViewById(R.id.money);
        mail = itemView.findViewById(R.id.mail);
    }
}
