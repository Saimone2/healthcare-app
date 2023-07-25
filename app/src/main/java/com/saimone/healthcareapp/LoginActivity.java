package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {
    EditText etUsername, etPassword;
    Button loginButton;
    TextView tvNewUser;
    private ImageView iconEyeVisible;
    private ImageView iconEyeHidden;
    private boolean isPasswordVisible = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etLoginUsername);
        etPassword = findViewById(R.id.etLoginPassword);
        loginButton = findViewById(R.id.btnLogin);
        tvNewUser = findViewById(R.id.tvRegisterNewUser);
        iconEyeVisible = findViewById(R.id.iconEyeVisible);
        iconEyeHidden = findViewById(R.id.iconEyeHidden);

        iconEyeVisible.setOnClickListener(v -> togglePasswordVisibility());
        iconEyeHidden.setOnClickListener(v -> togglePasswordVisibility());

        tvNewUser.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegisterActivity.class)));

        loginButton.setOnClickListener(view -> {
            String username = etUsername.getText().toString();
            String password = etPassword.getText().toString();

            try(Database db = new Database(getApplicationContext(), "healthcare", null, 1)) {
                if (username.length() == 0 || password.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter all data", Toast.LENGTH_SHORT).show();
                } else {
                    if (db.login(username, password) == 1) {
                        SharedPreferences sharedPreferences = getSharedPreferences("shared_preferences", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("username", username);
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        intent.putExtra("loggedInFlag", true);
                        startActivity(intent);
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void togglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible;

        if (isPasswordVisible) {
            etPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            iconEyeVisible.setVisibility(View.GONE);
            iconEyeHidden.setVisibility(View.VISIBLE);
        } else {
            etPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            iconEyeVisible.setVisibility(View.VISIBLE);
            iconEyeHidden.setVisibility(View.GONE);
        }

        etPassword.setSelection(etPassword.getText().length());
    }
}