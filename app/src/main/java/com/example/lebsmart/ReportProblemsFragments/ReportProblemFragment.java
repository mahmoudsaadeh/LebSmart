package com.example.lebsmart.ReportProblemsFragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
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

    RadioGroup problemsRadioGroup;
    RadioButton radioButtonElec, radioButtonWater, radioButtonBS;
    EditText problemDescriptionET;
    Button reportProblemBtn;

    String problemTypeString, description, reportDate;
    int problemType;

    public static final int NO_RADIO_BUTTON_SELECTED = -1;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.report_problem, container, false);

        // reportedBy - get current user's name (the logged in user)
        //reportDate - get current date

        problemsRadioGroup = root.findViewById(R.id.problemsRadioGroup);
        radioButtonElec = root.findViewById(R.id.radioButtonElec);
        radioButtonWater = root.findViewById(R.id.radioButtonWater);
        radioButtonBS = root.findViewById(R.id.radioButtonBS);
        problemDescriptionET = root.findViewById(R.id.problemDescriptionET);
        reportProblemBtn = root.findViewById(R.id.reportProblemBtn);

        reportProblemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reportProblem();
            }
        });

        progressDialog = new ProgressDialog(getActivity());

        return root;
    }

    public void reportProblem () {

        problemType = problemsRadioGroup.getCheckedRadioButtonId();
        description = problemDescriptionET.getText().toString();
        reportDate = CommonMethods.getCurrentDate();

        if (problemType == NO_RADIO_BUTTON_SELECTED) {
            Toast.makeText(getActivity(), "You should select a problem type before you proceed!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (problemType == R.id.radioButtonElec) {
                problemTypeString = radioButtonElec.getText().toString();
            }
            else if (problemType == R.id.radioButtonWater) {
                problemTypeString = radioButtonWater.getText().toString();
            }
            else if (problemType == R.id.radioButtonBS) {
                problemTypeString = radioButtonBS.getText().toString();
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
        ProblemAdd problemAdd = new ProblemAdd(problemTypeString, description, reportDate);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Problems");
        reference = reference.child(problemTypeString).child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        reference.setValue(problemAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("problemReport", "success");
                    Toast.makeText(getActivity(), "Problem reported successfully!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    Toast.makeText(getActivity(), "Failed to report the problem! Please try again.", Toast.LENGTH_SHORT).show();
                    Log.i("problemReport", "failed");
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
