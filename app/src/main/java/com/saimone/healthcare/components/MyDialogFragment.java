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
import com.saimone.healthcare.admin.LabTestAdminActivity;
import com.saimone.healthcare.database.Database;

public class MyDialogFragment extends DialogFragment {

    private String specialty;
    private String fullname;
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

    public static MyDialogFragment newInstance(String state, String fullname, String address, String experience, String phone, String fee, String specialty) {
        MyDialogFragment fragment = new MyDialogFragment();
        fragment.state = state;
        fragment.fullname = fullname;
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

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        String title = "Caution";
        String message = "Are you sure you want to delete the specialty and all its doctors?";
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
                        int result = db.deleteDoctor(fullname, address, experience, phone, fee, specialty);
                        if (result == 0) {
                            Toast.makeText(requireActivity().getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Doctor deleted", Toast.LENGTH_LONG).show();
                            Intent it = new Intent(getActivity(), FindDoctorAdminActivity.class);
                            it.putExtra("specialty", specialty);
                            startActivity(it);
                        }
                    }
                    case "DELETE SPECIALTY" -> {
                        int result = db.deleteSpecialty(specialty);
                        if (result == 0) {
                            Toast.makeText(requireActivity().getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Specialty deleted", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), FindDoctorSpecialityAdminActivity.class));
                        }
                    }
                    case "DELETE PRODUCT" -> {
                        int result = db.deleteProduct(name, description, price, productType);
                        if (result == 0) {
                            Toast.makeText(requireActivity().getApplicationContext(), "Something wrong", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireActivity().getApplicationContext(), "Product deleted", Toast.LENGTH_LONG).show();
                            navigateToAdminActivity();
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
        Class<?> adminActivityClass = productType.equals("lab")? LabTestAdminActivity.class : BuyMedicineAdminActivity.class;
        startActivity(new Intent(requireActivity().getApplicationContext(), adminActivityClass));
    }
}
