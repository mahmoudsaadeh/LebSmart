package com.example.lebsmart.Others;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AddBuildingFragment extends Fragment {

    ViewGroup viewGroup;

    EditText buildingNameAB, numberOfFloorsAB, latitudeET, longitudeET, totalApartments
            , occupiedApartmentsET, endemicBuildingET;

    Button addBuildingButton;

    String bName, nof, lat, lon, totalAp, occApp, endemicB;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.add_building_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());

        buildingNameAB = viewGroup.findViewById(R.id.buildingNameAB);
        numberOfFloorsAB = viewGroup.findViewById(R.id.numberOfFloorsAB);
        latitudeET = viewGroup.findViewById(R.id.latitudeET);
        longitudeET = viewGroup.findViewById(R.id.longitudeET);
        totalApartments = viewGroup.findViewById(R.id.totalApartments);
        occupiedApartmentsET = viewGroup.findViewById(R.id.occupiedApartmentsET);
        endemicBuildingET = viewGroup.findViewById(R.id.endemicBuildingET);

        addBuildingButton = viewGroup.findViewById(R.id.addBuildingButton);

        addBuildingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBuilding();
            }
        });

        return viewGroup;
    }

    AddBuilding addBuilding;

    public void addBuilding () {
        bName = buildingNameAB.getText().toString();
        nof = numberOfFloorsAB.getText().toString();
        lat = latitudeET.getText().toString();
        lon = longitudeET.getText().toString();
        totalAp = totalApartments.getText().toString();
        occApp = occupiedApartmentsET.getText().toString();
        endemicB = endemicBuildingET.getText().toString();

        if (bName.isEmpty()) {
            CommonMethods.warning(buildingNameAB, "Building name is required!");
            return;
        }

        if (nof.isEmpty()) {
            CommonMethods.warning(numberOfFloorsAB, "NOFs is required!");
            return;
        }

        if (lat.isEmpty()) {
            CommonMethods.warning(buildingNameAB, "Latitude is required!");
            return;
        }

        if (lon.isEmpty()) {
            CommonMethods.warning(buildingNameAB, "Longitude is required!");
            return;
        }

        if (totalAp.isEmpty()) {
            CommonMethods.warning(buildingNameAB, "Total apartments is required!");
            return;
        }

        if (occApp.isEmpty()) {
            CommonMethods.warning(buildingNameAB, "Occupied apartments is required!");
            return;
        }

        if (endemicB.isEmpty()) {
            CommonMethods.warning(endemicBuildingET, "Specify if the building is endemic before you proceed!");
            return;
        }

        CommonMethods.displayLoadingScreen(progressDialog);
        addBuilding = new AddBuilding(bName, nof, lat, lon, totalAp, occApp, endemicB);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BuildingInfo");
        reference = reference.child(bName);
        reference.setValue(addBuilding).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Log.i("add building part 1", "Building added successfully!");
                    Toast.makeText(getActivity(), "Building added successfully!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    Toast.makeText(getActivity(), "Failed to add building! Please try again.", Toast.LENGTH_SHORT).show();
                    Log.i("add building part 1", "Failed to add building! Please try again.");
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

        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                addBuildingToListOfBuildings();
            }
        }, 1777);*/

    }

    // apply this in sign up
    public void addBuildingToListOfBuildings () {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Buildings");
        reference = reference.child(bName).child("Resident ID");
        reference.setValue("empty").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(getActivity(), "Building added successfully!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    Toast.makeText(getActivity(), "Failed to add building! Please try again.", Toast.LENGTH_SHORT).show();
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
