package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthArticlesActivity extends AppCompatActivity {

    private final String[][] health_details = {
            {"W", "", "", "", "Click for more details"},
            {"H", "", "", "", "Click for more details"},
            {"S", "", "", "", "Click for more details"},
            {"P", "", "", "", "Click for more details"},
            {"O", "", "", "", "Click for more details"},
    };

    private final int[] images = {
            R.drawable.health1,
            R.drawable.health2,
            R.drawable.health3,
            R.drawable.health4,
            R.drawable.health5
    };

    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> item;
    SimpleAdapter sa;
    Button btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles);

        btnBack = findViewById(R.id.btnHABack);

        btnBack.setOnClickListener(view -> startActivity(new Intent(HealthArticlesActivity.this, HomeActivity.class)));

        list = new ArrayList<>();
        for (String[] doctor_detail : health_details) {
            item = new HashMap<>();
            item.put("line1", doctor_detail[0]);
            item.put("line2", doctor_detail[1]);
            item.put("line3", doctor_detail[2]);
            item.put("line4", doctor_detail[3]);
            item.put("line5", doctor_detail[4]);
            list.add(item);
        }

        sa = new SimpleAdapter(this, list, R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"}, new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
        ListView lvHA = findViewById(R.id.lvHA);
        lvHA.setAdapter(sa);

        lvHA.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent it = new Intent(HealthArticlesActivity.this, HealthArticlesDetailsActivity.class);
            it.putExtra("text1", health_details[i][0]);
            it.putExtra("text2", images[i]);
            startActivity(it);
        });
    }
}