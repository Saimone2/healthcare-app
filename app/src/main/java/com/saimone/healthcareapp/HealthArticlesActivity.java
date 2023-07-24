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
    ListView listView;
    Button btnBack;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> item;
    SimpleAdapter sa;
    private final String[][] health_details = {
            {"W", "", "", "", "Click for more details"},
            {"H", "", "", "", "Click for more details"},
            {"S", "", "", "", "Click for more details"},
            {"O", "", "", "", "Click for more details"}
    };
    private final int[] images = {
            R.drawable.health1,
            R.drawable.health2,
            R.drawable.health3,
            R.drawable.health4
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles);

        listView = findViewById(R.id.listViewHA);
        btnBack = findViewById(R.id.btnHABack);

        btnBack.setOnClickListener(view -> startActivity(new Intent(HealthArticlesActivity.this, HomeActivity.class)));

        list = new ArrayList<>();
        for (String[] health : health_details) {
            item = new HashMap<>();
            item.put("line1", health[0]);
            item.put("line2", health[1]);
            item.put("line3", health[2]);
            item.put("line4", health[3]);
            item.put("line5", health[4]);
            list.add(item);
        }
        sa = new SimpleAdapter(this, list,
                R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
        listView.setAdapter(sa);

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent it = new Intent(HealthArticlesActivity.this, HealthArticlesDetailsActivity.class);
            it.putExtra("text1", health_details[i][0]);
            it.putExtra("text2", images[i]);
            startActivity(it);
        });
    }
}