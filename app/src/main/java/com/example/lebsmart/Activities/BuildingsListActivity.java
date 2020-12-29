package com.example.lebsmart.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lebsmart.R;

import java.util.ArrayList;

public class BuildingsListActivity extends AppCompatActivity {

    ArrayList<String> buildingsArrayList = new ArrayList<>();

    ListView listView;
    TextView addBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buildings_list);

        for(int i = 0; i <= 11; i++){
            buildingsArrayList.add("Building " + i);
        }

        ArrayAdapter<String> adapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, buildingsArrayList);

        listView = findViewById(R.id.buildingsListView);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getItemAtPosition(position);

                Intent intent = new Intent(BuildingsListActivity.this, BuildingInfoActivity.class);
                intent.putExtra("buildingName", "" + selectedItem);
                startActivity(intent);
            }
        });

        addBuilding = findViewById(R.id.addBuildingTV);
        addBuilding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuildingsListActivity.this, AddBuildingActivity.class);
                startActivity(intent);
            }
        });

    }
}