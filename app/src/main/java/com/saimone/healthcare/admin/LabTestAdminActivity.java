package com.saimone.healthcare.admin;

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
import com.saimone.healthcare.admin.add.NewProductActivity;
import com.saimone.healthcare.admin.edit.EditProductActivity;
import com.saimone.healthcare.database.Database;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LabTestAdminActivity extends AppCompatActivity {
    TextView tvTitle;
    SearchView searchView;
    ListView listView;
    Button addNewButton, backButton;
    private ArrayList<Map<String, String>> list;
    private SimpleAdapter sa;
    private String[][] labtestsDetails = {};
    private String[][] filteredLabtestsDetails = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab_test_admin);

        tvTitle = findViewById(R.id.tvLTATitle);
        searchView = findViewById(R.id.searchViewLTA);
        listView = findViewById(R.id.listViewLTA);
        addNewButton = findViewById(R.id.btnLTAAddNew);
        backButton = findViewById(R.id.btnLTABack);

        backButton.setOnClickListener(view -> startActivity(new Intent(LabTestAdminActivity.this, AdminPanelActivity.class)));

        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(getResources().getColor(R.color.white));

        labtestsDetails = getLabTestsDetails();
        filteredLabtestsDetails = labtestsDetails;
        if(labtestsDetails.length == 0) {
            String str = "Lab tests not found";
            tvTitle.setText(str);
        } else {
            list = new ArrayList<>();
            for (String[] labTest : labtestsDetails) {
                HashMap<String, String> item = new HashMap<>();
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
                it.putExtra("name", filteredLabtestsDetails[i][0]);
                it.putExtra("description", filteredLabtestsDetails[i][1]);
                it.putExtra("price", filteredLabtestsDetails[i][2]);
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

    private void filterData(String query) {
        list.clear();
        if (query.isEmpty()) {
            list.addAll(generateItemList(labtestsDetails));
            filteredLabtestsDetails = labtestsDetails;
        } else {
            filteredLabtestsDetails = filterLabtestsDetails(query);
            list.addAll(generateItemList(filteredLabtestsDetails));
        }
        sa.notifyDataSetChanged();

        String str;
        if (list.isEmpty()) {
            str = "Lab tests not found";
        } else {
            str = "Lab tests";
        }
        tvTitle.setText(str);
    }

    private List<Map<String, String>> generateItemList(String[][] data) {
        List<Map<String, String>> itemList = new ArrayList<>();
        for (String[] labTest : data) {
            Map<String, String> item = new HashMap<>();
            item.put("line1", labTest[0]);
            item.put("line2", "");
            item.put("line3", "");
            item.put("line4", "");
            item.put("line5", "Total cost: " + labTest[2] + "$");
            itemList.add(item);
        }
        return itemList;
    }

    private String[][] filterLabtestsDetails(String query) {
        List<String[]> filteredList = new ArrayList<>();
        for (String[] labTest : labtestsDetails) {
            if (labTest[0].toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(labTest);
            }
        }
        return filteredList.toArray(new String[0][0]);
    }
}