package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable.Creator;
import android.os.PowerManager.WakeLock;
import android.text.Layout;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messangerapplication.Models.Mess;
import com.example.messangerapplication.Models.Note;
import com.example.messangerapplication.Models.User;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Messages extends AppCompatActivity {

    public int l = 10;

    private static int MAX_MESSAGE_LENGTH = 1000;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Message");//отвечает за сообщения

    DatabaseReference mR;

    EditText mEditTextMessage;
    ImageButton mSendButton;
    RecyclerView mMessagesRecycler;
    Button logOff;
    Button Notes;
    Button Settings;
    Button Channels;
    Button AddChannel;
    Button Wallet;
    RecyclerView channels;
    DrawerLayout mDrawerLayout;//боковое меню
    DrawerLayout drawerLayout;

    ArrayList<Mess> messages = new ArrayList<>();

    private ActionBarDrawerToggle toggle;
    private ActionBarDrawerToggle mToggle;

    public String name;

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


    DataAdapter dataAdapter;

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

        Wallet = findViewById(R.id.nav_wallet);
        Notes = findViewById(R.id.nav_notes);
        logOff =findViewById(R.id.logoff);
        Settings = findViewById(R.id.nav_settings);
        Channels = findViewById(R.id.nav_channels);
        channels = findViewById(R.id.channels_recycle);
        AddChannel = findViewById(R.id.add_channel);
        mSendButton = findViewById(R.id.send_message_b);
        mEditTextMessage = findViewById(R.id.message_input);
        mDrawerLayout = findViewById(R.id.drawer);//боковое меню
        drawerLayout = findViewById(R.id.drawer2);

        mMessagesRecycler = findViewById(R.id.messages_recycler);
        mMessagesRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);
        mMessagesRecycler.setLayoutManager(linearLayoutManager);

        final DataAdapter dataAdapter = new DataAdapter(Messages.this,messages);

        mMessagesRecycler.setAdapter(dataAdapter);

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
                if (channels.getVisibility() == View.INVISIBLE) {
                    ViewGroup.LayoutParams params = channels.getLayoutParams();
                    params.height = 300;
                    channels.setLayoutParams(params);
                    params.height = Settings.getHeight();
                    channels.setVisibility(View.VISIBLE);
                    AddChannel.setLayoutParams(params);
                    AddChannel.setVisibility(View.VISIBLE);
                }
                else {
                    ViewGroup.LayoutParams params = channels.getLayoutParams();
                    params.height = 0;
                    channels.setLayoutParams(params);
                    channels.setVisibility(View.INVISIBLE);
                    AddChannel.setLayoutParams(params);
                    AddChannel.setVisibility(View.INVISIBLE);
                }
            }
        });

        AddChannel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams params = channels.getLayoutParams();
                params.height = 0;
                channels.setLayoutParams(params);
                channels.setVisibility(View.INVISIBLE);
                AddChannel.setLayoutParams(params);
                AddChannel.setVisibility(View.INVISIBLE);
                mDrawerLayout.closeDrawers();
                Intent i = new Intent(getApplicationContext(), AddChannel.class);
                startActivity(i);
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
                Mess mess = new Mess();
                mess.setMes(msg);
                mess.setUs(usr);
                mess.setUid(uid);
                messages.add(mess);
                dataAdapter.notifyDataSetChanged();
                mMessagesRecycler.smoothScrollToPosition(messages.size());
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



        mToggle = new ActionBarDrawerToggle(Messages.this, mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);

        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {//открытие бокового меню и скрытие клавиатуры

            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                drawerLayout.closeDrawers();
                InputMethodManager imm = (InputMethodManager) Messages.this.getSystemService(Context
                        .INPUT_METHOD_SERVICE);
                assert imm != null;
                if (imm.isAcceptingText()){
                    View v = getCurrentFocus();
                    assert v != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                ViewGroup.LayoutParams params = channels.getLayoutParams();
                params.height = 0;
                channels.setLayoutParams(params);
                channels.setVisibility(View.INVISIBLE);
                AddChannel.setLayoutParams(params);
                AddChannel.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                //drawerLayout.closeDrawers();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        toggle = new ActionBarDrawerToggle(Messages.this, drawerLayout,R.string.open,R.string.close);

        drawerLayout.addDrawerListener(toggle);

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
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
            public void onDrawerOpened(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });



        toggle.syncState();
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
            drawerLayout.closeDrawers();//
            return true;
        }
        if(item.getItemId() == R.id.users) {
            if (drawerLayout.isDrawerOpen(Gravity.RIGHT)) {
                drawerLayout.closeDrawers();
            } else {
                mDrawerLayout.closeDrawers();
                drawerLayout.openDrawer(Gravity.RIGHT);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users, menu);
        return true;
    }
}
