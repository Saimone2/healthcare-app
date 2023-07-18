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

public class DoctorsDetailsActivity extends AppCompatActivity {
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

        // insertTestDataIntoDB();
        doctor_details = getDoctorDetails(title);

        if(doctor_details.length == 0) {
            String str = "Doctors not found";
            tvDDTitle.setText(str);
        } else {
            tvDDTitle.setText(title);
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

            sa = new SimpleAdapter(this, list, R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"}, new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            ListView lvDD = findViewById(R.id.lvDD);
            lvDD.setAdapter(sa);

            lvDD.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent intent = new Intent(DoctorsDetailsActivity.this, BookAppointmentActivity.class);
                intent.putExtra("text1", title);
                intent.putExtra("text2", doctor_details[i][0]);
                intent.putExtra("text3", doctor_details[i][1]);
                intent.putExtra("text4", doctor_details[i][3]);
                intent.putExtra("text5", doctor_details[i][4] + "$");
                startActivity(intent);
            });
        }

        btnDDBack = findViewById(R.id.btnDDBack);
        btnDDBack.setOnClickListener(view -> startActivity(new Intent(DoctorsDetailsActivity.this, FindDoctorActivity.class)));
    }

    private String[][] getDoctorDetails(String specialty) {
        try(DoctorDatabase db = new DoctorDatabase(getApplicationContext(), "healthcare-doctors", null, 1)) {
            return db.getDoctorsBySpecialty(specialty);
        }
    }

    private void insertTestDataIntoDB() {
        try(DoctorDatabase db = new DoctorDatabase(getApplicationContext(), "healthcare-doctors", null, 1)) {
            db.insertData("Family Physician", "John Smith", "303 Main St", 10, "555-1234", 150.0);
            db.insertData("Family Physician", "Sarah Johnson", "456 Elm St", 5, "555-5678", 120.0);
            db.insertData("Family Physician", "Emily Thompson", "789 Mine St", 8, "555-9012", 150.0);
            db.insertData("Family Physician", "Michael Davis", "321 Pine St", 12, "555-3456", 160.0);
            db.insertData("Family Physician", "Jessica Lee", "987 Maple St", 3, "555-7890", 110.0);

            db.insertData("Dietician", "John Doe", "123 Main St", 5, "555-1234", 100.0);
            db.insertData("Dietician", "Jane Smith", "456 Elm St", 7, "555-5678", 120.0);
            db.insertData("Dietician", "Michael Johnson", "789 Oak St", 3, "555-9012", 90.0);
            db.insertData("Dietician", "Emily Davis", "321 Pine St", 4, "555-3456", 110.0);
            db.insertData("Dietician", "David Wilson", "654 Maple St", 6, "555-7890", 95.0);

            db.insertData("Dentist", "Sarah Lee", "111 Baker St", 8, "555-1111", 200.0);
            db.insertData("Dentist", "Robert Thompson", "222 Jefferson St", 10, "555-2222", 220.0);
            db.insertData("Dentist", "Jennifer Anderson", "333 Lincoln St", 6, "555-3333", 180.0);
            db.insertData("Dentist", "Daniel Martinez", "444 Madison St", 9, "555-4444", 210.0);
            db.insertData("Dentist", "Elizabeth Clark", "555 Adams St", 7, "555-5555", 190.0);

            db.insertData("Surgeon", "William Rodriguez", "777 State St", 12, "555-7777", 500.0);
            db.insertData("Surgeon", "Karen Lewis", "888 Chestnut St", 15, "555-8888", 550.0);
            db.insertData("Surgeon", "Richard Hernandez", "999 Walnut St", 11, "555-9999", 480.0);
            db.insertData("Surgeon", "Patricia Thompson", "101 Park St", 13, "555-1010", 520.0);
            db.insertData("Surgeon", "Thomas Scott", "121 Market St", 14, "555-1212", 490.0);

            db.insertData("Cardiologist", "Linda Martin", "131 Spring St", 20, "555-1313", 300.0);
            db.insertData("Cardiologist", "Christopher Garcia", "141 Summer St", 18, "555-1414", 320.0);
            db.insertData("Cardiologist", "Susan Robinson", "151 Autumn St", 22, "555-1515", 280.0);
            db.insertData("Cardiologist", "Joseph Hill", "161 Winter St", 19, "555-1616", 330.0);
            db.insertData("Cardiologist", "Jessica King", "171 Park St", 21, "555-1717", 310.0);
        }
    }
}