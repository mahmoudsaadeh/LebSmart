package com.example.lebsmart.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lebsmart.R;

public class BuildingInfoActivity extends AppCompatActivity {

    TextView buildingNameTV;

    public void chooseBuilding(View view){
        Intent intent = new Intent(BuildingInfoActivity.this, MainScreenActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_building_info);

        buildingNameTV = findViewById(R.id.bN);

        Intent intent = getIntent();
        buildingNameTV.setText(intent.getStringExtra("buildingName"));



    }
}