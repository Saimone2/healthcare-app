package com.saimone.healthcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "create table users(username text, email text, password text)";
        sqLiteDatabase.execSQL(qry1);

        String qry2 = "create table cart(username text, product text, price float, order_type text)";
        sqLiteDatabase.execSQL(qry2);

        String qry3 = "create table orderplace(username text, fullname text, address text, pincode int, contactno text, date text, time text, price float, order_type text)";
        sqLiteDatabase.execSQL(qry3);

        String qry4 = "create table doctors(specialty text, fullname text, hospital_address text, experience int, phone text, fee double)";
        sqLiteDatabase.execSQL(qry4);

        String qry5 = "create table labtests(name text, description text, price double)";
        sqLiteDatabase.execSQL(qry5);

        String qry6 = "create table medicines(name text, description text, price double)";
        sqLiteDatabase.execSQL(qry6);

        fillDoctorsData(sqLiteDatabase);
        //fillLabTestsData(sqLiteDatabase);
        //fillMedicinesData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }

    public int register(String username, String email, String password) {
        String[] str = new String[1];
        str[0] = username;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=?", str);
        if(cursor.moveToFirst()) {
            cursor.close();
            db.close();
            return 0;
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("email", email);
        cv.put("password", password);

        db = getWritableDatabase();
        db.insert("users", null, cv);
        db.close();
        return 1;
    }

    public int login(String username, String password) {
        int result = 0;
        String[] str = new String[2];
        str[0] = username;
        str[1] = password;
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and password=?", str);
        if(cursor.moveToFirst()) {
            result = 1;
        }
        cursor.close();
        db.close();
        return result;
    }

    public void addToCart(String username, String product, float price, String orderType) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("product", product);
        cv.put("price", price);
        cv.put("order_type", orderType);

        SQLiteDatabase db = getWritableDatabase();
        db.insert("cart",null, cv);
        db.close();
    }

    public int checkCart(String username, String product) {
        int result = 0;
        String[] str = new String[2];
        str[0] = username;
        str[1] = product;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from cart where username=? and product=?", str);
        if(cursor.moveToFirst()) {
            result = 1;
        }
        cursor.close();
        db.close();
        return result;
    }

    public void removeCart(String username, String orderType) {
        String[] str = new String[2];
        str[0] = username;
        str[1] = orderType;
        SQLiteDatabase db = getWritableDatabase();
        db.delete("cart", "username=? and order_type=?", str);
        db.close();
    }

    public ArrayList<String> getCartData(String username, String orderType) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] str = new String[2];
        str[0] = username;
        str[1] = orderType;
        Cursor cursor = db.rawQuery("select * from cart where username=? and order_type=?", str);
        if(cursor.moveToFirst()) {
            do {
                String product = cursor.getString(1);
                String price = cursor.getString(2);
                arr.add(product + "$" + price);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return arr;
    }

    public void addOrder(String username, String fullname, String address, int pincode, String contactno, String date, String time, float price, String orderType) {
        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("fullname", fullname);
        cv.put("address", address);
        cv.put("pincode", pincode);
        cv.put("contactno", contactno);
        cv.put("date", date);
        cv.put("time", time);
        cv.put("price", price);
        cv.put("order_type", orderType);
        SQLiteDatabase db = getWritableDatabase();
        db.insert("orderplace", null, cv);
        db.close();
    }

    public ArrayList<String> getOrderData(String username) {
        ArrayList<String> arr = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String[] str = new String[1];
        str[0] = username;
        Cursor cursor = db.rawQuery("select * from orderplace where username=?", str);
        if(cursor.moveToFirst()) {
            do {
                arr.add(cursor.getString(1) + "$" + cursor.getString(2) + "$" + cursor.getString(3) + "$" + cursor.getString(4) + "$" + cursor.getString(5) + "$" + cursor.getString(6) + "$" + cursor.getString(7));
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return arr;
    }

    private void fillDoctorsData(SQLiteDatabase db) {
        insertDoctor(db,"Family Physician", "John Smith", "303 Main St", 10, "555-1234", 150.0);
        insertDoctor(db,"Family Physician", "Sarah Johnson", "456 Elm St", 5, "555-5678", 120.0);
        insertDoctor(db,"Family Physician", "Emily Thompson", "789 Mine St", 8, "555-9012", 150.0);
        insertDoctor(db,"Family Physician", "Michael Davis", "321 Pine St", 12, "555-3456", 160.0);
        insertDoctor(db,"Family Physician", "Jessica Lee", "987 Maple St", 3, "555-7890", 110.0);

        insertDoctor(db,"Dietician", "John Doe", "123 Main St", 5, "555-1234", 100.0);
        insertDoctor(db,"Dietician", "Jane Smith", "456 Elm St", 7, "555-5678", 120.0);
        insertDoctor(db,"Dietician", "Michael Johnson", "789 Oak St", 3, "555-9012", 90.0);
        insertDoctor(db,"Dietician", "Emily Davis", "321 Pine St", 4, "555-3456", 110.0);
        insertDoctor(db,"Dietician", "David Wilson", "654 Maple St", 6, "555-7890", 95.0);

        insertDoctor(db,"Dentist", "Sarah Lee", "111 Baker St", 8, "555-1111", 200.0);
        insertDoctor(db,"Dentist", "Robert Thompson", "222 Jefferson St", 10, "555-2222", 220.0);
        insertDoctor(db,"Dentist", "Jennifer Anderson", "333 Lincoln St", 6, "555-3333", 180.0);
        insertDoctor(db,"Dentist", "Daniel Martinez", "444 Madison St", 9, "555-4444", 210.0);
        insertDoctor(db,"Dentist", "Elizabeth Clark", "555 Adams St", 7, "555-5555", 190.0);

        insertDoctor(db,"Surgeon", "William Rodriguez", "777 State St", 12, "555-7777", 500.0);
        insertDoctor(db,"Surgeon", "Karen Lewis", "888 Chestnut St", 15, "555-8888", 550.0);
        insertDoctor(db,"Surgeon", "Richard Hernandez", "999 Walnut St", 11, "555-9999", 480.0);
        insertDoctor(db,"Surgeon", "Patricia Thompson", "101 Park St", 13, "555-1010", 520.0);
        insertDoctor(db,"Surgeon", "Thomas Scott", "121 Market St", 14, "555-1212", 490.0);

        insertDoctor(db,"Cardiologist", "Linda Martin", "131 Spring St", 20, "555-1313", 300.0);
        insertDoctor(db,"Cardiologist", "Christopher Garcia", "141 Summer St", 18, "555-1414", 320.0);
        insertDoctor(db,"Cardiologist", "Susan Robinson", "151 Autumn St", 22, "555-1515", 280.0);
        insertDoctor(db,"Cardiologist", "Joseph Hill", "161 Winter St", 19, "555-1616", 330.0);
        insertDoctor(db,"Cardiologist", "Jessica King", "171 Park St", 21, "555-1717", 310.0);
    }

    public void insertDoctor(SQLiteDatabase db, String specialty, String fullname, String hospitalAddress, int experience, String phone, double fee) {
        String[] str = new String[6];
        str[0] = specialty;
        str[1] = fullname;
        str[2] = hospitalAddress;
        str[3] = String.valueOf(experience);
        str[4] = phone;
        str[5] = String.valueOf(fee);

        Cursor cursor = db.rawQuery("select * from doctors where specialty=? and fullname=? and hospital_address=? and experience=? and phone=? and fee=?", str);
        if(cursor.moveToFirst()) {
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put("specialty", specialty);
        cv.put("fullname", fullname);
        cv.put("hospital_address", hospitalAddress);
        cv.put("experience", experience);
        cv.put("phone", phone);
        cv.put("fee", fee);
        db.insert("doctors", null, cv);
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

    private void fillLabTestsData(SQLiteDatabase db) {
        insertLabTest(db, "Full Body Checkup", """
                Blood Glucose Fasting
                Complete Hemogram
                HbA1c
                Iron Studies
                Kidney Function Test
                LDH Lactate Dehydrogenase, Serum
                Lipid Profile
                Liver Function Test
                """, 999.0);
        insertLabTest(db,"Blood Glucose Fasting", "Blood Glucose Fasting", 299.0);
        insertLabTest(db,"COVID-19 Antibody - IgG", "COVID-19 Antibody - IgG", 899.0);
        insertLabTest(db,"Thyroid Check", "Thyroid Profile Total (T3, T4, & TSH Ultra-sensitive)", 499.0);
        insertLabTest(db,"Immunity Check", """
                Complete Hemogram
                CRP (C Reactive Protein) Quantitative, Serum
                Iron Studies
                Kidney Function Test
                Liver Function Test
                Vitamin D Total 25 Hydroxy
                Lipid Profile
                """, 699.0);
    }

    public void insertLabTest(SQLiteDatabase db, String name, String description, double price) {
        String[] str = new String[3];
        str[0] = name;
        str[1] = description;
        str[2] = String.valueOf(price);
        Cursor cursor = db.rawQuery("select * from labtests where name=? and price=?", str);
        if(cursor.moveToFirst()) {
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("description", description);
        cv.put("price", price);
        db.insert("labtests", null, cv);
    }

    public String[][] getLabTests() {
        String[] str = new String[0];
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from labtests", str);
        if (cursor.moveToFirst()) {
            String[][] data = new String[cursor.getCount()][5];
            int i = 0;
            do {
                data[i][0] = cursor.getString(0);
                data[i][1] = cursor.getString(1);
                data[i][2] = cursor.getString(2);
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

    public void fillMedicinesData(SQLiteDatabase db) {
        insertMedicine(db,"HealthVit Chromium Picolinate 200mcg Capsule", """
                Chromium is an essential trace mineral that plays an important role in helping insulin regulation
                """, 300.0);
        insertMedicine(db,"Uprise-D3 1000IU Capsule", """
                Building and keeping the bones & teeth strong
                Reducing Fatigue/stress and muscular pains
                Boosting immunity and increasing resistance against infection
                """, 50.0);
        insertMedicine(db,"Vitamin B Complex Capsule", """
                Provides relief from vitamin B deficiencies
                Helps in formation of red blood cells
                Maintains healthy nervous system
                """, 450.0);
        insertMedicine(db,"Dolo 650 Tablet", """
                Dolo 650 Tablet helps relieve pain and fever by blocking the release of certain chemical message
                """, 30.0);
        insertMedicine(db,"Inlife Vitamin E Wheat Germ Oil Capsule", """
                It promotes health as well as akin benefit
                It helps reduce skin blemish and pigmentation
                It act as safeguard the skin from the harsh UVA and UVB sun rays
                """, 540.0);
        insertMedicine(db,"Feronia -XT Tablet", """
                Helps to reduce the iron deficiency due to chronic blood loss or low intake of iron
                """, 130.0);
        insertMedicine(db,"Tata 1mg Calcium + Vitamin D3", """
                Reduces the risk of calcium deficiency, Rickets, and Osteoporosis
                Promotes mobility and flexibility of joints
                """, 30.0);
    }

    public void insertMedicine(SQLiteDatabase db, String name, String description, double price) {
        String[] str = new String[3];
        str[0] = name;
        str[1] = description;
        str[2] = String.valueOf(price);
        Cursor cursor = db.rawQuery("select * from medicines where name=? and price=?", str);
        if(cursor.moveToFirst()) {
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("description", description);
        cv.put("price", price);
        db.insert("medicines", null, cv);
    }

    public String[][] getMedicines() {
        String[] str = new String[0];
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from medicines", str);
        if (cursor.moveToFirst()) {
            String[][] data = new String[cursor.getCount()][5];
            int i = 0;
            do {
                data[i][0] = cursor.getString(0);
                data[i][1] = cursor.getString(1);
                data[i][2] = cursor.getString(2);
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