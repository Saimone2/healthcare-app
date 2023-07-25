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

        String qry7 = "create table articles(name text, description text, image int)";
        sqLiteDatabase.execSQL(qry7);

        fillData(sqLiteDatabase);
    }

    private void fillData(SQLiteDatabase db) {
        fillDoctorsData(db);
        fillLabTestsData(db);
        fillMedicinesData(db);
        fillHealthArticlesData(db);
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
                These include a complete blood count (CBC) to assess red and white blood cell counts, hemoglobin levels, and platelets. It also measures blood sugar levels, cholesterol levels, liver and kidney function, and other important markers.
                                
                A urine sample is analyzed to check for signs of kidney function, infections, or other abnormalities.
                                
                It helps evaluate cardiovascular health and detect hypertension.
                                
                An ECG records the electrical activity of the heart and helps detect any irregularities in heart rhythm.
                                
                Some full body checkups may include X-rays, ultrasound, or other imaging studies to assess the condition of organs such as the heart, lungs, liver, and kidneys.
                                
                BMI is calculated based on height and weight measurements, providing an indication of the person's body fat and overall health.
                """, 999.0);
        insertLabTest(db,"Blood Glucose Fasting", """
                The Blood Glucose Fasting lab test is a common diagnostic tool used to measure the level of glucose (sugar) in a person's blood after an overnight fast. It is often done to screen for and diagnose diabetes or to monitor the effectiveness of diabetes treatment.
                                
                During the test, the individual is required to fast for a specific period, typically 8 to 12 hours. This means they should not eat or drink anything except water during this time. After the fasting period, a blood sample is taken, usually by drawing blood from a vein in the arm.
                                
                The blood sample is then analyzed in a laboratory to determine the concentration of glucose in the blood. The results are reported in milligrams per deciliter (mg/dL) or millimoles per liter (mmol/L) depending on the country's standard unit of measurement.
                                
                The fasting blood glucose level provides valuable information about the body's ability to regulate blood sugar levels. It helps identify potential diabetes or pre-diabetic conditions.
                """, 299.0);
        insertLabTest(db,"COVID-19 Antibody - IgG", """
                This a blood test used to detect the presence of immunoglobulin G (IgG) antibodies against the SARS-CoV-2 virus, which causes COVID-19.
                                
                The test involves taking a blood sample from the patient, usually through a simple blood draw. It is often used to determine if someone has been previously infected with the virus and has developed an immune response to it. The presence of IgG antibodies indicates that the individual has been exposed to the virus, even if they did not show symptoms or were asymptomatic.
                                
                It is important to note that the presence of IgG antibodies does not necessarily guarantee immunity, and the duration of the immunity provided by these antibodies is still an area of ongoing research. The test can be useful in understanding the prevalence of past infections within a population and for tracking the spread of the virus.
                """, 899.0);
        insertLabTest(db,"Thyroid Check", """
                Thyroid Stimulating Hormone is produced by the pituitary gland and stimulates the thyroid to produce its hormones. High levels of TSH may indicate an underactive thyroid (hypothyroidism), while low levels may suggest an overactive thyroid (hyperthyroidism).
                                
                Thyroid Hormones T4 (thyroxine) and T3 (triiodothyronine) are the main hormones produced by the thyroid gland. T4 is the primary hormone secreted by the thyroid, and it gets converted into the more active T3 in the body. Abnormal levels of these hormones can also indicate thyroid dysfunction.
                                
                The Thyroid Check lab test is commonly used to diagnose thyroid disorders, monitor thyroid hormone replacement therapy, and assess overall thyroid health.
                """, 499.0);
        insertLabTest(db,"Immunity Check", """
                Antibody Testing: This involves checking for specific antibodies in the blood to determine if a person has been exposed to certain infections or received vaccinations. For example, the COVID-19 Antibody - IgG test is a type of antibody test used to detect past exposure to the SARS-CoV-2 virus.
                                
                Complete Blood Count (CBC): This test provides information about different types of blood cells, including white blood cells, which play a crucial role in the immune response.
                                
                Immunoglobulin Levels: This test measures the levels of different types of immunoglobulins (IgG, IgA, IgM, IgE, IgD), which are antibodies produced by the immune system.
                                
                T-Cell Subsets: T-cells are a type of white blood cell that plays a central role in the immune response. Testing for T-cell subsets can help assess the function of the cellular immune response.
                """, 699.0);
    }

    public void insertLabTest(SQLiteDatabase db, String name, String description, double price) {
        String[] str = new String[2];
        str[0] = name;
        str[1] = String.valueOf(price);
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
        insertMedicine(db,"Painexil 30 tablets (Active ingredient: Ibuprofen)", """
                Painexil is a nonsteroidal anti-inflammatory drug (NSAID) commonly used to relieve pain, reduce inflammation, and lower fever. It is effective in managing mild to moderate pain caused by conditions such as headaches, menstrual cramps, arthritis, and muscle aches.
                """, 10.0);
        insertMedicine(db,"Calmazine 60 capsules (Active ingredients: Magnesium and Calcium)", """
                Calmazine is a supplement that combines magnesium and calcium to support healthy bones, muscles, and nerve function. It is often used to prevent or treat deficiencies of these essential minerals and may also aid in reducing muscle cramps and promoting relaxation.
                """, 15.0);
        insertMedicine(db,"Respitrin 20 tablets (Active ingredient: Salbutamol)", """
                Respitrin is a bronchodilator medication used to alleviate symptoms associated with respiratory conditions like asthma and chronic obstructive pulmonary disease (COPD). It works by relaxing the airway muscles and making it easier to breathe.
                """, 8.0);
        insertMedicine(db,"Gastrozole 30 capsules (Active ingredient: Omeprazole)", """
                Gastronome is a proton pump inhibitor (PPI) that reduces stomach acid production. It is prescribed to treat various gastrointestinal issues, including acid reflux, ulcers, and gastroesophageal reflux disease (GERD).
                """, 12.0);
        insertMedicine(db,"Sleepwellon 10 tablets (Active ingredient: Zolpidem)", """
                Sleepwellon is a sedative-hypnotic medication prescribed for short-term treatment of insomnia. It helps initiate and maintain sleep, allowing individuals to achieve a restful night's sleep.
                """, 20.0);
        insertMedicine(db,"Allerclear 30 tablets (Active ingredient: Loratadine)", """
                Allerclear is an antihistamine used to relieve symptoms associated with allergies, such as sneezing, runny nose, itchy eyes, and skin rashes. It provides relief from allergic reactions without causing drowsiness.
                """, 6.0);
        insertMedicine(db,"Cardiovas 60 tablets (Active ingredients: Amlodipine and Atenolol)", """
                Cardiovas is a combination medication used to manage hypertension (high blood pressure). Amlodipine is a calcium channel blocker that helps relax blood vessels, while Atenolol is a beta-blocker that slows down the heart rate, collectively reducing blood pressure.
                """, 18.0);
        insertMedicine(db,"Diabetrix 60 tablets (Active ingredient: Metformin)", """
                Diabetrix is an oral antidiabetic medication primarily used to treat type 2 diabetes. It improves insulin sensitivity, reduces glucose production in the liver, and helps lower blood sugar levels.
                """, 14.0);
    }

    public void insertMedicine(SQLiteDatabase db, String name, String description, double price) {
        String[] str = new String[2];
        str[0] = name;
        str[1] = String.valueOf(price);
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

    public int checkAppointmentExists(String username, String fullname, String address, String contact, String date, String time) {
        int result = 0;
        String[] str = new String[6];
        str[0] = username;
        str[1] = fullname;
        str[2] = address;
        str[3] = contact;
        str[4] = date;
        str[5] = time;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from orderplace where username=? and fullname=? and address=? and contactno=? and date=? and time=?", str);
        if(cursor.moveToFirst()) {
            result = 1;
        }
        cursor.close();
        db.close();
        return result;
    }

    private void fillHealthArticlesData(SQLiteDatabase db) {
        insertHealthArticles(db, "Walking Daily", "Find out the benefits of walking every day", R.drawable.health1);
        insertHealthArticles(db, "Home care of COVID-19", "How to securely protect yourself from COVID-19", R.drawable.health2);
        insertHealthArticles(db, "Stop Smoking", "Stages of body recovery after smoking", R.drawable.health3);
        insertHealthArticles(db, "Healthy Gut", "What to eat for a healthy gut", R.drawable.health4);
    }

    private void insertHealthArticles(SQLiteDatabase db, String name, String description, int image) {
        String[] str = new String[1];
        str[0] = name;
        Cursor cursor = db.rawQuery("select * from articles where name=?", str);
        if(cursor.moveToFirst()) {
            cursor.close();
            return;
        }
        cursor.close();

        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("description", description);
        cv.put("image", image);
        db.insert("articles", null, cv);
    }

    public String[][] getHealthArticles() {
        String[] str = new String[0];
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from articles", str);
        if (cursor.moveToFirst()) {
            String[][] data = new String[cursor.getCount()][3];
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