package com.saimone.healthcare.user;

import androidx.appcompat.app.AppCompatActivity;

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

public class FindDoctorActivity extends AppCompatActivity {
    TextView tvTitle;
    SearchView searchView;
    ListView listView;
    Button backButton;
    private ArrayList<Map<String, String>> list;
    private SimpleAdapter sa;
    private String[] specialties = {};
    private String[] filteredSpecialties = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_doctor);

        tvTitle = findViewById(R.id.tvFDTitle);
        searchView = findViewById(R.id.searchViewFD);
        listView = findViewById(R.id.listViewFD);
        backButton = findViewById(R.id.btnFDBack);

        backButton.setOnClickListener(view -> startActivity(new Intent(FindDoctorActivity.this, HomeActivity.class)));

        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(getResources().getColor(R.color.white));

        specialties = getSpecialties();
        filteredSpecialties = specialties;
        if(specialties.length == 0) {
            String str = "Specialties not found";
            tvTitle.setText(str);
        } else {
            list = new ArrayList<>();
            for (String specialty : specialties) {
                HashMap<String, String> item = new HashMap<>();
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
                Intent it = new Intent(FindDoctorActivity.this, FindDoctorsDetailsActivity.class);
                it.putExtra("specialty", filteredSpecialties[i]);
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
    }

    private String[] getSpecialties() {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getSpecialties();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[]{};
    }

    private void filterData(String query) {
        list.clear();
        if (query.isEmpty()) {
            list.addAll(generateItemList(specialties));
            filteredSpecialties = specialties;
        } else {
            filteredSpecialties = filterSpecialties(query);
            list.addAll(generateItemList(filteredSpecialties));
        }
        sa.notifyDataSetChanged();

        String str;
        if (list.isEmpty()) {
            str = "Specialties not found";
        } else {
            str = "Specialty doctors";
        }
        tvTitle.setText(str);
    }

    private List<Map<String, String>> generateItemList(String[] data) {
        List<Map<String, String>> itemList = new ArrayList<>();
        for (String specialty : data) {
            Map<String, String> item = new HashMap<>();
            item.put("line1", specialty);
            item.put("line2", "");
            item.put("line3", "");
            item.put("line4", "");
            item.put("line5", "");
            itemList.add(item);
        }
        return itemList;
    }

    private String[] filterSpecialties(String query) {
        List<String> filteredList = new ArrayList<>();
        for (String specialty : specialties) {
            if (specialty.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(specialty);
            }
        }
        return filteredList.toArray(new String[0]);
    }
}