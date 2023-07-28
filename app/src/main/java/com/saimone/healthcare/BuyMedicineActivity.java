package com.saimone.healthcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class BuyMedicineActivity extends AppCompatActivity {
    Button cartButton, backButton;
    ListView listView;
    TextView tvTitle;
    String[][] medicine_details = {};
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_medicine);

        cartButton = findViewById(R.id.btnBMGoToCart);
        backButton = findViewById(R.id.btnBMBack);
        listView = findViewById(R.id.listViewBM);
        tvTitle = findViewById(R.id.tvBMTitle);

        backButton.setOnClickListener(view -> startActivity(new Intent(BuyMedicineActivity.this, HomeActivity.class)));

        cartButton.setOnClickListener(view -> startActivity(new Intent(BuyMedicineActivity.this, CartBuyMedicineActivity.class)));

        medicine_details = getMedicinesDetails();
        if(medicine_details.length == 0) {
            String str = "Medicine not found";
            tvTitle.setText(str);
        } else {
            list = new ArrayList<>();
            for (String[] medicine : medicine_details) {
                item = new HashMap<>();
                item.put("line1", medicine[0]);
                item.put("line2", "");
                item.put("line3", "");
                item.put("line4", "");
                item.put("line5", "Total cost: " + medicine[2] + "$");
                list.add(item);
            }
            sa = new SimpleAdapter(this, list,
                    R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            listView.setAdapter(sa);

            listView.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent it = new Intent(BuyMedicineActivity.this, BuyMedicineDetailsActivity.class);
                it.putExtra("text1", medicine_details[i][0]);
                it.putExtra("text2", medicine_details[i][1]);
                it.putExtra("text3", medicine_details[i][2]);
                startActivity(it);
            });
        }
    }

    private String[][] getMedicinesDetails() {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getMedicines();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[][]{};
    }
}