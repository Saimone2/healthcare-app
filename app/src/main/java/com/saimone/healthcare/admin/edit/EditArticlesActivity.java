package com.saimone.healthcare.admin.edit;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.saimone.healthcare.R;
import com.saimone.healthcare.admin.HealthArticlesAdminActivity;
import com.saimone.healthcare.components.MyDialogFragment;
import com.saimone.healthcare.database.Database;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class EditArticlesActivity extends AppCompatActivity {
    ImageView imageView;
    EditText etName, etDescription;
    Button backButton, updateButton, uploadButton, deleteButton;
    String newImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_articles);

        imageView = findViewById(R.id.ivEAUpload);
        etName = findViewById(R.id.etEAName);
        etDescription = findViewById(R.id.etEADescription);
        updateButton = findViewById(R.id.btnEAUpdate);
        uploadButton = findViewById(R.id.btnEAUpload);
        deleteButton = findViewById(R.id.btnEADelete);
        backButton = findViewById(R.id.btnEABack);

        String name = getIntent().getStringExtra("name");
        String description = getIntent().getStringExtra("description");
        String imagePath = getIntent().getStringExtra("image_path");
        newImagePath = imagePath;

        etName.setText(name);
        etDescription.setText(description);
        loadImageFromFilePath(imagePath, imageView);

        backButton.setOnClickListener(view -> startActivity(new Intent(EditArticlesActivity.this, HealthArticlesAdminActivity.class)));

        updateButton.setOnClickListener(view -> {
            String newName = etName.getText().toString();
            String newDescription = etDescription.getText().toString();

            if (validateInput(newName, newDescription)) {
                if (newImagePath != null && !newImagePath.isEmpty()) {
                    try (Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                        assert name != null;
                        int res = db.updateArticle(name, description, imagePath, newName, newDescription, newImagePath);
                        if (res == 0) {
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
                } else {
                    Toast.makeText(getApplicationContext(), "Please select an image", Toast.LENGTH_SHORT).show();
                }
            }
        });

        uploadButton.setOnClickListener(view -> getImage());

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

    private void loadImageFromFilePath(String imagePath, ImageView imageView) {
        try {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            imageView.setImageBitmap(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                newImagePath = uploadImage(getApplicationContext());
            }
        }
    }
}