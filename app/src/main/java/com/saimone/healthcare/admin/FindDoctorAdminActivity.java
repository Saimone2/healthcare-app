package com.saimone.healthcare.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.add.NewDoctorActivity;
import com.saimone.healthcare.admin.edit.EditDoctorActivity;
import com.saimone.healthcare.components.MyDialogFragment;
import com.saimone.healthcare.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDoctorAdminActivity extends AppCompatActivity {
    TextView tvTitle;
    SearchView searchView;
    ListView listView;
    Button addNewButton, backButton, deleteButton;
    private ArrayList<Map<String, String>> list;
    private SimpleAdapter sa;
    private String[][] doctorsDetails = {};
    private String[][] filteredDoctorsDetails = {};
    private String specialty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor_admin);

        tvTitle = findViewById(R.id.tvFDATitle);
        searchView = findViewById(R.id.searchViewFDA);
        listView = findViewById(R.id.listViewFDA);
        addNewButton = findViewById(R.id.btnFDAAddNew);
        backButton = findViewById(R.id.btnFDABack);
        deleteButton = findViewById(R.id.btnFDADelete);

        backButton.setOnClickListener(view -> startActivity(new Intent(FindDoctorAdminActivity.this, FindDoctorSpecialityAdminActivity.class)));

        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(getResources().getColor(R.color.white));

        specialty = getIntent().getStringExtra("specialty");
        doctorsDetails = getDoctorsBySpecialty(specialty);
        filteredDoctorsDetails = doctorsDetails;
        if(doctorsDetails.length == 0) {
            String str = "Doctors not found";
            tvTitle.setText(str);
        } else {
            tvTitle.setText(specialty);
            list = new ArrayList<>();
            for (String[] doctor_detail : doctorsDetails) {
                HashMap<String, String> item = new HashMap<>();
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
                it.putExtra("fullname", filteredDoctorsDetails[i][0]);
                it.putExtra("hospital_address", filteredDoctorsDetails[i][1]);
                it.putExtra("experience", filteredDoctorsDetails[i][2]);
                it.putExtra("phone", filteredDoctorsDetails[i][3]);
                it.putExtra("fee", filteredDoctorsDetails[i][4]);
                startActivity(it);
            });
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return true;
            }
        });

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

    private void filterData(String query) {
        list.clear();
        if (query.isEmpty()) {
            list.addAll(generateItemList(doctorsDetails));
            filteredDoctorsDetails = doctorsDetails;
        } else {
            filteredDoctorsDetails = filterDoctorsDetails(query);
            list.addAll(generateItemList(filteredDoctorsDetails));
        }
        sa.notifyDataSetChanged();

        String str;
        if (list.isEmpty()) {
            str = "Doctors not found";
        } else {
            str = specialty;
        }
        tvTitle.setText(str);
    }

    private List<Map<String, String>> generateItemList(String[][] data) {
        List<Map<String, String>> itemList = new ArrayList<>();
        for (String[] doctor : data) {
            Map<String, String> item = new HashMap<>();
            item.put("line1", doctor[0]);
            item.put("line2", "Address: " + doctor[1]);
            item.put("line3", "Exp: " + doctor[2]);
            item.put("line4", "Mobile No: " + doctor[3]);
            item.put("line5", "Service fee: " + doctor[4] + "$");
            itemList.add(item);
        }
        return itemList;
    }

    private String[][] filterDoctorsDetails(String query) {
        List<String[]> filteredList = new ArrayList<>();
        for (String[] doctor : doctorsDetails) {
            if (doctor[0].toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(doctor);
            }
        }
        return filteredList.toArray(new String[0][0]);
    }
}