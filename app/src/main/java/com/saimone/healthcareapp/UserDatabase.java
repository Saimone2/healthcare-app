package com.saimone.healthcareapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

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
        cv.put("orderType", orderType);

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
}