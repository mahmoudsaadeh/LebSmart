package com.example.lebsmart.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.SignUpTabFragment;
import com.google.android.gms.tasks.Tasks;

public class BuildingInfoActivity extends AppCompatActivity {

    TextView buildingNameTV;

    String buildingName;

    public void chooseBuilding(View view){
        /*Intent intent = new Intent(BuildingInfoActivity.this, MainScreenActivity.class);
        startActivity(intent);*/
        //Intent intent = new Intent(BuildingInfoActivity.this, LoginActivity.class);
        //intent.putExtra("bN", buildingName);

        //SignUpTabFragment.buildingChosen = buildingName;
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

        buildingNameTV = findViewById(R.id.bN);

        Intent intent = getIntent();
        buildingName = intent.getStringExtra("buildingName");
        buildingNameTV.setText(buildingName);

    }
}