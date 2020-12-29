package com.example.lebsmart.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.lebsmart.R;

public class AddBuildingActivity extends AppCompatActivity {

    public void addBuilding(View view){
        //if all field are filled, add to db
        //else, present error msgs
        Toast.makeText(this, "Building added successfully!", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_building);
    }
}