package com.example.lebsmart.ApartmentsFragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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

import com.example.lebsmart.Activities.MainScreenActivity;
import com.example.lebsmart.Database.FirebaseDatabaseMethods;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class AddApartmentFragment extends Fragment {

    ViewGroup viewGroup;

    RadioGroup apartmentRadioGrp;
    RadioButton rent;
    RadioButton sale;
    EditText priceET;
    EditText areaET;
    Button addApartment;

    String stateString, price, area;
    int state;

    public static final int NO_RADIO_BUTTON_SELECTED = -1;

    ProgressDialog progressDialog;

    String userBuilding;

    ApartmentAdd apartmentAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_add_apartment, container, false);

        apartmentRadioGrp = viewGroup.findViewById(R.id.apartmentRadioGrp);
        rent = viewGroup.findViewById(R.id.rent);
        sale = viewGroup.findViewById(R.id.sale);
        priceET = viewGroup.findViewById(R.id.priceET);
        areaET = viewGroup.findViewById(R.id.areaET);
        addApartment = viewGroup.findViewById(R.id.addApartment);

        addApartment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addApartmentToAll();
            }
        });

        progressDialog = new ProgressDialog(getActivity());

        return viewGroup;
    }

    public void addApartmentToAll() {

        price = priceET.getText().toString();
        area = areaET.getText().toString();
        state = apartmentRadioGrp.getCheckedRadioButtonId();

        if (state == NO_RADIO_BUTTON_SELECTED) {
            Toast.makeText(getActivity(), "You should select a State before you proceed!", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            if (state == R.id.sale) {
                stateString = sale.getText().toString();
            }
            else if (apartmentRadioGrp.getCheckedRadioButtonId() == R.id.rent) {
                stateString = rent.getText().toString();
            }
        }

        if (price.isEmpty()) {
            CommonMethods.warning(priceET, "Price is required before submission!");
            return;
        }
        if (area.isEmpty()) {
            CommonMethods.warning(areaET, "Apartment area is required!");
            return;
        }

        CommonMethods.displayLoadingScreen(progressDialog);
        apartmentAdd = new ApartmentAdd(stateString, price, area);


        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ApartmentsAll");
        reference = reference.child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.setValue(apartmentAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("apartmentAdd to all", "success");
                    //Toast.makeText(getActivity(), "Apartment added successfully!", Toast.LENGTH_SHORT).show();
                    //CommonMethods.dismissLoadingScreen(progressDialog);

                    getCurrentUserBuilding();
                }
                else {
                    Log.i("apartmentAdd to all", "failed");
                    Toast.makeText(getActivity(), "Failed to add the apartment! Please try again.", Toast.LENGTH_SHORT).show();
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

    } // end method addApartmentt

    public void getCurrentUserBuilding () {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userBuilding = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("buildingChosen").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                saveToCategorizedDb();
            }
        }, 1555);

    }

    public void saveToCategorizedDb () {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("ApartmentsCategorized");
        reference = reference.child(userBuilding).child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.setValue(apartmentAdd).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("apartmentAdd to cat", "success");
                    Toast.makeText(getActivity(), "Apartment added successfully!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    Log.i("apartmentAdd to cat", "failed");
                    Toast.makeText(getActivity(), "Failed to add the apartment! Please try again.", Toast.LENGTH_SHORT).show();
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