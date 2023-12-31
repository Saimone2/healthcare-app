package com.saimone.healthcare.user;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.components.CustomTimePickerDialog;
import com.saimone.healthcare.database.Database;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CartLabTestActivity extends AppCompatActivity {
    TextView tvTotalCost;
    ListView listView;
    Button dateButton, timeButton, checkoutButton, backButton;
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    private DatePickerDialog datePickerDialog;
    private CustomTimePickerDialog timePickerDialog;
    double totalAmount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_lab_test);

        tvTotalCost = findViewById(R.id.tvCLTTotalCost);
        listView = findViewById(R.id.lvCLTDetails);
        dateButton = findViewById(R.id.btnCLTDate);
        timeButton = findViewById(R.id.btnCLTTime);
        checkoutButton = findViewById(R.id.btnCLTCheckout);
        backButton = findViewById(R.id.btnCLTBack);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
        String username = sharedPreferences.getString("username", "");

        try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
            float totalAmount = 0;
            ArrayList<String> dbData = db.getCartData(username, "lab");
            String[][] packages = new String[dbData.size()][];
            for (int i = 0; i < packages.length; i++) {
                packages[i] = new String[5];
            }
            for (int i = 0; i < dbData.size(); i++) {
                String arrData = dbData.get(i);
                String[] strData = arrData.split(Pattern.quote("$"));
                packages[i][0] = strData[0];
                packages[i][4] = "Cost: " + strData[1] + "$";
                totalAmount = totalAmount + Float.parseFloat(strData[1]);
            }
            String str = "Total cost: " + totalAmount + "$";
            this.totalAmount = totalAmount;
            tvTotalCost.setText(str);

            list = new ArrayList<>();
            for (String[] aPackage : packages) {
                item = new HashMap<>();
                item.put("line1", aPackage[0]);
                item.put("line2", aPackage[1]);
                item.put("line3", aPackage[2]);
                item.put("line4", aPackage[3]);
                item.put("line5", aPackage[4]);
                list.add(item);
            }
            sa = new SimpleAdapter(this, list,
                    R.layout.multi_lines, new String[]{"line1", "line2", "line3", "line4", "line5"},
                    new int[] {R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            listView.setAdapter(sa);
        } catch (Exception e) {
            e.printStackTrace();
        }

        initDatePicker();
        dateButton.setOnClickListener(view -> datePickerDialog.show());

        initTimePicker();
        timeButton.setOnClickListener(view -> timePickerDialog.show());

        backButton.setOnClickListener(view -> startActivity(new Intent(CartLabTestActivity.this, LabTestActivity.class)));

        checkoutButton.setOnClickListener(view -> {
            if(totalAmount == 0) {
                Toast.makeText(getApplicationContext(), "Cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                Intent it = new Intent(CartLabTestActivity.this, LabTestBookActivity.class);
                it.putExtra("price", tvTotalCost.getText());
                it.putExtra("date", dateButton.getText());
                it.putExtra("time", timeButton.getText());
                startActivity(it);
            }
        });
    }

    private void initDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = (datePicker, i, i1, i2) -> {
            i1 = i1 + 1;
            String str;
            if (i1 < 10) {
                if(i2 < 10){
                    str = i + "/0" + i1 + "/0" + i2;
                } else {
                    str = i + "/0" + i1 + "/" + i2;
                }
            } else {
                if(i2 < 10){
                    str = i + "/" + i1 + "/0" + i2;
                } else {
                    str = i + "/" + i1 + "/" + i2;
                }
            }
            dateButton.setText(str);
        };

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        if(month == 13) {
            month = 1;
        }
        int minDay;
        if (day >= 28) {
            if ((day == 28 && month == 2 && year % 4 == 0) || (day == 29 && month == 2) || (day == 30 && (month == 4 || month == 6 || month == 9 || month == 11)) || day == 31) {
                minDay = 1;
            } else {
                minDay = day + 1;
            }
        } else {
            minDay = day + 1;
        }

        String str;
        if (minDay < 10) {
            if(month < 10){
                str = year + "/0" + month + "/0" + minDay;
            } else {
                str = year + "/" + month + "/0" + minDay;
            }
        } else {
            if(month < 10){
                str = year + "/0" + month + "/" + minDay;
            } else {
                str = year + "/" + month + "/" + minDay;
            }
        }
        dateButton.setText(str);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() + 86400000);
        datePickerDialog.getDatePicker().setMaxDate(calendar.getTimeInMillis() + 1_209_600_000);
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, i, i1) -> {
            String str;
            if (i1 < 10) {
                str = i + ":0" + i1;
            } else {
                str = i + ":" + i1;
            }
            timeButton.setText(str);
        };

        String str = "9:00";
        timeButton.setText(str);

        int style = AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new CustomTimePickerDialog(this, style, timeSetListener, 9, 0, true);
        timePickerDialog.setMinTime(9, 0);
        timePickerDialog.setMaxTime(18, 0);
    }
}