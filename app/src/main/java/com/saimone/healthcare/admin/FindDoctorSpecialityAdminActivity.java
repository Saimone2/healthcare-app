package com.saimone.healthcare.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.add.NewSpecialtyActivity;
import com.saimone.healthcare.database.Database;

import java.util.ArrayList;
import java.util.HashMap;

public class FindDoctorSpecialityAdminActivity extends AppCompatActivity {
    TextView tvTitle;
    ListView listView;
    Button addNewButton, backButton;
    String[] specialties;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> item;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor_speciality_admin);

        tvTitle = findViewById(R.id.tvFDSATitle);
        backButton = findViewById(R.id.btnFDSABack);
        listView = findViewById(R.id.listViewFDSA);
        addNewButton = findViewById(R.id.btnFDSAAddNew);

        backButton.setOnClickListener(view -> startActivity(new Intent(FindDoctorSpecialityAdminActivity.this, AdminPanelActivity.class)));

        specialties = getSpecialties();
        if(specialties.length == 0) {
            String str = "Specialties not found";
            tvTitle.setText(str);
        } else {
            list = new ArrayList<>();
            for (String specialty : specialties) {
                item = new HashMap<>();
                item.put("line1", specialty);
                item.put("line2", "");
                item.put("line3", "");
                item.put("line4", "");
                item.put("line5", "");
                list.add(item);
            }
            sa = new SimpleAdapter(this, list,
                    R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            listView.setAdapter(sa);

            listView.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent it = new Intent(FindDoctorSpecialityAdminActivity.this, FindDoctorAdminActivity.class);
                it.putExtra("specialty", specialties[i]);
                startActivity(it);
            });
        }

        addNewButton.setOnClickListener(view -> startActivity(new Intent(FindDoctorSpecialityAdminActivity.this, NewSpecialtyActivity.class)));
    }

    private String[] getSpecialties() {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getSpecialties();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{};
    }
}