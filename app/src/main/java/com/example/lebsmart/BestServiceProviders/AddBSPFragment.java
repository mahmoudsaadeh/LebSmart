package com.example.lebsmart.BestServiceProviders;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.ReportProblemsFragments.ProblemAdd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AddBSPFragment extends Fragment {

    ViewGroup root;

    EditText otherET;

    RadioGroup spRadioGroup;
    RadioButton radioButtonElectrician;
    RadioButton radioButtonCarpenter;
    RadioButton radioButtonPainter;
    RadioButton radioButtonPlumber;
    RadioButton radioButtonBlacksmith;
    RadioButton radioButtonOther;

    LinearLayout addBspLL;
    RatingBar spRatingBar;
    Button addSPButton;

    EditText spNameET, spPhoneET, spPlaceOfResidenceET;

    ProgressDialog progressDialog;

    String spName, spPhone, spPOR, spJobString, spJobStringOther, other;
    int spJob;
    float spRating;

    public static final int NO_RADIO_BUTTON_SELECTED = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.add_bsp, container, false);

        otherET = root.findViewById(R.id.otherET);

        addBspLL = root.findViewById(R.id.addBspLL);
        spRatingBar = root.findViewById(R.id.spRatingBar);
        addSPButton = root.findViewById(R.id.addSPButton);

        spNameET = root.findViewById(R.id.spNameET);
        spPhoneET = root.findViewById(R.id.spPhoneET);
        spPlaceOfResidenceET = root.findViewById(R.id.spPlaceOfResidenceET);

        radioButtonElectrician = root.findViewById(R.id.radioButtonElectrician);
        radioButtonCarpenter = root.findViewById(R.id.radioButtonCarpenter);
        radioButtonPainter = root.findViewById(R.id.radioButtonPainter);
        radioButtonPlumber = root.findViewById(R.id.radioButtonPlumber);
        radioButtonBlacksmith = root.findViewById(R.id.radioButtonBlacksmith);
        radioButtonOther = root.findViewById(R.id.radioButtonOther);

        spRadioGroup = root.findViewById(R.id.spRadioGroup);

        progressDialog = new ProgressDialog(getActivity());

        //spRatingBar.setRating(0);

        addSPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBSP();
            }
        });

        /*addBspLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        spRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        addSPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });*/


        radioButtonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.VISIBLE);
                //CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonElectrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.GONE);
                //CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonCarpenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.GONE);
                //CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonPainter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.GONE);
                //CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonPlumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.GONE);
                //CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonBlacksmith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.GONE);
                //CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        return root;
    }

    public void addBSP () {

        spName = spNameET.getText().toString();
        spPhone = spPhoneET.getText().toString();
        spPOR = spPlaceOfResidenceET.getText().toString();
        spJob = spRadioGroup.getCheckedRadioButtonId();
        spRating = spRatingBar.getRating();

        other = otherET.getText().toString(); // get the type of the SPs job

        if (spName.isEmpty()) {
            CommonMethods.warning(spNameET, "SP name is required!");
            return;
        }

        if (spPhone.isEmpty()) {
            CommonMethods.warning(spPhoneET, "SP phone number is required!");
            return;
        }
        else if (spPhone.length() < 8) {
            CommonMethods.warning(spPhoneET, "Phone number should be of 8 numbers!");
            return;
        }
        /*else {
            char[] phoneArray = spPhone.toCharArray();
            String areaCode = phoneArray[0] + "" + phoneArray[1];
            //Log.i("areacode", areaCode);
            StringBuilder number = new StringBuilder();
            for (int i=2; i<phoneArray.length; i++) {
                number.append(phoneArray[i]);
            }
            //Log.i("num", number);
            spPhone = areaCode + "-" + number;
        }*/

        if (spPOR.isEmpty()) {
            CommonMethods.warning(spPlaceOfResidenceET, "SP place of residence is required!");
            return;
        }

        if (spJob == NO_RADIO_BUTTON_SELECTED) {
            Toast.makeText(getActivity(), "You should specify the SP's job before you proceed!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (spJob == R.id.radioButtonElectrician) {
                spJobString = radioButtonElectrician.getText().toString();
            }
            else if (spJob == R.id.radioButtonCarpenter) {
                spJobString = radioButtonCarpenter.getText().toString();
            }
            else if (spJob == R.id.radioButtonPainter) {
                spJobString = radioButtonPainter.getText().toString();
            }
            else if (spJob == R.id.radioButtonPlumber) {
                spJobString = radioButtonPlumber.getText().toString();
            }
            else if (spJob == R.id.radioButtonBlacksmith) {
                spJobString = radioButtonBlacksmith.getText().toString();
            }
            else if (spJob == R.id.radioButtonOther) {
                //spJobString = radioButtonOther.getText().toString();
                if (other.isEmpty()) {
                    CommonMethods.warning(otherET, "You should specify a job before you proceed!");
                    return;
                }
                else {
                    spJobString = "Other";
                    spJobStringOther = other;
                }
            }
        }

        CommonMethods.displayLoadingScreen(progressDialog);

        checkIfSPAlreadyExists();

    } // end method addBSP


    boolean alreadyExists;

    public void checkIfSPAlreadyExists () {
        alreadyExists = false;

        DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("SPs").child(spJobString);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    Log.i("spSnapshot", Objects.requireNonNull(snapshot.getValue()).toString());
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Log.i("spDataSnapshot1", Objects.requireNonNull(dataSnapshot.getValue()).toString());
                        Log.i("spDataSnapshot2", Objects.requireNonNull(dataSnapshot.child("spPhone").getValue()).toString());
                        if (Objects.requireNonNull(dataSnapshot.child("spPhone").getValue()).toString().equals(spPhone)) {
                            alreadyExists = true;
                            break;
                        }
                    }
                }
                else {
                    alreadyExists = false;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                if (alreadyExists) {
                    Toast.makeText(getActivity(), "SP already exists!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    addSPToDb();
                }

            }
        }, 4100);

    }

    public void addSPToDb () {
        BSPAdd bspAdd;

        if (spJob == R.id.radioButtonOther) {
            bspAdd = new BSPAdd(spName, spPhone, spJobStringOther, spPOR, FirebaseAuth.getInstance().getCurrentUser().getUid());
        }
        else {
            bspAdd = new BSPAdd(spName, spPhone, spJobString, spPOR, FirebaseAuth.getInstance().getCurrentUser().getUid());
        }

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("SPs");
        reference = reference.child(spJobString).child(spPhone);
        reference.setValue(bspAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "SP added successfully!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    Toast.makeText(getActivity(), "Failed to ", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                CommonMethods.dismissLoadingScreen(progressDialog);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("SPsRatings");
                reference1 = reference1.child(spJobString).child(spPhone).child("ratedBy")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                reference1.setValue(spRating).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("rating", "added to SPsRatings node");
                        }
                        else {
                            Log.i("rating", "was NOT added to SPsRatings node");
                        }
                    }
                });
            }
        }, 3777);

    }

}
