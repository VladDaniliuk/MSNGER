package com.example.messangerapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;

import com.example.messangerapplication.Models.Channel;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddChannel extends Activity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width),(int)(height*0.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;

        final Button Add = findViewById(R.id.add);
        final Button Create = findViewById(R.id.create);
        final EditText CreateAddres = findViewById(R.id.create_addres);
        final EditText CreateName = findViewById(R.id.create_name);
        final EditText EnterAddres = findViewById(R.id.enter_addres);
        final ImageButton CreateAddresButton = findViewById(R.id.create_addres_button);
        final ImageButton SaveAddresButton = findViewById(R.id.save_addres_button);

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add.setClickable(true);
                Add.setBackgroundColor(Color.parseColor("#6c6c6c"));
                Create.setBackgroundColor(Color.WHITE);
                Create.setClickable(false);
                SaveAddresButton.setVisibility(View.GONE);
                CreateAddresButton.setVisibility(View.VISIBLE);
                EnterAddres.setText("");
                EnterAddres.setVisibility(View.GONE);
                CreateName.setVisibility(View.VISIBLE);
                CreateAddres.setVisibility(View.VISIBLE);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create.setClickable(true);
                Create.setBackgroundColor(Color.parseColor("#6c6c6c"));
                Add.setBackgroundColor(Color.WHITE);
                Add.setClickable(false);
                SaveAddresButton.setVisibility(View.VISIBLE);
                CreateAddresButton.setVisibility(View.GONE);
                EnterAddres.setVisibility(View.VISIBLE);
                CreateName.setVisibility(View.GONE);
                CreateName.setText("");
                CreateAddres.setVisibility(View.GONE);
                CreateAddres.setText("");
            }
        });

        CreateAddresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RelativeLayout root = findViewById(R.id.root_element);
                if(TextUtils.isEmpty(CreateName.getText().toString())) {
                    Snackbar.make(root,"Add name of channel",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(CreateAddres.getText().toString())) {
                    Snackbar.make(root,"Add address of channel",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("Channels");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(CreateAddres.getText().toString()).exists()) {
                            Snackbar.make(root,"Channel with this addres is sushestvuet",
                                    Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            Channel channel = new Channel();
                            channel.setID(CreateAddres.getText().toString());
                            channel.setName(CreateName.getText().toString());
                            FirebaseDatabase.getInstance().getReference().child("MyChannels")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(CreateAddres.getText().toString()).setValue(channel);
                            ref.child(CreateAddres.getText().toString()).setValue(channel);
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        });

        SaveAddresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final RelativeLayout root = findViewById(R.id.root_element);
                if(TextUtils.isEmpty(EnterAddres.getText().toString())) {
                    Snackbar.make(root,"Add addres of channel",Snackbar.LENGTH_SHORT).show();
                    return;
                }
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference()
                        .child("Channels");
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(!dataSnapshot.child(EnterAddres.getText().toString()).exists()) {
                            Snackbar.make(root,"Channel with this addres is not sushestvuet"
                                    ,Snackbar.LENGTH_SHORT).show();
                            return;
                        }
                        else {
                            FirebaseDatabase.getInstance().getReference().child("MyChannels")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .child(EnterAddres.getText().toString()).setValue(dataSnapshot
                                    .child(EnterAddres.getText().toString()).getValue());
                            finish();
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
            }
        });
    }
}
