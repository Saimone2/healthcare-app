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

            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                if (name.length() == 0 || description.length() == 0 || price.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter all data", Toast.LENGTH_SHORT).show();
                } else {
                    double dblPrice = Double.parseDouble(price);
                    if(dblPrice <= 0 || dblPrice > 999) {
                        Toast.makeText(getApplicationContext(), "Incorrect price", Toast.LENGTH_SHORT).show();
                    } else {
                        if(db.addNewProduct(name, description, price, product) == 0) {
                            Toast.makeText(getApplicationContext(), "This product is already in the database", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "New item added", Toast.LENGTH_LONG).show();
                            switch (product) {
                                case "lab" ->
                                        startActivity(new Intent(NewProductActivity.this, LabTestAdminActivity.class));
                                case "medicine" ->
                                        startActivity(new Intent(NewProductActivity.this, BuyMedicineAdminActivity.class));
                                default -> Toast.makeText(getApplicationContext(), "Unknown product", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}