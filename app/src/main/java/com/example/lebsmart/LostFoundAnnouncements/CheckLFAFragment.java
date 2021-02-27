package com.example.lebsmart.LostFoundAnnouncements;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
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

public class CheckLFAFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

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

    Spinner spinnerLFAs;

    boolean userSelect = false;

    ArrayList<String> categories = new ArrayList<>();

    DatabaseReference databaseReference;

    String chosenCategory;

    String foundBy, foundersBuilding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.check_lfa_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());

        spinnerLFAs = root.findViewById(R.id.lfasSpinner);

        categories.clear();
        categories.add("Your Building");
        categories.add("The Smart City");

        lfas = new ArrayList<>();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLFAs.setAdapter(adapter);

        spinnerLFAs.setSelection(categories.indexOf("Your Building"));

        spinnerLFAs.setOnItemSelectedListener(this);
        spinnerLFAs.setOnTouchListener(this);

        dbCheckLFAs("Your Building");

        swipeRefreshLayout = root.findViewById(R.id.checkLFAsRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new LostFoundAnnouncementFragment()).commit();*/

                spinnerLFAs.setSelection(categories.indexOf("Your Building"));
                dbCheckLFAs("Your Building");

                swipeRefreshLayout.setRefreshing(false);
            }
        });



        return root;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        userSelect = true;
        return false;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (userSelect) {

            dbCheckLFAs(categories.get(position));

            userSelect = false;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    boolean snapshotExists;

    public void dbCheckLFAs (String category) {
        CommonMethods.displayLoadingScreen(progressDialog);

        users.clear();
        lfaTitleAL.clear();
        lfaDescriptionAL.clear();
        lfaDateAL.clear();

        snapshotExists = true;

        if (category.equals("Your Building")) {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("LFAsCategorized").child("Your Building")
                    .child(CheckApartmentsFragment.getUserBuilding);

            chosenCategory = "Your Building";
        }
        else {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("LFAsCategorized").child("The Smart City");

            chosenCategory = "The Smart City";
        }

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    snapshotExists = false;
                    Toast.makeText(getActivity(), "No LFAs were found!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    databaseReference.removeEventListener(this);
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
            }
        }, 4100);

    }


    public void getUserInfoLFA () {
        CommonMethods.displayLoadingScreen(progressDialog);

        lfaFoundByAL.clear();
        lfaFoundersBuildingAL.clear();

        lfas.clear();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (int i=0; i< users.size(); i++) {
                    lfaFoundByAL.add(Objects.requireNonNull(snapshot.child(users.get(i)).child("fullName").getValue()).toString());
                    lfaFoundersBuildingAL.add(Objects.requireNonNull(snapshot.child(users.get(i)).child("buildingChosen").getValue()).toString());
                }
                for (int j=0; j<users.size(); j++) {
                    if (users.get(j).equals(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                        foundBy = lfaFoundByAL.get(j) + " (You)";
                        foundersBuilding = lfaFoundersBuildingAL.get(j) + " (Your Building)";
                    }
                    else {
                        foundBy = lfaFoundByAL.get(j);
                        foundersBuilding = lfaFoundersBuildingAL.get(j);
                    }
                    lfas.add(new LFA(lfaTitleAL.get(j), lfaDateAL.get(j), lfaDescriptionAL.get(j),
                            foundBy, foundersBuilding));
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

                        if (chosenCategory.equals("Your Building")) {
                            final DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference("LFAsCategorized").child("Your Building")
                                    .child(CheckApartmentsFragment.getUserBuilding)
                                    .child(users.get(position));
                            reference.removeValue();
                        }
                        else {
                            final DatabaseReference reference = FirebaseDatabase.getInstance()
                                    .getReference("LFAsCategorized").child("The Smart City")
                                    .child(users.get(position));
                            reference.removeValue();
                        }

                        Snackbar.make(recyclerViewLFA, "LFA removed!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        lfas.add(position, deletedLFA);
                                        lfaRVA.notifyItemInserted(position);

                                        if (chosenCategory.equals("Your Building")) {
                                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                                    .getReference("LFAsCategorized").child("Your Building")
                                                    .child(CheckApartmentsFragment.getUserBuilding)
                                                    .child(users.get(position));
                                            reference.setValue(reAddDeletedLFA).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                        else {
                                            DatabaseReference reference = FirebaseDatabase.getInstance()
                                                    .getReference("LFAsCategorized").child("The Smart City")
                                                    .child(users.get(position));
                                            reference.setValue(reAddDeletedLFA).addOnCompleteListener(new OnCompleteListener<Void>() {
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
