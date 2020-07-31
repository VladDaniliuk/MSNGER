package com.example.messangerapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout mess;
    TextView sender;
    ImageView imageView;
    ImageView smileView;
    TextView message;
    TextView time;
    ImageView confirm;
    ViewHolder(View itemView,int a) {
        super(itemView);
        confirm = itemView.findViewById(R.id.confirm);
        message = itemView.findViewById(R.id.message_item);
        imageView = itemView.findViewById(R.id.message_image);
        smileView = itemView.findViewById(R.id.message_smile);
        mess = itemView.findViewById(R.id.message);
        time = itemView.findViewById(R.id.time);
        sender = itemView.findViewById(R.id.sender);

    }
}