package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Address;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messangerapplication.Models.Channel;
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

import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Messages extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private static int MAX_MESSAGE_LENGTH = 1000;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Message");//отвечает за сообщения
    DatabaseReference myRefChannel = FirebaseDatabase.getInstance().getReference().child("MyChannels").child(user.getUid());

    DatabaseReference mR;

    EditText mEditTextMessage;
    ImageButton mSendButton;
    ImageView Icon;
    RecyclerView mMessagesRecycler;
    Button logOff;
    Button Notes;
    Button Settings;
    Button Channels;
    Button Wallet;
    DrawerLayout mDrawerLayout;//боковое меню

    ArrayList<Channel> channels = new ArrayList<>();
    ArrayList<Mess> messages = new ArrayList<>();

    private ActionBarDrawerToggle mToggle;

    public String name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messages);
        setTitle("General chat");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User contact = dataSnapshot.getValue(User.class);
                        name = contact.getName();
                        TextView textView = findViewById(R.id.nickname);
                        textView.setText(name);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        Icon = findViewById(R.id.icon);
        Wallet = findViewById(R.id.nav_wallet);
        Notes = findViewById(R.id.nav_notes);
        logOff =findViewById(R.id.logoff);
        Settings = findViewById(R.id.nav_settings);
        Channels = findViewById(R.id.nav_channels);
        mSendButton = findViewById(R.id.send_message_b);
        mEditTextMessage = findViewById(R.id.message_input);
        mDrawerLayout = findViewById(R.id.drawer);//боковое меню


        mMessagesRecycler = findViewById(R.id.messages_recycler);
        mMessagesRecycler.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        mMessagesRecycler.setLayoutManager(linearLayoutManager);

        final DataAdapter dataAdapter = new DataAdapter(Messages.this,messages);

        mMessagesRecycler.setAdapter(dataAdapter);

        Icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Messages.this,MyUserPageActivity.class);
                startActivity(intent);
                finish();
            }
        });

        Settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Messages.this,SettingsActivity.class));
                finish();
            }
        });

        Wallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Messages.this,WalletActivity.class));
                finish();
            }
        });

        logOff.setOnClickListener(new View.OnClickListener() {// завершение работы приложения
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Messages.this,LogRegActivity.class));
                finish();
            }
        });

        Notes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Messages.this,Notes.class));
                finish();
            }
        });

        Channels.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ChannelActivity.class);
                startActivity(i);
                finish();
            }
        });

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
                mess.setTime(new SimpleDateFormat("HH:mm").format(new Date()));
                mess.setUs(name);
                mess.setMes(msg);
                mess.setUid(user.getUid());
                DatabaseReference mR =  myRef.push();
                String uid = mR.getKey();
                mess.setMesuid(uid);
                mR.setValue(mess);
                mEditTextMessage.setText("");
            }
        });

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String msg = dataSnapshot.child("mes").getValue(String.class);
                String usr = dataSnapshot.child("us").getValue(String.class);
                String uid = dataSnapshot.child("uid").getValue(String.class);
                String time = dataSnapshot.child("time").getValue(String.class);
                Mess mess = new Mess();
                mess.setTime(time);
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

        mToggle = new ActionBarDrawerToggle(Messages.this, mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {//открытие бокового меню и скрытие клавиатуры
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                InputMethodManager imm = (InputMethodManager) Messages.this.getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                assert imm != null;
                if (imm.isAcceptingText()){
                    View v = getCurrentFocus();
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) { }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) { }

            @Override
            public void onDrawerStateChanged(int newState) { }
        });

        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {//боковое менб открытие кнопкой
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context
                .INPUT_METHOD_SERVICE);
        assert imm != null;
        if (imm.isAcceptingText()){
            View v = getCurrentFocus();
            assert v != null;
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        if(mToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (item.getItemId() == R.id.users) {
            Intent i = new Intent(getApplicationContext(), UsersActivity.class);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users, menu);
        return true;
    }
}
