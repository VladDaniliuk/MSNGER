package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Layout;
import android.text.PrecomputedText;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messangerapplication.Models.Mess;
import com.example.messangerapplication.Models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

public class Messages extends AppCompatActivity {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    static int PAGE_COUNT = 3;

    private static int MAX_MESSAGE_LENGTH = 1000;
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Message");//отвечает за сообщения
    DatabaseReference myRefSmiles = FirebaseDatabase.getInstance().getReference().child("Smiles");

    DatabaseReference mR;

    TabLayout mTabLayout;

    EditText mEditTextMessage;
    ImageButton mSendButton;
    ImageButton stSendButton;
    ImageButton aSendButton;
    ImageView Icon;
    RecyclerView mMessagesRecycler;
    ViewPager mSmilesRecycler;
    PagerAdapter pagerAdapter;
    Button logOff;
    Button Notes;
    Button Settings;
    Button Channels;
    LinearLayout layout;
    Button Wallet;
    DrawerLayout mDrawerLayout;//боковое меню

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
        stSendButton = findViewById(R.id.send_sticker_b);
        aSendButton = findViewById(R.id.send_file_b);
        mEditTextMessage = findViewById(R.id.message_input);
        mDrawerLayout = findViewById(R.id.drawer);//боковое меню
        mMessagesRecycler = findViewById(R.id.messages_recycler);

        mSmilesRecycler = findViewById(R.id.smile_recycler);
        pagerAdapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
        mSmilesRecycler.setAdapter(pagerAdapter);

        layout = findViewById(R.id.linearLayout);

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

        mEditTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                stSendButton.setVisibility(View.GONE);
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mEditTextMessage.getText().toString().equals("")) {
                    mSendButton.setVisibility(View.GONE);
                    stSendButton.setVisibility(View.VISIBLE);
                    aSendButton.setVisibility(View.VISIBLE);
                } else {
                    stSendButton.setVisibility(View.GONE);
                    mSendButton.setVisibility(View.VISIBLE);
                    aSendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        mEditTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams params = mSmilesRecycler.getLayoutParams();
                if (params.height == 500) {
                    params.height = 0;
                    mSmilesRecycler.setLayoutParams(params);
                }
            }
        });

        stSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams params = mSmilesRecycler.getLayoutParams();
                if (params.height == 0) {
                    InputMethodManager imm = (InputMethodManager) Messages.this.getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    assert imm != null;
                    if (imm.isAcceptingText()){
                        View v = getCurrentFocus();
                        assert v != null;
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    params.height = 500;
                    mSmilesRecycler.setLayoutParams(params);
                } else {
                    params.height = 0;
                    mSmilesRecycler.setLayoutParams(params);
                }
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
                mess.setType("mess");
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

        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mTabLayout.setupWithViewPager(mSmilesRecycler);

        mToggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private Context context = null;

        public MyFragmentPagerAdapter(Context context,FragmentManager fm) {
            super(fm);
            this.context = context;
        }
        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position);
        }
        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
        @Override
        public String getPageTitle(int position) {
            return (PageFragment.getTitle(context, position));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    final Uri imageUri = imageReturnedIntent.getData();

                    DatabaseReference mR =  myRef.push();

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
        }
    }


    private static long back_pressed;

    @Override
    public void onBackPressed() {
        ViewGroup.LayoutParams params = mSmilesRecycler.getLayoutParams();
        if (params.height == 0) {
            if (back_pressed + 2000 > System.currentTimeMillis())
                super.onBackPressed();
            else
                Toast.makeText(getBaseContext(), "Press once again to exit!",
                        Toast.LENGTH_SHORT).show();
            back_pressed = System.currentTimeMillis();
        } else {
            params.height = 0;
            mSmilesRecycler.setLayoutParams(params);
        }
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