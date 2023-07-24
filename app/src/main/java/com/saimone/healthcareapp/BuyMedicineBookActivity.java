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

public class BuyMedicineBookActivity extends AppCompatActivity {
    EditText etFullName, etAddress, etPinCode, etContactno;
    Button bookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_book);

        etFullName = findViewById(R.id.etBMBFullName);
        etAddress = findViewById(R.id.etBMBAddress);
        etPinCode = findViewById(R.id.etBMBPinCode);
        etContactno = findViewById(R.id.etBMBContactNumber);
        bookButton = findViewById(R.id.btnBMBBook);

        Intent it = getIntent();
        String[] price = it.getStringExtra("price").split(Pattern.quote("$"));
        String date = it.getStringExtra("date");

        bookButton.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                db.addOrder(username, etFullName.getText().toString(), etAddress.getText().toString(), Integer.parseInt(etPinCode.getText().toString()), etContactno.getText().toString(), date, "", Float.parseFloat(price[1]), "medicine");
                db.removeCart(username, "medicine");
                Toast.makeText(getApplicationContext(), "Your booking is done successfully", Toast.LENGTH_LONG).show();
                startActivity(new Intent(BuyMedicineBookActivity.this, HomeActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}