package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class LabTestActivity extends AppCompatActivity {
    TextView tvTitle;
    ListView listView;
    Button cartButton, backButton;
    String[][] labtests_details = {};
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    private final String[][] packages = {

            {"Full Body Checkup", "", "", "", "999"},
            {"Blood Glucose Fasting", "", "", "", "299"},
            {"COVID-19 Antibody - IgG", "", "", "", "899"},
            {"Thyroid Check", "", "", "", "499"},
            {"Immunity Check", "", "", "", "699"}
    };
    private final String[] packages_details = {
            "Blood Glucose Fasting",
            "Blood Glucose Fasting",
            "COVID-19 Antibody - IgG",
            "Thyroid Profile",
            "Complete Hemogram"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test);

        cartButton = findViewById(R.id.btnLTGoToCart);
        backButton = findViewById(R.id.btnLTBack);
        listView = findViewById(R.id.listViewLT);
        tvTitle = findViewById(R.id.tvLTTitle);

        backButton.setOnClickListener(view -> startActivity(new Intent(LabTestActivity.this, HomeActivity.class)));

        labtests_details = getLabTestsDetails();
        if(labtests_details.length == 0) {
            String str = "Lab Tests not found";
            tvTitle.setText(str);
        } else {
            list = new ArrayList<>();
            for (String[] aPackage : packages) {
                item = new HashMap<>();
                item.put("line1", aPackage[0]);
                item.put("line2", aPackage[1]);
                item.put("line3", aPackage[2]);
                item.put("line4", aPackage[3]);
                item.put("line5", "Total cost: " + aPackage[4] + "$");
                list.add(item);
            }
            sa = new SimpleAdapter(this, list,
                    R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            listView.setAdapter(sa);
        }

        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Intent it = new Intent(LabTestActivity.this, LabTestDetailsActivity.class);
            it.putExtra("text1", packages[i][0]);
            it.putExtra("text2", packages_details[i]);
            it.putExtra("text3", packages[i][4]);
            startActivity(it);
        });

        cartButton.setOnClickListener(view -> startActivity(new Intent(LabTestActivity.this, CartLabTestActivity.class)));
    }

    private String[][] getLabTestsDetails() {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getLabTests();
        }
    }
}