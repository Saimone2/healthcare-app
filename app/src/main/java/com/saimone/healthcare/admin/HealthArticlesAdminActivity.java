package com.saimone.healthcare.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.add.NewArticlesActivity;
import com.saimone.healthcare.admin.edit.EditArticlesActivity;
import com.saimone.healthcare.database.Database;

import java.util.ArrayList;
import java.util.HashMap;

public class HealthArticlesAdminActivity extends AppCompatActivity {
    TextView tvTitle;
    ListView listView;
    Button addNewButton, backButton;
    ArrayList<HashMap<String, String>> list;
    HashMap<String, String> item;
    SimpleAdapter sa;
    String[][] healthArticles = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health_articles_admin);

        addNewButton = findViewById(R.id.btnHAAAddNew);
        tvTitle = findViewById(R.id.tvHAATitle);
        listView = findViewById(R.id.listViewHAA);
        backButton = findViewById(R.id.btnHAABack);

        backButton.setOnClickListener(view -> startActivity(new Intent(HealthArticlesAdminActivity.this, AdminPanelActivity.class)));

        addNewButton.setOnClickListener(view -> startActivity(new Intent(HealthArticlesAdminActivity.this, NewArticlesActivity.class)));

        healthArticles = getHealthArticles();
        if(healthArticles.length == 0) {
            String str = "Health articles not found";
            tvTitle.setText(str);
        } else {
            list = new ArrayList<>();
            for (String[] healthArticle : healthArticles) {
                item = new HashMap<>();
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
                Intent it = new Intent(HealthArticlesAdminActivity.this, EditArticlesActivity.class);
                it.putExtra("name", healthArticles[i][0]);
                it.putExtra("description", healthArticles[i][1]);
                it.putExtra("image_path", healthArticles[i][2]);
                startActivity(it);
            });
        }
    }

    private String[][] getHealthArticles() {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getHealthArticles();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new String[][]{};
    }
}