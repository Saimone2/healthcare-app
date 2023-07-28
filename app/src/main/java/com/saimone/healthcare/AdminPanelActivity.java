package com.saimone.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class AdminPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        boolean loggedIn = getIntent().getBooleanExtra("loggedInFlag", false);

        if(loggedIn) {
            Toast.makeText(getApplicationContext(), "Welcome Admin", Toast.LENGTH_SHORT).show();
        }

        CardView cvLabTest = findViewById(R.id.cvLabTest);
        cvLabTest.setOnClickListener(view -> startActivity(new Intent(AdminPanelActivity.this, LabTestAdminActivity.class)));

        CardView cvBuyMedicine = findViewById(R.id.cvBuyMedicine);
        cvBuyMedicine.setOnClickListener(view -> startActivity(new Intent(AdminPanelActivity.this, BuyMedicineAdminActivity.class)));

        CardView findDoctor = findViewById(R.id.cvFindDoctor);
        findDoctor.setOnClickListener(view -> startActivity(new Intent(AdminPanelActivity.this, FindDoctorAdminActivity.class)));

        CardView cvHealthDoctor = findViewById(R.id.cvHealthArticles);
        cvHealthDoctor.setOnClickListener(view -> startActivity(new Intent(AdminPanelActivity.this, HealthArticlesAdminActivity.class)));

        CardView logout = findViewById(R.id.cvLogout);
        logout.setOnClickListener(view -> startActivity(new Intent(AdminPanelActivity.this, LoginActivity.class)));
    }
}