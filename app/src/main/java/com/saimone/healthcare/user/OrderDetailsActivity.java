package com.saimone.healthcare.user;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.regex.Pattern;

public class OrderDetailsActivity extends AppCompatActivity {
    TextView tvTitle;
    SearchView searchView;
    ListView listView;
    Button backButton;
    private ArrayList<Map<String, String>> list;
    private SimpleAdapter sa;
    private String[][] orderDetails = {};
    private String[][] filteredOrderDetails = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        tvTitle = findViewById(R.id.tvODTitle);
        searchView = findViewById(R.id.searchViewOD);
        listView = findViewById(R.id.listViewOD);
        backButton = findViewById(R.id.btnODBack);

        backButton.setOnClickListener(view -> startActivity(new Intent(OrderDetailsActivity.this, HomeActivity.class)));

        @SuppressLint("DiscouragedApi") int searchPlateId = searchView.getContext().getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        EditText searchPlate = searchView.findViewById(searchPlateId);
        searchPlate.setTextColor(getResources().getColor(R.color.white));

        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");
        ArrayList<String> dbData = getOrderData(username);
        if (dbData.isEmpty()) {
            String str = "No orders";
            tvTitle.setText(str);
        } else {
            orderDetails = new String[dbData.size()][];
            for (int i = 0; i < orderDetails.length; i++) {
                orderDetails[i] = new String[5];
                String arrData = dbData.get(i);
                String[] strData = arrData.split(Pattern.quote("$"));
                orderDetails[i][0] = strData[0];
                orderDetails[i][1] = strData[1];

                if (strData[7].compareTo("medicine") == 0) {
                    orderDetails[i][3] = "Delivery: " + strData[4];
                    orderDetails[i][4] = "Buying medicines";
                } else if(strData[7].compareTo("lab") == 0) {
                    orderDetails[i][3] = "Conducting: " + strData[4] + " " + strData[5];
                    orderDetails[i][4] = "Registration for lab tests";
                } else {
                    orderDetails[i][3] = "Reception time: " + strData[4] + " " + strData[5];
                    orderDetails[i][4] = "Doctor's appointment";
                }
                orderDetails[i][2] = "Price: " + strData[6] + "$";
            }
            filteredOrderDetails = orderDetails;

            list = new ArrayList<>();
            for (String[] order : orderDetails) {
                HashMap<String, String> item = new HashMap<>();
                item.put("line1", order[0]);
                item.put("line2", order[3]);
                item.put("line3", order[1]);
                item.put("line4", order[2]);
                item.put("line5", order[4]);
                list.add(item);
            }
            sa = new SimpleAdapter(this, list,
                    R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                    new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            listView.setAdapter(sa);
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

    private ArrayList<String> getOrderData(String username) {
        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            return db.getOrderData(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    private void filterData(String query) {
        list.clear();
        if (query.isEmpty()) {
            list.addAll(generateItemList(orderDetails));
        } else {
            filteredOrderDetails = filterOrderDetails(query);
            list.addAll(generateItemList(filteredOrderDetails));
        }
        sa.notifyDataSetChanged();

        String str;
        if (list.isEmpty()) {
            str = "No orders";
        } else {
            str = "Orders details";
        }
        tvTitle.setText(str);
    }

    private List<Map<String, String>> generateItemList(String[][] data) {
        List<Map<String, String>> itemList = new ArrayList<>();
        for (String[] order : data) {
            Map<String, String> item = new HashMap<>();
            item.put("line1", order[0]);
            item.put("line2", order[3]);
            item.put("line3", order[1]);
            item.put("line4", order[2]);
            item.put("line5", order[4]);
            itemList.add(item);
        }
        return itemList;
    }

    private String[][] filterOrderDetails(String query) {
        List<String[]> filteredList = new ArrayList<>();
        for (String[] order : orderDetails) {
            if (order[0].toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(order);
            }
        }
        return filteredList.toArray(new String[0][0]);
    }
}