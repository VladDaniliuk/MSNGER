package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.messangerapplication.Models.ChannelUser;
import com.example.messangerapplication.Models.User;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChannelUsersActivity extends AppCompatActivity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    ArrayList<User> users = new ArrayList<>();
    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_users);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();
        UID = arguments.get("UID").toString();

        myRef.child("Channels").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setTitle(dataSnapshot.child("name").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        RecyclerView recyclerView = findViewById(R.id.channel_users_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);

        final ChannelUserAdapter userAdapter = new ChannelUserAdapter(ChannelUsersActivity.this, users);

        recyclerView.setAdapter(userAdapter);

        myRef.child("ChannelUsers").child(UID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String ChId = dataSnapshot.getValue(String.class);
                myRef.child("User").child(ChId).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        User contact = new User();
                        contact.setName(dataSnapshot.child("name").getValue(String.class));
                        contact.setUID(ChId);
                        contact.setEmail(dataSnapshot.child("email").getValue(String.class));
                        users.add(contact);
                        userAdapter.notifyDataSetChanged();
                        recyclerView.smoothScrollToPosition(users.size());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {}
                });
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
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(ChannelUsersActivity.this, ChannelMeccagesActivity.class);
                intent.putExtra("UID",UID);
                startActivity(intent);
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
