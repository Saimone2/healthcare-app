package com.saimone.healthcare.admin.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.FindDoctorSpecialityAdminActivity;
import com.saimone.healthcare.database.Database;

public class NewSpecialtyActivity extends AppCompatActivity {
    EditText etName;
    Button addNewButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_specialty);

        etName = findViewById(R.id.etNSSpecName);
        addNewButton = findViewById(R.id.btnNSAddNew);
        backButton = findViewById(R.id.btnNSBack);

        backButton.setOnClickListener(view -> startActivity(new Intent(NewSpecialtyActivity.this, FindDoctorSpecialityAdminActivity.class)));

        addNewButton.setOnClickListener(view -> {
            String name = etName.getText().toString();

            if(validateInput(name)) {
                try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                    int res = db.addNewSpecialty(name);
                    if(res == 0) {
                        Toast.makeText(getApplicationContext(), "This specialty is already in the database", Toast.LENGTH_SHORT).show();
                    } else if (res == 1){
                        Toast.makeText(getApplicationContext(), "New specialty added", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(NewSpecialtyActivity.this, FindDoctorSpecialityAdminActivity.class));
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

    private boolean validateInput(String newName) {
        if (newName.isEmpty()) {
            Toast.makeText(this, "Please enter all data", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}