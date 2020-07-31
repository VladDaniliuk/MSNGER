package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.ScrollView;
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
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UserMessagesActivity extends AppCompatActivity {

    static int PAGE_COUNT = 6;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("UserMessages");//отвечает за сообщения
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    private static int MAX_MESSAGE_LENGTH = 1000;

    private static String imageStoragePath;

    ViewPager mSmilesRecycler;
    EditText mEditTextMessage;
    ImageView mSendButton;
    ImageView aSendButton;
    ImageView moreOptions;
    String ID;
    String UID;
    ScrollView scrollView;
    LinearLayout layout;
    PagerAdapter pagerAdapter;

    ImageView smileButton;
    ImageView moneyButton;
    ImageView cameraButton;

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
        ID = arguments.get("ID").toString();
        UID = arguments.get("UID").toString();

        scrollView = findViewById(R.id.scrollView);
        layout = findViewById(R.id.linearLayout);

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

        mSmilesRecycler = findViewById(R.id.smile_recycler);
        mSendButton = findViewById(R.id.send_message_b);
        mEditTextMessage = findViewById(R.id.message_input);
        aSendButton = findViewById(R.id.send_file_b);
        moreOptions = findViewById(R.id.more);

        smileButton = findViewById(R.id.smiles);
        moneyButton = findViewById(R.id.money);
        cameraButton = findViewById(R.id.camera);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        pagerAdapter = new MyFragmentPagerAdapter(this, getSupportFragmentManager());
        mSmilesRecycler.setAdapter(pagerAdapter);

        mEditTextMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams params = mSmilesRecycler.getLayoutParams();
                if (params.height == 500) {
                    params.height = 1;
                    mSmilesRecycler.setLayoutParams(params);
                }
            }
        });

        moneyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseDatabase.getInstance().getReference().child("Wallet").
                        addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.child(user.getUid()).exists()) {
                                    Intent i = new Intent(getApplicationContext(),
                                            SendRequestMoneyMessActivity.class);
                                    i.putExtra("UID", UID);
                                    startActivity(i);
                                } else {
                                    showDialog(1);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
            }
        });

        smileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewGroup.LayoutParams params = mSmilesRecycler.getLayoutParams();
                if (params.height == 500) {
                    params.height = 1;
                    mSmilesRecycler.setLayoutParams(params);
                } else {
                    InputMethodManager imm = (InputMethodManager) UserMessagesActivity.this.getSystemService(Context
                            .INPUT_METHOD_SERVICE);
                    assert imm != null;
                    if (imm.isAcceptingText()){
                        View v = getCurrentFocus();
                        assert v != null;
                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                    }
                    params.height = 500;
                    mSmilesRecycler.setLayoutParams(params);
                }
            }
        });

        mEditTextMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(mEditTextMessage.getText().toString().equals("")) {
                    mSendButton.setVisibility(View.GONE);
                    moreOptions.setVisibility(View.GONE);
                    smileButton.setVisibility(View.VISIBLE);
                    moneyButton.setVisibility(View.VISIBLE);
                    cameraButton.setVisibility(View.VISIBLE);
                    aSendButton.setVisibility(View.VISIBLE);
                } else {
                    moreOptions.setVisibility(View.VISIBLE);
                    smileButton.setVisibility(View.GONE);
                    moneyButton.setVisibility(View.GONE);
                    cameraButton.setVisibility(View.GONE);
                    mSendButton.setVisibility(View.VISIBLE);
                    aSendButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        moreOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClipToOutline(true);

                PopupMenu menu = new PopupMenu(UserMessagesActivity.this, moreOptions);
                menu.inflate(R.menu.popup);
                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        return false;
                    }
                });
                menu.setForceShowIcon(true);
                menu.show();

                menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.menu_share_smiles:
                                ViewGroup.LayoutParams params = mSmilesRecycler.getLayoutParams();
                                if (params.height == 500) {
                                    params.height = 1;
                                    mSmilesRecycler.setLayoutParams(params);
                                } else {
                                    InputMethodManager imm = (InputMethodManager) UserMessagesActivity.this.getSystemService(Context
                                            .INPUT_METHOD_SERVICE);
                                    assert imm != null;
                                    if (imm.isAcceptingText()){
                                        View v = getCurrentFocus();
                                        assert v != null;
                                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    }
                                    params.height = 500;
                                    mSmilesRecycler.setLayoutParams(params);
                                }
                                return true;
                            case R.id.share_photo:
                                Toast.makeText(getBaseContext(), "Press once again to exit!",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.share_money:
                                Toast.makeText(getBaseContext(), "Press once again to exit!",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            case R.id.share_camera:
                                Toast.makeText(getBaseContext(), "Press once again to exit!",
                                        Toast.LENGTH_SHORT).show();
                                return true;
                            default:
                                return true;
                        }
                    }
                });
            }
        });

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                String tm = dataSnapshot.child("time").getValue(String.class);
                String mesUid = dataSnapshot.child("mesuid").getValue(String.class);
                String type = dataSnapshot.child("type").getValue(String.class);

                View view;

                if (type.equals("image")){
                    if(uid.equals(user.getUid())) {
                        view = getLayoutInflater().inflate(R.layout.image_my_message,null);
                    } else {
                        view = getLayoutInflater().inflate(R.layout.image_message,null);
                        TextView sender = view.findViewById(R.id.sender);
                        sender.setText(usr);
                    }
                    ImageView imageView = view.findViewById(R.id.message_image);
                    Picasso.with(imageView.getContext()).load(msg).
                            resize(500,500).into(imageView);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(imageView.getContext(), ImageActivity.class);
                            intent.putExtra("image_id", msg);
                            imageView.getContext().startActivity(intent);
                        }
                    });
                } else if (type.equals("smile")) {
                    if(uid.equals(user.getUid())) {
                        view = getLayoutInflater().inflate(R.layout.smile_my_message,null);
                    } else {
                        view = getLayoutInflater().inflate(R.layout.smile_message,null);
                        TextView sender = view.findViewById(R.id.sender);
                        sender.setText(usr);
                    }
                    ImageView smileView = view.findViewById(R.id.message_smile);
                    Picasso.with(smileView.getContext()).load(msg).into(smileView);
                    smileView.setVisibility(View.VISIBLE);
                } else {
                    if(uid.equals(user.getUid())) {
                        view = getLayoutInflater().inflate(R.layout.item_my_message,null);
                    } else {
                        view = getLayoutInflater().inflate(R.layout.item_message,null);
                        TextView sender = view.findViewById(R.id.sender);
                        sender.setText(usr);
                    }
                    TextView message = view.findViewById(R.id.message_item);
                    message.setText(msg);
                }

                TextView time = view.findViewById(R.id.time);
                time.setText(tm);

                layout.addView(view);

                scrollView.scrollToDescendant(view);
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

    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            final AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle("Ошибка");
            // сообщение
            adb.setMessage("У вас отсутствует электронный кошелёк. Что бы его активировать, " +
                    "перейдите во вкладку Wallet.");
            adb.setCancelable(false);
            // кнопка положительного ответа
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        private Context context = null;

        public MyFragmentPagerAdapter(Context context, FragmentManager fm) {
            super(fm);
            this.context = context;
        }
        @Override
        public Fragment getItem(int position) {
            return PageFragment.newInstance(position,myRef.child(ID));
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case 1:
                if(resultCode == RESULT_OK){
                    final Uri imageUri = imageReturnedIntent.getData();

                    if(!ID.equals("")) {
                        DatabaseReference mR = myRef.child(ID).push();

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