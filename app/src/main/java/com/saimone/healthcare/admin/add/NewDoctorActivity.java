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
            String fee = etFee.getText().toString().replace("$", "");

            if(validateInput(fullname, address, experience, phone, fee)) {
                try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                    int res = db.addNewDoctor(fullname, address, experience, phone, fee, specialty);
                    if (res == 0) {
                        Toast.makeText(getApplicationContext(), "This doctor is already in the database", Toast.LENGTH_SHORT).show();
                    } else if (res == 1) {
                        Toast.makeText(getApplicationContext(), "New doctor added", Toast.LENGTH_LONG).show();
                        Intent it = new Intent(NewDoctorActivity.this, FindDoctorAdminActivity.class);
                        it.putExtra("specialty", specialty);
                        startActivity(it);
                    } else {
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validateInput(String newFullname, String newAddress, String newExperience, String newPhone, String newFee) {
        if (newFullname.isEmpty() || newAddress.isEmpty() || newExperience.isEmpty() || newPhone.isEmpty() || newFee.isEmpty()) {
            Toast.makeText(this, "Please enter all data", Toast.LENGTH_SHORT).show();
            return false;
        }

        double fee;
        try {
            fee = Double.parseDouble(newFee);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Incorrect price", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (fee <= 0 || fee > 999) {
            Toast.makeText(this, "Incorrect price", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}