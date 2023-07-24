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
    TextView tvTitle, tvTotalCost, tvDetails;
    Button cartButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine_details);

        tvTitle = findViewById(R.id.tvBMDTitle);
        tvTotalCost = findViewById(R.id.tvBMDTotalCost);
        tvDetails = findViewById(R.id.tvBMDDetails);
        cartButton = findViewById(R.id.btnBMDAddToCart);
        backButton = findViewById(R.id.btnBMDBack);

        Intent it = getIntent();
        tvTitle.setText(it.getStringExtra("text1"));
        tvDetails.setText(it.getStringExtra("text2"));
        String str = "Total Cost:" + it.getStringExtra("text3") + "$";
        tvTotalCost.setText(str);

        backButton.setOnClickListener(view -> new Intent(BuyMedicineDetailsActivity.this, BuyMedicineActivity.class));

        cartButton.setOnClickListener(view -> {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String product = tvTitle.getText().toString();
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