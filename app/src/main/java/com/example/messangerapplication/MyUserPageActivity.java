package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
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

public class MyUserPageActivity extends AppCompatActivity {

    private static int MAX_POST_LENGTH = 1000;

    String name;
    ImageButton back;
    Button note;
    ImageButton sendPost;
    EditText editTextPost;
    ScrollView scrollView;
    TextView status;

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Post").
            child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_user_page);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        scrollView = findViewById(R.id.scrollView);
        LinearLayout layout = findViewById(R.id.linearLayout);

        View view = getLayoutInflater().inflate(R.layout.activity_my_user_page_up,null);
        layout.addView(view);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
                TextView textView = findViewById(R.id.nickname);
                textView.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        FirebaseDatabase.getInstance().getReference().child("Status").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        status.setText(dataSnapshot.getValue(String.class));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                });

        back = findViewById(R.id.back);
        note = findViewById(R.id.notes);
        sendPost = findViewById(R.id.create_post);
        editTextPost = findViewById(R.id.edit_text_post);
        status = findViewById(R.id.status);

        status.setOnClickListener(View -> {
            showDialog(1);
        });

        note.setOnClickListener(View -> {
            startActivity(new Intent(MyUserPageActivity.this, Notes.class));
            finish();
        });

        back.setOnClickListener(View -> {
            startActivity(new Intent(MyUserPageActivity.this, Messages.class));
            finish();
        });

        sendPost.setOnClickListener(View -> {
            String post = editTextPost.getText().toString();
            if(post.equals("")) {
                Toast.makeText(getApplicationContext(),"Введите запись", Toast.LENGTH_SHORT).
                        show();
                return;
            }
            if(post.length() > MAX_POST_LENGTH) {
                Toast.makeText(getApplicationContext(),"Слишком длинный пост",Toast
                        .LENGTH_SHORT).show();
                return;
            }

            Post postCard = new Post();
            postCard.setTime(new SimpleDateFormat("HH:mm").format(new Date()));
            postCard.setUid(FirebaseAuth.getInstance().getCurrentUser().getUid());
            postCard.setText(post);
            postCard.setName(name);
            DatabaseReference mR = myRef.push();
            postCard.setId(mR.getKey());
            mR.setValue(postCard);

            editTextPost.setText("");

            InputMethodManager imm = (InputMethodManager) MyUserPageActivity.this.
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            View v = getCurrentFocus();
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            editTextPost.clearFocus();
        });

        myRef.addChildEventListener(new ChildEventListener() {
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

                View view = getLayoutInflater().inflate(R.layout.item_post,null);

                TextView name = view.findViewById(R.id.name);
                TextView time = view.findViewById(R.id.time);
                TextView textPost = view.findViewById(R.id.post);
                ImageView like = view.findViewById(R.id.like);
                ImageView dislike = view.findViewById(R.id.dislike);

                TextView likeKol = view.findViewById(R.id.like_col);
                TextView dislikeKol = view.findViewById(R.id.dislike_col);

                name.setText(post.getName());
                time.setText(post.getTime());
                textPost.setText(post.getText());


                myRef.child(id).child("like").addChildEventListener(new ChildEventListener() {
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

                myRef.child(id).child("dislike").addChildEventListener(new ChildEventListener() {
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
                        myRef.child(id).child("like").child(FirebaseAuth.getInstance().
                                getCurrentUser().getUid()).addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()) {
                                    like.setImageResource(R.drawable.thumb_up);
                                    myRef.child(id).child("like").child(FirebaseAuth.getInstance().
                                            getCurrentUser().getUid()).removeValue();
                                } else {
                                    myRef.child(id).child("like").child(FirebaseAuth.getInstance().
                                            getCurrentUser().getUid()).setValue(1);
                                    like.setImageResource(R.drawable.thumb_up_clicked);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) { }
                        });
                    }
                });

                dislike.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myRef.child(id).child("dislike").child(FirebaseAuth.getInstance().
                                getCurrentUser().getUid()).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()) {
                                            dislike.setImageResource(R.drawable.thumb_down);
                                            myRef.child(id).child("dislike").child(FirebaseAuth.getInstance().
                                                    getCurrentUser().getUid()).removeValue();
                                        } else {
                                            myRef.child(id).child("dislike").child(FirebaseAuth.getInstance().
                                                    getCurrentUser().getUid()).setValue(1);
                                            dislike.setImageResource(R.drawable.thumb_down_clicked);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) { }
                                });
                    }
                });

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
    }

    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            final androidx.appcompat.app.AlertDialog.Builder adb = new AlertDialog.Builder(this);
            adb.setTitle("Статус");
            adb.setCancelable(true);

            final EditText input = new EditText(this);
            input.setHint("Задайте новый статус...");
            adb.setView(input);

            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    FirebaseDatabase.getInstance().getReference().child("Status").
                            child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(
                                    input.getText().toString());
                    status.setText(input.getText().toString());
                    input.setText("");
                }
            });

            adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                }
            });
            return adb.create();
        }
        return super.onCreateDialog(id);
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
