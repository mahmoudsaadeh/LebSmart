package com.example.lebsmart.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.firebase.auth.FirebaseAuth;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText resetPasswordEmailEt;
    private Button resetPasswordButton;
    private ProgressDialog progressDialog;

    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){
            e.printStackTrace();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        resetPasswordEmailEt = (EditText) findViewById(R.id.resetPassEmailET);
        resetPasswordButton = (Button) findViewById(R.id.resetPasswordButton);
        progressDialog = new ProgressDialog(ResetPasswordActivity.this);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    public void resetPassword (View view){
        String email = resetPasswordEmailEt.getText().toString().trim();

        if(email.isEmpty()) {
            resetPasswordEmailEt.setError("Email is required!");
            resetPasswordEmailEt.requestFocus();
            return;
        }

        if(isNotAnEmail(email)) {
            resetPasswordEmailEt.setError("Please provide a correct email address!");
            resetPasswordEmailEt.requestFocus();
            return;
        }
        resetPassword(email);
    }

    public void resetPassword(String email){
        CommonMethods.displayLoadingScreen(progressDialog);
        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    Toast.makeText(ResetPasswordActivity.this, "A password reset email was sent!", Toast.LENGTH_SHORT).show();

                    CommonMethods.dismissLoadingScreen(progressDialog);

                    Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                }
                else {
                    Toast.makeText(ResetPasswordActivity.this, "Something went wrong! Please try again.", Toast.LENGTH_LONG).show();

                    CommonMethods.dismissLoadingScreen(progressDialog);

                    Intent intent1 = new Intent(getApplicationContext(), LoginActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent1);
                }
            }
        });
    }

    public static boolean isNotAnEmail(String email){
        return !Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

} // end class
