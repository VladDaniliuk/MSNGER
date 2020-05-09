package com.example.messangerapplication;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messangerapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WalletActivity extends AppCompatActivity {

    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Wallet");
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        setTitle("Wallet");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        db.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.child(user.getUid()).exists()) {
                    showDialog(1);
                }
                String a = dataSnapshot.child(user.getUid()).getValue().toString();
                int money = Integer.parseInt(a);
                TextView textView = findViewById(R.id.money);
                textView.setText(a);
                ProgressBar progressBar = findViewById(R.id.progressBar);
                progressBar.setProgress(75 * money / Integer.parseInt(dataSnapshot.child("all")
                        .getValue().toString()));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        ImageButton Share = findViewById(R.id.share);
        ImageButton Receive = findViewById(R.id.receive);
        ImageButton Earn = findViewById(R.id.earn);

        Share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), ShareActivity.class);
                startActivity(i);
            }
        });

        Receive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), RequestActivity.class);
                startActivity(i);
            }
        });

        Earn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Заработать валюту на просмотре " +
                                "рекламы можно будет в скором времени",
                        Toast.LENGTH_SHORT).show();
                return;
            }
        });
    }

    protected Dialog onCreateDialog(int id) {
        if (id == 1) {
            final AlertDialog.Builder adb = new AlertDialog.Builder(this);
            // заголовок
            adb.setTitle("Внимание");
            // сообщение
            adb.setMessage("После подтверждения будет создан электронный кошелк. " +
                    "Кошелек удалению не подлежит.");
            adb.setCancelable(false);
            // кнопка положительного ответа
            adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    db.child(user.getUid()).setValue(100);
                    db.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int a = dataSnapshot.child("all").getValue(Integer.class);
                            db.child("all").setValue(a + 100);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });
                }
            });
            // кнопка отрицательного ответа
            adb.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                               public void onClick(DialogInterface dialog, int id) {
                                   startActivity(new Intent(WalletActivity.this,
                                           Messages.class));
                                   finish();
                               }
                           });
            // создаем диалог
            return adb.create();
        }
        return super.onCreateDialog(id);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(WalletActivity.this, Messages.class));
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