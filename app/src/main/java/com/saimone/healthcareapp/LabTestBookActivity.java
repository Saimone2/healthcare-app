package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class LabTestBookActivity extends AppCompatActivity {
    EditText etLTBFullName, etLTBAddress, etLTBPinCode, etLTBContactNumber;
    Button btnLTBBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_book);

        etLTBFullName = findViewById(R.id.etLTBFullName);
        etLTBAddress = findViewById(R.id.etLTBAddress);
        etLTBPinCode = findViewById(R.id.etLTBPinCode);
        etLTBContactNumber = findViewById(R.id.etLTBContactNumber);
        btnLTBBook = findViewById(R.id.btnLTBBook);

        Intent it = getIntent();
        String[] price = it.getStringExtra("price").split(Pattern.quote(":"));
        String date = it.getStringExtra("date");
        String time = it.getStringExtra("time");

        btnLTBBook.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            try(UserDatabase db = new UserDatabase(getApplicationContext(), "healthcare-users", null, 1)) {
                db.addOrder(username, etLTBFullName.getText().toString(), etLTBAddress.getText().toString(), Integer.parseInt(etLTBPinCode.getText().toString()), etLTBContactNumber.getText().toString(), date, time, Float.parseFloat(price[1]), "lab");
                db.removeCart(username, "lab");

                Toast.makeText(getApplicationContext(), "Your booking is done successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LabTestBookActivity.this, HomeActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}