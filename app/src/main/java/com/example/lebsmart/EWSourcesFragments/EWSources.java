package com.example.lebsmart.EWSourcesFragments;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class EWSources extends Fragment {

    ViewGroup root;
    ProgressDialog progressDialog;
    TextView  electricitySource,waterSource,waterLevel;
    String today;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.electricity_water_sources, container, false);

        electricitySource=root.findViewById(R.id.electricitySource);
        waterSource=root.findViewById(R.id.waterSource);
        waterLevel=root.findViewById(R.id.waterLevel);
       progressDialog = new ProgressDialog(getActivity());
       //CommonMethods.displayLoadingScreen(progressDialog);
        Date date = new Date(); // This object contains the current date value
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        today=formatter.format(date).split(" ",2)[0];
        getElectricitySource();
        getWaterSource();
        //CommonMethods.dismissLoadingScreen(progressDialog);
        return root;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
    public void getElectricitySource(){
        CommonMethods.displayLoadingScreen(progressDialog);
        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String phone=snapshot.child("phone").getValue().toString();
                DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Electricity").child(CheckApartmentsFragment.getUserBuilding)
                        .child(phone).child(today);
                reference2.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String source="";
                        String government=snapshot.child("government").getValue().toString();
                        String distributor=snapshot.child("distributor").getValue().toString();
                        if(government.equalsIgnoreCase("on")){
                            source="Government";
                        }
                        else if(distributor.equalsIgnoreCase("on")){
                            source="Distributor";
                        }
                        else source="Power Outage!";
                        electricitySource.setText(source);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    public void getWaterSource(){
        final DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference1.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                final String phone=snapshot.child("phone").getValue().toString();
                final DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference("Water").child(CheckApartmentsFragment.getUserBuilding)
                        .child(phone).child(today);
                reference2.child("government").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String governmentExistence,governmentLevel;
                        governmentExistence=snapshot.child("water existence").getValue().toString();
                        governmentLevel=snapshot.child("water level").getValue().toString();
                        if(governmentExistence.equalsIgnoreCase("yes")){
                            waterLevel.setText(governmentLevel.substring(0,4)+"%");
                            waterSource.setText("Government");}
                        else{
                            DatabaseReference reference3 = reference2.child("distributor");
                        reference3.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                String distributorExistence,distributorLevel;
                                distributorExistence=snapshot.child("water existence").getValue().toString();
                                distributorLevel=snapshot.child("water level").getValue().toString();
                                if(distributorExistence.equalsIgnoreCase("yes")){
                                    waterSource.setText("Distributor");
                                    waterLevel.setText(distributorLevel.substring(0,4)+"%");
                                }
                                else{
                                    waterSource.setText("Water Outage!");
                                    waterLevel.setText("0.00 %");

                                }
                                CommonMethods.dismissLoadingScreen(progressDialog);}




                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        }

                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }

                });
                /*
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });








    }

}
