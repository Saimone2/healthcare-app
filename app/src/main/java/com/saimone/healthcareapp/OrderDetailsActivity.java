package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

public class OrderDetailsActivity extends AppCompatActivity {
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    ListView lvOD;
    Button btnODBack;
    TextView tvODTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        lvOD = findViewById(R.id.lvOD);
        btnODBack = findViewById(R.id.btnODBack);
        tvODTitle = findViewById(R.id.tvODTitle);

        btnODBack.setOnClickListener(view -> startActivity(new Intent(OrderDetailsActivity.this, HomeActivity.class)));

        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            ArrayList<String> dbData = db.getOrderData(username);
            if (dbData.isEmpty()) {
                String str = "No orders";
                tvODTitle.setText(str);
            } else {
                String[][] order_details = new String[dbData.size()][];
                for (int i = 0; i < order_details.length; i++) {
                    order_details[i] = new String[5];
                    String arrData = dbData.get(i);
                    String[] strData = arrData.split(Pattern.quote("$"));
                    order_details[i][0] = strData[0];
                    order_details[i][1] = strData[1];

                    if (strData[7].compareTo("medicine") == 0) {
                        order_details[i][3] = "Del:" + strData[4];
                    } else {
                        order_details[i][3] = "Del:" + strData[4] + " " + strData[5];
                    }
                    order_details[i][2] = "Rs. " + strData[6];
                    order_details[i][4] = strData[7];

                    list = new ArrayList<>();
                    for (String[] order : order_details) {
                        item = new HashMap<>();
                        item.put("line1", order[0]);
                        item.put("line2", order[1]);
                        item.put("line3", order[2]);
                        item.put("line4", order[3]);
                        item.put("line5", order[4]);
                        list.add(item);
                    }
                    sa = new SimpleAdapter(this, list, R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"}, new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
                    lvOD.setAdapter(sa);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}