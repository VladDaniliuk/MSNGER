package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Stack;

public class SendMoneyActivity extends AppCompatActivity {

    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_money);
        setTitle("Send money");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle arguments = getIntent().getExtras();
        String UID = arguments.get("UID").toString();
        TextView name = findViewById(R.id.name);
        myRef.child("User").child(UID).child("name").addValueEventListener(
                new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        EditText Sum = findViewById(R.id.sum);
        ImageButton Send = findViewById(R.id.share);

        Send.setOnClickListener(view -> {
            String sum = Sum.getText().toString();
            Sum.setText("");
            if(sum.equals("")) {
                Toast.makeText(getApplicationContext(),"Введите сумму",
                        Toast.LENGTH_SHORT).show();
                return;
            }
            int Summ = Integer.parseInt(sum);

            myRef.child("Wallet").child(User.getUid()).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int a = dataSnapshot.getValue(Integer.class);
                    if (a < Summ) {
                        Toast.makeText(getApplicationContext(), "Вы не имеете такой " +
                                        "суммы на счету",Toast.LENGTH_SHORT).show();
                    } else {
                        a -= Summ;
                        myRef.child("Wallet").child(User.getUid()).setValue(a);
                        myRef.child("Wallet").child(UID).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int money = dataSnapshot.getValue(Integer.class);
                                money += Summ;
                                myRef.child("Wallet").child(UID).setValue(money);
                                Toast.makeText(getApplicationContext(), "Перевод средств" +
                                        " успешно завершен", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(SendMoneyActivity.this,
                                        WalletActivity.class));
                                finish();;
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(SendMoneyActivity.this,
                            WalletActivity.class));
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
