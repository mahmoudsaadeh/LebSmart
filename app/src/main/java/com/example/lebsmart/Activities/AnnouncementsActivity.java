package com.example.lebsmart.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lebsmart.CommitteeDR.CommitteeDR;
import com.example.lebsmart.CommitteeDR.CommitteeRVA;
import com.example.lebsmart.R;

import java.util.ArrayList;
import java.util.List;

public class AnnouncementsActivity extends AppCompatActivity {

    RecyclerView recyclerViewCommittee;
    CommitteeRVA committeeRVA;

    TextView announcementTypeTV;

    List<CommitteeDR> committeeDRList;

    String announcementType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        announcementTypeTV = findViewById(R.id.announcementType);

        announcementType = "";
        Intent intent = getIntent();
        announcementType = intent.getStringExtra("announcementType");

        announcementTypeTV.setText(announcementType);

        committeeDRList = new ArrayList<>();
        committeeDRList.add(new CommitteeDR("Pay for Electricity", "Don't forget to pau in few days...!", "" + announcementType, "Friday, December 2nd, 2021"));
        committeeDRList.add(new CommitteeDR("Pay for Water", "Don't forget to pau in few days...!", "" + announcementType, "Friday, December 2nd, 2021"));
        committeeDRList.add(new CommitteeDR("Bring a private motor to building", "Don't forget to pau in few days...!", "" + announcementType, "Friday, December 2nd, 2021"));
        committeeDRList.add(new CommitteeDR("Elevator Maintenance", "Don't forget to pau in few days...!", "" + announcementType, "Friday, December 2nd, 2021"));
        committeeDRList.add(new CommitteeDR("Fix garage door", "Don't forget to pau in few days...!", "" + announcementType, "Friday, December 2nd, 2021"));

        recyclerViewCommittee = findViewById(R.id.committeeRecyclerView);
        committeeRVA = new CommitteeRVA(committeeDRList);

        recyclerViewCommittee.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewCommittee.setAdapter(committeeRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewCommittee.addItemDecoration(dividerItemDecoration);


    }
}