package com.example.messangerapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;

public class ImageActivity extends AppCompatActivity {

    ImageView back;
    ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_image);

        mImageView = findViewById(R.id.my_image_view);
        back = findViewById(R.id.back);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mImageView.setImageBitmap(extras.getParcelable("image_id"));
            Picasso.with(this).load(extras.get("image_id").toString()).into(mImageView);
        }

        mImageView.setOnClickListener(view -> {
            if(back.getVisibility() == View.VISIBLE){
                back.setVisibility(View.GONE);
            } else {
                back.setVisibility(View.VISIBLE);
            }
        });

        back.setOnClickListener(view -> {
            finish();
        });
    }
}