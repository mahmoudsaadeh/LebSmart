package com.example.lebsmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;

public class BuildingsListActivity extends AppCompatActivity {

    ArrayList<String> buildingsArrayList = new ArrayList<>();

    ListView listView;

    ProgressDialog progressDialog;

    TextView noBuildings;

    boolean buildingsAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildings_list);

        progressDialog = new ProgressDialog(BuildingsListActivity.this);

        /*for(int i = 0; i <= 11; i++){
            buildingsArrayList.add("Building " + i);
        }*/

        noBuildings = findViewById(R.id.noBuildings);

        listView = findViewById(R.id.buildingsListView);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(BuildingsListActivity.this, BuildingInfoActivity.class);
                intent.putExtra("buildingName", "" + selectedItem);
                startActivity(intent);
            }
        });

        getBuildings();

    }

    public void getBuildings () {

        CommonMethods.displayLoadingScreen(progressDialog);

        buildingsArrayList.clear();

        buildingsAvailable = true;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("BuildingInfo");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()) {
                    buildingsAvailable = false;
                    return;
                }

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    buildingsArrayList.add(dataSnapshot.getKey());
                }

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
                if (buildingsAvailable) {
                    ArrayAdapter<String> adapter =
                            new ArrayAdapter<String>(BuildingsListActivity.this, android.R.layout.simple_list_item_1, buildingsArrayList);
                    listView.setAdapter(adapter);

                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
                else {
                    // set listview visibility to gone
                    // and show noBuildings ET
                    listView.setVisibility(View.GONE);
                    noBuildings.setVisibility(View.VISIBLE);
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
            }
        }, 2777);

    } // end method

}