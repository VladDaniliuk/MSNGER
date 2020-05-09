package com.example.messangerapplication;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class RequestChoiceActivity extends Activity {

    RecyclerView mRequestRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Bundle arguments = getIntent().getExtras();
        int Money = Integer.parseInt(arguments.get("Money").toString());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int) (width), (int) (height * 0.5));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.BOTTOM;
        mRequestRecycler = findViewById(R.id.share_recycler);
        mRequestRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        linearLayoutManager.setStackFromEnd(true);

        mRequestRecycler.setLayoutManager(linearLayoutManager);

        final ConstraintLayout Request = findViewById(R.id.share);

        Request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Поделиться валютой с помощью qr " +
                                "кода можно будет позже",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
