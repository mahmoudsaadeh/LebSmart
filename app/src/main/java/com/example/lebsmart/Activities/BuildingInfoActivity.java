package com.example.lebsmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.ProgressDialog;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.RandomFragments.SignUpTabFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class BuildingInfoActivity extends AppCompatActivity {

    TextView buildingNameTV;
    TextView bL;
    TextView nOF, oA, eB;

    String buildingName, lat, lon;
    //String nofs, occupiedAp, totalAp, endemicB;

    LatLng latLng;

    ProgressDialog progressDialog;

    public void chooseBuilding(View view){

        SignUpTabFragment.selectABuildingTV.setText(buildingName);

        if (SignUpTabFragment.selectABuildingTV.getText().toString().equals("Select a Building")) {
            Toast.makeText(this, "Failed to select building! Please try again.", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "Building selected successfully!", Toast.LENGTH_SHORT).show();
            //finishActivity(1);
            this.finish();
        }

       /* Bundle bundle = new Bundle();
        bundle.putString("bN", buildingName );
        SignUpTabFragment fragInfo = new SignUpTabFragment();
        fragInfo.setArguments(bundle);*/
        //fragInfo.startActivity(intent, bundle);

        /*FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.commit();*/

        //startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_info);

        progressDialog = new ProgressDialog(BuildingInfoActivity.this);

        buildingNameTV = findViewById(R.id.bN);
        bL = findViewById(R.id.bL);

        nOF = findViewById(R.id.nOF);
        oA = findViewById(R.id.oA);
        eB = findViewById(R.id.eB);

        Intent intent = getIntent();
        buildingName = intent.getStringExtra("buildingName");
        buildingNameTV.setText(buildingName);

        getBuildingInfo(buildingName);

    }

    public void getBuildingInfo (String bName) {
        CommonMethods.displayLoadingScreen(progressDialog);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("BuildingInfo").child(bName);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()) {
                    return;
                }

                nOF.setText(snapshot.child("nofs").getValue().toString());
                oA.setText(snapshot.child("occupiedApartments").getValue().toString() + "/" +
                        snapshot.child("totalApartments").getValue().toString());
                eB.setText(snapshot.child("endemicBuilding").getValue().toString());

                lat = snapshot.child("lat").getValue().toString();
                lon = snapshot.child("lon").getValue().toString();

                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("error", error.getMessage());
                CommonMethods.dismissLoadingScreen(progressDialog);
            }
        });


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setNameLoc();
            }
        }, 2777);

    }


    public void setNameLoc () {
        latLng = new LatLng(Float.parseFloat(lat), Float.parseFloat(lon));
        //latLng = new LatLng(33.862018114256614, 35.52957594045413);

        bL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // https://developer.android.com/guide/components/intents-common#Maps
                // https://stackoverflow.com/questions/6205827/how-to-open-standard-google-map-application-from-my-application

                //String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latLng.latitude, latLng.longitude);
                String uri = String.format(Locale.ENGLISH, "geo:%f,%f?z=%d&q=%f,%f(%s)",
                        0.0, 0.0, 16, latLng.latitude,
                        latLng.longitude, "Building Location");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                BuildingInfoActivity.this.startActivity(intent);
            }
        });

        bL.setText(getLocationByName(latLng));

        CommonMethods.dismissLoadingScreen(progressDialog);

    }


    public String getLocationByName (LatLng selectedLocation){
        String addressLine = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(selectedLocation.latitude, selectedLocation.longitude, 1);
            addressLine = addresses.get(0).getAddressLine(0);
            //Log.i("address line: ", addresses.get(0).getAddressLine(0) + "");
            /*Log.i("Locality: ", addresses.get(0).getLocality() + "");
            Log.i("country name: ", addresses.get(0).getCountryName() + "");
            Log.i("Locale: ", addresses.get(0).getLocale() + "");*/

        } catch (IOException e) {
            e.printStackTrace();
        }

        return addressLine;
    }


}