package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class BookAppointmentActivity extends AppCompatActivity {
    EditText et1, et2, et3, et4;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        tvTitle = findViewById(R.id.tvBookTitle);
        et1 = findViewById(R.id.etBookFullName);
        et2 = findViewById(R.id.etBookAddress);
        et3 = findViewById(R.id.etBookContactNumber);
        et4 = findViewById(R.id.etBookFee);

        et1.setKeyListener(null);
        et2.setKeyListener(null);
        et3.setKeyListener(null);
        et4.setKeyListener(null);

        Intent it = getIntent();
        String title = it.getStringExtra("text1");
        String fullname = it.getStringExtra("text2");
        String address = it.getStringExtra("text3");
        String contactNum = it.getStringExtra("text4");
        String fee = it.getStringExtra("text5");

        tvTitle.setText(title);
        et1.setText(fullname);
        et2.setText(address);
        et3.setText(contactNum);
        et4.setText(fee);
    }
}