package com.saimone.healthcare.admin.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.FindDoctorAdminActivity;
import com.saimone.healthcare.components.MyDialogFragment;
import com.saimone.healthcare.database.Database;

public class EditDoctorActivity extends AppCompatActivity {
    TextView tvTitle;
    EditText etFullName, etAddress, etExperience, etPhone, etFee;
    Button backButton, updateButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor);

        tvTitle = findViewById(R.id.tvEDTitle);
        etFullName = findViewById(R.id.etEDFullName);
        etAddress = findViewById(R.id.etEDAddress);
        etExperience = findViewById(R.id.etEDExperience);
        etPhone = findViewById(R.id.etEDContactNumber);
        etFee = findViewById(R.id.etEDFee);
        backButton = findViewById(R.id.btnEDBack);
        updateButton = findViewById(R.id.btnEDUpdate);
        deleteButton = findViewById(R.id.btnEDDelete);

        String specialty = getIntent().getStringExtra("specialty");
        String fullname = getIntent().getStringExtra("fullname");
        String address = getIntent().getStringExtra("hospital_address");
        String experience = getIntent().getStringExtra("experience");
        String phone = getIntent().getStringExtra("phone");
        String fee = getIntent().getStringExtra("fee");

        tvTitle.setText(specialty);
        etFullName.setText(fullname);
        etAddress.setText(address);
        etExperience.setText(experience);
        etPhone.setText(phone);
        String feeStr = fee + "$";
        etFee.setText(feeStr);

        backButton.setOnClickListener(view -> {
            Intent it = new Intent(EditDoctorActivity.this, FindDoctorAdminActivity.class);
            it.putExtra("specialty", specialty);
            startActivity(it);
        });

        deleteButton.setOnClickListener(view -> {
            FragmentManager manager = getSupportFragmentManager();
            MyDialogFragment myDialogFragment = MyDialogFragment.newInstance("DELETE DOCTOR", fullname, address, experience, phone, fee, specialty);
            myDialogFragment.show(manager, "myDialog");
        });

        updateButton.setOnClickListener(view -> {
            String newFullname = etFullName.getText().toString();
            String newAddress = etAddress.getText().toString();
            String newExperience = etExperience.getText().toString();
            String newPhone = etPhone.getText().toString();
            String newFee = etFee.getText().toString().replace("$", "");

            if (validateInput(newFullname, newAddress, newExperience, newPhone, newFee)) {
                try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                    int res = db.updateDoctor(fullname, address, experience, phone, fee, newFullname, newAddress, newExperience, newPhone, newFee, specialty);
                    if (res == 0) {
                        Toast.makeText(getApplicationContext(), "This doctor is already in the database", Toast.LENGTH_SHORT).show();
                    } else if(res == 1) {
                        Toast.makeText(getApplicationContext(), "Doctor's info updated", Toast.LENGTH_LONG).show();
                        Intent it = new Intent(EditDoctorActivity.this, FindDoctorAdminActivity.class);
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