package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

public class FindDoctorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        CardView cvFDBack = findViewById(R.id.cvFDBack);
        cvFDBack.setOnClickListener(view -> startActivity(new Intent(FindDoctorActivity.this, HomeActivity.class)));
    }
}