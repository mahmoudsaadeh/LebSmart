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
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lebsmart.ApartmentsFragments.Apartment;
import com.example.lebsmart.ApartmentsFragments.ApartmentAdd;
import com.example.lebsmart.ApartmentsFragments.ApartmentsFragment;
import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.ReportProblemsFragments.Problem;
import com.example.lebsmart.ReportProblemsFragments.ProblemAdd;
import com.example.lebsmart.ReportProblemsFragments.ReportProblemRVA;
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
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ProblemsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

    RecyclerView recyclerViewProblems;
    ReportProblemRVA reportProblemRVA;

    TextView problemTV;

    List<Problem> problems;

    String problemWithin;


    ProgressDialog progressDialog;

    ArrayList<String> users = new ArrayList<>(); // users IDs in Problems child in db

    ArrayList<String> problemDescription = new ArrayList<>();
    ArrayList<String> problemReportDate = new ArrayList<>();
    ArrayList<String> problemTypeAL = new ArrayList<>();
    ArrayList<String> reportedByNames = new ArrayList<>();

    ArrayList<String> reportersBuilding = new ArrayList<>();

    SwipeRefreshLayout refreshLayoutProblems;

    int currentUserPositionInList = -1;

    boolean checkIfExist;

    Spinner problemsSpinner;

    boolean userSelect = false;

    ArrayList<String> allProblems = new ArrayList<>();

    boolean categoryMethodTriggered;

    String userSelectString;

    String reporterBuilding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);

        problemTV = findViewById(R.id.reportedPrblmTV);

        problemWithin = "";
        Intent intent = getIntent();
        problemWithin = intent.getStringExtra("problemWithin");

        problemTV.setText("Problems Within\n" + problemWithin);

        problems = new ArrayList<>();

        progressDialog = new ProgressDialog(this);

        problemsSpinner = findViewById(R.id.problemsSpinner);

        dbCheckProblems();

        refreshLayoutProblems = findViewById(R.id.refreshLayoutProblems);
        refreshLayoutProblems.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reload activity after db
                // no need
                dbCheckProblems();

                refreshLayoutProblems.setRefreshing(false);
            }
        });


    } // end onCreate


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelect = true;
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (userSelect) {
            if (allProblems.get(position).equals("All")) {
                Log.i("show all problems", "entered");
                userSelectString = allProblems.get(position);
                dbCheckProblems();
            }
            else {
                Toast.makeText(ProblemsActivity.this, "" + allProblems.get(position), Toast.LENGTH_SHORT).show();
                Log.i("onItemSelected pbs", "entered");
                userSelectString = allProblems.get(position);
                dbCheckProblemsCategory(allProblems.get(position));
            }

            userSelect = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    DatabaseReference databaseReference;

    public void dbCheckProblems () {
        CommonMethods.displayLoadingScreen(progressDialog);

        users.clear();
        problemDescription.clear();
        problemReportDate.clear();
        problemTypeAL.clear();

        categoryMethodTriggered = false;

        checkIfExist = true;

        if (problemWithin.equals("Your Building")) {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("ProblemsAll").child(problemWithin)
                    .child(CheckApartmentsFragment.getUserBuilding);
        }
        else {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("ProblemsAll").child(problemWithin);
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(ProblemsActivity.this, "No problems found!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    checkIfExist = false;
                    databaseReference.removeEventListener(this);
                    return;
                }
                int x = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    users.add(dataSnapshot.getKey());
                    // .child(Objects.requireNonNull(dataSnapshot.getKey()))
                    problemDescription.add(Objects.requireNonNull(dataSnapshot.child("problemDescription").getValue()).toString());
                    problemReportDate.add(Objects.requireNonNull(dataSnapshot.child("problemReportDate").getValue()).toString());
                    problemTypeAL.add(Objects.requireNonNull(dataSnapshot.child("problemType").getValue()).toString());

                    if (Objects.equals(dataSnapshot.getKey(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
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
                if (checkIfExist) {
                    getUserInfo();
                }
            }
        }, 4100);
    }



    DatabaseReference databaseReferenceCategory;

    public void dbCheckProblemsCategory (String chosenProblemType) {
        CommonMethods.displayLoadingScreen(progressDialog);

        categoryMethodTriggered = true;

        users.clear();
        problemDescription.clear();
        problemReportDate.clear();
        problemTypeAL.clear();

        checkIfExist = true;

        if (problemWithin.equals("Your Building")) {
            databaseReferenceCategory = FirebaseDatabase.getInstance()
                    .getReference("ProblemsCategorized").child(problemWithin)
                    .child(CheckApartmentsFragment.getUserBuilding).child(chosenProblemType);
        }
        else {
            databaseReferenceCategory = FirebaseDatabase.getInstance()
                    .getReference("ProblemsCategorized").child(problemWithin)
                    .child(chosenProblemType);
        }

        databaseReferenceCategory.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(ProblemsActivity.this, "No problems found!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    checkIfExist = false;
                    databaseReferenceCategory.removeEventListener(this);
                    return;
                }
                int x = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    users.add(dataSnapshot.getKey());

                    problemDescription.add(Objects.requireNonNull(dataSnapshot.child("problemDescription").getValue()).toString());
                    problemReportDate.add(Objects.requireNonNull(dataSnapshot.child("problemReportDate").getValue()).toString());
                    problemTypeAL.add(Objects.requireNonNull(dataSnapshot.child("problemType").getValue()).toString());

                    if (Objects.equals(dataSnapshot.getKey(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                        currentUserPositionInList = x;
                    }
                    x++;
                }
                databaseReferenceCategory.removeEventListener(this);
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
                if (checkIfExist) {
                    getUserInfo();
                }
            }
        }, 4100);
    }



    public void getUserInfo() {
        CommonMethods.displayLoadingScreen(progressDialog);

        reportedByNames.clear();
        reportersBuilding.clear();

        problems.clear();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                /*Log.i("snp1", snapshot.getValue().toString());
                Log.i("userz", users.toString());
                Log.i("snp2", snapshot.child(users.get(i)).getValue().toString());
                Log.i("snp3", snapshot.child(users.get(i)).child("buildingChosen").getValue().toString());*/

                for (int i=0; i< users.size(); i++) {
                    reportedByNames.add(Objects.requireNonNull(snapshot.child(users.get(i)).child("fullName").getValue()).toString());
                    reportersBuilding.add(Objects.requireNonNull(snapshot.child(users.get(i)).child("buildingChosen").getValue()).toString());
                }
                for (int j=0; j<users.size(); j++) {
                    if (reportersBuilding.get(j).equals(CheckApartmentsFragment.getUserBuilding)) {
                        reporterBuilding = reportersBuilding.get(j) + " (Your Building)";
                    }
                    else {
                        reporterBuilding = reportersBuilding.get(j);
                    }
                    problems.add(new Problem(problemTypeAL.get(j), problemDescription.get(j), reportedByNames.get(j)
                            , problemReportDate.get(j), reporterBuilding));
                }


                // only set this array when we get All problems
                if (!categoryMethodTriggered) {
                    Log.i("allProblems", "updated");
                    allProblems.clear();
                    //allBuildings = building; // by reference, not what needed
                    //Collections.copy(allBuildings, building);
                    allProblems = (ArrayList<String>) problemTypeAL.clone();
                    allProblems.add("All");

                    Set<String> set = new HashSet<>(allProblems);
                    allProblems.clear();
                    allProblems.addAll(set); // duplicates removed

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(ProblemsActivity.this, android.R.layout.simple_spinner_item, allProblems);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    problemsSpinner.setAdapter(adapter);

                    problemsSpinner.setSelection(allProblems.indexOf("All"));

                    userSelectString = "All";
                }

                Log.i("allProblems AL", allProblems.toString());


                CommonMethods.dismissLoadingScreen(progressDialog);
                reference.removeEventListener(this);
                setProblemsRV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("dbError", error.getMessage());
            }
        });

    }



    Problem deletedProblem = null;
    ProblemAdd reAddDeletedProblem = null;
    Problem tempProblem = null;

    public void setProblemsRV () {

        problemsSpinner.setOnItemSelectedListener(this);
        problemsSpinner.setOnTouchListener(this);

        recyclerViewProblems = findViewById(R.id.problemsRecyclerView);
        reportProblemRVA = new ReportProblemRVA(problems, currentUserPositionInList);

        recyclerViewProblems.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewProblems.setAdapter(reportProblemRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewProblems.addItemDecoration(dividerItemDecoration);


        if (currentUserPositionInList != -1) {
            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    // used for rearranging items inside the RV only
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    // A USER CAN ONLY DELETE HIS APARTMENT, NOT OTHERS
                    final int position = viewHolder.getAdapterPosition();
                    tempProblem = problems.get(position);

                    if (direction == ItemTouchHelper.LEFT && position == currentUserPositionInList) {
                        deletedProblem = problems.get(position); // used for undo action
                        reAddDeletedProblem = new ProblemAdd(problemTypeAL.get(position)
                                , problemDescription.get(position), problemReportDate.get(position));

                        problems.remove(position);
                        reportProblemRVA.notifyItemRemoved(position);

                        if (problemWithin.equals("Your Building")) {
                            final DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference("ProblemsAll").child(problemWithin)
                                    .child(CheckApartmentsFragment.getUserBuilding)
                                    .child(users.get(position));
                            reference.removeValue();

                            final DatabaseReference reference2 = FirebaseDatabase.getInstance()
                                    .getReference("ProblemsCategorized").child(problemWithin)
                                    .child(CheckApartmentsFragment.getUserBuilding)
                                    .child(problemTypeAL.get(position))
                                    .child(users.get(position));
                            reference2.removeValue();
                        }
                        else {
                            final DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference("ProblemsAll").child(problemWithin)
                                    .child(users.get(position));
                            reference.removeValue();

                            final DatabaseReference reference2 = FirebaseDatabase.getInstance()
                                    .getReference("ProblemsCategorized").child(problemWithin)
                                    .child(problemTypeAL.get(position))
                                    .child(users.get(position));
                            reference2.removeValue();
                        }

                        Snackbar.make(recyclerViewProblems, "The problem you reported is removed!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        problems.add(position, deletedProblem);
                                        reportProblemRVA.notifyItemInserted(position);

                                        if (problemWithin.equals("Your Building")) {
                                            final DatabaseReference reference = FirebaseDatabase.getInstance()
                                                    .getReference("ProblemsAll").child(problemWithin)
                                                    .child(CheckApartmentsFragment.getUserBuilding)
                                                    .child(users.get(position));
                                            reference.setValue(reAddDeletedProblem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.i("undo1", "success");
                                                    } else {
                                                        Log.i("undo1", "failed");
                                                    }
                                                }
                                            });

                                            final DatabaseReference reference2 = FirebaseDatabase.getInstance()
                                                    .getReference("ProblemsCategorized").child(problemWithin)
                                                    .child(CheckApartmentsFragment.getUserBuilding)
                                                    .child(problemTypeAL.get(position))
                                                    .child(users.get(position));
                                            reference2.setValue(reAddDeletedProblem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.i("undo2", "success");
                                                    } else {
                                                        Log.i("undo2", "failed");
                                                    }
                                                }
                                            });
                                        }
                                        else {
                                            final DatabaseReference reference = FirebaseDatabase.getInstance()
                                                    .getReference("ProblemsAll").child(problemWithin)
                                                    .child(users.get(position));
                                            reference.setValue(reAddDeletedProblem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.i("undo3", "success");
                                                    } else {
                                                        Log.i("undo3", "failed");
                                                    }
                                                }
                                            });

                                            final DatabaseReference reference2 = FirebaseDatabase.getInstance()
                                                    .getReference("ProblemsCategorized").child(problemWithin)
                                                    .child(problemTypeAL.get(position))
                                                    .child(users.get(position));
                                            reference2.setValue(reAddDeletedProblem).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.i("undo4", "success");
                                                    } else {
                                                        Log.i("undo4", "failed");
                                                    }
                                                }
                                            });
                                        }

                                    }
                                }).show();
                    }
                    else {
                        problems.remove(position);
                        reportProblemRVA.notifyItemRemoved(position);
                        problems.add(position, tempProblem);
                        reportProblemRVA.notifyItemInserted(position);
                        Toast.makeText(getApplicationContext(), "Cannot delete a problem that you didn't report!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerViewProblems);
        }


    }


}