package com.example.messangerapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView sender;
    TextView message;
    ViewHolder(View itemView) {
        super(itemView);
        message = itemView.findViewById(R.id.message_item);
        sender = itemView.findViewById(R.id.sender);
    }
}
