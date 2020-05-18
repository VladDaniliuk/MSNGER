package com.example.messangerapplication;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView sender;
    TextView message;
    TextView time;
    ViewHolder(View itemView) {
        super(itemView);
        time = itemView.findViewById(R.id.time);
        sender = itemView.findViewById(R.id.sender);
        message = itemView.findViewById(R.id.message_item);
    }

}
