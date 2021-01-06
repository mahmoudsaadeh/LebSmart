package com.example.lebsmart.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lebsmart.BestServiceProviders.BSP;
import com.example.lebsmart.BestServiceProviders.BSPsRVA;
import com.example.lebsmart.R;
import com.example.lebsmart.ReportProblemsFragments.Problem;
import com.example.lebsmart.ReportProblemsFragments.ReportProblemRVA;

import java.util.ArrayList;
import java.util.List;

public class BSPsActivity extends AppCompatActivity {

    RecyclerView recyclerViewSP;
    BSPsRVA bsPsRVA;

    TextView spTV;

    List<BSP> bsps;

    String spCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_s_ps);

        spTV = findViewById(R.id.spTV);

        spCategory = "";
        Intent intent = getIntent();
        spCategory = intent.getStringExtra("spCategory");

        spTV.setText(spCategory);

        bsps = new ArrayList<>();
        // get info from db and fill them here
        // should check the type of problems clicked, and get the ones with the associated type only
        bsps.add(new BSP("abo ahmad", "78-885247", "" + spCategory, "4.1", "Aramoun"));
        bsps.add(new BSP("najii", "78-885247", "" + spCategory, "4.5", "Beirut"));
        bsps.add(new BSP("najii2", "78-885247", "" + spCategory, "4.5", "Beirut"));
        bsps.add(new BSP("najii5", "78-885247", "" + spCategory, "4.5", "Beirut"));

        recyclerViewSP = findViewById(R.id.spRecyclerView);
        bsPsRVA = new BSPsRVA(bsps);

        recyclerViewSP.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewSP.setAdapter(bsPsRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewSP.addItemDecoration(dividerItemDecoration);

    }
}