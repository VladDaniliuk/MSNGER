package com.example.messangerapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendRequestMoneyMessActivity extends Activity {

    ConstraintLayout send, request;
    EditText summ;
    Button bRequest, bSend;
    ImageButton bBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request_money_mess);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height * 0.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;

        Bundle extras = getIntent().getExtras();
        String UID = extras.getString("UID");

        send = findViewById(R.id.send);
        request = findViewById(R.id.request);
        summ = findViewById(R.id.summ);
        bRequest = findViewById(R.id.request_button);
        bBack = findViewById(R.id.back);
        bSend = findViewById(R.id.send_button);

        send.setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference().child("Wallet").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(UID).exists()) {
                                send.setVisibility(View.GONE);
                                request.setVisibility(View.GONE);
                                summ.setVisibility(View.VISIBLE);
                                bRequest.setVisibility(View.VISIBLE);
                                bBack.setVisibility(View.VISIBLE);
                                bSend.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getBaseContext(), "Этот пользователь не " +
                                        "имеет электронного кошелька", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            return;
                        }
                    });
        });

        request.setOnClickListener(view -> {
            FirebaseDatabase.getInstance().getReference().child("Wallet").
                    addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.child(UID).exists()) {
                                send.setVisibility(View.GONE);
                                request.setVisibility(View.GONE);
                                summ.setVisibility(View.VISIBLE);
                                bRequest.setVisibility(View.VISIBLE);
                                bBack.setVisibility(View.VISIBLE);
                            } else {
                                Toast.makeText(getBaseContext(), "Этот пользователь не имеет" +
                                        " электронного кошелька", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            return;
                        }
                    });
        });

        bBack.setOnClickListener(view -> {
            send.setVisibility(View.VISIBLE);
            request.setVisibility(View.VISIBLE);
            summ.setVisibility(View.GONE);
            bRequest.setVisibility(View.GONE);
            bBack.setVisibility(View.GONE);
            bSend.setVisibility(View.GONE);
            summ.setText("");

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.
                    INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        });

    }
}
