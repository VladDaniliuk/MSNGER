package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.messangerapplication.Models.Channel;
import com.example.messangerapplication.Models.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class ChannelActivity extends AppCompatActivity {

    RecyclerView mChannelRecycler;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("MyChannels").child(user.getUid());
    ArrayList<Channel> channels = new ArrayList<>();
    ImageButton addChannel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        setTitle("Channels");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        addChannel = findViewById(R.id.add_channel);

        mChannelRecycler = findViewById(R.id.channels_recycle);
        mChannelRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        mChannelRecycler.setLayoutManager(linearLayoutManager);

        final ChannelAdapter channelAdapter = new ChannelAdapter(ChannelActivity.this,channels);

        mChannelRecycler.setAdapter(channelAdapter);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String addres = dataSnapshot.child("id").getValue(String.class);
                String name = dataSnapshot.child("name").getValue(String.class);
                Channel channel = new Channel();
                channel.setName(name);
                channel.setID(addres);
                channels.add(channel);
                channelAdapter.notifyDataSetChanged();
                mChannelRecycler.smoothScrollToPosition(channels.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) { }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) { }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        mChannelRecycler.setHasFixedSize(true);

        addChannel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), AddChannel.class);
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ChannelActivity.this, Messages.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis())
            super.onBackPressed();
        else
            Toast.makeText(getBaseContext(), "Press once again to exit!",
                    Toast.LENGTH_SHORT).show();
        back_pressed = System.currentTimeMillis();
    }
}
