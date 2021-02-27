package com.example.lebsmart.LostFoundAnnouncements;

import android.app.ProgressDialog;
import android.os.Bundle;
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
import com.example.lebsmart.CommitteeDR.CommitteeDR;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddLFAFragment extends Fragment {

    ViewGroup root;

    ProgressDialog progressDialog;

    Button addLFAButton;

    EditText lfaTitle, lfaDescription;

    String title, description, date, lfaWithinString;
    int lfaWithin;

    RadioGroup lfaWithinRadioGroup;
    RadioButton radioButtonYourBuildingLfa, radioButtonSmartCityLfa;

    public static final int NO_RADIO_BUTTON_SELECTED = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.add_lfa_fragment, container, false);

        lfaWithinRadioGroup = root.findViewById(R.id.lfaWithinRadioGroup);
        radioButtonYourBuildingLfa = root.findViewById(R.id.radioButtonYourBuildingLfa);
        radioButtonSmartCityLfa = root.findViewById(R.id.radioButtonSmartCityLfa);

        addLFAButton = root.findViewById(R.id.addLFAButton);
        lfaTitle = root.findViewById(R.id.lfaTitle);
        lfaDescription = root.findViewById(R.id.lfaDescription);

        progressDialog = new ProgressDialog(getActivity());

        addLFAButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLFA();
            }
        });

        return root;
    }


    public void addLFA () {

        lfaWithin = lfaWithinRadioGroup.getCheckedRadioButtonId();

        title = lfaTitle.getText().toString();
        description = lfaDescription.getText().toString();
        date = CommonMethods.getCurrentDate();

        if (lfaWithin == NO_RADIO_BUTTON_SELECTED) {
            Toast.makeText(getActivity(), "Please fill the first entry!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            if (lfaWithin == R.id.radioButtonYourBuildingLfa) {
                lfaWithinString = radioButtonYourBuildingLfa.getText().toString();
            } else if (lfaWithin == R.id.radioButtonSmartCityLfa) {
                lfaWithinString = radioButtonSmartCityLfa.getText().toString();
            }
        }

        if (title.isEmpty()) {
            CommonMethods.warning(lfaTitle, "Title is required!");
            return;
        }

        if (description.isEmpty()) {
            CommonMethods.warning(lfaDescription, "Description is required!");
            return;
        }

        CommonMethods.displayLoadingScreen(progressDialog);
        LFAAdd lfaAdd = new LFAAdd(title, date, description);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("LFAsCategorized");

        if (lfaWithinString.equals("Your Building")) {
            reference = reference.child(lfaWithinString).child(CheckApartmentsFragment.getUserBuilding)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        }
        else {
            reference = reference.child(lfaWithinString)
                    .child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
        }

        reference.setValue(lfaAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    }


}
