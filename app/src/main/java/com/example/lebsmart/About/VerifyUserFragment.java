package com.example.lebsmart.About;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class VerifyUserFragment extends Fragment {

    ViewGroup viewGroup;

    EditText userIdVU;
    Button verifyUserBtn;

    VerifiedUsers verifiedUsers;

    String userId;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.verify_user_fragment, container, false);

        userIdVU = viewGroup.findViewById(R.id.userIdVU);
        verifyUserBtn = viewGroup.findViewById(R.id.verifyUserBtn);

        progressDialog = new ProgressDialog(getActivity());

        verifyUserBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifyUser();
            }
        });

        return viewGroup;
    }



    public void verifyUser() {
        userId = "";
        userId = userIdVU.getText().toString();

        CommonMethods.displayLoadingScreen(progressDialog);

        if(userId.isEmpty()) {
            CommonMethods.warning(userIdVU, "User Id is required!");
            CommonMethods.dismissLoadingScreen(progressDialog);
            return;
        }

        verifiedUsers = new VerifiedUsers(userId, "verified");

        // check if user exists in firebase auth before verification!!

        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                .getReference("VerifiedUsers").child(userId);
        reference1.setValue(verifiedUsers).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("user", "verified");
                    Toast.makeText(getActivity(), "User verified successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.i("user", "not verified");
                    Toast.makeText(getActivity(), "Failed to verify user!", Toast.LENGTH_SHORT).show();
                }

                CommonMethods.dismissLoadingScreen(progressDialog);
            }

        });

    }


}
