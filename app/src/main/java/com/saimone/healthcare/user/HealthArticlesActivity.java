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

public class HealthArticlesActivity extends AppCompatActivity {
    TextView tvTitle;
    SearchView searchView;
    ListView listView;
    Button backButton;
    private ArrayList<Map<String, String>> list;
    private SimpleAdapter sa;
    private String[][] healthArticles = {};
    private String[][] filteredHealthArticles = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles);

        tvTitle = findViewById(R.id.tvHATitle);
        searchView = findViewById(R.id.searchViewHA);
        listView = findViewById(R.id.listViewHA);
        backButton = findViewById(R.id.btnHABack);

        backButton.setOnClickListener(view -> startActivity(new Intent(HealthArticlesActivity.this, HomeActivity.class)));

        int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(getResources().getColor(R.color.white));

        healthArticles = getHealthArticles();
        filteredHealthArticles = healthArticles;
        if(healthArticles.length == 0) {
            String str = "Health articles not found";
            tvTitle.setText(str);
        } else {
            list = new ArrayList<>();
            for (String[] healthArticle : healthArticles) {
                HashMap<String, String> item = new HashMap<>();
                item.put("line1", healthArticle[0]);
                item.put("line2", healthArticle[1]);
                item.put("line3", "");
                item.put("line4", "");
                item.put("line5", "    View Details");
                list.add(item);
            }
            sa = new SimpleAdapter(this, list,
                    R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            listView.setAdapter(sa);

            listView.setOnItemClickListener((adapterView, view, i, l) -> {
                Intent it = new Intent(HealthArticlesActivity.this, HealthArticlesDetailsActivity.class);
                it.putExtra("title", filteredHealthArticles[i][0]);
                it.putExtra("image_path", filteredHealthArticles[i][2]);
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

    private String[][] getHealthArticles() {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getHealthArticles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[][]{};
    }

    private void filterData(String query) {
        list.clear();
        if (query.isEmpty()) {
            list.addAll(generateItemList(healthArticles));
            filteredHealthArticles = healthArticles;
        } else {
            filteredHealthArticles = filterHealthArticles(query);
            list.addAll(generateItemList(filteredHealthArticles));
        }
        sa.notifyDataSetChanged();

        String str;
        if (list.isEmpty()) {
            str = "Health articles not found";
        } else {
            str = "Health articles";
        }
        tvTitle.setText(str);
    }

    private List<Map<String, String>> generateItemList(String[][] data) {
        List<Map<String, String>> itemList = new ArrayList<>();
        for (String[] article : data) {
            Map<String, String> item = new HashMap<>();
            item.put("line1", article[0]);
            item.put("line2", article[1]);
            item.put("line3", "");
            item.put("line4", "");
            item.put("line5", "    View Details");
            itemList.add(item);
        }
        return itemList;
    }

    private String[][] filterHealthArticles(String query) {
        List<String[]> filteredList = new ArrayList<>();
        for (String[] article : healthArticles) {
            if (article[0].toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(article);
            }
        }
        return filteredList.toArray(new String[0][0]);
    }
}