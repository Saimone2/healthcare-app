package com.saimone.healthcare.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.saimone.healthcare.R;
import com.saimone.healthcare.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindDoctorsDetailsActivity extends AppCompatActivity {
    TextView tvTitle;
    SearchView searchView;
    ListView listView;
    Button backButton;
    private ArrayList<Map<String, String>> list;
    private SimpleAdapter sa;
    private String[][] doctorsDetails = {};
    private String[][] filteredDoctorsDetails = {};
    String specialty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        tvTitle = findViewById(R.id.tvDDTitle);
        searchView = findViewById(R.id.searchViewDD);
        listView = findViewById(R.id.listViewDD);
        backButton = findViewById(R.id.btnDDBack);

        backButton.setOnClickListener(view -> startActivity(new Intent(FindDoctorsDetailsActivity.this, FindDoctorActivity.class)));

        @SuppressLint("DiscouragedApi") int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(getResources().getColor(R.color.white));

        specialty = getIntent().getStringExtra("specialty");
        doctorsDetails = getDoctorDetails(specialty);
        filteredDoctorsDetails = doctorsDetails;
        if(doctorsDetails.length == 0) {
            String str = "Doctors not found";
            tvTitle.setText(str);
        } else {
            tvTitle.setText(specialty);
            list = new ArrayList<>();
            for (String[] doctor : doctorsDetails) {
                HashMap<String, String> item = new HashMap<>();
                item.put("line1", doctor[0]);
                item.put("line2", "Address: " + doctor[1]);
                item.put("line3", "Exp: " + doctor[2]);
                item.put("line4", "Mobile No: " + doctor[3]);
                item.put("line5", "Service fee: " + doctor[4] + "$");
                list.add(item);
            }
            sa = new SimpleAdapter(this, list,
                    R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            listView.setAdapter(sa);

            listView.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent intent = new Intent(FindDoctorsDetailsActivity.this, BookAppointmentActivity.class);
                intent.putExtra("title", specialty);
                intent.putExtra("fullname", filteredDoctorsDetails[i][0]);
                intent.putExtra("address", filteredDoctorsDetails[i][1]);
                intent.putExtra("phone", filteredDoctorsDetails[i][3]);
                intent.putExtra("fee", filteredDoctorsDetails[i][4] + "$");
                startActivity(intent);
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
    }

    private String[][] getDoctorDetails(String specialty) {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getDoctorsBySpecialty(specialty);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[][]{};
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