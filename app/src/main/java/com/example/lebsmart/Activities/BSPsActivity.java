package com.example.lebsmart.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.example.lebsmart.BestServiceProviders.BSPsRVA;
import com.example.lebsmart.MeetingsFragments.MeetingAdd;
import com.example.lebsmart.MeetingsFragments.Meetings;
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

public class BSPsActivity extends AppCompatActivity {

    RecyclerView recyclerViewSP;
    BSPsRVA bsPsRVA;

    TextView spTV;

    List<BSP> bsps;

    String spCategory;

    ProgressDialog progressDialog;

    ArrayList<String> job = new ArrayList<>();
    ArrayList<String> phone = new ArrayList<>();
    ArrayList<String> addedBy = new ArrayList<>(); // users ids
    ArrayList<String> addedByNames = new ArrayList<>(); // users names
    ArrayList<String> pOR = new ArrayList<>();
    ArrayList<String> rating = new ArrayList<>(); // user's ratings to SPs
    ArrayList<Float> overallRatingAL = new ArrayList<>(); // overall rating
    ArrayList<String> name = new ArrayList<>(); // sps names

    int currentUserPositionInList = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b_s_ps);

        spTV = findViewById(R.id.spTV);

        spCategory = "";
        Intent intent = getIntent();
        spCategory = intent.getStringExtra("spCategory");

        spTV.setText(spCategory);

        progressDialog = new ProgressDialog(BSPsActivity.this);

        bsps = new ArrayList<>();

        dbCheckSPs();

    }



    public String removeS(String category) {
        if (spCategory != null && spCategory.length() > 0 && spCategory.charAt(spCategory.length() - 1) == 's') {
            category = spCategory.substring(0, spCategory.length() - 1);
            //Log.i("category", category);
        }
        return category;
    }


    DatabaseReference databaseReference;
    String category;
    boolean snapshotExists;

    public void dbCheckSPs () {
        CommonMethods.displayLoadingScreen(progressDialog);

        // remove 's' to get node in db
        category = removeS(spCategory);

        databaseReference = FirebaseDatabase.getInstance().getReference("SPs");
        databaseReference = databaseReference.child(category);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                snapshotExists = true;
                if (!snapshot.exists()) {
                    Toast.makeText(BSPsActivity.this, "No SPs found!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    snapshotExists = false;
                    databaseReference.removeEventListener(this);
                    return;
                }
                int x = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    phone.add(dataSnapshot.getKey());
                    // addedBy contains IDs and not names
                    addedBy.add(Objects.requireNonNull(dataSnapshot.child("addedBy").getValue()).toString());

                    job.add(Objects.requireNonNull(dataSnapshot.child("spJob").getValue()).toString());
                    name.add(Objects.requireNonNull(dataSnapshot.child("spName").getValue()).toString());
                    pOR.add(Objects.requireNonNull(dataSnapshot.child("spPlaceOfResidence").getValue()).toString());
                    //rating.add(Objects.requireNonNull(dataSnapshot.child("spRating").getValue()).toString());

                    if (dataSnapshot.child("addedBy").getValue().toString().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
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
                    //getUserInfoBSPs();
                    getRatings();
                }
            }
        }, 3111);

    }



    public void getRatings () {

        overallRatingAL.clear();
        rating.clear();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("SPsRatings").child(category);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Log.i("ratingSnapshot", "does not exist");
                    return;
                } else {
                    Log.i("ratingSnapshot", "exists");
                }

                float ratingsSum;
                DataSnapshot getAllRatingsSnapshot;
                float overall;
                int numberOfRatingsPerSP;
                boolean noCurrentUserRate;

                // loops over every SP
                for (int i=0; i<phone.size();i++) {
                    getAllRatingsSnapshot = snapshot.child(phone.get(i)).child("ratedBy");
                    ratingsSum = 0;
                    overall = 0;
                    numberOfRatingsPerSP = 0;
                    noCurrentUserRate = true;
                    // loop and get all ratings of every SP
                    for (DataSnapshot ratingz : getAllRatingsSnapshot.getChildren()) {
                        ratingsSum = ratingsSum + Float.parseFloat(ratingz.getValue().toString());

                        numberOfRatingsPerSP++;
                        Log.i("rateVal " + numberOfRatingsPerSP, ratingz.getValue().toString());

                        if (ratingz.getKey().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                            rating.add(ratingz.getValue().toString());
                            Log.i("myRateForTheSP " + numberOfRatingsPerSP, ratingz.getValue().toString());
                            noCurrentUserRate = false; // so there is a rating from the current user to this SP
                        }
                    }

                    if (noCurrentUserRate) {
                        rating.add("nf"); // not found
                    }
                    //Log.i("ratingsSum", String.valueOf(ratingsSum));
                    //Log.i("phone.size()", String.valueOf(phone.size()));
                    overall = ratingsSum / numberOfRatingsPerSP;
                    overallRatingAL.add(overall);
                    //Log.i("overallRate" + i, String.valueOf(overall));
                    //Log.i("phone" + i, phone.get(i));
                }
                Log.i("rating AL", rating.toString());

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
                getUserInfoBSPs();
            }
        }, 2555);

    }


    public void getUserInfoBSPs () {
        CommonMethods.displayLoadingScreen(progressDialog);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i=0; i< addedBy.size(); i++) {
                    addedByNames.add(Objects.requireNonNull(snapshot.child(addedBy.get(i)).child("fullName").getValue()).toString());
                }
                for (int j=0; j<addedBy.size(); j++) {
                    bsps.add(new BSP(name.get(j), phone.get(j), job.get(j), String.valueOf(overallRatingAL.get(j)), pOR.get(j), addedByNames.get(j)));
                }
                CommonMethods.dismissLoadingScreen(progressDialog);
                reference.removeEventListener(this);

                setBSPsRVA();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("dbError", error.getMessage());
            }
        });

    }


    BSP deletedBSP = null;
    BSPAdd reAddDeletedBSP = null;
    BSP tempBSP = null;

    public void setBSPsRVA () {
        recyclerViewSP = findViewById(R.id.spRecyclerView);
        bsPsRVA = new BSPsRVA(bsps, currentUserPositionInList, removeS(spCategory)
                , BSPsActivity.this, progressDialog, rating);

        recyclerViewSP.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewSP.setAdapter(bsPsRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewSP.addItemDecoration(dividerItemDecoration);

        // deleting a BSP will delete all his ratings and cannot be undone
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
                    tempBSP = bsps.get(position);

                    if (direction == ItemTouchHelper.LEFT && position == currentUserPositionInList) {
                        deletedBSP = bsps.get(position); // used for undo action
                        reAddDeletedBSP = new BSPAdd(name.get(position), phone.get(position),
                                job.get(position), pOR.get(position), addedBy.get(position));

                        bsps.remove(position);
                        bsPsRVA.notifyItemRemoved(position);

                        final DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("SPs").child(category).child(phone.get(position));
                        reference.removeValue();

                        // remove his rating as well
                        final DatabaseReference reference2 = FirebaseDatabase.getInstance()
                                .getReference("SPsRatings").child(category).child(phone.get(position));
                        reference2.removeValue();

                        // undo labke, hard to re add ratings

                        /*Snackbar.make(recyclerViewSP, "SP removed!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        bsps.add(position, deletedBSP);
                                        bsPsRVA.notifyItemInserted(position);

                                        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                .getReference("SPs").child(category).child(phone.get(position));
                                        reference1.setValue(reAddDeletedBSP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.i("undo", "success");
                                                } else {
                                                    Log.i("undo", "failed");
                                                }
                                            }

                                        });

                                        DatabaseReference reference3 = FirebaseDatabase.getInstance()
                                                .getReference("SPsRatings").child(category).child(phone.get(position));
                                        reference3.setValue(reAddDeletedBSP).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                }).show();*/
                    }
                    else {
                        bsps.remove(position);
                        bsPsRVA.notifyItemRemoved(position);
                        bsps.add(position, tempBSP);
                        bsPsRVA.notifyItemInserted(position);
                        Toast.makeText(BSPsActivity.this, "Cannot delete a SP that you didn't add!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerViewSP);
        }


    }

}