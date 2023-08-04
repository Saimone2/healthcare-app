package com.saimone.healthcare.components;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.saimone.healthcare.admin.BuyMedicineAdminActivity;
import com.saimone.healthcare.admin.FindDoctorAdminActivity;
import com.saimone.healthcare.admin.FindDoctorSpecialityAdminActivity;
import com.saimone.healthcare.admin.HealthArticlesAdminActivity;
import com.saimone.healthcare.admin.LabTestAdminActivity;
import com.saimone.healthcare.database.Database;
import com.saimone.healthcare.user.HomeActivity;

public class MyDialogFragment extends DialogFragment {

    private String specialty;
    private String fullName;
    private String address;
    private String experience;
    private String phone;
    private String fee;
    private String state;
    private String name;
    private String description;
    private String price;
    private String productType;

    public static MyDialogFragment newInstance(String state, String specialty) {
        MyDialogFragment fragment = new MyDialogFragment();
        fragment.state = state;
        fragment.specialty = specialty;
        return fragment;
    }

    public static MyDialogFragment newInstance(String state, String fullName, String address, String experience, String phone, String fee, String specialty) {
        MyDialogFragment fragment = new MyDialogFragment();
        fragment.state = state;
        fragment.fullName = fullName;
        fragment.address = address;
        fragment.experience = experience;
        fragment.phone = phone;
        fragment.fee = fee;
        fragment.specialty = specialty;
        return fragment;
    }

    public static MyDialogFragment newInstance(String state, String name, String description, String priceStr, String productType) {
        MyDialogFragment fragment = new MyDialogFragment();
        fragment.state = state;
        fragment.name = name;
        fragment.description = description;
        fragment.price = priceStr;
        fragment.productType = productType;
        return fragment;
    }

    public static MyDialogFragment newInstance(String state, String name, String description) {
        MyDialogFragment fragment = new MyDialogFragment();
        fragment.state = state;
        fragment.name = name;
        fragment.description = description;
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Caution";
        String message = "Are you sure you want to delete this item?";
        String button1String = "NO";
        String button2String = "YES";

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(button1String, (dialog, id) -> {});

        builder.setNegativeButton(button2String, (dialog, id) -> {
            try (Database db = new Database(requireActivity().getApplicationContext(), "healthcare", null, 1)) {
                switch (state) {
                    case "DELETE DOCTOR" -> {
                        int res = db.deleteDoctor(fullName, address, experience, phone, fee, specialty);
                        if(res == 0) {
                            Toast.makeText(requireActivity().getApplicationContext(), "The doctor was not found in the database", Toast.LENGTH_SHORT).show();
                        } else if (res == 1) {
                            Toast.makeText(requireActivity().getApplicationContext(), "Doctor deleted", Toast.LENGTH_LONG).show();
                            Intent it = new Intent(getActivity(), FindDoctorAdminActivity.class);
                            it.putExtra("specialty", specialty);
                            startActivity(it);
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    case "DELETE SPECIALTY" -> {
                        int res = db.deleteSpecialty(specialty);
                        if(res == 0) {
                            Toast.makeText(requireActivity().getApplicationContext(), "The specialty is not found in the database", Toast.LENGTH_SHORT).show();
                        } else if (res == 1) {
                            Toast.makeText(requireActivity().getApplicationContext(), "Specialty deleted", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), FindDoctorSpecialityAdminActivity.class));
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                    case "DELETE PRODUCT" -> {
                        if (db.deleteProduct(name, description, price, productType) == 0) {
                            Toast.makeText(requireActivity().getApplicationContext(), "The product is not found in the database", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Product deleted", Toast.LENGTH_LONG).show();
                            navigateToAdminActivity();
                        }
                    }
                    case "DELETE ARTICLE" -> {
                        int res = db.deleteArticle(name, description);
                        if (res == 0) {
                            Toast.makeText(requireActivity().getApplicationContext(), "The article is not found in the database", Toast.LENGTH_SHORT).show();
                        } else if (res == 1) {
                            Toast.makeText(requireActivity().getApplicationContext(), "Article deleted", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), HealthArticlesAdminActivity.class));
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Database Error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            } catch (Exception e) {
                Toast.makeText(requireActivity().getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }

    private void navigateToAdminActivity() {
        Class<?> adminActivityClass = switch (productType) {
            case "lab" -> LabTestAdminActivity.class;
            case "medicine" -> BuyMedicineAdminActivity.class;
            default -> HomeActivity.class;
        };
        startActivity(new Intent(requireActivity().getApplicationContext(), adminActivityClass));
    }
}