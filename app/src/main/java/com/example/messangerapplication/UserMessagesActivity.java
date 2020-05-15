package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messangerapplication.Models.Mess;
import com.example.messangerapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserMessagesActivity extends AppCompatActivity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("UserMessages");//отвечает за сообщения
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private static int MAX_MESSAGE_LENGTH = 1000;


    EditText mEditTextMessage;
    ImageButton mSendButton;
    RecyclerView mMessagesRecycler;

    ArrayList<Mess> messages = new ArrayList<>();

    public String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_messages);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();
        String ID = arguments.get("ID").toString();
        String UID = arguments.get("UID").toString();

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

        mDatabase.child("User").child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setTitle(dataSnapshot.child("name").getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        mDatabase.child("User").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User contact = dataSnapshot.getValue(User.class);
                        name = contact.getName();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) { }
                });

        mSendButton = findViewById(R.id.send_message_b);
        mEditTextMessage = findViewById(R.id.message_input);

        mMessagesRecycler = findViewById(R.id.messages_recycler);
        mMessagesRecycler.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        mMessagesRecycler.setLayoutManager(linearLayoutManager);

        final DataAdapter dataAdapter = new DataAdapter(UserMessagesActivity.this,messages);

        mMessagesRecycler.setAdapter(dataAdapter);

        mSendButton.setOnClickListener(new View.OnClickListener() {//отправка сообщения по клику
            @Override
            public void onClick(View v) {
                String msg = mEditTextMessage.getText().toString();
                if(msg.equals("")){
                    Toast.makeText(getApplicationContext(),"Введите сообщение",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                if(msg.length() > MAX_MESSAGE_LENGTH){
                    Toast.makeText(getApplicationContext(),"Слишком длинное сообщение",Toast
                            .LENGTH_SHORT).show();
                    return;
                }
                Mess mess = new Mess();
                mess.setUs(name);
                mess.setMes(msg);
                mess.setUid(user.getUid());
                DatabaseReference mR =  myRef.child(ID).push();
                String uid = mR.getKey();
                mess.setMesuid(uid);
                mR.setValue(mess);
                mEditTextMessage.setText("");
            }
        });

        myRef.child(ID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String msg = dataSnapshot.child("mes").getValue(String.class);
                String usr = dataSnapshot.child("us").getValue(String.class);
                String uid = dataSnapshot.child("uid").getValue(String.class);
                Mess mess = new Mess();
                mess.setMes(msg);
                mess.setUs(usr);
                mess.setUid(uid);
                messages.add(mess);
                dataAdapter.notifyDataSetChanged();
                mMessagesRecycler.smoothScrollToPosition(messages.size());
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
                startActivity(new Intent(UserMessagesActivity.this, Messages.class));
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