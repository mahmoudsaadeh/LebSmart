package com.example.lebsmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lebsmart.BestServiceProviders.BSP;
import com.example.lebsmart.BestServiceProviders.BSPAdd;
import com.example.lebsmart.CommitteeDR.CommitteeAdd;
import com.example.lebsmart.CommitteeDR.CommitteeDR;
import com.example.lebsmart.CommitteeDR.CommitteeRVA;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AnnouncementsActivity extends AppCompatActivity {

    RecyclerView recyclerViewCommittee;
    CommitteeRVA committeeRVA;

    TextView announcementTypeTV;

    List<CommitteeDR> committeeDRList;

    String announcementType;

    ProgressDialog progressDialog;

    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> type = new ArrayList<>();
    ArrayList<String> addedBy = new ArrayList<>(); // users ids
    ArrayList<String> addedByNames = new ArrayList<>(); // users names, all should be committee members

    int currentUserPositionInList = -1;

    boolean snapshotExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_announcements);

        announcementTypeTV = findViewById(R.id.announcementType);

        announcementType = "";
        Intent intent = getIntent();
        announcementType = intent.getStringExtra("announcementType");

        announcementTypeTV.setText(announcementType);

        // to get data from db
        if (announcementType.equals("Committee Decisions")) {
            announcementType = "Decision";
        }
        else {
            announcementType = "Reminder";
        }

        progressDialog = new ProgressDialog(AnnouncementsActivity.this);

        committeeDRList = new ArrayList<>();
        /*committeeDRList.add(new CommitteeDR("Pay for Electricity", "Don't forget to pau in few days...!", "" + announcementType, "Friday, December 2nd, 2021"));
        committeeDRList.add(new CommitteeDR("Pay for Water", "Don't forget to pau in few days...!", "" + announcementType, "Friday, December 2nd, 2021"));
        committeeDRList.add(new CommitteeDR("Bring a private motor to building", "Don't forget to pau in few days...!", "" + announcementType, "Friday, December 2nd, 2021"));
        committeeDRList.add(new CommitteeDR("Elevator Maintenance", "Don't forget to pau in few days...!", "" + announcementType, "Friday, December 2nd, 2021"));
        committeeDRList.add(new CommitteeDR("Fix garage door", "Don't forget to pau in few days...!", "" + announcementType, "Friday, December 2nd, 2021"));
*/

        getAnnouncementsFromDb();

    }


    DatabaseReference databaseReference;

    public void getAnnouncementsFromDb () {
        CommonMethods.displayLoadingScreen(progressDialog);

        databaseReference = FirebaseDatabase.getInstance().getReference("CommitteeDRs");
        databaseReference = databaseReference.child(announcementType);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshotExists = true;
                if (!snapshot.exists()) {
                    snapshotExists = false;
                    return;
                }
                int x = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    addedBy.add(dataSnapshot.getKey()); // addedBy contains IDs and not names

                    date.add(Objects.requireNonNull(dataSnapshot.child("drDate").getValue()).toString());
                    description.add(Objects.requireNonNull(dataSnapshot.child("drDescription").getValue()).toString());
                    title.add(Objects.requireNonNull(dataSnapshot.child("drTitle").getValue()).toString());
                    type.add(Objects.requireNonNull(dataSnapshot.child("drType").getValue()).toString());

                    if (dataSnapshot.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        currentUserPositionInList = x;
                    }
                    x++;
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
                // Do this function after some time
                if (snapshotExists) {
                    getUserInfo();
                }
                else {
                    Toast.makeText(AnnouncementsActivity.this, "No " + announcementType + "s to display!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
            }
        }, 4100);

    }

    public void getUserInfo () {
        CommonMethods.displayLoadingScreen(progressDialog);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i=0; i< addedBy.size(); i++) {
                    addedByNames.add(Objects.requireNonNull(snapshot.child(addedBy.get(i)).child("fullName").getValue()).toString());
                }
                for (int j=0; j<addedBy.size(); j++) {
                    committeeDRList.add(new CommitteeDR(title.get(j), description.get(j), type.get(j),
                            date.get(j), addedByNames.get(j)));
                }
                CommonMethods.dismissLoadingScreen(progressDialog);
                reference.removeEventListener(this);

                setCommitteeRVA();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("dbError", error.getMessage());
            }
        });

    }



    CommitteeDR deletedDR = null;
    CommitteeAdd reAddDeletedDR = null;
    CommitteeDR tempDR = null;

    public void setCommitteeRVA () {
        recyclerViewCommittee = findViewById(R.id.committeeRecyclerView);
        committeeRVA = new CommitteeRVA(committeeDRList, currentUserPositionInList);

        recyclerViewCommittee.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewCommittee.setAdapter(committeeRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewCommittee.addItemDecoration(dividerItemDecoration);


        if (currentUserPositionInList != -1) {
            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    // A USER CAN ONLY DELETE the bsp he added, NOT OTHERS
                    final int position = viewHolder.getAdapterPosition();
                    tempDR = committeeDRList.get(position);

                    if (direction == ItemTouchHelper.LEFT && position == currentUserPositionInList) {
                        deletedDR = committeeDRList.get(position); // used for undo action
                        reAddDeletedDR = new CommitteeAdd(title.get(position), description.get(position),
                                type.get(position), date.get(position));

                        committeeDRList.remove(position);
                        committeeRVA.notifyItemRemoved(position);

                        final DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("CommitteeDRs").child(announcementType)
                                .child(addedBy.get(position));
                        reference.removeValue();

                        Snackbar.make(recyclerViewCommittee, announcementType + " removed!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        committeeDRList.add(position, deletedDR);
                                        committeeRVA.notifyItemInserted(position);

                                        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                .getReference("CommitteeDRs")
                                                .child(announcementType).child(addedBy.get(position));
                                        reference1.setValue(reAddDeletedDR).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.i("undo", "success");
                                                } else {
                                                    Log.i("undo", "failed");
                                                }
                                            }

                                        });

                                    }
                                }).show();
                    }
                    else {
                        committeeDRList.remove(position);
                        committeeRVA.notifyItemRemoved(position);
                        committeeDRList.add(position, tempDR);
                        committeeRVA.notifyItemInserted(position);
                        Toast.makeText(AnnouncementsActivity.this, "Cannot delete a " + announcementType + " that you didn't add!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerViewCommittee);
        }


    }

}