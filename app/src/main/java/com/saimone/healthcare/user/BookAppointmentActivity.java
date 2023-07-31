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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.components.CustomTimePickerDialog;
import com.saimone.healthcare.database.Database;

import java.util.Calendar;
import java.util.regex.Pattern;

public class BookAppointmentActivity extends AppCompatActivity {
    EditText etFullName, etAddress, etContactno, etFee;
    TextView tvTitle;
    Button dateButton, timeButton, backButton, bookButton;
    private DatePickerDialog datePickerDialog;
    private CustomTimePickerDialog timePickerDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_appointment);

        tvTitle = findViewById(R.id.tvBATitle);
        etFullName = findViewById(R.id.etBAFullName);
        etAddress = findViewById(R.id.etBAAddress);
        etContactno = findViewById(R.id.etBAContactNumber);
        etFee = findViewById(R.id.etBAFee);
        dateButton = findViewById(R.id.btnBADate);
        timeButton = findViewById(R.id.btnBATime);
        backButton = findViewById(R.id.btnBABack);
        bookButton = findViewById(R.id.btnBABook);

        etFullName.setKeyListener(null);
        etAddress.setKeyListener(null);
        etContactno.setKeyListener(null);
        etFee.setKeyListener(null);

        Intent it = getIntent();
        String title = it.getStringExtra("text1");
        String fullname = it.getStringExtra("text2");
        String address = it.getStringExtra("text3");
        String contactno = it.getStringExtra("text4");
        String fee = it.getStringExtra("text5");

        tvTitle.setText(title);
        etFullName.setText(fullname);
        etAddress.setText(address);
        etContactno.setText(contactno);
        etFee.setText(fee);

        initDatePicker();
        dateButton.setOnClickListener(view -> datePickerDialog.show());

        initTimePicker();
        timeButton.setOnClickListener(view -> timePickerDialog.show());

        backButton.setOnClickListener(view -> startActivity(new Intent(BookAppointmentActivity.this, FindDoctorActivity.class)));

        bookButton.setOnClickListener(view -> {
            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
                String username = sharedPreferences.getString("username", "");
                String date = dateButton.getText().toString();
                String time = timeButton.getText().toString();
                String[] price = fee.split(Pattern.quote("$"));

                int result = db.addOrder(username, title + ": " + fullname, address, 0, contactno, date, time, Float.parseFloat(price[0]), "appointment");
                if(result == 1) {
                    Toast.makeText(getApplicationContext(), "Appointment is done successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(BookAppointmentActivity.this, HomeActivity.class));
                } else if (result == 0) {
                    Toast.makeText(getApplicationContext(), "Appointment already booked", Toast.LENGTH_SHORT).show();
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
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis() + 86_400_000);
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

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);

        String str;
        str = hours + ":00";
        timeButton.setText(str);

        int style = AlertDialog.THEME_HOLO_DARK;
        timePickerDialog = new CustomTimePickerDialog(this, style, timeSetListener, hours, minutes, true);
        timePickerDialog.setMinTime(9, 0);
        timePickerDialog.setMaxTime(18, 0);
    }
}