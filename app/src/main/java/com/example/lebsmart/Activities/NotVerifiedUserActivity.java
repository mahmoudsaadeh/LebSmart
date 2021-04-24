package com.example.lebsmart.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.lebsmart.R;
import com.google.firebase.auth.FirebaseAuth;

public class NotVerifiedUserActivity extends AppCompatActivity {

    Button logoutz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_verified_user);

        logoutz = findViewById(R.id.logoutNV);

        logoutz.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

    }


    public void logout() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            FirebaseAuth.getInstance().signOut();
            LoginActivity.sp.edit().putBoolean("loggedin", false).apply();
            Intent intent = new Intent(NotVerifiedUserActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //finishAffinity();

            startActivity(intent);
            //finishAffinity();

            finish();
            //finishAndRemoveTask();
        }
        else {
            Toast.makeText(NotVerifiedUserActivity.this, "You're already logged out!", Toast.LENGTH_SHORT).show();

            FirebaseAuth.getInstance().signOut();
            LoginActivity.sp.edit().putBoolean("loggedin", false).apply();
            Intent intent = new Intent(NotVerifiedUserActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //finishAffinity();

            startActivity(intent);
            //finishAffinity();
            finish();
        }
    }


}