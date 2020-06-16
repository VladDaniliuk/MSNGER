package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messangerapplication.Models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentActivity extends AppCompatActivity {

    private static int MAX_POST_LENGTH = 1000;
    DatabaseReference myRef;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Bundle arguments = getIntent().getExtras();
        String ID = arguments.get("ID").toString();
        String UID = arguments.get("UID").toString();
        myRef = FirebaseDatabase.getInstance().getReference().child("Post").child(UID).child(ID);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        ScrollView scrollView = findViewById(R.id.scrollView);
        LinearLayout layout = findViewById(R.id.linearLayout);
        ImageButton sendCom = findViewById(R.id.send_note_b);
        EditText editTextPost = findViewById(R.id.note_input);

        sendCom.setVisibility(View.GONE);

        View view = getLayoutInflater().inflate(R.layout.item_post,null);
        layout.addView(view);

        Post post = new Post();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                post.setId(dataSnapshot.child("id").getValue(String.class));
                post.setTime(dataSnapshot.child("time").getValue(String.class));
                post.setUid(dataSnapshot.child("uid").getValue(String.class));
                post.setText(dataSnapshot.child("text").getValue(String.class));
                post.setName(dataSnapshot.child("name").getValue(String.class));

                TextView name = view.findViewById(R.id.name);
                TextView time = view.findViewById(R.id.time);
                TextView textPost = view.findViewById(R.id.post);
                ImageView like = view.findViewById(R.id.like);
                ImageView dislike = view.findViewById(R.id.dislike);
                ImageView comment = view.findViewById(R.id.comment);

                TextView likeKol = view.findViewById(R.id.like_col);
                TextView dislikeKol = view.findViewById(R.id.dislike_col);

                name.setText(post.getName());
                time.setText(post.getTime());
                textPost.setText(post.getText());

                comment.setVisibility(View.GONE);

                myRef.child("like").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot,
                                             @Nullable String s) {
                        likeKol.setText(String.valueOf(Integer.parseInt(likeKol.getText().
                                toString()) + 1));
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot,
                                               @Nullable String s) { }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        likeKol.setText(String.valueOf(Integer.parseInt(likeKol.getText().
                                toString()) - 1));
                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot,
                                             @Nullable String s) { }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

                myRef.child("dislike").addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot dataSnapshot,
                                             @Nullable String s) {
                        dislikeKol.setText(String.valueOf(Integer.parseInt(dislikeKol.getText().
                                toString()) + 1));
                    }
                    @Override
                    public void onChildChanged(@NonNull DataSnapshot dataSnapshot,
                                               @Nullable String s) { }
                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                        dislikeKol.setText(String.valueOf(Integer.parseInt(dislikeKol.getText().
                                toString()) - 1));
                    }
                    @Override
                    public void onChildMoved(@NonNull DataSnapshot dataSnapshot,
                                             @Nullable String s) { }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

                if(dataSnapshot.child("like").child(FirebaseAuth.getInstance().getCurrentUser().
                        getUid().toString()).exists()) {
                    like.setImageResource(R.drawable.thumb_up_clicked);
                }

                if(dataSnapshot.child("dislike").child(FirebaseAuth.getInstance().getCurrentUser().
                        getUid().toString()).exists()) {
                    dislike.setImageResource(R.drawable.thumb_down_clicked);
                }

                like.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myRef.child("like").child(FirebaseAuth.getInstance().getCurrentUser().
                                getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()) {
                                            like.setImageResource(R.drawable.thumb_up);
                                            myRef.child("like").child(FirebaseAuth.getInstance().
                                                    getCurrentUser().getUid()).removeValue();
                                        } else {
                                            myRef.child("like").child(FirebaseAuth.getInstance().
                                                    getCurrentUser().getUid()).setValue(1);
                                            like.setImageResource(R.drawable.thumb_up_clicked);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError){}
                                });
                    }
                });

                dislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myRef.child("dislike").child(FirebaseAuth.getInstance().getCurrentUser().
                                getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()) {
                                            dislike.setImageResource(R.drawable.thumb_down);
                                            myRef.child("dislike").child(FirebaseAuth.getInstance().
                                                    getCurrentUser().getUid()).removeValue();
                                        } else {
                                            myRef.child("dislike").child(FirebaseAuth.getInstance().
                                                    getCurrentUser().getUid()).setValue(1);
                                            dislike.setImageResource(R.drawable.thumb_down_clicked);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError){}
                                });
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
        myRef.child("comments").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String tm = dataSnapshot.child("time").getValue(String.class);
                String uid = dataSnapshot.child("uid").getValue(String.class);
                String txt = dataSnapshot.child("text").getValue(String.class);
                String nm = dataSnapshot.child("name").getValue(String.class);
                String id = dataSnapshot.getKey();

                Post post = new Post();
                post.setName(nm);
                post.setUid(uid);
                post.setId(id);
                post.setText(txt);
                post.setTime(tm);

                View view = getLayoutInflater().inflate(R.layout.item_comment,null);

                TextView name,time,text;
                name = view.findViewById(R.id.name);
                time = view.findViewById(R.id.time);
                text = view.findViewById(R.id.post);

                name.setText(post.getName());
                time.setText(post.getTime());
                text.setText(post.getText());

                layout.addView(view);
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


        editTextPost.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(editTextPost.getText().toString().equals("")) {
                    sendCom.setVisibility(View.GONE);
                } else {
                    sendCom.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        sendCom.setOnClickListener(View -> {
            String com = editTextPost.getText().toString();
            if(com.equals("")) {
                Toast.makeText(getApplicationContext(),"Введите запись", Toast.LENGTH_SHORT).
                        show();
                return;
            }
            if(com.length() > MAX_POST_LENGTH) {
                Toast.makeText(getApplicationContext(),"Слишком длинный пост",Toast
                        .LENGTH_SHORT).show();
                return;
            }

            Post postCard = new Post();
            postCard.setTime(new SimpleDateFormat("HH:mm").format(new Date()));
            postCard.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            postCard.setText(com);
            postCard.setName(name);
            DatabaseReference mR = myRef.child("comments").push();
            postCard.setId(mR.getKey());
            mR.setValue(postCard);

            editTextPost.setText("");

            InputMethodManager imm = (InputMethodManager) CommentActivity.this.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            View v = getCurrentFocus();
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            editTextPost.clearFocus();
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(CommentActivity.this, Messages.class));
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