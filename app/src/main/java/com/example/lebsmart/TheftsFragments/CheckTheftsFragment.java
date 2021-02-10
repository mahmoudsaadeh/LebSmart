package com.example.lebsmart.TheftsFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lebsmart.MeetingsFragments.MeetingAdd;
import com.example.lebsmart.MeetingsFragments.Meetings;
import com.example.lebsmart.MeetingsFragments.MeetingsFragment;
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

public class CheckTheftsFragment extends Fragment {

    RecyclerView recyclerViewThefts;
    TheftsRVA theftsRVA;

    ViewGroup root;

    List<Thefts> thefts;

    ProgressDialog progressDialog;

    ArrayList<String> users = new ArrayList<>(); // users IDs in Thefts child in db

    ArrayList<String> theftTitleAL = new ArrayList<>();
    ArrayList<String> theftDescriptionAL = new ArrayList<>();
    ArrayList<String> theftDateAL = new ArrayList<>();
    ArrayList<String> theftTimeAL = new ArrayList<>();
    ArrayList<String> reportedByNames = new ArrayList<>(); // users names
    ArrayList<String> theftLocationAL = new ArrayList<>();

    SwipeRefreshLayout swipeRefreshLayout;

    int currentUserPositionInList = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.check_theft_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());

        thefts = new ArrayList<>();
        /*thefts.add(new Thefts("hadiz", "car stolen", "friday august 7th 2020", "11 : 15", "2nd car stolen in the area, one of them is mine; take care!!", "3a2isha bakkar, beirut, lebanon"));
        thefts.add(new Thefts("khaled", "money stolen", "friday august 7th 2020", "11 : 15", "2nd car stolen in the area, one of them is mine; take care!!", "3a2isha bakkar, beirut, lebanon"));
        thefts.add(new Thefts("ramzi", "house stolen", "friday august 7th 2020", "11 : 15", "2nd car stolen in the area, one of them is mine; take care!!", "3a2isha bakkar, beirut, lebanon"));
        thefts.add(new Thefts("hamdi", "motorcycle stolen", "friday august 7th 2020", "11 : 15", "2nd car stolen in the area, one of them is mine; take care!!", "3a2isha bakkar, beirut, lebanon"));
        thefts.add(new Thefts("rola", "bike stolen", "friday august 7th 2020", "11 : 15", "2nd car stolen in the area, one of them is mine; take care!!", "3a2isha bakkar, beirut, lebanon"));
*/

        dbCheckThefts();

        swipeRefreshLayout = root.findViewById(R.id.checkTheftsRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TheftFragment()).commit();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return root;

    }

    public void dbCheckThefts() {
        CommonMethods.displayLoadingScreen(progressDialog);

        users.clear();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Thefts");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()) {
                    return;
                }
                int x = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //Log.i("snapshotMeetings", Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    users.add(dataSnapshot.getKey());
                    //Log.i("key", dataSnapshot.getKey());
                    theftTitleAL.add(Objects.requireNonNull(dataSnapshot.child("reportTitle").getValue()).toString());
                    theftDateAL.add(Objects.requireNonNull(dataSnapshot.child("reportDate").getValue()).toString());
                    theftTimeAL.add(Objects.requireNonNull(dataSnapshot.child("reportTime").getValue()).toString());
                    theftDescriptionAL.add(Objects.requireNonNull(dataSnapshot.child("reportMessage").getValue()).toString());
                    theftLocationAL.add(Objects.requireNonNull(dataSnapshot.child("reportLocation").getValue()).toString());

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
                getUserInfoThefts();
            }
        }, 4100);

    }



    public void getUserInfoThefts() {
        CommonMethods.displayLoadingScreen(progressDialog);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i=0; i< users.size(); i++) {
                    reportedByNames.add(Objects.requireNonNull(snapshot.child(users.get(i)).child("fullName").getValue()).toString());
                }
                for (int j=0; j<users.size(); j++) {
                    thefts.add(new Thefts(reportedByNames.get(j), theftTitleAL.get(j), theftDateAL.get(j),
                            theftTimeAL.get(j), theftDescriptionAL.get(j), theftLocationAL.get(j)));
                }
                CommonMethods.dismissLoadingScreen(progressDialog);
                reference.removeEventListener(this);
                setTheftsRVA();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("dbError", error.getMessage());
            }
        });

    }


    Thefts deletedTheft = null;
    TheftsAdd reAddDeletedTheft = null;
    Thefts tempTheft = null;

    public void setTheftsRVA() {
        recyclerViewThefts = root.findViewById(R.id.theftsRecyclerView);
        theftsRVA = new TheftsRVA(thefts, currentUserPositionInList);

        recyclerViewThefts.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerViewThefts.setAdapter(theftsRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewThefts.addItemDecoration(dividerItemDecoration);


        if (currentUserPositionInList != -1) {
            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    // A USER CAN ONLY DELETE HIS Meeting, NOT OTHERS
                    final int position = viewHolder.getAdapterPosition();
                    tempTheft = thefts.get(position);

                    if (direction == ItemTouchHelper.LEFT && position == currentUserPositionInList) {
                        deletedTheft = thefts.get(position); // used for undo action
                        reAddDeletedTheft = new TheftsAdd(theftTitleAL.get(position), theftDateAL.get(position),
                                theftTimeAL.get(position), theftDescriptionAL.get(position),
                                theftLocationAL.get(position));

                        thefts.remove(position);
                        theftsRVA.notifyItemRemoved(position);

                        final DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("Thefts").child(users.get(position));
                        reference.removeValue();

                        Snackbar.make(recyclerViewThefts, "Theft removed!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        thefts.add(position, deletedTheft);
                                        theftsRVA.notifyItemInserted(position);

                                        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                .getReference("Thefts").child(users.get(position));
                                        reference1.setValue(reAddDeletedTheft).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        thefts.remove(position);
                        theftsRVA.notifyItemRemoved(position);
                        thefts.add(position, tempTheft);
                        theftsRVA.notifyItemInserted(position);
                        Toast.makeText(getActivity(), "Cannot delete a theft that you haven't reported!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerViewThefts);
        }


    }

}
