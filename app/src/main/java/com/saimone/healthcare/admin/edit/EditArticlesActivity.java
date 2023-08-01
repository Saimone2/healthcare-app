package com.saimone.healthcare.admin.edit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.HealthArticlesAdminActivity;
import com.saimone.healthcare.components.MyDialogFragment;
import com.saimone.healthcare.database.Database;

public class EditArticlesActivity extends AppCompatActivity {
    EditText etName, etDescription;
    Button backButton, updateButton, deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_articles);

        etName = findViewById(R.id.etEAName);
        etDescription = findViewById(R.id.etEADescription);
        updateButton = findViewById(R.id.btnEAUpdate);
        deleteButton = findViewById(R.id.btnEADelete);
        backButton = findViewById(R.id.btnEABack);

        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");

        etName.setText(name);
        etDescription.setText(description);

        backButton.setOnClickListener(view -> startActivity(new Intent(EditArticlesActivity.this, HealthArticlesAdminActivity.class)));

        updateButton.setOnClickListener(view -> {
            String newName = etName.getText().toString();
            String newDescription = etDescription.getText().toString();

            if (validateInput(newName, newDescription)) {
                try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                    int res = db.updateArticle(name, description, newName, newDescription);
                    if(res == 0) {
                        Toast.makeText(getApplicationContext(), "This article is already in the database", Toast.LENGTH_SHORT).show();
                    } else if (res == 1) {
                        Toast.makeText(getApplicationContext(), "Article updated", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(EditArticlesActivity.this, HealthArticlesAdminActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        });

        deleteButton.setOnClickListener(view -> {
            FragmentManager manager = getSupportFragmentManager();
            MyDialogFragment myDialogFragment = MyDialogFragment.newInstance("DELETE ARTICLE", name, description);
            myDialogFragment.show(manager, "myDialog");
        });
    }

    private boolean validateInput(String newName, String newDescription){
        if (newName.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(this, "Please enter all data", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}