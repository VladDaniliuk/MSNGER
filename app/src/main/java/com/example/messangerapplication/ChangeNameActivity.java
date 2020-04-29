package com.example.messangerapplication;

import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.messangerapplication.Models.Mess;
import com.example.messangerapplication.Models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class ChangeNameActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_name);
        setTitle("Change name");

        ActionBar actionBar =getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                startActivity(new Intent(ChangeNameActivity.this,SettingsActivity.class));
                finish();
                return true;
            case R.id.accept:
                final EditText newName = findViewById(R.id.new_name_input);
                if(newName.getText().length() == 0) {
                    Toast.makeText(getBaseContext(), "Write your new name!",
                            Toast.LENGTH_SHORT).show();
                } else {
                    final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                    mDatabase.child("User").child(user.getUid()).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    User contact = dataSnapshot.getValue(User.class);
                                    contact.setName(newName.getText().toString());
                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                    String Uid = user.getUid();
                                    ref.child("User").child(Uid).setValue(contact);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                    Toast.makeText(getBaseContext(), "Change name complite!",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChangeNameActivity.this,SettingsActivity.class));
                    finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.acception, menu);
        return true;
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
