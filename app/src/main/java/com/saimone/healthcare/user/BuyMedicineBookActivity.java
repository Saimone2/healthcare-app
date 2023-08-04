package com.saimone.healthcare.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.database.Database;

public class BuyMedicineBookActivity extends AppCompatActivity {
    EditText etFullName, etAddress, etPinCode, etPhone;
    Button bookButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_book);

        etFullName = findViewById(R.id.etBMBFullName);
        etAddress = findViewById(R.id.etBMBAddress);
        etPinCode = findViewById(R.id.etBMBPinCode);
        etPhone = findViewById(R.id.etBMBContactNumber);
        bookButton = findViewById(R.id.btnBMBBook);

        Intent it = getIntent();
        String priceStr = it.getStringExtra("price");
        assert priceStr != null;
        int p1 = priceStr.indexOf(":") + 1;
        int p2 = priceStr.indexOf("$", p1);
        float price = Float.parseFloat(priceStr.substring(p1, p2));
        String date = it.getStringExtra("date");

        bookButton.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String fullName = etFullName.getText().toString();
            String address = etAddress.getText().toString();
            String pinCode = etPinCode.getText().toString();
            String phone = etPhone.getText().toString();

            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                if (fullName.length() == 0 || address.length() == 0 || pinCode.length() == 0 || phone.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter all data", Toast.LENGTH_SHORT).show();
                } else {
                    db.addOrder(username, fullName, address, Integer.parseInt(pinCode), phone, date, "", price, "medicine");
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