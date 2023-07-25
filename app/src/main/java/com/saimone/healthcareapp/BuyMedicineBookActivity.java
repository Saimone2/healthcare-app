package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
        String priceStr = it.getStringExtra("price");
        int p1 = priceStr.indexOf(":") + 1;
        int p2 = priceStr.indexOf("$", p1);
        float price = Float.parseFloat(priceStr.substring(p1, p2));
        String date = it.getStringExtra("date");

        bookButton.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String fullname = etFullName.getText().toString();
            String address = etAddress.getText().toString();
            String pinCode = etPinCode.getText().toString();
            String contactno = etContactno.getText().toString();

            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                if (fullname.length() == 0 || address.length() == 0 || pinCode.length() == 0 || contactno.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter all data", Toast.LENGTH_SHORT).show();
                } else {
                    db.addOrder(username, fullname, address, Integer.parseInt(pinCode), contactno, date, "", price, "medicine");
                    db.removeCart(username, "medicine");
                    Toast.makeText(getApplicationContext(), "Your booking is done successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(BuyMedicineBookActivity.this, HomeActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}