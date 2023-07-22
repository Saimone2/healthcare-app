package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LabTestDetailsActivity extends AppCompatActivity {
    TextView tvLDPackageName, tvTotalCost;
    TextView etLDDetails;
    Button btnLDAddToCart, btnLTBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_details);

        tvLDPackageName = findViewById(R.id.tvLDPackageName);
        tvTotalCost = findViewById(R.id.tvLDTotalCost);
        etLDDetails = findViewById(R.id.etLDDetails);

        etLDDetails.setKeyListener(null);

        Intent it = getIntent();
        tvLDPackageName.setText(it.getStringExtra("text1"));
        etLDDetails.setText(it.getStringExtra("text2"));
        String str = "Total cost: " + it.getStringExtra("text3") + "$";
        tvTotalCost.setText(str);

        btnLTBack = findViewById(R.id.btnLDBack);
        btnLTBack.setOnClickListener(view -> startActivity(new Intent(LabTestDetailsActivity.this, LabTestActivity.class)));

        btnLDAddToCart = findViewById(R.id.btnLDAddToCart);
        btnLDAddToCart.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String product = tvLDPackageName.getText().toString();
            float price = Float.parseFloat(it.getStringExtra("text3"));

            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                if(db.checkCart(username, product) == 1) {
                    Toast.makeText(getApplicationContext(), "This product has already been added", Toast.LENGTH_SHORT).show();
                } else {
                    db.addToCart(username, product, price, "lab");
                    Toast.makeText(getApplicationContext(), "The product is placed in the cart", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LabTestDetailsActivity.this, LabTestActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}