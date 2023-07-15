package com.saimone.healthcareapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    EditText etRegisterUsername, etRegisterEmail, etRegisterPassword, etRegisterConfirmPassword;
    Button btnRegister;
    TextView tvAlreadyHaveAnAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegisterUsername = findViewById(R.id.etRegisterUsername);
        etRegisterEmail = findViewById(R.id.etRegisterEmail);
        etRegisterPassword = findViewById(R.id.etRegisterPassword);
        etRegisterConfirmPassword = findViewById(R.id.etRegisterConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvAlreadyHaveAnAccount = findViewById(R.id.tvAlreadyHaveAnAccount);

        btnRegister.setOnClickListener(view -> {
            String username = etRegisterUsername.getText().toString();
            String email = etRegisterEmail.getText().toString();
            String password = etRegisterPassword.getText().toString();
            String confirmPassword = etRegisterConfirmPassword.getText().toString();

            if(username.length() == 0 || email.length() == 0 || password.length() == 0 || confirmPassword.length() == 0) {
                Toast.makeText(getApplicationContext(), "Please enter all data", Toast.LENGTH_SHORT).show();
            } else {
                if(password.equals(confirmPassword)) {
                    if(!isValidUsername(username)) {
                        Toast.makeText(getApplicationContext(), "The username must contain at least 4 characters of letters or numbers only", Toast.LENGTH_SHORT).show();
                    } else if (!isValidEmail(email)) {
                        Toast.makeText(getApplicationContext(), "Email entered incorrectly", Toast.LENGTH_SHORT).show();
                    } else if (!isValidPassword(password)) {
                            Toast.makeText(getApplicationContext(), "Password must contain more than 8 characters, have 1 letter, a symbol and a special character", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Register Success", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "The entered passwords do not match", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tvAlreadyHaveAnAccount.setOnClickListener(view -> startActivity(new Intent(RegisterActivity.this, LoginActivity.class)));
    }

    private boolean isValidUsername(String username) {
        if(username.length() < 4) {
            return false;
        } else {
            for (int i = 0; i < username.length(); i++) {
                char c = username.charAt(i);
                if (!Character.isLetter(c) && !Character.isDigit(c)) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean isValidEmail(String email) {
        String valid = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        return Pattern.matches(valid, email);
    }

    public static boolean isValidPassword(String password) {
        int oneLetter = 0, oneDigit = 0, oneSpecial = 0;

        if(password.length() < 8) {
            return false;
        } else {
            for (int i = 0; i < password.length(); i++) {
                char c = password.charAt(i);
                if(Character.isLetter(c)) {
                    oneLetter = 1;
                } else if (Character.isDigit(c)) {
                   oneDigit = 1;
                } else if (c >= 33 && c <= 46 || c == 64) {
                   oneSpecial = 1;
                }
            }
            return oneLetter == 1 && oneDigit == 1 && oneSpecial == 1;
        }
    }
}