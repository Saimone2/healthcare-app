package com.saimone.healthcare.admin.add;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.HealthArticlesAdminActivity;
import com.saimone.healthcare.database.Database;

public class NewArticlesActivity extends AppCompatActivity {
    EditText etName, etDescription;
    Button backButton, addNewButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_articles);

        etName = findViewById(R.id.etNAName);
        etDescription = findViewById(R.id.etNADescription);
        addNewButton = findViewById(R.id.btnNAAddNew);
        backButton = findViewById(R.id.btnNABack);

        backButton.setOnClickListener(view -> startActivity(new Intent(NewArticlesActivity.this, HealthArticlesAdminActivity.class)));

        addNewButton.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String description = etDescription.getText().toString();

            if(validateInput(name, description)) {
                try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                    int res = db.addNewArticle(name, description);
                    if (res == 0) {
                        Toast.makeText(getApplicationContext(), "This article is already in the database", Toast.LENGTH_SHORT).show();
                    } else if (res == 1) {
                        Toast.makeText(getApplicationContext(), "New article added", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(NewArticlesActivity.this, HealthArticlesAdminActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validateInput(String newName, String newDescription) {
        if (newName.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(this, "Please enter all data", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}