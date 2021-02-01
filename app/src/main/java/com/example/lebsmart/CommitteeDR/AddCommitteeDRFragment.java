package com.example.lebsmart.CommitteeDR;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
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

import java.util.Objects;

public class AddCommitteeDRFragment extends Fragment {

    ViewGroup root;

    RadioGroup committeeRadioGroup;
    RadioButton radioButtonDecision, radioButtonReminder;

    Button addAnnouncementButton;

    LinearLayout addCommitteeDrLL;

    EditText announcementTitleET, announcementDescriptionET;

    ProgressDialog progressDialog;

    String title, description, typeString, date;
    int type;

    public static final int NO_RADIO_BUTTON_SELECTED = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.add_committee_dr, container, false);

        radioButtonDecision = root.findViewById(R.id.radioButtonDecision);
        radioButtonReminder = root.findViewById(R.id.radioButtonReminder);
        committeeRadioGroup = root.findViewById(R.id.committeeRadioGroup);

        addAnnouncementButton = root.findViewById(R.id.addAnnouncementButton);
        announcementTitleET = root.findViewById(R.id.announcementTitleET);
        announcementDescriptionET = root.findViewById(R.id.announcementDescriptionET);

        addCommitteeDrLL = root.findViewById(R.id.addCommitteeDrLL);

        progressDialog = new ProgressDialog(getActivity());

        /*
        radioButtonDecision.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        addCommitteeDrLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });
        */

        addAnnouncementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CommonMethods.hideSoftKeyboard(getActivity());
                addAnnouncement();
            }
        });



        return root;
    }


    public void addAnnouncement () {
        title = announcementTitleET.getText().toString();
        description = announcementDescriptionET.getText().toString();
        type = committeeRadioGroup.getCheckedRadioButtonId();
        date = CommonMethods.getCurrentDate();

        if (title.isEmpty()) {
            CommonMethods.warning(announcementTitleET, "Announcement title is required!");
            return;
        }

        if (description.isEmpty()) {
            CommonMethods.warning(announcementDescriptionET, "Announcement description is required!");
            return;
        }

        if (type == NO_RADIO_BUTTON_SELECTED) {
            Toast.makeText(getActivity(), "Announcement type is required!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (type == R.id.radioButtonReminder) {
                typeString = radioButtonReminder.getText().toString();
            }
            else if (type == R.id.radioButtonDecision) {
                typeString = radioButtonDecision.getText().toString();
            }
        }

        if (date.isEmpty()) {
            date = "Could not get date!";
        }

        CommonMethods.displayLoadingScreen(progressDialog);
        CommitteeAdd committeeAdd = new CommitteeAdd(title, description, typeString, date);

        // every committee member is allowed to add only 1 reminder and 1 decision
        // there should be 3 committee members only per building
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("CommitteeDRs");
        reference = reference.child(typeString)
                .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        reference.setValue(committeeAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Announcement added successfully!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    Toast.makeText(getActivity(), "Failed to add announcement! Please try again.", Toast.LENGTH_SHORT).show();
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

    } // end method

}
