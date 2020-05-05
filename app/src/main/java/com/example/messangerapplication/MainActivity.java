package com.example.messangerapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
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
                        startRotationTimer();
                    }
                }
            });
        } else {
            startRotationTimer();
        }
    }

    private void startRotationTimer() {
        new CountDownTimer(700, 100) {
            public void onTick(long millisUntilFinished) {}
            public void onFinish() {

                startActivity(new Intent(MainActivity.this, LogRegActivity.class));
                finish();
            }
        }.start();
    }
}
