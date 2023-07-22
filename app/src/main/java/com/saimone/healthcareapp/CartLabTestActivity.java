package com.saimone.healthcareapp;

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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

public class CartLabTestActivity extends AppCompatActivity {
    HashMap<String, String> item;
    ArrayList<HashMap<String, String>> list;
    SimpleAdapter sa;
    TextView tvCLTotalCost;
    ListView lvCLDetails;
    Button btnCLDate, btnCLTime, btnCLCheckout, btnCLBack;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_lab_test);

        btnCLDate = findViewById(R.id.btnCLDate);
        btnCLTime = findViewById(R.id.btnCLTime);
        btnCLCheckout = findViewById(R.id.btnCLCheckout);
        btnCLBack = findViewById(R.id.btnCLBack);
        tvCLTotalCost = findViewById(R.id.tvCLTotalCost);
        lvCLDetails = findViewById(R.id.lvCLDetails);

        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
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
            String str = "Total cost: " + totalAmount;
            tvCLTotalCost.setText(str);

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
            sa = new SimpleAdapter(this, list, R.layout.multi_lines, new String[]{"line1","line2","line3","line4","line5"}, new int[]{R.id.line_a, R.id.line_b, R.id.line_c, R.id.line_d, R.id.line_e});
            lvCLDetails.setAdapter(sa);

        } catch (Exception e) {
            e.printStackTrace();
        }

        initDatePicker();
        btnCLDate.setOnClickListener(view -> datePickerDialog.show());

        initTimePicker();
        btnCLTime.setOnClickListener(view -> timePickerDialog.show());

        btnCLBack.setOnClickListener(view -> startActivity(new Intent(CartLabTestActivity.this, LabTestActivity.class)));

        btnCLCheckout.setOnClickListener(view -> {
            Intent it = new Intent(CartLabTestActivity.this, LabTestBookActivity.class);
            it.putExtra("price", tvCLTotalCost.getText());
            it.putExtra("date", btnCLDate.getText());
            it.putExtra("time", btnCLTime.getText());
            startActivity(it);
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
            btnCLDate.setText(str);
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
        btnCLDate.setText(str);

        int style = AlertDialog.THEME_HOLO_DARK;
        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() + 86400000);
    }

    private void initTimePicker() {
        TimePickerDialog.OnTimeSetListener timeSetListener = (timePicker, i, i1) -> {
            String str;
            if (i1 < 10) {
                str = i + ":0" + i1;
            } else {
                str = i + ":" + i1;
            }
            btnCLTime.setText(str);
        };

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        String str;
        str = hours + ":00";
        btnCLTime.setText(str);

        int style = AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, hours, minutes, true);
    }
}