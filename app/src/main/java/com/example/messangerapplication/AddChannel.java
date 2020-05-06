package com.example.messangerapplication;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable.Creator;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class AddChannel extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_channel);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width),(int)(height*0.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;

        final Button Add = findViewById(R.id.add);
        final Button Create = findViewById(R.id.create);

        Create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Add.setClickable(true);
                Add.setBackgroundColor(Color.parseColor("#6c6c6c"));
                Create.setBackgroundColor(Color.WHITE);
                Create.setClickable(false);
            }
        });

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Create.setClickable(true);
                Create.setBackgroundColor(Color.parseColor("#6c6c6c"));
                Add.setBackgroundColor(Color.WHITE);
                Add.setClickable(false);
            }
        });
    }
}
