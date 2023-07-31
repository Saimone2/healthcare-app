package com.saimone.healthcare.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.saimone.healthcare.auth.LoginActivity;
import com.saimone.healthcare.R;

public class AdminPanelActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_panel);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        username = Character.toUpperCase(username.charAt(0)) + username.substring(1);

        boolean loggedIn = getIntent().getBooleanExtra("loggedInFlag", false);

        if(loggedIn) {
            Toast.makeText(getApplicationContext(), "Welcome " + username, Toast.LENGTH_SHORT).show();
        }

        CardView cvLabTest = findViewById(R.id.cvLabTest);
        cvLabTest.setOnClickListener(view -> startActivity(new Intent(AdminPanelActivity.this, LabTestAdminActivity.class)));

        CardView cvBuyMedicine = findViewById(R.id.cvBuyMedicine);
        cvBuyMedicine.setOnClickListener(view -> startActivity(new Intent(AdminPanelActivity.this, BuyMedicineAdminActivity.class)));

        CardView findDoctor = findViewById(R.id.cvFindDoctor);
        findDoctor.setOnClickListener(view -> startActivity(new Intent(AdminPanelActivity.this, FindDoctorSpecialityAdminActivity.class)));

        CardView cvHealthDoctor = findViewById(R.id.cvHealthArticles);
        cvHealthDoctor.setOnClickListener(view -> startActivity(new Intent(AdminPanelActivity.this, HealthArticlesAdminActivity.class)));

        CardView logout = findViewById(R.id.cvLogout);
        logout.setOnClickListener(view -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(AdminPanelActivity.this, LoginActivity.class));
            finishAffinity();
        });
    }
}