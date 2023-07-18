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

        CardView cvFDFamilyPhysician = findViewById(R.id.cvFDFamilyPhysician);
        cvFDFamilyPhysician.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorActivity.this, DoctorsDetailsActivity.class);
            it.putExtra("title", "Family Physician");
            startActivity(it);
        });

        CardView cvFDDietician = findViewById(R.id.cvFDDietician);
        cvFDDietician.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorActivity.this, DoctorsDetailsActivity.class);
            it.putExtra("title", "Dietician");
            startActivity(it);
        });

        CardView cvFDDentist = findViewById(R.id.cvFDDentist);
        cvFDDentist.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorActivity.this, DoctorsDetailsActivity.class);
            it.putExtra("title", "Dentist");
            startActivity(it);
        });

        CardView cvFDSurgeon = findViewById(R.id.cvFDSurgeon);
        cvFDSurgeon.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorActivity.this, DoctorsDetailsActivity.class);
            it.putExtra("title", "Surgeon");
            startActivity(it);
        });

        CardView cvFDCardiologist = findViewById(R.id.cvFDCardiologist);
        cvFDCardiologist.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorActivity.this, DoctorsDetailsActivity.class);
            it.putExtra("title", "Cardiologist");
            startActivity(it);
        });

        CardView cvFDBack = findViewById(R.id.cvFDBack);
        cvFDBack.setOnClickListener(view -> startActivity(new Intent(FindDoctorActivity.this, HomeActivity.class)));
    }
}