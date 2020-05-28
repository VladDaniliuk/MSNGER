package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messangerapplication.Models.Mess;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserPageActivity extends AppCompatActivity {

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_page);

        Bundle arguments = getIntent().getExtras();
        String UID = arguments.get("UID").toString();
        Button startDialog = findViewById(R.id.button2);
        LinearLayout bDelAndMess = findViewById(R.id.del_and_mess);
        Button delDialog = findViewById(R.id.delete_dialog);
        Button messButton = findViewById(R.id.mess_b);



        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("User").child(UID).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.getValue(String.class);
                TextView textView = findViewById(R.id.nickname);
                textView.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseDatabase.getInstance().getReference().child("UserMessageList").
                child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                child(UID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    startDialog.setVisibility(View.GONE);
                    bDelAndMess.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

        startDialog.setOnClickListener(view -> {
            Mess message = new Mess();
            message.setMes("Теперь вы можете переписываться");
            message.setUid("0");
            message.setUs("Admin");

            DatabaseReference mR = FirebaseDatabase.getInstance().getReference().
                    child("UserMessageList").child(FirebaseAuth.getInstance().getCurrentUser().
                    getUid()).child(UID).push();

            String uid = mR.getKey();

            FirebaseDatabase.getInstance().getReference().child("UserMessageList").
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(UID).
                    setValue(uid);

            FirebaseDatabase.getInstance().getReference().child("UserMessageList").child(UID).
                    child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(uid);

            mR = FirebaseDatabase.getInstance().getReference().child("UserMessages").child(uid).
                    push();

            message.setMesuid(uid);

            mR.setValue(message);

            Toast.makeText(getBaseContext(),"Диалог был создан", Toast.LENGTH_SHORT).show();

            startDialog.setVisibility(View.GONE);
            bDelAndMess.setVisibility(View.VISIBLE);
        });

        delDialog.setOnClickListener(view -> {
            Toast.makeText(getBaseContext(),"Скоро будет доработано...", Toast.LENGTH_SHORT).
                    show();
        });

        messButton.setOnClickListener(view -> {
            Intent intent = new Intent(getBaseContext(), UserMessagesActivity.class);
            FirebaseDatabase.getInstance().getReference().child("UserMessageList").child(
                    FirebaseAuth.getInstance().getCurrentUser().getUid()).child(UID).
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String uid = dataSnapshot.getValue(String.class);
                            intent.putExtra("ID", uid);
                            intent.putExtra("UID",UID);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) { }
                    });



        });

        ImageButton back = findViewById(R.id.back);

        back.setOnClickListener(view -> {
            startActivity(new Intent(UserPageActivity.this, Messages.class));
            finish();
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
