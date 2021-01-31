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

import com.example.lebsmart.ApartmentsFragments.Apartment;
import com.example.lebsmart.ApartmentsFragments.ApartmentAdd;
import com.example.lebsmart.ApartmentsFragments.ApartmentsFragment;
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
import java.util.List;
import java.util.Objects;

public class ProblemsActivity extends AppCompatActivity {

    RecyclerView recyclerViewProblems;
    ReportProblemRVA reportProblemRVA;

    TextView problemTV;

    List<Problem> problems;

    String problemType;


    ProgressDialog progressDialog;

    ArrayList<String> users = new ArrayList<>(); // users IDs in Problems child in db

    ArrayList<String> problemDescription = new ArrayList<>();
    ArrayList<String> problemReportDate = new ArrayList<>();
    ArrayList<String> problemTypeAL = new ArrayList<>();
    ArrayList<String> reportedByNames = new ArrayList<>();

    //SwipeRefreshLayout swipeRefreshLayout;

    int currentUserPositionInList = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);

        problemTV = findViewById(R.id.reportedPrblmTV);

        problemType = "";
        Intent intent = getIntent();
        problemType = intent.getStringExtra("problemType");

        problemTV.setText(problemType + " Reported Problems:");

        problems = new ArrayList<>();

        progressDialog = new ProgressDialog(this);
        // get info from db and fill them here
        // should check the type of problems clicked, and get the ones with the associated type only
        /*problems.add(new Problem("water", "3otol1", "me1", "today1"));
        problems.add(new Problem("electricity", "3otol2", "me2", "today2"));
        problems.add(new Problem("bs", "3otol3", "me3", "today3"));
        problems.add(new Problem("bs", "3otol4", "me4", "today4"));
        problems.add(new Problem("water", "3otol5", "me5", "today5"));
*/

        dbCheckProblems();

        /*swipeRefreshLayout = findViewById(R.id.);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // reload activity after db
                // no need

                swipeRefreshLayout.setRefreshing(false);
            }
        });*/


    }

    public void dbCheckProblems () {
        CommonMethods.displayLoadingScreen(progressDialog);

        users.clear();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("Problems").child(problemType);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()) {
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
                getUserInfo();
            }
        }, 4100);
    }


    public void getUserInfo() {
        CommonMethods.displayLoadingScreen(progressDialog);

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
                }
                for (int j=0; j<users.size(); j++) {
                    problems.add(new Problem(problemTypeAL.get(j), problemDescription.get(j), reportedByNames.get(j), problemReportDate.get(j)));
                }
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
        recyclerViewProblems = findViewById(R.id.problemsRecyclerView);
        reportProblemRVA = new ReportProblemRVA(problems);

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

                        final DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("Problems").child(problemType).child(users.get(position));
                        reference.removeValue();

                        Snackbar.make(recyclerViewProblems, "The problem you reported is removed!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        problems.add(position, deletedProblem);
                                        reportProblemRVA.notifyItemInserted(position);

                                        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                .getReference("Problems").child(problemType).child(users.get(position));
                                        reference1.setValue(reAddDeletedProblem).addOnCompleteListener(new OnCompleteListener<Void>() {
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