package com.example.lebsmart.FireDetection;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FireDetectionFragment extends Fragment {

    ViewGroup root;
    ProgressDialog progressDialog;
    TextView fireWithinBuilding,fireWithinSmartCity;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fire_detection_fragment, container, false);
        progressDialog = new ProgressDialog(getActivity());
        //CommonMethods.displayLoadingScreen(progressDialog);
        fireWithinBuilding=root.findViewById(R.id.lastFDInUrBuildingDT);
        fireWithinSmartCity=root.findViewById(R.id.lastFDWithinSCDTP);
        updateFireInfo();
        //CommonMethods.dismissLoadingScreen(progressDialog);

        return root;
    }
    public void updateFireInfo(){
        CommonMethods.displayLoadingScreen(progressDialog);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Flame").child(CheckApartmentsFragment.getUserBuilding);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String lfdB=snapshot.child("last flame detected").getValue().toString();
                fireWithinBuilding.setText(lfdB.replace("-","/"));
                CommonMethods.dismissLoadingScreen(progressDialog);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Flame").child("last flame detected");
        reference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String building=snapshot.child("building").getValue().toString();
                String date=snapshot.child("date").getValue().toString().replace("-","/");
                fireWithinSmartCity.setText(building+" building, \non "+date);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}
