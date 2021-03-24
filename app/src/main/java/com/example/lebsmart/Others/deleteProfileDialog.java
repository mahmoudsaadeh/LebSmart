package com.example.lebsmart.Others;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.example.lebsmart.Activities.LoginActivity;
import com.example.lebsmart.Activities.MainScreenActivity;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.RandomFragments.LoginTabFragment;
import com.example.lebsmart.RandomFragments.MyProfileFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class deleteProfileDialog  extends DialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Delete Profile")
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setMessage("Are you sure you want to delete your profile?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.delete();
                        Toast.makeText(getActivity(), "Your account was successfully deleted!", Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        LoginActivity.sp.edit().putBoolean("loggedin", false).apply();
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        //finishAffinity();

                        startActivity(intent);
                    }
                });


        return builder.create();
    }
}