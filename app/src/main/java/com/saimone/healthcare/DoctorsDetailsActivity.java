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

public class DoctorsDetailsActivity extends AppCompatActivity {
    TextView tvTitle;
    ListView listView;
    Button backButton;
    String[][] doctor_details = {};
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> item;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_details);

        tvTitle = findViewById(R.id.tvDDTitle);
        backButton = findViewById(R.id.btnDDBack);
        listView = findViewById(R.id.listViewDD);

        Intent it = getIntent();
        String title = it.getStringExtra("title");

        doctor_details = getDoctorDetails(title);
        if(doctor_details.length == 0) {
            String str = "Doctors not found";
            tvTitle.setText(str);
        } else {
            tvTitle.setText(title);
            list = new ArrayList<>();
            for (String[] doctor_detail : doctor_details) {
                item = new HashMap<>();
                item.put("line1", doctor_detail[0]);
                item.put("line2", "Address: " + doctor_detail[1]);
                item.put("line3", "Exp: " + doctor_detail[2]);
                item.put("line4", "Mobile No: " + doctor_detail[3]);
                item.put("line5", "Service fee: " + doctor_detail[4] + "$");
                list.add(item);
            }
            sa = new SimpleAdapter(this, list,
                    R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            listView.setAdapter(sa);

            listView.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent intent = new Intent(DoctorsDetailsActivity.this, BookAppointmentActivity.class);
                intent.putExtra("text1", title);
                intent.putExtra("text2", doctor_details[i][0]);
                intent.putExtra("text3", doctor_details[i][1]);
                intent.putExtra("text4", doctor_details[i][3]);
                intent.putExtra("text5", doctor_details[i][4] + "$");
                startActivity(intent);
            });
        }

        backButton.setOnClickListener(view -> startActivity(new Intent(DoctorsDetailsActivity.this, FindDoctorActivity.class)));
    }

    private String[][] getDoctorDetails(String specialty) {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getDoctorsBySpecialty(specialty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[][]{};
    }
}