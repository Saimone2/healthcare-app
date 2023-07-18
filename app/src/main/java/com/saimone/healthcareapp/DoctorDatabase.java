package com.saimone.healthcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DoctorDatabase extends SQLiteOpenHelper {

    public DoctorDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "create table doctors(specialty text, fullname text, hospital_address text, experience int, phone text, fee double)";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public int insertData(String specialty, String fullname, String hospitalAddress, int experience, String phone, Double fee) {
        String[] str = new String[6];
        str[0] = specialty;
        str[1] = fullname;
        str[2] = hospitalAddress;
        str[3] = String.valueOf(experience);
        str[4] = phone;
        str[5] = String.valueOf(fee);
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from doctors where specialty=? and fullname=? and hospital_address=? and experience=? and phone=? and fee=?", str);
        if(cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return 0;
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put("specialty", specialty);
        cv.put("fullname", fullname);
        cv.put("hospital_address", hospitalAddress);
        cv.put("experience", experience);
        cv.put("phone", phone);
        cv.put("fee", fee);

        db = getWritableDatabase();
        db.insert("doctors", null, cv);
        db.close();
        return 1;
    }

    public String[][] getDoctorsBySpecialty(String specialty) {
        String[] str = new String[1];
        str[0] = specialty;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from doctors where specialty=?", str);

        if (cursor.moveToFirst()) {
            String[][] data = new String[cursor.getCount()][5];
            int i = 0;
            do {
                data[i][0] = cursor.getString(1);
                data[i][1] = cursor.getString(2);
                data[i][2] = cursor.getString(3);
                data[i][3] = cursor.getString(4);
                data[i][4] = cursor.getString(5);
                i++;
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
            return data;
        } else {
            cursor.close();
            db.close();
            return new String[][]{};
        }
    }
}