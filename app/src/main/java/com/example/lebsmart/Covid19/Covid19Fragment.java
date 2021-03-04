package com.example.lebsmart.Covid19;

import android.app.ProgressDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Covid19Fragment extends Fragment {

    ViewGroup root;


    ProgressDialog progressDialog;
    TextView buildingStatus,userStatus;
    Button registerAsPatient,announceRecovery;
    String userStat;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.covid19_fragment, container, false);
        progressDialog = new ProgressDialog(getActivity());
        CommonMethods.displayLoadingScreen(progressDialog);
        buildingStatus=root.findViewById(R.id.Covid19BuildingStatus);
        userStatus=root.findViewById(R.id.Covid19UserStatus);
        registerAsPatient=root.findViewById(R.id.covid19Register);
        announceRecovery=root.findViewById(R.id.announceRecovery);
        getCurrentStatus();


        registerAsPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerPatient();
            }
        });
        announceRecovery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateStatus();
            }
        });
        CommonMethods.dismissLoadingScreen(progressDialog);
        return root;
    }

    public void getCurrentStatus() {
       getBuildingStatus();
       getUserStatus();

    }

   public void getBuildingStatus() {
       DatabaseReference reference = FirebaseDatabase.getInstance().getReference("BuildingInfo").child(CheckApartmentsFragment.getUserBuilding);
       reference.addValueEventListener(new ValueEventListener() {
           @RequiresApi(api = Build.VERSION_CODES.O)
           @Override
           public void onDataChange(@NonNull DataSnapshot snapshot) {
               String buildingCovid19Status=snapshot.child("endemicBuilding").getValue().toString();
               if(buildingCovid19Status.equalsIgnoreCase("no")){
                   buildingStatus.setText("No");
               }
               else {
                   buildingStatus.setText("Yes");
               }
           }

           @Override
           public void onCancelled(@NonNull DatabaseError error) {

           }
       });
    }
    public void getUserStatus(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Covid19Info").child(CheckApartmentsFragment.getUserBuilding).child("Infected Individuals");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid()).exists()) {
                    userStat="Infected";
                    userStatus.setText(userStat);
                }
                else{
                    userStat="Not Infected";
                    userStatus.setText(userStat);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void registerPatient(){
        CommonMethods.displayLoadingScreen(progressDialog);
        if(userStat.equalsIgnoreCase("infected")){
            Toast.makeText(getActivity(), "You are already registered as COVID19 patient!", Toast.LENGTH_SHORT).show();
            CommonMethods.dismissLoadingScreen(progressDialog);
        }
        else{
            addPatientToDb();


    }}
    public void updateBuildingStatus(String newStatus){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("BuildingInfo").child(CheckApartmentsFragment.getUserBuilding)
                .child("endemicBuilding");
        ref.setValue(newStatus);
    }
    public void updateStatus(){
        if (userStat.equalsIgnoreCase("not infected"))
        {
            Toast.makeText(getActivity(), "You are not registered as COVID19 patient!", Toast.LENGTH_SHORT).show();
        }
        else{
            removePatientFromDb();
            addToRecoveredPatientsDb();



        }
    }
    public void addPatientToDb() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Covid19Info").child(CheckApartmentsFragment.getUserBuilding);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String number;
                if (snapshot.child("Infected Individuals").getValue() == null) {
                    number="1";
                }
                else{
                    number=String.valueOf(Integer.parseInt(snapshot.child("numberOfInfectedIndividuals").getValue().toString())+1);
                }
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Covid19Info")
                        .child(CheckApartmentsFragment.getUserBuilding);
                ref.child("Infected Individuals").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        child("infectedSince").setValue(CommonMethods.getCurrentDate().split(" ",2)[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            CommonMethods.dismissLoadingScreen(progressDialog);
                            Toast.makeText(getActivity(), "You were successfully registered as COVID19 patient!", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            CommonMethods.dismissLoadingScreen(progressDialog);
                            Toast.makeText(getActivity(), "Registration failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ref.child("numberOfInfectedIndividuals").setValue(number);
                updateBuildingStatus("Yes");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void removePatientFromDb(){
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Covid19Info").child(CheckApartmentsFragment.getUserBuilding);

        ref.child("Infected Individuals").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(null);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                DatabaseReference ref1= FirebaseDatabase.getInstance().getReference("Covid19Info").child(CheckApartmentsFragment.getUserBuilding);
                String number=String.valueOf(Integer.parseInt(snapshot.child("numberOfInfectedIndividuals").getValue().toString())-1);
                ref1.child("numberOfInfectedIndividuals").setValue(number);

                if(number.equalsIgnoreCase("0")){
                    updateBuildingStatus("No");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        });

    }
    public void addToRecoveredPatientsDb(){
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Covid19Info").child(CheckApartmentsFragment.getUserBuilding);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                String number;
                if (snapshot.child("Recovered Individuals").getValue() == null) {
                    number="1";
                }
                else{
                    number=String.valueOf(Integer.parseInt(snapshot.child("numberOfRecoveredIndividuals").getValue().toString())+1);
                }
                DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Covid19Info")
                        .child(CheckApartmentsFragment.getUserBuilding);
                ref.child("Recovered Individuals").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                        child("recoveredSince").setValue(CommonMethods.getCurrentDate().split(" ",2)[0]).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            CommonMethods.dismissLoadingScreen(progressDialog);
                            Toast.makeText(getActivity(), "Your COVID19 status was updated successfully!", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            CommonMethods.dismissLoadingScreen(progressDialog);
                            Toast.makeText(getActivity(), "Recovery announcement failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                ref.child("numberOfRecoveredIndividuals").setValue(number);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}
