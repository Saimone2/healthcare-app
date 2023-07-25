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
    TextView tvTitle;
    ListView listView;
    Button backButton;
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    String[][] order_details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        tvTitle = findViewById(R.id.tvODTitle);
        listView = findViewById(R.id.listViewOD);
        backButton = findViewById(R.id.btnODBack);

        backButton.setOnClickListener(view -> startActivity(new Intent(OrderDetailsActivity.this, HomeActivity.class)));

        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");

            ArrayList<String> dbData = db.getOrderData(username);
            if (dbData.isEmpty()) {
                String str = "No orders";
                tvTitle.setText(str);
            } else {
                order_details = new String[dbData.size()][];
                for (int i = 0; i < order_details.length; i++) {
                    order_details[i] = new String[5];
                    String arrData = dbData.get(i);
                    String[] strData = arrData.split(Pattern.quote("$"));
                    order_details[i][0] = strData[0];
                    order_details[i][1] = strData[1];

                    if (strData[7].compareTo("medicine") == 0) {
                        order_details[i][3] = "Delivery: " + strData[4];
                        order_details[i][4] = "Buying medicines";
                    } else if(strData[7].compareTo("lab") == 0) {
                        order_details[i][3] = "Delivery: " + strData[4] + " " + strData[5];
                        order_details[i][4] = "Registration for lab tests";
                    } else {
                        order_details[i][3] = "Reception time: " + strData[4] + " " + strData[5];
                        order_details[i][4] = "Doctor's appointment";
                    }
                    order_details[i][2] = "Price: " + strData[6] + "$";
                }

                list = new ArrayList<>();
                for (String[] order_detail : order_details) {
                    item = new HashMap<>();
                    item.put("line1", order_detail[0]);
                    item.put("line2", order_detail[3]);
                    item.put("line3", order_detail[1]);
                    item.put("line4", order_detail[2]);
                    item.put("line5", order_detail[4]);
                    list.add(item);
                }
                sa = new SimpleAdapter(this, list,
                        R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                        new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
                listView.setAdapter(sa);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}