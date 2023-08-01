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
import com.saimone.healthcare.admin.BuyMedicineAdminActivity;
import com.saimone.healthcare.admin.LabTestAdminActivity;
import com.saimone.healthcare.components.MyDialogFragment;
import com.saimone.healthcare.database.Database;

public class EditProductActivity extends AppCompatActivity {
    TextView tvTitle;
    EditText etName, etDescription, etPrice;
    Button updateButton, backButton, deleteButton;
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
        tvTitle.setText(productType.equals("lab") ? "Edit lab test" : "Edit medicine");

        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String price = getIntent().getStringExtra("price");

        etName.setText(name);
        etDescription.setText(description);
        String priceStr = price + "$";
        etPrice.setText(priceStr);

        backButton.setOnClickListener(view -> navigateToAdminActivity());

        deleteButton.setOnClickListener(view -> {
            FragmentManager manager = getSupportFragmentManager();
            MyDialogFragment myDialogFragment = MyDialogFragment.newInstance("DELETE PRODUCT", name, description, price, productType);
            myDialogFragment.show(manager, "myDialog");
        });

        updateButton.setOnClickListener(view -> {
            String newName = etName.getText().toString();
            String newDescription = etDescription.getText().toString();
            String newPrice = etPrice.getText().toString().replace("$", "");

            if (validateInput(newName, newDescription, newPrice)) {
                try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                    int res = db.updateProduct(name, description, price, newName, newDescription, newPrice, productType);
                    if (res == 0) {
                        Toast.makeText(getApplicationContext(), "This product is already in the database", Toast.LENGTH_SHORT).show();
                    } else if (res == 1) {
                        Toast.makeText(getApplicationContext(), "Product updated", Toast.LENGTH_LONG).show();
                        navigateToAdminActivity();
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

    private void navigateToAdminActivity() {
        Class<?> adminActivityClass = productType.equals("lab")? LabTestAdminActivity.class : BuyMedicineAdminActivity.class;
        startActivity(new Intent(EditProductActivity.this, adminActivityClass));
    }

    private boolean validateInput(String newName, String newDescription, String newPrice) {
        if (newName.isEmpty() || newDescription.isEmpty() || newPrice.isEmpty()) {
            Toast.makeText(this, "Please enter all data", Toast.LENGTH_SHORT).show();
            return false;
        }

        double price;
        try {
            price = Double.parseDouble(newPrice);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Incorrect price", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (price <= 0 || price > 999) {
            Toast.makeText(this, "Incorrect price", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}