package com.example.lebsmart;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class ResetPasswordActivity extends AppCompatActivity {

    public void resetPassword (View view) {
        Toast.makeText(this, "Reset Password!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);



    }
}