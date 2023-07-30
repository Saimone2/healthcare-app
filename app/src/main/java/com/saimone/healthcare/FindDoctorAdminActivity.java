package com.saimone.healthcare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class FindDoctorAdminActivity extends AppCompatActivity {
    TextView tvTitle;
    ListView listView;
    Button addNewButton, backButton, deleteButton;
    String[][] doctors_details = {};
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> item;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor_admin);

        tvTitle = findViewById(R.id.tvFDATitle);
        backButton = findViewById(R.id.btnFDABack);
        listView = findViewById(R.id.listViewFDA);
        addNewButton = findViewById(R.id.btnFDAAddNew);
        deleteButton = findViewById(R.id.btnFDADelete);

        backButton.setOnClickListener(view -> startActivity(new Intent(FindDoctorAdminActivity.this, FindDoctorSpecialityAdminActivity.class)));

        String specialty = getIntent().getStringExtra("specialty");
        doctors_details = getDoctorsBySpecialty(specialty);

        if(doctors_details.length == 0) {
            String str = "Doctors not found";
            tvTitle.setText(str);
        } else {
            tvTitle.setText(specialty);
            list = new ArrayList<>();
            for (String[] doctor_detail : doctors_details) {
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
                Intent it = new Intent(FindDoctorAdminActivity.this, EditDoctorActivity.class);
                it.putExtra("specialty", specialty);
                it.putExtra("fullname", doctors_details[i][0]);
                it.putExtra("hospital_address", doctors_details[i][1]);
                it.putExtra("experience", doctors_details[i][2]);
                it.putExtra("phone", doctors_details[i][3]);
                it.putExtra("fee", doctors_details[i][4]);
                startActivity(it);
            });
        }

        addNewButton.setOnClickListener(view -> {
            Intent it = new Intent(FindDoctorAdminActivity.this, NewDoctorActivity.class);
            it.putExtra("specialty", specialty);
            startActivity(it);
        });

        deleteButton.setOnClickListener(view -> {
            FragmentManager manager = getSupportFragmentManager();
            MyDialogFragment myDialogFragment = MyDialogFragment.newInstance("DELETE SPECIALTY", specialty);
            myDialogFragment.show(manager, "myDialog");
        });
    }

    private String[][] getDoctorsBySpecialty(String specialty) {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getDoctorsBySpecialty(specialty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[][] {};
    }
}