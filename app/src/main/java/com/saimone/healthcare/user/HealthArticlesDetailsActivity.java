package com.saimone.healthcare.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.saimone.healthcare.R;

public class HealthArticlesDetailsActivity extends AppCompatActivity {
    TextView tvTitle;
    ImageView imageView;
    Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles_details);

        tvTitle = findViewById(R.id.tvHADTitle);
        imageView = findViewById(R.id.ivHealthPoster);
        backButton = findViewById(R.id.btnHADBack);

        Intent it = getIntent();
        String title = it.getStringExtra("title");
        String imagePath = it.getStringExtra("image_path");

        tvTitle.setText(title);
        loadImageFromFilePath(imagePath, imageView);

        backButton.setOnClickListener(view -> startActivity(new Intent(HealthArticlesDetailsActivity.this, HealthArticlesActivity.class)));
    }

    private void loadImageFromFilePath(String imagePath, ImageView imageView) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}