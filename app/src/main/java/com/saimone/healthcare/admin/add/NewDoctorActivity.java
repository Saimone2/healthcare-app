package com.saimone.healthcare.admin.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.FindDoctorAdminActivity;
import com.saimone.healthcare.database.Database;

public class NewDoctorActivity extends AppCompatActivity {
    TextView tvTitle;
    EditText etFullName, etAddress, etExperience, etContactno, etFee;
    Button addNewButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_doctor);

        tvTitle = findViewById(R.id.tvNDTitle);
        etFullName = findViewById(R.id.etNDFullName);
        etAddress = findViewById(R.id.etNDAddress);
        etExperience = findViewById(R.id.etNDExperience);
        etContactno = findViewById(R.id.etNDContactNumber);
        etFee = findViewById(R.id.etNDFee);
        addNewButton = findViewById(R.id.btnNDAddNew);
        backButton = findViewById(R.id.btnNDBack);

        String specialty = getIntent().getStringExtra("specialty");
        tvTitle.setText(specialty);

        backButton.setOnClickListener(view -> {
            Intent it = new Intent(NewDoctorActivity.this, FindDoctorAdminActivity.class);
            it.putExtra("specialty", specialty);
            startActivity(it);
        });

        addNewButton.setOnClickListener(view -> {
            String fullname = etFullName.getText().toString();
            String address = etAddress.getText().toString();
            String experience = etExperience.getText().toString();
            String phone = etContactno.getText().toString();
            String feeStr = etFee.getText().toString().replace("$", "");

            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                if (fullname.length() == 0 || address.length() == 0 || experience.length() == 0 || phone.length() == 0 || feeStr.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter all data", Toast.LENGTH_SHORT).show();
                } else {
                    double fee = Double.parseDouble(feeStr);
                    if(fee <= 0 || fee > 999) {
                        Toast.makeText(getApplicationContext(), "Incorrect price", Toast.LENGTH_SHORT).show();
                    } else {
                        if(db.addNewDoctor(fullname, address, experience, phone, fee, specialty) == 0) {
                            Toast.makeText(getApplicationContext(), "This doctor is already in the database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "New doctor added", Toast.LENGTH_LONG).show();
                            Intent it = new Intent(NewDoctorActivity.this, FindDoctorAdminActivity.class);
                            it.putExtra("specialty", specialty);
                            startActivity(it);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}