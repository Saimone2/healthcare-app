package com.saimone.healthcare.admin.add;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.HealthArticlesAdminActivity;
import com.saimone.healthcare.database.Database;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class NewArticlesActivity extends AppCompatActivity {
    ImageView imageView;
    EditText etName, etDescription;
    Button backButton, uploadButton, addNewButton;
    String imagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_articles);

        etName = findViewById(R.id.etNAName);
        etDescription = findViewById(R.id.etNADescription);
        imageView = findViewById(R.id.ivNAUpload);
        addNewButton = findViewById(R.id.btnNAAddNew);
        uploadButton = findViewById(R.id.btnNAUpload);
        backButton = findViewById(R.id.btnNABack);

        backButton.setOnClickListener(view -> startActivity(new Intent(NewArticlesActivity.this, HealthArticlesAdminActivity.class)));

        addNewButton.setOnClickListener(view -> {
            String name = etName.getText().toString();
            String description = etDescription.getText().toString();

            if(validateInput(name, description)) {
                if (imagePath != null && !imagePath.isEmpty()) {
                    try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                        int res = db.addNewArticle(name, description, imagePath);
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
                } else {
                    Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        uploadButton.setOnClickListener(view -> getImage());
    }

    private boolean validateInput(String newName, String newDescription) {
        if (newName.isEmpty() || newDescription.isEmpty()) {
            Toast.makeText(this, "Please enter all data", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void getImage() {
        Intent intentChooser = new Intent();
        intentChooser.setType("image/*");
        intentChooser.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentChooser, 1);
    }

    private String uploadImage(Context context) {
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] byteArray = baos.toByteArray();

        String filename = System.currentTimeMillis() + "image.jpeg";
        FileOutputStream outputStream;

        try {
            outputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
            outputStream.write(byteArray);
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        File file = new File(context.getFilesDir(), filename);
        return file.getAbsolutePath();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && data != null && data.getData() != null) {
            if(resultCode == RESULT_OK) {
                imageView.setImageURI(data.getData());
                imagePath = uploadImage(getApplicationContext());
            }
        }
    }
}