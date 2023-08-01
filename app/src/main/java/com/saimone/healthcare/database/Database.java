package com.saimone.healthcare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import org.mindrot.jbcrypt.BCrypt;

import androidx.annotation.Nullable;

import com.saimone.healthcare.R;

import java.util.ArrayList;


public class Database extends SQLiteOpenHelper {
    public Database(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String qry1 = "CREATE TABLE users(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, email TEXT, password TEXT)";
        sqLiteDatabase.execSQL(qry1);

        String qry2 = "create table cart(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, product TEXT, price DOUBLE, order_type TEXT)";
        sqLiteDatabase.execSQL(qry2);

        String qry3 = "create table orderplace(id INTEGER PRIMARY KEY AUTOINCREMENT, username TEXT, fullname TEXT, address TEXT, pincode int, phone TEXT, date TEXT, time TEXT, price DOUBLE, order_type TEXT)";
        sqLiteDatabase.execSQL(qry3);

        String qry4 = "create table specialty(id INTEGER PRIMARY KEY AUTOINCREMENT, specialty_name TEXT)";
        sqLiteDatabase.execSQL(qry4);

        String qry5 = "create table doctors(id INTEGER PRIMARY KEY AUTOINCREMENT, fullname TEXT, hospital_address TEXT, experience INTEGER, phone TEXT, fee DOUBLE, specialty_id INTEGER, foreign key (specialty_id) references specialty(id))";
        sqLiteDatabase.execSQL(qry5);

        String qry6 = "create table labtests(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, price DOUBLE)";
        sqLiteDatabase.execSQL(qry6);

        String qry7 = "create table medicines(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, price DOUBLE)";
        sqLiteDatabase.execSQL(qry7);

        String qry8 = "create table articles(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, description TEXT, image INTEGER)";
        sqLiteDatabase.execSQL(qry8);

        fillData(sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    private void fillData(SQLiteDatabase db) {
        fillDoctorsData(db);
        fillLabTestsData(db);
        fillMedicinesData(db);
        fillHealthArticlesData(db);
    }

    private void fillDoctorsData(SQLiteDatabase db) {
        insertSpecialty(db, "Family Physician");
        insertDoctor(db,"Family Physician", "John Smith", "303 Main St", 10, "555-1234", 150.0);
        insertDoctor(db,"Family Physician", "Sarah Johnson", "456 Elm St", 5, "555-5678", 120.0);
        insertDoctor(db,"Family Physician", "Emily Thompson", "789 Mine St", 8, "555-9012", 150.0);
        insertDoctor(db,"Family Physician", "Michael Davis", "321 Pine St", 12, "555-3456", 160.0);
        insertDoctor(db,"Family Physician", "Jessica Lee", "987 Maple St", 3, "555-7890", 110.0);

        insertSpecialty(db, "Dietician");
        insertDoctor(db,"Dietician", "John Doe", "123 Main St", 5, "555-1234", 100.0);
        insertDoctor(db,"Dietician", "Jane Smith", "456 Elm St", 7, "555-5678", 120.0);
        insertDoctor(db,"Dietician", "Michael Johnson", "789 Oak St", 3, "555-9012", 90.0);
        insertDoctor(db,"Dietician", "Emily Davis", "321 Pine St", 4, "555-3456", 110.0);
        insertDoctor(db,"Dietician", "David Wilson", "654 Maple St", 6, "555-7890", 95.0);

        insertSpecialty(db, "Dentist");
        insertDoctor(db,"Dentist", "Sarah Lee", "111 Baker St", 8, "555-1111", 200.0);
        insertDoctor(db,"Dentist", "Robert Thompson", "222 Jefferson St", 10, "555-2222", 220.0);
        insertDoctor(db,"Dentist", "Jennifer Anderson", "333 Lincoln St", 6, "555-3333", 180.0);
        insertDoctor(db,"Dentist", "Daniel Martinez", "444 Madison St", 9, "555-4444", 210.0);
        insertDoctor(db,"Dentist", "Elizabeth Clark", "555 Adams St", 7, "555-5555", 190.0);

        insertSpecialty(db, "Surgeon");
        insertDoctor(db,"Surgeon", "William Rodriguez", "777 State St", 12, "555-7777", 500.0);
        insertDoctor(db,"Surgeon", "Karen Lewis", "888 Chestnut St", 15, "555-8888", 550.0);
        insertDoctor(db,"Surgeon", "Richard Hernandez", "999 Walnut St", 11, "555-9999", 480.0);
        insertDoctor(db,"Surgeon", "Patricia Thompson", "101 Park St", 13, "555-1010", 520.0);
        insertDoctor(db,"Surgeon", "Thomas Scott", "121 Market St", 14, "555-1212", 490.0);

        insertSpecialty(db, "Cardiologist");
        insertDoctor(db,"Cardiologist", "Linda Martin", "131 Spring St", 20, "555-1313", 300.0);
        insertDoctor(db,"Cardiologist", "Christopher Garcia", "141 Summer St", 18, "555-1414", 320.0);
        insertDoctor(db,"Cardiologist", "Susan Robinson", "151 Autumn St", 22, "555-1515", 280.0);
        insertDoctor(db,"Cardiologist", "Joseph Hill", "161 Winter St", 19, "555-1616", 330.0);
        insertDoctor(db,"Cardiologist", "Jessica King", "171 Park St", 21, "555-1717", 310.0);
    }

    private void fillLabTestsData(SQLiteDatabase db) {
        insertLabTest(db, "Full Body Checkup", """
                These include a complete blood count (CBC) to assess red and white blood cell counts, hemoglobin levels, and platelets. It also measures blood sugar levels, cholesterol levels, liver and kidney function, and other important markers.
                                
                A urine sample is analyzed to check for signs of kidney function, infections, or other abnormalities.
                                
                It helps evaluate cardiovascular health and detect hypertension.
                                
                An ECG records the electrical activity of the heart and helps detect any irregularities in heart rhythm.
                                
                Some full body checkups may include X-rays, ultrasound, or other imaging studies to assess the condition of organs such as the heart, lungs, liver, and kidneys.
                                
                BMI is calculated based on height and weight measurements, providing an indication of the person's body fat and overall health.
                """, 250.0);
        insertLabTest(db,"Blood Glucose Fasting", """
                The Blood Glucose Fasting lab test is a common diagnostic tool used to measure the level of glucose (sugar) in a person's blood after an overnight fast. It is often done to screen for and diagnose diabetes or to monitor the effectiveness of diabetes treatment.
                                
                During the test, the individual is required to fast for a specific period, typically 8 to 12 hours. This means they should not eat or drink anything except water during this time. After the fasting period, a blood sample is taken, usually by drawing blood from a vein in the arm.
                                
                The blood sample is then analyzed in a laboratory to determine the concentration of glucose in the blood. The results are reported in milligrams per deciliter (mg/dL) or millimoles per liter (mmol/L) depending on the country's standard unit of measurement.
                                
                The fasting blood glucose level provides valuable information about the body's ability to regulate blood sugar levels. It helps identify potential diabetes or pre-diabetic conditions.
                """, 25.0);
        insertLabTest(db,"COVID-19 Antibody - IgG", """
                This a blood test used to detect the presence of immunoglobulin G (IgG) antibodies against the SARS-CoV-2 virus, which causes COVID-19.
                                
                The test involves taking a blood sample from the patient, usually through a simple blood draw. It is often used to determine if someone has been previously infected with the virus and has developed an immune response to it. The presence of IgG antibodies indicates that the individual has been exposed to the virus, even if they did not show symptoms or were asymptomatic.
                                
                It is important to note that the presence of IgG antibodies does not necessarily guarantee immunity, and the duration of the immunity provided by these antibodies is still an area of ongoing research. The test can be useful in understanding the prevalence of past infections within a population and for tracking the spread of the virus.
                """, 60.0);
        insertLabTest(db,"Thyroid Check", """
                Thyroid Stimulating Hormone is produced by the pituitary gland and stimulates the thyroid to produce its hormones. High levels of TSH may indicate an underactive thyroid (hypothyroidism), while low levels may suggest an overactive thyroid (hyperthyroidism).
                                
                Thyroid Hormones T4 (thyroxine) and T3 (triiodothyronine) are the main hormones produced by the thyroid gland. T4 is the primary hormone secreted by the thyroid, and it gets converted into the more active T3 in the body. Abnormal levels of these hormones can also indicate thyroid dysfunction.
                                
                The Thyroid Check lab test is commonly used to diagnose thyroid disorders, monitor thyroid hormone replacement therapy, and assess overall thyroid health.
                """, 85.0);
        insertLabTest(db,"Immunity Check", """
                Antibody Testing: This involves checking for specific antibodies in the blood to determine if a person has been exposed to certain infections or received vaccinations. For example, the COVID-19 Antibody - IgG test is a type of antibody test used to detect past exposure to the SARS-CoV-2 virus.
                                
                Complete Blood Count (CBC): This test provides information about different types of blood cells, including white blood cells, which play a crucial role in the immune response.
                                
                Immunoglobulin Levels: This test measures the levels of different types of immunoglobulins (IgG, IgA, IgM, IgE, IgD), which are antibodies produced by the immune system.
                                
                T-Cell Subsets: T-cells are a type of white blood cell that plays a central role in the immune response. Testing for T-cell subsets can help assess the function of the cellular immune response.
                """, 130.0);
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

    private void fillHealthArticlesData(SQLiteDatabase db) {
        insertHealthArticles(db, "Walking Daily", "Find out the benefits of walking every day", R.drawable.health1);
        insertHealthArticles(db, "Home care of COVID-19", "How to securely protect yourself from COVID-19", R.drawable.health2);
        insertHealthArticles(db, "Stop Smoking", "Stages of body recovery after smoking", R.drawable.health3);
        insertHealthArticles(db, "Healthy Gut", "What to eat for a healthy gut", R.drawable.health4);
    }

    public int register(String username, String email, String password) {
        String[] str = new String[1];
        str[0] = username;

        try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from users where username=?", str)) {
            if (!cursor.moveToFirst()) {
                String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

                ContentValues cv = new ContentValues();
                cv.put("username", username);
                cv.put("email", email);
                cv.put("password", hashedPassword);
                db.insert("users", null, cv);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int login(String username, String password) {
        String[] str = new String[1];
        str[0] = username;

        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery("select password from users where username=?", str)) {
            if (cursor.moveToFirst()) {
                String hashedPasswordFromDB = cursor.getString(cursor.getColumnIndexOrThrow("password"));
                return BCrypt.checkpw(password, hashedPasswordFromDB) ? 1 : 0;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void addToCart(String username, String product, float price, String orderType) {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("username", username);
        cv.put("product", product);
        cv.put("price", price);
        cv.put("order_type", orderType);

        db.insert("cart",null, cv);
        db.close();
    }

    public int checkCart(String username, String product) {
        String[] str = new String[2];
        str[0] = username;
        str[1] = product;

        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery("select * from cart where username=? and product=?", str)) {
            return cursor.moveToFirst() ? 1 : 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public void removeCart(String username, String orderType) {
        SQLiteDatabase db = getWritableDatabase();

        String[] str = new String[2];
        str[0] = username;
        str[1] = orderType;

        db.delete("cart", "username=? and order_type=?", str);
        db.close();
    }

    public ArrayList<String> getCartData(String username, String orderType) {
        ArrayList<String> arr = new ArrayList<>();

        String[] str = new String[2];
        str[0] = username;
        str[1] = orderType;

        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery("select username, product, price, order_type from cart where username=? and order_type=?", str)) {
            if (cursor.moveToFirst()) {
                do {
                    String product = cursor.getString(1);
                    String price = cursor.getString(2);
                    arr.add(product + "$" + price);
                } while (cursor.moveToNext());
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int addOrder(String username, String fullname, String address, int pincode, String phone, String date, String time, float price, String orderType) {
        String[] str = new String[8];
        str[0] = username;
        str[1] = fullname;
        str[2] = address;
        str[3] = phone;
        str[4] = date;
        str[5] = time;
        str[6] = String.valueOf(price);
        str[7] = orderType;

        try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from orderplace where username=? and fullname=? and address=? and phone=? and date=? and time=? and price=? and order_type=?", str)) {
            if (!cursor.moveToFirst()) {
                ContentValues cv = new ContentValues();
                cv.put("username", username);
                cv.put("fullname", fullname);
                cv.put("address", address);
                cv.put("pincode", pincode);
                cv.put("phone", phone);
                cv.put("date", date);
                cv.put("time", time);
                cv.put("price", price);
                cv.put("order_type", orderType);

                db.insert("orderplace", null, cv);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ArrayList<String> getOrderData(String username) {
        String[] str = new String[1];
        str[0] = username;

        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery("select * from orderplace where username=?", str)) {
            ArrayList<String> arr = new ArrayList<>();
            if (cursor.moveToFirst()) {
                do {
                    arr.add(cursor.getString(2) + "$" + cursor.getString(3) + "$" + cursor.getString(4) + "$" + cursor.getString(5) + "$" + cursor.getString(6) + "$" + cursor.getString(7) + "$" + cursor.getString(8) + "$" + cursor.getString(9));
                } while (cursor.moveToNext());
            }
            return arr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void insertSpecialty(SQLiteDatabase db, String specialty) {
        String[] str = new String[1];
        str[0] = specialty;

        try(Cursor cursor = db.rawQuery("select * from specialty where specialty_name=?", str)) {
            if(!cursor.moveToFirst()) {
                ContentValues cv = new ContentValues();
                cv.put("specialty_name", specialty);
                db.insert("specialty", null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[] getSpecialties() {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery("select specialty_name from specialty", new String[0])) {
            if (cursor.moveToFirst()) {
                String[] data = new String[cursor.getCount()];
                int i = 0;
                do {
                    data[i] = cursor.getString(cursor.getColumnIndexOrThrow("specialty_name"));
                    i++;
                } while (cursor.moveToNext());
                return data;
            } else {
                return new String[]{};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertDoctor(SQLiteDatabase db, String specialtyName, String fullname, String hospitalAddress, int experience, String phone, double fee) {
        String id = getSpecialtyIdByName(db, specialtyName);
        if(id != null && !id.isEmpty()) {
            String[] str = new String[6];
            str[0] = fullname;
            str[1] = hospitalAddress;
            str[2] = String.valueOf(experience);
            str[3] = phone;
            str[4] = String.valueOf(fee);
            str[5] = id;

            try (Cursor cursor = db.rawQuery("select * from doctors where fullname=? and hospital_address=? and experience=? and phone=? and fee=? and specialty_id=?", str)) {
                if (!cursor.moveToFirst()) {
                    ContentValues cv = new ContentValues();
                    cv.put("fullname", fullname);
                    cv.put("hospital_address", hospitalAddress);
                    cv.put("experience", experience);
                    cv.put("phone", phone);
                    cv.put("fee", fee);
                    cv.put("specialty_id", id);
                    db.insert("doctors", null, cv);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String[][] getDoctorsBySpecialty(String specialtyName) {
        SQLiteDatabase db = getReadableDatabase();

        String id = getSpecialtyIdByName(db, specialtyName);
        if(id == null || id.isEmpty()) {
            db.close();
            return new String[][]{};
        } else {
            String[] str = new String[1];
            str[0] = id;

            try(Cursor cursor = db.rawQuery("select * from doctors where specialty_id=?", str)) {
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
                    db.close();
                    return data;
                } else {
                    db.close();
                    return new String[][]{};
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
        }
        return null;
    }

    public void insertLabTest(SQLiteDatabase db, String name, String description, double price) {
        String[] str = new String[2];
        str[0] = name;
        str[1] = String.valueOf(price);

        try(Cursor cursor = db.rawQuery("select * from labtests where name=? and price=?", str)) {
            if(!cursor.moveToFirst()) {
                ContentValues cv = new ContentValues();
                cv.put("name", name);
                cv.put("description", description);
                cv.put("price", price);
                db.insert("labtests", null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[][] getLabTests() {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery("select name, description, price from labtests", new String[0])) {
            if (cursor.moveToFirst()) {
                String[][] data = new String[cursor.getCount()][5];
                int i = 0;
                do {
                    data[i][0] = cursor.getString(0);
                    data[i][1] = cursor.getString(1);
                    data[i][2] = cursor.getString(2);
                    i++;
                } while (cursor.moveToNext());
                return data;
            } else {
                return new String[][]{};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertMedicine(SQLiteDatabase db, String name, String description, double price) {
        String[] str = new String[2];
        str[0] = name;
        str[1] = String.valueOf(price);

        try (Cursor cursor = db.rawQuery("select * from medicines where name=? and price=?", str)) {
            if (!cursor.moveToFirst()) {
                ContentValues cv = new ContentValues();
                cv.put("name", name);
                cv.put("description", description);
                cv.put("price", price);
                db.insert("medicines", null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[][] getMedicines() {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery("select name, description, price from medicines", new String[0])) {
            if (cursor.moveToFirst()) {
                String[][] data = new String[cursor.getCount()][3];
                int i = 0;
                do {
                    data[i][0] = cursor.getString(0);
                    data[i][1] = cursor.getString(1);
                    data[i][2] = cursor.getString(2);
                    i++;
                } while (cursor.moveToNext());
                return data;
            } else {
                return new String[][]{};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void insertHealthArticles(SQLiteDatabase db, String name, String description, int image) {
        String[] str = new String[1];
        str[0] = name;

        try (Cursor cursor = db.rawQuery("select * from articles where name=?", str)) {
            if (!cursor.moveToFirst()) {
                ContentValues cv = new ContentValues();
                cv.put("name", name);
                cv.put("description", description);
                cv.put("image", image);
                db.insert("articles", null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String[][] getHealthArticles() {
        try (SQLiteDatabase db = getReadableDatabase(); Cursor cursor = db.rawQuery("select name, description, image from articles", new String[0])) {
            if (cursor.moveToFirst()) {
                String[][] data = new String[cursor.getCount()][3];
                int i = 0;
                do {
                    data[i][0] = cursor.getString(0);
                    data[i][1] = cursor.getString(1);
                    data[i][2] = cursor.getString(2);
                    i++;
                } while (cursor.moveToNext());
                return data;
            } else {
                return new String[][]{};
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int addNewProduct(String name, String description, String price, String product) {
        String[] str = new String[2];
        str[0] = name;
        str[1] = price;

        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("description", description);
        cv.put("price", price);

        switch (product) {
            case "lab": {
                try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from labtests where name=? and price=?", str)) {
                    if (cursor.moveToFirst()) {
                        return 0;
                    } else {
                        db.insert("labtests", null, cv);
                        return 1;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return -1;
            }
            case "medicine": {
                try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from medicines where name=? and price=?", str)) {
                    if (cursor.moveToFirst()) {
                        return 0;
                    }
                    db.insert("medicines", null, cv);
                    return 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return -1;
            }
            default:
                return -1;
        }
    }

    public int updateProduct(String oldName, String oldDescription, String oldPrice, String newName, String newDescription, String newPrice, String product) {
        String[] str = new String[3];
        str[0] = oldName;
        str[1] = oldDescription;
        str[2] = oldPrice;

        String[] str1 = new String[3];
        str1[0] = newName;
        str1[1] = newDescription;
        str1[2] = newPrice;

        ContentValues cv = new ContentValues();
        cv.put("name", newName);
        cv.put("description", newDescription);
        cv.put("price", newPrice);

        switch (product) {
            case "lab": {
                try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from labtests where name=? and description=? and price=?", str1)) {
                    if (!cursor.moveToFirst()) {
                        db.update("labtests", cv, "name=? and description=? and price=?", str);
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return -1;
            }
            case "medicine": {
                try(SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from medicines where name=? and description=? and price=?", str1)) {
                    if (!cursor.moveToFirst()) {
                        db.update("medicines", cv, "name=? and description=? and price=?", str);
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return -1;
            }
            default:
                return -1;
        }
    }

    public int deleteProduct(String name, String description, String price, String product) {
        String[] str = new String[3];
        str[0] = name;
        str[1] = description;
        str[2] = price;

        switch (product) {
            case "lab": {
                try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from labtests where name=? and description=? and price=?", str)) {
                    if (cursor.moveToFirst()) {
                        db.delete("labtests", "name=? and description=? and price=?", str);
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return -1;
            }
            case "medicine": {
                try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from medicines where name=? and description=? and price=?", str)) {
                    if (cursor.moveToFirst()) {
                        db.delete("medicines", "name=? and description=? and price=?", str);
                        return 1;
                    } else {
                        return 0;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return -1;
            }
            default:
                return -1;
        }
    }

    public int addNewDoctor(String fullname, String address, String experience, String phone, String fee, String specialtyName) {
        SQLiteDatabase db = getWritableDatabase();

        String id = getSpecialtyIdByName(db, specialtyName);
        if (id == null || id.isEmpty()) {
            db.close();
            return 0;
        } else {
            String[] str = new String[6];
            str[0] = fullname;
            str[1] = address;
            str[2] = experience;
            str[3] = phone;
            str[4] = fee;
            str[5] = id;

            try(Cursor cursor1 = db.rawQuery("select * from doctors where fullname=? and hospital_address=? and experience=? and phone=? and fee=? and specialty_id=?", str)) {
                if (cursor1.moveToFirst()) {
                    db.close();
                    return 0;
                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("fullname", fullname);
                    cv.put("hospital_address", address);
                    cv.put("experience", experience);
                    cv.put("phone", phone);
                    cv.put("fee", fee);
                    cv.put("specialty_id", id);

                    db.insert("doctors", null, cv);
                    db.close();
                    return 1;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
            return -1;
        }
    }

    public int updateDoctor(String oldFullname, String oldAddress, String oldExperience, String oldPhone, String oldFee, String newFullname, String newAddress, String newExperience, String newPhone, String newFee, String specialtyName) {
        SQLiteDatabase db = getWritableDatabase();

        String id = getSpecialtyIdByName(db, specialtyName);
        if (id == null || id.isEmpty()) {
            db.close();
            return 0;
        } else {
            String[] str = new String[6];
            str[0] = newFullname;
            str[1] = newAddress;
            str[2] = newExperience;
            str[3] = newPhone;
            str[4] = newFee;
            str[5] = id;

            try (Cursor cursor = db.rawQuery("select * from doctors where fullname=? and hospital_address=? and experience=? and phone=? and fee=? and specialty_id=?", str)) {
                if (!cursor.moveToFirst()) {
                    ContentValues cv = new ContentValues();
                    cv.put("fullname", newFullname);
                    cv.put("hospital_address", newAddress);
                    cv.put("experience", newExperience);
                    cv.put("phone", newPhone);
                    cv.put("fee", newFee);
                    cv.put("specialty_id", id);

                    String[] str1 = new String[6];
                    str1[0] = oldFullname;
                    str1[1] = oldAddress;
                    str1[2] = oldExperience;
                    str1[3] = oldPhone;
                    str1[4] = oldFee;
                    str1[5] = id;

                    db.update("doctors", cv, "fullname=? and hospital_address=? and experience=? and phone=? and fee=? and specialty_id=?", str1);
                    db.close();
                    return 1;
                } else {
                    db.close();
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
            return -1;
        }
    }

    public int deleteDoctor(String fullname, String address, String experience, String phone, String fee, String specialtyName) {
        SQLiteDatabase db = getWritableDatabase();

        String id = getSpecialtyIdByName(db, specialtyName);
        if(id == null || id.isEmpty()) {
            db.close();
            return 0;
        } else {
            String[] str = new String[6];
            str[0] = fullname;
            str[1] = address;
            str[2] = experience;
            str[3] = phone;
            str[4] = fee;
            str[5] = id;

            try(Cursor cursor = db.rawQuery("select * from doctors where fullname=? and hospital_address=? and experience=? and phone=? and fee=? and specialty_id=?", str)) {
                if (cursor.moveToFirst()) {
                    db.delete("doctors", "fullname=? and hospital_address=? and experience=? and phone=? and fee=? and specialty_id=?", str);
                    db.close();
                    return 1;
                } else {
                    db.close();
                    return 0;
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                db.close();
            }
            return -1;
        }
    }

    public int addNewSpecialty(String specialtyName) {
        String[] str = new String[1];
        str[0] = specialtyName;

        try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from specialty where specialty_name=?", str)) {
            if (cursor.moveToFirst()) {
                return 0;
            } else {
                ContentValues cv = new ContentValues();
                cv.put("specialty_name", specialtyName);
                db.insert("specialty", null, cv);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int deleteSpecialty(String specialtyName) {
        SQLiteDatabase db = getWritableDatabase();

        String id = getSpecialtyIdByName(db, specialtyName);
        if(id == null || id.isEmpty()) {
            db.close();
            return 0;
        } else {
            String[] str = new String[1];
            str[0] = id;

            String[] str1 = new String[1];
            str1[0] = specialtyName;

            db.delete("doctors", "specialty_id=?", str);
            db.delete("specialty", "specialty_name=?", str1);
            db.close();
            return 1;
        }
    }


    private String getSpecialtyIdByName(SQLiteDatabase db, String specialtyName) {
        String[] str = new String[1];
        str[0] = specialtyName;

        try (Cursor cursor = db.rawQuery("SELECT id FROM specialty WHERE specialty_name=?", str)) {
            if (cursor.moveToFirst()) {
                return cursor.getString(cursor.getColumnIndexOrThrow("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public int addNewArticle(String name, String description) {
        String[] str = new String[2];
        str[0] = name;
        str[1] = description;

        try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from articles where name=? and description=?", str)) {
            if (cursor.moveToFirst()) {
                return 0;
            } else {
                ContentValues cv = new ContentValues();
                cv.put("name", name);
                cv.put("description", description);
                db.insert("articles", null, cv);
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }


    public int updateArticle(String oldName, String oldDescription, String newName, String newDescription) {
        String[] str = new String[2];
        str[0] = newName;
        str[1] = newDescription;

        try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from articles where name=? and description=?", str)) {
            if (!cursor.moveToFirst()) {
                ContentValues cv = new ContentValues();
                cv.put("name", newName);
                cv.put("description", newDescription);

                String[] str1 = new String[2];
                str1[0] = oldName;
                str1[1] = oldDescription;

                db.update("articles", cv, "name=? and description=?", str1);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public int deleteArticle(String name, String description) {
        String[] str = new String[2];
        str[0] = name;
        str[1] = description;

        try (SQLiteDatabase db = getWritableDatabase(); Cursor cursor = db.rawQuery("select * from articles where name=? and description=?", str)) {
            if (cursor.moveToFirst()) {
                db.delete("articles", "name=? and description=?", str);
                return 1;
            } else {
                return 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}