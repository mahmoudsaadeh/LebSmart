package com.example.lebsmart.ReportProblemsFragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class ReportProblemFragment extends Fragment {

    ViewGroup root;

    RadioGroup problemWithinRadioGroup, problemsWithinBuildingRadioGroup, problemsWithinSCRadioGroup;
    RadioButton radioButtonYourBuilding, radioButtonSmartCity;
    RadioButton radioButtonElec, radioButtonWater, radioButtonBS, radioButtonSanitation
            , radioButtonElecSC, radioButtonWaterSC, radioButtonSanitationSC, radioButtonOtherSC;
    EditText problemDescriptionET;
    Button reportProblemBtn;

    String problemTypeString, description, reportDate, problemWithinString;
    int problemType, problemWithin;

    public static final int NO_RADIO_BUTTON_SELECTED = -1;

    ProgressDialog progressDialog;

    DatabaseReference reference;

    ProblemAdd problemAdd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.report_problem, container, false);

        // reportedBy - get current user's name (the logged in user)
        //reportDate - get current date
        problemWithinRadioGroup = root.findViewById(R.id.problemWithinRadioGroup);

        problemsWithinBuildingRadioGroup = root.findViewById(R.id.problemsWithinBuildingRadioGroup);
        problemsWithinSCRadioGroup = root.findViewById(R.id.problemsWithinSCRadioGroup);

        radioButtonYourBuilding = root.findViewById(R.id.radioButtonYourBuilding);
        radioButtonSmartCity = root.findViewById(R.id.radioButtonSmartCity);

        radioButtonElec = root.findViewById(R.id.radioButtonElec);
        radioButtonWater = root.findViewById(R.id.radioButtonWater);
        radioButtonBS = root.findViewById(R.id.radioButtonBS);
        radioButtonSanitation = root.findViewById(R.id.radioButtonSanitation);

        radioButtonElecSC = root.findViewById(R.id.radioButtonElecSC);
        radioButtonWaterSC = root.findViewById(R.id.radioButtonWaterSC);
        radioButtonSanitationSC = root.findViewById(R.id.radioButtonSanitationSC);
        radioButtonOtherSC = root.findViewById(R.id.radioButtonOtherSC);

        problemDescriptionET = root.findViewById(R.id.problemDescriptionET);
        reportProblemBtn = root.findViewById(R.id.reportProblemBtn);

        problemWithinRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonYourBuilding) {
                    problemsWithinBuildingRadioGroup.setVisibility(View.VISIBLE);
                    problemsWithinSCRadioGroup.setVisibility(View.GONE);
                }
                else if (checkedId == R.id.radioButtonSmartCity) {
                    problemsWithinBuildingRadioGroup.setVisibility(View.GONE);
                    problemsWithinSCRadioGroup.setVisibility(View.VISIBLE);
                }
            }
        });

        reportProblemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportProblem();
            }
        });

        progressDialog = new ProgressDialog(getActivity());

        return root;
    }

    // to to db with categorization
    public void reportProblem () {

        problemWithin = problemWithinRadioGroup.getCheckedRadioButtonId();

        if (problemWithin == R.id.radioButtonYourBuilding) {
            problemType = problemsWithinBuildingRadioGroup.getCheckedRadioButtonId();
        }
        else if (problemWithin == R.id.radioButtonSmartCity) {
            problemType = problemsWithinSCRadioGroup.getCheckedRadioButtonId();
        }

        description = problemDescriptionET.getText().toString();
        reportDate = CommonMethods.getCurrentDate();

        if (problemWithin == NO_RADIO_BUTTON_SELECTED) {
            Toast.makeText(getActivity(), "Please fill the first entry!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (problemWithin == R.id.radioButtonYourBuilding) {
                problemWithinString = radioButtonYourBuilding.getText().toString();
            } else if (problemWithin == R.id.radioButtonSmartCity) {
                problemWithinString = radioButtonSmartCity.getText().toString();
            }
        }

        if (problemType == NO_RADIO_BUTTON_SELECTED) {
            Toast.makeText(getActivity(), "You should select a problem type before you proceed!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (problemWithin == R.id.radioButtonYourBuilding) {
                if (problemType == R.id.radioButtonElec) {
                    problemTypeString = radioButtonElec.getText().toString();
                }
                else if (problemType == R.id.radioButtonWater) {
                    problemTypeString = radioButtonWater.getText().toString();
                }
                else if (problemType == R.id.radioButtonBS) {
                    problemTypeString = radioButtonBS.getText().toString();
                }
                else if (problemType == R.id.radioButtonSanitation) {
                    problemTypeString = radioButtonSanitation.getText().toString();
                }
            }
            else if (problemWithin == R.id.radioButtonSmartCity) {
                if (problemType == R.id.radioButtonElecSC) {
                    problemTypeString = radioButtonElecSC.getText().toString();
                }
                else if (problemType == R.id.radioButtonWaterSC) {
                    problemTypeString = radioButtonWaterSC.getText().toString();
                }
                else if (problemType == R.id.radioButtonOtherSC) {
                    problemTypeString = "Other";
                }
                else if (problemType == R.id.radioButtonSanitationSC) {
                    problemTypeString = radioButtonSanitationSC.getText().toString();
                }
            }
        }

        if (description.isEmpty()) {
            CommonMethods.warning(problemDescriptionET, "Problem description is required before submission!");
            return;
        }

        if (reportDate.isEmpty()) {
            reportDate = "Could not get date!";
        }

        CommonMethods.displayLoadingScreen(progressDialog);
        problemAdd = new ProblemAdd(problemTypeString, description, reportDate);

        if (problemWithinString.equals("Your Building")) {
            reference = FirebaseDatabase.getInstance().getReference("ProblemsCategorized");
            reference = reference.child(problemWithinString)
                    .child(CheckApartmentsFragment.getUserBuilding).child(problemTypeString)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        }
        else {
            reference = FirebaseDatabase.getInstance().getReference("ProblemsCategorized");
            reference = reference.child(problemWithinString).child(problemTypeString)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        }

        reference.setValue(problemAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("problemReport", "success");
                    //Toast.makeText(getActivity(), "Problem reported successfully!", Toast.LENGTH_SHORT).show();
                    //CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    Log.i("problemReport", "failed");
                    //Toast.makeText(getActivity(), "Failed to report the problem! Please try again.", Toast.LENGTH_SHORT).show();
                    //CommonMethods.dismissLoadingScreen(progressDialog);
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
                addToDbAll();
            }
        }, 1555);

    }

    public void addToDbAll() {
        if (problemWithinString.equals("Your Building")) {
            reference = FirebaseDatabase.getInstance().getReference("ProblemsAll");
            reference = reference.child(problemWithinString)
                    .child(CheckApartmentsFragment.getUserBuilding)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        }
        else {
            reference = FirebaseDatabase.getInstance().getReference("ProblemsAll");
            reference = reference.child(problemWithinString)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        }

        reference.setValue(problemAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    //Log.i("problemReport", "success");
                    Toast.makeText(getActivity(), "Problem reported successfully!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    //Log.i("problemReport", "failed");
                    Toast.makeText(getActivity(), "Failed to report the problem! Please try again.", Toast.LENGTH_SHORT).show();
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
    }

}
