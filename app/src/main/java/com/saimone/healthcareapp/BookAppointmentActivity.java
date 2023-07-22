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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class BookAppointmentActivity extends AppCompatActivity {
    EditText et1, et2, et3, et4;
    TextView tvTitle;

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    Button btnBookDate, btnBookTime, btnBookBack, btnToBook;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        tvTitle = findViewById(R.id.tvBookTitle);
        et1 = findViewById(R.id.etBookFullName);
        et2 = findViewById(R.id.etBookAddress);
        et3 = findViewById(R.id.etBookContactNumber);
        et4 = findViewById(R.id.etBookFee);

        et1.setKeyListener(null);
        et2.setKeyListener(null);
        et3.setKeyListener(null);
        et4.setKeyListener(null);

        Intent it = getIntent();
        String title = it.getStringExtra("text1");
        String fullname = it.getStringExtra("text2");
        String address = it.getStringExtra("text3");
        String contactNum = it.getStringExtra("text4");
        String fee = it.getStringExtra("text5");

        tvTitle.setText(title);
        et1.setText(fullname);
        et2.setText(address);
        et3.setText(contactNum);
        et4.setText(fee);

        btnBookDate = findViewById(R.id.btnBookDate);
        btnBookTime = findViewById(R.id.btnBookTime);

        initDatePicker();
        btnBookDate.setOnClickListener(view -> datePickerDialog.show());

        initTimePicker();
        btnBookTime.setOnClickListener(view -> timePickerDialog.show());

        btnBookBack = findViewById(R.id.btnBookBack);
        btnBookBack.setOnClickListener(view -> startActivity(new Intent(BookAppointmentActivity.this, FindDoctorActivity.class)));

        btnToBook = findViewById(R.id.btnToBook);
        btnToBook.setOnClickListener(view -> {
            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                SharedPreferences sharedPreferences = getSharedPreferences("shared_prefs", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                if(db.checkAppointmentExists(username, title + "=>" + fullname, address, contactNum, 0, btnBookDate.getText().toString(), btnBookTime.getText().toString()) == 1 ) {
                    Toast.makeText(getApplicationContext(), "Appointment already booked", Toast.LENGTH_SHORT).show();
                } else {
                    db.addOrder(username, title + "=>" + fullname, address, 0, contactNum, btnBookDate.getText().toString(), btnBookTime.getText().toString(), Float.parseFloat(fee), "appointment");
                    Toast.makeText(getApplicationContext(), "Appointment is done successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookAppointmentActivity.this, HomeActivity.class));
                }
            } catch (Exception e) {
                e.printStackTrace();
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
            btnBookDate.setText(str);
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
        btnBookDate.setText(str);

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
            btnBookTime.setText(str);
        };

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        String str;
        str = hours + ":00";
        btnBookTime.setText(str);

        int style = AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new TimePickerDialog(this, style, timeSetListener, hours, minutes, true);
    }
}