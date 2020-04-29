package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loader);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        auth = FirebaseAuth.getInstance();
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            auth.getCurrentUser().reload().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(MainActivity.this,
                                "Authorisation successful!",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, Messages.class));
                        finish();
                    } else {
                        Toast.makeText(MainActivity.this,
                                "Failed to authorise user.",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LogRegActivity.class));
                        finish();
                    }
                }
            });
        } else {
            startActivity(new Intent(MainActivity.this, LogRegActivity.class));
            finish();
        }
    }
}
