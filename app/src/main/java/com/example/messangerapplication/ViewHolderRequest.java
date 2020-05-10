package com.example.messangerapplication;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ViewHolderRequest extends RecyclerView.ViewHolder {
    TextView name;
    TextView money;
    TextView mail;
    RelativeLayout button;
    Button conf;
    Button del;

    public ViewHolderRequest (@NonNull View itemView) {
        super(itemView);
        conf = itemView.findViewById(R.id.confirm);
        del = itemView.findViewById(R.id.decline);
        button = itemView.findViewById(R.id.request_item);
        name = itemView.findViewById(R.id.name);
        money = itemView.findViewById(R.id.money);
        mail = itemView.findViewById(R.id.mail);
    }
}
