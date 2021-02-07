package com.example.lebsmart.LostFoundAnnouncements;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
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

import com.example.lebsmart.MeetingsFragments.MeetingRVA;
import com.example.lebsmart.MeetingsFragments.Meetings;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.TheftsFragments.TheftFragment;
import com.example.lebsmart.TheftsFragments.Thefts;
import com.example.lebsmart.TheftsFragments.TheftsAdd;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class CheckLFAFragment extends Fragment {

    RecyclerView recyclerViewLFA;
    LFAsRVA lfaRVA;

    ViewGroup root;

    List<LFA> lfas;

    ProgressDialog progressDialog;

    ArrayList<String> users = new ArrayList<>(); // users IDs in LFAs child in db

    ArrayList<String> lfaTitleAL = new ArrayList<>();
    ArrayList<String> lfaDescriptionAL = new ArrayList<>();
    ArrayList<String> lfaDateAL = new ArrayList<>();
    ArrayList<String> lfaFoundByAL = new ArrayList<>(); // users names
    ArrayList<String> lfaFoundersBuildingAL = new ArrayList<>(); // users buildings

    SwipeRefreshLayout swipeRefreshLayout;

    int currentUserPositionInList = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.check_lfa_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());

        lfas = new ArrayList<>();
        /*lfas.add(new LFA("Found keys on stairs", "" + currentDate, "found 4 keys with tennis portocle", "get current user(the user that added the ann)"));
        lfas.add(new LFA("Found money on stairs", "" + currentDate, "found 4 keys with tennis portocle", "get current user(the user that added the ann)"));
        lfas.add(new LFA("Found bag in garage", "" + currentDate, "found 4 keys with tennis portocle", "get current user(the user that added the ann)"));
        lfas.add(new LFA("Found ID card in eleveator", "" + currentDate, "found 4 keys with tennis portocle", "get current user(the user that added the ann)"));
        lfas.add(new LFA("Found car keys in parking", "" + currentDate, "found 4 keys with tennis portocle", "get current user(the user that added the ann)"));
*/

        dbCheckLFAs();

        swipeRefreshLayout = root.findViewById(R.id.checkLFAsRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LostFoundAnnouncementFragment()).commit();

                swipeRefreshLayout.setRefreshing(false);
            }
        });



        return root;
    }

    boolean snapshotExists;

    public void dbCheckLFAs () {
        CommonMethods.displayLoadingScreen(progressDialog);

        users.clear();

        snapshotExists = true;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("LFAs");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    snapshotExists = false;
                    return;
                }
                int x = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    users.add(dataSnapshot.getKey());

                    lfaTitleAL.add(Objects.requireNonNull(dataSnapshot.child("lfaTitle").getValue()).toString());
                    lfaDescriptionAL.add(Objects.requireNonNull(dataSnapshot.child("lfaDescription").getValue()).toString());
                    lfaDateAL.add(Objects.requireNonNull(dataSnapshot.child("lfaDate").getValue()).toString());

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
                if (snapshotExists) {
                    getUserInfoLFA();
                }
                else {
                    Toast.makeText(getActivity(), "No announcements to display!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                }
            }
        }, 4100);

    }


    public void getUserInfoLFA () {
        CommonMethods.displayLoadingScreen(progressDialog);

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i=0; i< users.size(); i++) {
                    lfaFoundByAL.add(Objects.requireNonNull(snapshot.child(users.get(i)).child("fullName").getValue()).toString());
                    lfaFoundersBuildingAL.add(Objects.requireNonNull(snapshot.child(users.get(i)).child("buildingChosen").getValue()).toString());
                }
                for (int j=0; j<users.size(); j++) {
                    lfas.add(new LFA(lfaTitleAL.get(j), lfaDateAL.get(j), lfaDescriptionAL.get(j),
                            lfaFoundByAL.get(j), lfaFoundersBuildingAL.get(j)));
                }
                CommonMethods.dismissLoadingScreen(progressDialog);
                reference.removeEventListener(this);
                setLFAsRVA();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("dbError", error.getMessage());
            }
        });
    }


    LFA deletedLFA = null;
    LFAAdd reAddDeletedLFA = null;
    LFA tempLFA = null;

    public void setLFAsRVA () {
        recyclerViewLFA = root.findViewById(R.id.lfaRecyclerView);
        lfaRVA = new LFAsRVA(lfas, currentUserPositionInList);

        recyclerViewLFA.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerViewLFA.setAdapter(lfaRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewLFA.addItemDecoration(dividerItemDecoration);


        if (currentUserPositionInList != -1) {
            ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
                    ItemTouchHelper.LEFT) {
                @Override
                public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                    // A USER CAN ONLY DELETE HIS announcement, NOT OTHERS
                    final int position = viewHolder.getAdapterPosition();
                    tempLFA = lfas.get(position);

                    if (direction == ItemTouchHelper.LEFT && position == currentUserPositionInList) {
                        deletedLFA = lfas.get(position); // used for undo action
                        reAddDeletedLFA = new LFAAdd(lfaTitleAL.get(position),
                                lfaDateAL.get(position), lfaDescriptionAL.get(position));

                        lfas.remove(position);
                        lfaRVA.notifyItemRemoved(position);

                        final DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("LFAs").child(users.get(position));
                        reference.removeValue();

                        Snackbar.make(recyclerViewLFA, "LFA removed!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        lfas.add(position, deletedLFA);
                                        lfaRVA.notifyItemInserted(position);

                                        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                .getReference("LFAs").child(users.get(position));
                                        reference1.setValue(reAddDeletedLFA).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        lfas.remove(position);
                        lfaRVA.notifyItemRemoved(position);
                        lfas.add(position, tempLFA);
                        lfaRVA.notifyItemInserted(position);
                        Toast.makeText(getActivity(), "Cannot delete a LFA that you didn't add!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerViewLFA);
        }



    }

}
