package com.saimone.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditProductActivity extends AppCompatActivity {
    TextView tvTitle;
    EditText etName, etDescription, etPrice;
    Button updateButton, backButton, deleteButton;
    private static final String PRODUCT_TYPE_LAB = "lab";
    private String productType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);

        tvTitle = findViewById(R.id.tvEPTitle);
        etName = findViewById(R.id.etEPName);
        etDescription = findViewById(R.id.etEPDescription);
        etPrice = findViewById(R.id.etEPPrice);
        updateButton = findViewById(R.id.btnEPUpdate);
        backButton = findViewById(R.id.btnEPBack);
        deleteButton = findViewById(R.id.btnEPDelete);

        productType = getIntent().getStringExtra("product");
        tvTitle.setText(productType.equals(PRODUCT_TYPE_LAB) ? "Edit lab test" : "Edit medicine");

        etName.setText(getIntent().getStringExtra("name"));
        etDescription.setText(getIntent().getStringExtra("description"));
        String str = getIntent().getStringExtra("price") + "$";
        etPrice.setText(str);

        backButton.setOnClickListener(view -> navigateToAdminActivity());

        deleteButton.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String description = etDescription.getText().toString();
            String priceStr = etPrice.getText().toString().replace("$", "");

            try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                int result = db.deleteProduct(name, description, priceStr, productType);
                if (result == 0) {
                    Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Product updated", Toast.LENGTH_LONG).show();
                    navigateToAdminActivity();
                }
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });

        updateButton.setOnClickListener(view -> {
            if (validateInput()) {
                String name = etName.getText().toString();
                String description = etDescription.getText().toString();
                String priceStr = etPrice.getText().toString().replace("$", "");
                double price = Double.parseDouble(priceStr);

                try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                    int result = db.updateProduct(getIntent().getStringExtra("name"), getIntent().getStringExtra("description"), getIntent().getStringExtra("price"), name, description, price, productType);
                    if (result == 0) {
                        Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Product updated", Toast.LENGTH_LONG).show();
                        navigateToAdminActivity();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void navigateToAdminActivity() {
        Class<?> adminActivityClass = productType.equals(PRODUCT_TYPE_LAB)? LabTestAdminActivity.class : BuyMedicineAdminActivity.class;
        startActivity(new Intent(EditProductActivity.this, adminActivityClass));
    }

    private boolean validateInput() {
        String name = etName.getText().toString();
        String description = etDescription.getText().toString();
        String priceStr = etPrice.getText().toString().replace("$", "");

        if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Please enter all data", Toast.LENGTH_SHORT).show();
            return false;
        }

        double price;
        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Incorrect price", Toast.LENGTH_LONG).show();
            return false;
        }

        if (price <= 0 || price > 999) {
            Toast.makeText(this, "Incorrect price", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}