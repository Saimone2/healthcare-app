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

public class LabTestAdminActivity extends AppCompatActivity {
    TextView tvTitle;
    ListView listView;
    Button addNewButton, backButton;
    String[][] labtests_details = {};
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_admin);

        addNewButton = findViewById(R.id.btnLTAAddNew);
        backButton = findViewById(R.id.btnLTABack);
        listView = findViewById(R.id.listViewLTA);
        tvTitle = findViewById(R.id.tvLTATitle);

        backButton.setOnClickListener(view -> startActivity(new Intent(LabTestAdminActivity.this, AdminPanelActivity.class)));

        labtests_details = getLabTestsDetails();
        if(labtests_details.length == 0) {
            String str = "Lab Tests not found";
            tvTitle.setText(str);
        } else {
            list = new ArrayList<>();
            for (String[] labTest : labtests_details) {
                item = new HashMap<>();
                item.put("line1", labTest[0]);
                item.put("line2", "");
                item.put("line3", "");
                item.put("line4", "");
                item.put("line5", "Total cost: " + labTest[2] + "$");
                list.add(item);
            }
            sa = new SimpleAdapter(this, list,
                    R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            listView.setAdapter(sa);

            listView.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent it = new Intent(LabTestAdminActivity.this, EditProductActivity.class);
                it.putExtra("product", "lab");
                it.putExtra("name", labtests_details[i][0]);
                it.putExtra("description", labtests_details[i][1]);
                it.putExtra("price", labtests_details[i][2]);
                startActivity(it);
            });
        }

        addNewButton.setOnClickListener(view -> {
            Intent it = new Intent(LabTestAdminActivity.this, NewProductActivity.class);
            it.putExtra("product", "lab");
            startActivity(it);
        });
    }

    private String[][] getLabTestsDetails() {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getLabTests();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[][]{};
    }
}