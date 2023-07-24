package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        tvTitle.setText(it.getStringExtra("text1"));

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            int resourceId = bundle.getInt("text2");
            imageView.setImageResource(resourceId);
        }

        backButton.setOnClickListener(view -> startActivity(new Intent(HealthArticlesDetailsActivity.this, HealthArticlesActivity.class)));
    }
}