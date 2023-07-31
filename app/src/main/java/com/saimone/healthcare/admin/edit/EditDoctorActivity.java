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
    EditText etFullName, etAddress, etExperience, etContactno, etFee;
    Button backButton, updateButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_doctor);

        tvTitle = findViewById(R.id.tvEDTitle);
        etFullName = findViewById(R.id.etEDFullName);
        etAddress = findViewById(R.id.etEDAddress);
        etExperience = findViewById(R.id.etEDExperience);
        etContactno = findViewById(R.id.etEDContactNumber);
        etFee = findViewById(R.id.etEDFee);
        backButton = findViewById(R.id.btnEDBack);
        updateButton = findViewById(R.id.btnEDUpdate);
        deleteButton = findViewById(R.id.btnEDDelete);

        String specialty = getIntent().getStringExtra("specialty");
        etFullName.setText(getIntent().getStringExtra("fullname"));
        etAddress.setText(getIntent().getStringExtra("hospital_address"));
        etExperience.setText(getIntent().getStringExtra("experience"));
        etContactno.setText(getIntent().getStringExtra("phone"));
        String str = getIntent().getStringExtra("fee") + "$";
        etFee.setText(str);
        tvTitle.setText(specialty);

        backButton.setOnClickListener(view -> {
            Intent it = new Intent(EditDoctorActivity.this, FindDoctorAdminActivity.class);
            it.putExtra("specialty", specialty);
            startActivity(it);
        });

        deleteButton.setOnClickListener(view -> {
            String fullname = etFullName.getText().toString();
            String address = etAddress.getText().toString();
            String experience = etExperience.getText().toString();
            String phone = etContactno.getText().toString();
            String fee = etFee.getText().toString().replace("$", "");

            FragmentManager manager = getSupportFragmentManager();
            MyDialogFragment myDialogFragment = MyDialogFragment.newInstance("DELETE DOCTOR", fullname, address, experience, phone, fee, specialty);
            myDialogFragment.show(manager, "myDialog");
        });

        updateButton.setOnClickListener(view -> {
            if (validateInput()) {
                String fullname = etFullName.getText().toString();
                String address = etAddress.getText().toString();
                String experience = etExperience.getText().toString();
                String contactno = etContactno.getText().toString();
                String feeStr = etFee.getText().toString().replace("$", "");

                try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                    int result = db.updateDoctor(getIntent().getStringExtra("fullname"), getIntent().getStringExtra("hospital_address"), getIntent().getStringExtra("experience"), getIntent().getStringExtra("phone"), getIntent().getStringExtra("fee"), fullname, address, experience, contactno, feeStr, specialty);
                    if (result == 0) {
                        Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Doctor's info updated", Toast.LENGTH_LONG).show();
                        Intent it = new Intent(EditDoctorActivity.this, FindDoctorAdminActivity.class);
                        it.putExtra("specialty", specialty);
                        startActivity(it);
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInput() {
        String fullname = etFullName.getText().toString();
        String address = etAddress.getText().toString();
        String experience = etExperience.getText().toString();
        String contactno = etContactno.getText().toString();
        String feeStr = etFee.getText().toString().replace("$", "");

        if (fullname.isEmpty() || address.isEmpty() || experience.isEmpty() || contactno.isEmpty() || feeStr.isEmpty()) {
            Toast.makeText(this, "Please enter all data", Toast.LENGTH_SHORT).show();
            return false;
        }

        double fee;
        try {
            fee = Double.parseDouble(feeStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Incorrect price", Toast.LENGTH_LONG).show();
            return false;
        }

        if (fee <= 0 || fee > 999) {
            Toast.makeText(this, "Incorrect price", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}