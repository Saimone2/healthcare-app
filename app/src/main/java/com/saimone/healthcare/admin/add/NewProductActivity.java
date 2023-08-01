package com.saimone.healthcare.admin.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.BuyMedicineAdminActivity;
import com.saimone.healthcare.admin.LabTestAdminActivity;
import com.saimone.healthcare.database.Database;

public class NewProductActivity extends AppCompatActivity {
    TextView tvTitle;
    EditText etName, etDescription, etPrice;
    Button addNewButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_product);

        tvTitle = findViewById(R.id.tvNPTitle);
        etName = findViewById(R.id.etNPName);
        etDescription = findViewById(R.id.etNPDescription);
        etPrice = findViewById(R.id.etNPPrice);
        addNewButton = findViewById(R.id.btnNPAddNew);
        backButton = findViewById(R.id.btnNPBack);

        Intent it = getIntent();
        String product = it.getStringExtra("product");

        String str = switch (product) {
            case "lab" -> "Lab tests";
            case "medicine" -> "Medicines";
            default -> "Unknown";
        };
        tvTitle.setText(str);

        backButton.setOnClickListener(view -> {
            switch (product) {
                case "lab" ->
                        startActivity(new Intent(NewProductActivity.this, LabTestAdminActivity.class));
                case "medicine" ->
                        startActivity(new Intent(NewProductActivity.this, BuyMedicineAdminActivity.class));
                default -> Toast.makeText(getApplicationContext(), "Unknown product", Toast.LENGTH_SHORT).show();
            }
        });

        addNewButton.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String description = etDescription.getText().toString();
            String price = etPrice.getText().toString();

            if(validateInput(name, description, price)) {
                try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                    int res = db.addNewProduct(name, description, price, product);
                    if (res == 0) {
                        Toast.makeText(getApplicationContext(), "This product is already in the database", Toast.LENGTH_SHORT).show();
                    } else if (res == 1) {
                        Toast.makeText(getApplicationContext(), "New product added", Toast.LENGTH_LONG).show();
                        switch (product) {
                            case "lab" -> startActivity(new Intent(NewProductActivity.this, LabTestAdminActivity.class));
                            case "medicine" -> startActivity(new Intent(NewProductActivity.this, BuyMedicineAdminActivity.class));
                            default -> Toast.makeText(getApplicationContext(), "Unknown product", Toast.LENGTH_SHORT).show();
                        }
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

    private boolean validateInput(String newName, String newDescription, String newPrice) {
        if (newName.isEmpty() || newDescription.isEmpty() || newPrice.isEmpty()) {
            Toast.makeText(this, "Please enter all data", Toast.LENGTH_SHORT).show();
            return false;
        }

        double fee;
        try {
            fee = Double.parseDouble(newPrice);
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