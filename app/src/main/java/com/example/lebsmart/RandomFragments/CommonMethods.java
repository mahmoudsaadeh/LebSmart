package com.example.lebsmart.RandomFragments;


import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;


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


}
