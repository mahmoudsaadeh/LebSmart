package com.example.lebsmart.About;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RateUsFragment extends Fragment {

    ViewGroup root;

    Button submit;

    RadioGroup rateUsRadioGroup;
    RadioButton radioButton1, radioButton2, radioButton3, radioButton4, radioButton5;

    EditText reviewET;

    ProgressDialog progressDialog;

    public static final int NO_RADIO_BUTTON_SELECTED = -1;

    String review, ratingString;
    int rating;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.rate_us_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());

        rateUsRadioGroup = root.findViewById(R.id.rateUsRadioGroup);

        radioButton1 = root.findViewById(R.id.radioButton1);
        radioButton2 = root.findViewById(R.id.radioButton2);
        radioButton3 = root.findViewById(R.id.radioButton3);
        radioButton4 = root.findViewById(R.id.radioButton4);
        radioButton5 = root.findViewById(R.id.radioButton5);

        reviewET = root.findViewById(R.id.reviewET);


        submit = root.findViewById(R.id.submitReviewButton);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rateUs();
            }
        });

        return root;
    }

    public void rateUs () {
        rating = rateUsRadioGroup.getCheckedRadioButtonId();
        review = reviewET.getText().toString();

        if (rating == NO_RADIO_BUTTON_SELECTED) {
            Toast.makeText(getActivity(), "You should add a rating before submission!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (rating == R.id.radioButton1) {
                ratingString = radioButton1.getText().toString();
            }
            else if (rating == R.id.radioButton2) {
                ratingString = radioButton2.getText().toString();
            }
            else if (rating == R.id.radioButton3) {
                ratingString = radioButton3.getText().toString();
            }
            else if (rating == R.id.radioButton4) {
                ratingString = radioButton4.getText().toString();
            }
            else if (rating == R.id.radioButton5) {
                ratingString = radioButton5.getText().toString();
            }
        }

        if (review.isEmpty()) {
            review = "No review was added";
        }

        CommonMethods.displayLoadingScreen(progressDialog);
        RateUs rateUs = new RateUs(ratingString, review);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("RateUs");
        reference = reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.setValue(rateUs).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    showDialog();
                }
                else {
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    Toast.makeText(getActivity(), "There was a problem submitting your rating! Please try again.", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                CommonMethods.dismissLoadingScreen(progressDialog);
            }
        });

    }

    public void showDialog () {
        // we should first check if storing the data was successful in db
        // on suceess, we show the dialog

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.thank_you_dialog, null);

        builder.setView(view);

        //ImageView check = view.findViewById(R.id.checkImage);
        //Toast.makeText(getActivity(), "test", Toast.LENGTH_SHORT).show();

        final AlertDialog dialog = builder.create();

        ImageView cross = view.findViewById(R.id.crossImage);
        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //builder.setView(view);
        dialog.show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // close dialog if it wasn't closed manually after 5.5 secs
                dialog.dismiss();
            }
        }, 4777);

    }

}
