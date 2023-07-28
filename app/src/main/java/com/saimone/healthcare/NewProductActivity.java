package com.saimone.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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

        String str;
        if(product.equals("lab")) {
            str = "Lab tests";
        } else {
            str = "Medicines";
        }
        tvTitle.setText(str);

        backButton.setOnClickListener(view -> {
            if(product.equals("lab")) {
                startActivity(new Intent(NewProductActivity.this, LabTestAdminActivity.class));
            } else {
                startActivity(new Intent(NewProductActivity.this, BuyMedicineAdminActivity.class));
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
                            if(product.equals("lab")) {
                                startActivity(new Intent(NewProductActivity.this, LabTestAdminActivity.class));
                            } else if(product.equals("medicine")) {
                                startActivity(new Intent(NewProductActivity.this, BuyMedicineAdminActivity.class));
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