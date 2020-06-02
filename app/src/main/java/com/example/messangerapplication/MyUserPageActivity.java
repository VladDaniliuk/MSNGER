package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.method.Touch;
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

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Post").
            child(FirebaseAuth.getInstance().getCurrentUser().getUid());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitu_my_user_page_2);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        scrollView = findViewById(R.id.scrollView);
        LinearLayout layout = findViewById(R.id.linearLayout);

        View view = getLayoutInflater().inflate(R.layout.activity_my_user_page,null);
        layout.addView(view);



        for(int i = 0; i < 20; i++) {
            view = getLayoutInflater().inflate(R.layout.item_post,null);
            TextView textView = view.findViewById(R.id.textView2);
            textView.setText("{eq");

            ImageView imageView = view.findViewById(R.id.imageView5);
            Button button = view.findViewById(R.id.delete_post);
            ImageView like = view.findViewById(R.id.like);
            ImageView dislike = view.findViewById(R.id.dislike);
            ImageView comment = view.findViewById(R.id.comment);

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    button.setVisibility(View.VISIBLE);
                }
            });

            like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"like", Toast.LENGTH_SHORT).show();
                }
            });

            dislike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"dislike", Toast.LENGTH_SHORT).
                            show();
                }
            });

            comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(),"comment", Toast.LENGTH_SHORT).
                            show();
                }
            });

            layout.addView(view);
        }

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

        back = findViewById(R.id.back);
        note = findViewById(R.id.notes);
        sendPost = findViewById(R.id.create_post);
        editTextPost = findViewById(R.id.edit_text_post);

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
