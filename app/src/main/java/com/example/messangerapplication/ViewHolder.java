package com.example.messangerapplication;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    RelativeLayout mess;
    TextView sender;
    TextView message;
    TextView time;
    ViewHolder(View itemView) {
        super(itemView);
        mess = itemView.findViewById(R.id.message);
        time = itemView.findViewById(R.id.time);
        sender = itemView.findViewById(R.id.sender);
        message = itemView.findViewById(R.id.message_item);
    }
}
