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
    EditText etFullName, etAddress, etPinCode, etContactno;
    Button bookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_book);

        etFullName = findViewById(R.id.etLTBFullName);
        etAddress = findViewById(R.id.etLTBAddress);
        etPinCode = findViewById(R.id.etLTBPinCode);
        etContactno = findViewById(R.id.etLTBContactNumber);
        bookButton = findViewById(R.id.btnLTBBook);

        Intent it = getIntent();
        String[] price = it.getStringExtra("price").split(Pattern.quote("$"));
        String date = it.getStringExtra("date");
        String time = it.getStringExtra("time");

        bookButton.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                db.addOrder(username, etFullName.getText().toString(), etAddress.getText().toString(), Integer.parseInt(etPinCode.getText().toString()), etContactno.getText().toString(), date, time, Float.parseFloat(price[1]), "lab");
                db.removeCart(username, "lab");
                Toast.makeText(getApplicationContext(), "Your booking is done successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(LabTestBookActivity.this, HomeActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}