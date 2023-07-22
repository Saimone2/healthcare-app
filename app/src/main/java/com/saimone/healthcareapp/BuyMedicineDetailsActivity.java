package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class BuyMedicineDetailsActivity extends AppCompatActivity {
    TextView tvBMDPackageName, tvTotalCost, etBMDDetails;
    Button btnBMDAddToCart, btnBMDBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_details);

        tvBMDPackageName = findViewById(R.id.tvBMDPackageName);
        tvTotalCost = findViewById(R.id.tvBMDTotalCost);
        etBMDDetails = findViewById(R.id.etBMDDetails);
        btnBMDAddToCart = findViewById(R.id.btnBMDAddToCart);
        btnBMDBack = findViewById(R.id.btnBMDBack);

        Intent it = getIntent();
        tvBMDPackageName.setText(it.getStringExtra("text1"));
        etBMDDetails.setText(it.getStringExtra("text2"));
        String str = "Total Cost:" + it.getStringExtra("text3") + "$";
        tvTotalCost.setText(str);

        btnBMDBack.setOnClickListener(view -> new Intent(BuyMedicineDetailsActivity.this, BuyMedicineActivity.class));

        btnBMDAddToCart.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String product = tvBMDPackageName.getText().toString();
            float price = Float.parseFloat(it.getStringExtra("text3"));

            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                if(db.checkCart(username, product) == 1) {
                    Toast.makeText(getApplicationContext(), "This product has already been added", Toast.LENGTH_SHORT).show();
                } else {
                    db.addToCart(username, product, price, "medicine");
                    Toast.makeText(getApplicationContext(), "The product is placed in the cart", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BuyMedicineDetailsActivity.this, BuyMedicineActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

    }
}