package com.example.lebsmart.RandomFragments;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;
import android.util.Patterns;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.lebsmart.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;


public class CommonMethods {

    public static final int PASSWORD_LENGTH = 6;


    public static void warning(TextView textview, String warning) {
        textview.setError(warning);
        textview.requestFocus();
    }


    public static boolean checkIfEmpty(String string){
        return string.isEmpty();
    }

    public static boolean isNotAnEmail(String email){
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean checkIfPassLengthNotValid(String password){
        return password.length() < PASSWORD_LENGTH;
    }


    public static String getEmail(EditText username) {
        return username.getText().toString().trim();
    }

    public static String getPassword(EditText password) {
        return password.getText().toString().trim();
    }


    public static boolean checkIfConfirmPassMatchesPass(String confPass, String pass){
        return confPass.equals(pass);
    }


    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                Objects.requireNonNull(activity.getCurrentFocus()).getWindowToken(), 0);
    }

    public static void displayLoadingScreen(ProgressDialog progressDialog) {
        //Log.i("loadingScreenShow1", "entered");
        if (!progressDialog.isShowing()) {
            //Log.i("loadingScreenShow2", "entered");
            try {
                progressDialog.show();
                progressDialog.setCanceledOnTouchOutside(false);

                progressDialog.setContentView(R.layout.loading_screen);
                progressDialog.getWindow().setBackgroundDrawableResource(
                        android.R.color.transparent
                );
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void dismissLoadingScreen (ProgressDialog progressDialog) {
        try {
            if ((progressDialog != null) && progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
        } catch (final Exception e) {
            // Handle or log or ignore
            e.printStackTrace();
        }
    }

    public static String getCurrentDate () {
        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return formatter.format(date);
    }

}
