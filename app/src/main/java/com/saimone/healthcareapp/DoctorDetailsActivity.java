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

public class DoctorDetailsActivity extends AppCompatActivity {
    TextView tvDDTitle;
    Button btnDDBack;
    String[][] doctor_details = {};
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> item;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        tvDDTitle = findViewById(R.id.tvDDTitle);
        Intent it = getIntent();
        String title = it.getStringExtra("title");
        tvDDTitle.setText(title);

        doctor_details = getDoctorDetails(title);

        list = new ArrayList<>();
        for (String[] doctor_detail : doctor_details) {
            item = new HashMap<>();
            item.put("line1", doctor_detail[0]);
            item.put("line2", doctor_detail[1]);
            item.put("line3", doctor_detail[2]);
            item.put("line4", doctor_detail[3]);
            item.put("line5", "Cons Fees: " + doctor_detail[4] + "$");
            list.add(item);
        }
        sa = new SimpleAdapter(this, list, R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"}, new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
        ListView  lvDD = findViewById(R.id.lvDD);
        lvDD.setAdapter(sa);

        btnDDBack = findViewById(R.id.btnDDBack);
        btnDDBack.setOnClickListener(view -> startActivity(new Intent(DoctorDetailsActivity.this, FindDoctorActivity.class)));
    }

    private String[][] getDoctorDetails(String title) {
        if(title.equals("Family Physician")) {
            return new String[][] {
                    {"Doctor Name : 1", "Hospital Address : ", "Experience : ", "Mobile No:", ""},
                    {"Doctor Name : 2", "Hospital Address : ", "Experience : ", "Mobile No:", ""},
                    {"Doctor Name : 3", "Hospital Address : ", "Experience : ", "Mobile No:", ""}
            };
        } else {
            return null;
        }
    }
}