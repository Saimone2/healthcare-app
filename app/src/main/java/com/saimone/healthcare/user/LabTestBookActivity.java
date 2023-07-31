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
        String priceStr = it.getStringExtra("price");
        int p1 = priceStr.indexOf(":") + 1;
        int p2 = priceStr.indexOf("$", p1);
        float price = Float.parseFloat(priceStr.substring(p1, p2));

        String date = it.getStringExtra("date");
        String time = it.getStringExtra("time");

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
                    db.addOrder(username, fullname, address, Integer.parseInt(pinCode), contactno, date, time, price, "lab");
                    db.removeCart(username, "lab");
                    Toast.makeText(getApplicationContext(), "Your booking is done successfully", Toast.LENGTH_LONG).show();
                    startActivity(new Intent(LabTestBookActivity.this, HomeActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}