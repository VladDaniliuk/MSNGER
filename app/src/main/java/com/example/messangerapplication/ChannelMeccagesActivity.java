package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messangerapplication.Models.Mess;
import com.example.messangerapplication.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ChannelMeccagesActivity extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private static int MAX_MESSAGE_LENGTH = 1000;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Channels");
    DatabaseReference myRefMessages = FirebaseDatabase.getInstance().getReference().child("ChannelMessages");

    EditText mEditTextMessage;
    ImageButton mSendButton;
    ImageButton aSendButton;
    RecyclerView mMessagesRecycler;

    ArrayList<Mess> messages = new ArrayList<>();

    public String name;

    String UID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel_meccages);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User contact = dataSnapshot.getValue(User.class);
                        name = contact.getName();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();
        UID = arguments.get("UID").toString();
        myRef.child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                setTitle(dataSnapshot.child("name").getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        mSendButton = findViewById(R.id.send_message_b);
        mEditTextMessage = findViewById(R.id.message_input);
        aSendButton = findViewById(R.id.send_file_b);

        mMessagesRecycler = findViewById(R.id.messages_recycler);
        mMessagesRecycler.setHasFixedSize(true);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        mMessagesRecycler.setLayoutManager(linearLayoutManager);

        final ChannelMeccagesAdapter channelMeccagesAdapter = new ChannelMeccagesAdapter(ChannelMeccagesActivity.this,messages);
        mMessagesRecycler.setAdapter(channelMeccagesAdapter);

        mEditTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mEditTextMessage.getText().toString().equals("")) {
                    mSendButton.setVisibility(View.GONE);
                    aSendButton.setVisibility(View.VISIBLE);
                } else {
                    mSendButton.setVisibility(View.VISIBLE);
                    aSendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        aSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                //Тип получаемых объектов - image:
                photoPickerIntent.setType("image/*");
                //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
                startActivityForResult(photoPickerIntent, 1);
            }
        });

        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                mess.setType("mess");
                mess.setUid(user.getUid());
                DatabaseReference mR =  myRefMessages.child(UID).push();
                String uid = mR.getKey();
                mess.setMesuid(uid);
                mR.setValue(mess);
                mEditTextMessage.setText("");
            }
        });

        myRefMessages.child(UID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String msg = dataSnapshot.child("mes").getValue(String.class);
                String usr = dataSnapshot.child("us").getValue(String.class);
                String uid = dataSnapshot.child("uid").getValue(String.class);
                String time = dataSnapshot.child("time").getValue(String.class);
                String mesUid = dataSnapshot.child("mesuid").getValue(String.class);
                String type = dataSnapshot.child("type").getValue(String.class);

                Mess mess = new Mess();
                mess.setTime(time);
                mess.setMes(msg);
                mess.setType(type);
                mess.setUs(usr);
                mess.setMesuid(mesUid);
                mess.setUid(uid);
                messages.add(mess);
                channelMeccagesAdapter.notifyDataSetChanged();
                mMessagesRecycler.smoothScrollToPosition(messages.size());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}

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
                startActivity(new Intent(ChannelMeccagesActivity.this, ChannelActivity.class));
                finish();
                return true;
            case R.id.users:
                Intent intent = new Intent(ChannelMeccagesActivity.this, ChannelUsersActivity.class);
                intent.putExtra("UID",UID);
                startActivity(intent);
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    final Uri imageUri = imageReturnedIntent.getData();

                    DatabaseReference mR =  myRefMessages.child(UID).push();

                    StorageReference mStorageRef = FirebaseStorage.getInstance().getReference().child("image").child(mR.getKey());

                    mStorageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                            mStorageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Mess mess = new Mess();
                                    mess.setMes(uri.toString());
                                    mess.setTime(new SimpleDateFormat("HH:mm").format(new Date()));
                                    mess.setUs(name);
                                    mess.setType("image");
                                    mess.setUid(user.getUid());

                                    String uid = mR.getKey();
                                    mess.setMesuid(uid);
                                    mR.setValue(mess);
                                }
                            });


                        }
                    });
                }
        }}

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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.users, menu);
        return true;
    }
}
