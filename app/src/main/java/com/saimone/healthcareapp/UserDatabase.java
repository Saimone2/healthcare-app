package com.saimone.healthcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class UserDatabase extends SQLiteOpenHelper {
    public UserDatabase(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
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
}