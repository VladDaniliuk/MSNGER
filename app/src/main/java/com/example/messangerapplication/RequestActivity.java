package com.example.messangerapplication;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.messangerapplication.Models.Request;
import com.example.messangerapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class RequestActivity extends AppCompatActivity {

    DatabaseReference db = FirebaseDatabase.getInstance().getReference();
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle arguments = getIntent().getExtras();
        String UID = arguments.get("UID").toString();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        setTitle("Request money");
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView name = findViewById(R.id.name);
        EditText moneyRequest = findViewById(R.id.edit_request);
        ConstraintLayout requestButton = findViewById(R.id.button);

        db.child("User").child(UID).child("name").addListenerForSingleValueEvent(
                new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.getValue(String.class));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        requestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String money = moneyRequest.getText().toString();
                if (money.equals("")) {
                    Toast.makeText(getApplicationContext(), "Введитe сумму",
                            Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    moneyRequest.setText("");
                    Request request = new Request();
                    request.setUID(user.getUid());
                    request.setMoney(money);
                    request.setMail(user.getEmail());
                    DatabaseReference myRef = db.child("MoneyRequest").child(UID).push();
                    request.setReqUid(myRef.getKey());
                    myRef.setValue(request);
                    Toast.makeText(getApplicationContext(), "Запрос совершен",
                            Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(), WalletActivity.class);
                    startActivity(i);
                    finish();
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(RequestActivity.this, WalletActivity.class));
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
