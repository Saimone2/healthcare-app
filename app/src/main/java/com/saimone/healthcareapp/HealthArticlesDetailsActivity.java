package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HealthArticlesDetailsActivity extends AppCompatActivity {

    TextView tv1;
    ImageView img;
    Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles_details);

        tv1 = findViewById(R.id.tvHADTitle);
        img = findViewById(R.id.content);
        btn = findViewById(R.id.btnHADBack);

        Intent it = getIntent();
        tv1.setText(it.getStringExtra("text1"));

        Bundle bundle = getIntent().getExtras();
        if(bundle != null) {
            int resId = bundle.getInt("text2");
            img.setImageResource(resId);
        }

        btn.setOnClickListener(view -> startActivity(new Intent(HealthArticlesDetailsActivity.this, HomeActivity.class)));
    }
}