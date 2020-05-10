package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.messangerapplication.Models.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmRequestActivity extends Activity {

    DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
    FirebaseUser User = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_request);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*0.25));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;

        Bundle arguments = getIntent().getExtras();
        String reqUID = arguments.get("UID").toString();

        Button Yes = findViewById(R.id.yes);
        Button No = findViewById(R.id.no);
        No.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myRef.child("MoneyRequest").child(User.getUid()).child(reqUID).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Request money = dataSnapshot.getValue(Request.class);
                        myRef.child("Wallet").child(User.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot1) {
                                int wallet = dataSnapshot1.getValue(Integer.class);
                                if(wallet < Integer.parseInt(money.getMoney())) {
                                    Toast.makeText(getApplicationContext(), "Вы не имеете такой " +
                                            "суммы на счету",Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    wallet -= Integer.parseInt(money.getMoney());
                                    myRef.child("Wallet").child(User.getUid()).setValue(wallet);
                                    myRef.child("Wallet").child(money.getUID()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot2) {
                                            int sendMoney = dataSnapshot2.getValue(Integer.class);
                                            sendMoney += Integer.parseInt(money.getMoney());
                                            myRef.child("Wallet").child(money.getUID()).setValue(sendMoney);
                                            myRef.child("MoneyRequest").child(User.getUid()).child(reqUID).removeValue();
                                            Toast.makeText(getApplicationContext(), "Операция успешно завершена",Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(ConfirmRequestActivity.this, WalletActivity.class);
                                            startActivity(intent);
                                            finish();
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



        /*

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

         */
    }
}
