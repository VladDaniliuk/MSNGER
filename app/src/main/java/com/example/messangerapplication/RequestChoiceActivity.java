package com.example.messangerapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Share;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import okhttp3.Request;

public class RequestChoiceActivity extends Activity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("User");//отвечает за сообщения
    RecyclerView mRequestRecycler;
    ArrayList<Share> requests = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height * 0.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;

        mRequestRecycler = findViewById(R.id.share_recycler);
        mRequestRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        mRequestRecycler.setLayoutManager(linearLayoutManager);
        final RequestAdapter requestAdapter = new RequestAdapter(RequestChoiceActivity.this,requests);

        mRequestRecycler.setAdapter(requestAdapter);

        final ConstraintLayout Request = findViewById(R.id.share);

        Request.setOnClickListener(view ->
                Toast.makeText(getApplicationContext(),"Запросить валюту с помощью qr " +
                        "кода можно будет позже",
                Toast.LENGTH_SHORT).show());

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String mail = dataSnapshot.child("email").getValue(String.class);
                String UID = dataSnapshot.getKey();
                Share reqest = new Share();
                reqest.setUID(UID);
                reqest.setName(name);
                reqest.setMail(mail);
                requests.add(reqest);
                requestAdapter.notifyDataSetChanged();
                mRequestRecycler.smoothScrollToPosition(requests.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    protected void onPause() {
        super.onPause();
        finish();
    }
}
