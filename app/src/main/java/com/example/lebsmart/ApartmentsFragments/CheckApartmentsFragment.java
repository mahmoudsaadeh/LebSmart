package com.example.lebsmart.ApartmentsFragments;


import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;


public class CheckApartmentsFragment extends Fragment implements AdapterView.OnItemSelectedListener, View.OnTouchListener {

    RecyclerView recyclerView;
    ApartmentsRecyclerViewAdapter apartmentsRecyclerViewAdapter;

    ViewGroup root;

    List<Apartment> list;

    ProgressDialog progressDialog;

    ArrayList<String> users = new ArrayList<>(); // users IDs in Apartments child in db

    ArrayList<String> state = new ArrayList<>();
    ArrayList<String> price = new ArrayList<>();
    ArrayList<String> area = new ArrayList<>();
    ArrayList<String> building = new ArrayList<>();
    ArrayList<String> ownerPhone = new ArrayList<>();
    ArrayList<String> ownerName = new ArrayList<>();

    SwipeRefreshLayout swipeRefreshLayout;

    int currentUserPositionInList = -1;

    public static boolean checkIfCM; // for committee and meetings fragment

    public static String getUserBuilding; // for meetings fragment

    //TextView showAllApartmentsTV;

    Spinner spin;

    boolean checkIfExist;

    boolean userSelect = false;

    ArrayList<String> allBuildings = new ArrayList<>();

    boolean categoryMethodTriggered;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_check_apartments, container, false);

        progressDialog = new ProgressDialog(getActivity());

        //showAllApartmentsTV = root.findViewById(R.id.showAllApartmentsTV);
        getUserData(); // should be triggered in all conditions since its data is used in other fragments

        list = new ArrayList<>();

        dbCheckApartments();

        swipeRefreshLayout = root.findViewById(R.id.refreshLayoutCheckApartment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Reload current fragment
                // try recalling a function or so

                /*Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ApartmentsFragment()).commit();*/
                dbCheckApartments();

                swipeRefreshLayout.setRefreshing(false);
            }
        });



        spin = root.findViewById(R.id.apartmentsSpinner);

        // Inflate the layout for this fragment
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
            if (allBuildings.get(position).equals("All")) {
                Log.i("show all", "entered");
                dbCheckApartments();
            }
            else {
                //Toast.makeText(getActivity(), "" + allBuildings.get(position), Toast.LENGTH_SHORT).show();
                Log.i("onItemSelected", "entered");
                //dbCheckApartmentsCategory("b");
                dbCheckApartmentsCategory(allBuildings.get(position));
            }

            userSelect = false;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void dbCheckApartmentsCategory(final String chosenBuilding) {
        CommonMethods.displayLoadingScreen(progressDialog);

        categoryMethodTriggered = true;

        checkIfExist = true;

        users.clear();
        state.clear();
        area.clear();
        price.clear();

        // saved it in getUserInfo as well (can omit this maybe)
        //allBuildings = building; // save the AL of all buildings since building is cleared below

        Log.i("dbCat", "entered");

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance()
                .getReference("ApartmentsCategorized").child(chosenBuilding);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(getActivity(), "No apartments found in " + chosenBuilding, Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    checkIfExist = false;
                    databaseReference.removeEventListener(this);
                    return;
                }
                //Log.i("1", "1");
                int x = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //Log.i("snapshot", dataSnapshot.getValue().toString());
                    users.add(dataSnapshot.getKey());
                    //Log.i("key", dataSnapshot.getKey());
                    state.add(dataSnapshot.child("state").getValue().toString());
                    area.add(dataSnapshot.child("area").getValue().toString());
                    price.add(dataSnapshot.child("price").getValue().toString());

                    if (Objects.equals(dataSnapshot.getKey(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                        currentUserPositionInList = x;
                    }
                    x++;
                }
                //Log.i("2", "2");
                //CommonMethods.dismissLoadingScreen(progressDialog);
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("error", error.getMessage());
                CommonMethods.dismissLoadingScreen(progressDialog);
            }
        });

        //Log.i("3", "3");

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


    Apartment deletedApartment = null;
    ApartmentAdd reAddDeletedApartment = null;
    Apartment tempApartment = null;


    public void dbCheckApartments() {
        CommonMethods.displayLoadingScreen(progressDialog);

        categoryMethodTriggered = false;

        users.clear();
        state.clear();
        area.clear();
        price.clear();

        Log.i("dbAll", "entered");

        checkIfExist = true;

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            Log.i("user", "=null");
            CommonMethods.dismissLoadingScreen(progressDialog);
            return;
        }

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("ApartmentsAll");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(getActivity(), "No apartments found!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    checkIfExist = false;
                    databaseReference.removeEventListener(this);
                    return;
                }
                //Log.i("1", "1");
                int x = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //Log.i("snapshot", dataSnapshot.getValue().toString());
                    users.add(dataSnapshot.getKey());
                    //Log.i("key", dataSnapshot.getKey());
                    state.add(dataSnapshot.child("state").getValue().toString());
                    area.add(dataSnapshot.child("area").getValue().toString());
                    price.add(dataSnapshot.child("price").getValue().toString());

                    if (Objects.equals(dataSnapshot.getKey(), Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid())) {
                        currentUserPositionInList = x;
                    }
                    x++;
                }
                //Log.i("2", "2");
                //CommonMethods.dismissLoadingScreen(progressDialog);
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("error", error.getMessage());
                CommonMethods.dismissLoadingScreen(progressDialog);
            }
        });

        //Log.i("3", "3");

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

        building.clear();
        ownerName.clear();
        ownerPhone.clear();

        Log.i("getUserInfo", "entered");

        list.clear();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // added in a separate method below, since this method will not be triggered
                // if no apartments where found, and thus causing errors and wrong results in other fragments
                /*checkIfCM = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("userType").getValue().equals("Committee member");

                getUserBuilding = snapshot.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("buildingChosen").getValue().toString();*/

                //Log.i("snp1", snapshot.getValue().toString());
                //Log.i("userz", users.toString());
                /*Log.i("snp2", snapshot.child(users.get(i)).getValue().toString());
                Log.i("snp3", snapshot.child(users.get(i)).child("buildingChosen").getValue().toString());*/

                for (int i=0; i< users.size(); i++) {
                    building.add(snapshot.child(users.get(i)).child("buildingChosen").getValue().toString());
                    ownerPhone.add(snapshot.child(users.get(i)).child("phone").getValue().toString());
                    ownerName.add(snapshot.child(users.get(i)).child("fullName").getValue().toString());
                    //Log.i("building" + i, building.get(i));
                }
                for (int j=0; j<users.size(); j++) {
                    list.add(new Apartment(state.get(j), price.get(j), area.get(j), building.get(j), ownerPhone.get(j), ownerName.get(j)));
                }

                // only set this array when we get All apartments
                if (!categoryMethodTriggered) {
                    Log.i("allBuildings", "updated");
                    allBuildings.clear();
                    //allBuildings = building; // by reference, not what needed
                    //Collections.copy(allBuildings, building);
                    allBuildings = (ArrayList<String>) building.clone();
                    allBuildings.add("All");

                    Set<String> set = new HashSet<>(allBuildings);
                    allBuildings.clear();
                    allBuildings.addAll(set); // duplicates removed

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, allBuildings);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spin.setAdapter(adapter);

                    spin.setSelection(allBuildings.indexOf("All"));
                }

                Log.i("allBuilingszzz", allBuildings.toString());



                CommonMethods.dismissLoadingScreen(progressDialog);
                reference.removeEventListener(this);
                setApartmentRV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("dbError", error.getMessage());
            }
        });

    }

    public void setApartmentRV() {

        spin.setOnItemSelectedListener(this);
        spin.setOnTouchListener(this);

        //Log.i("setAdapter", "entered");

        recyclerView = root.findViewById(R.id.recyclerView);
        apartmentsRecyclerViewAdapter = new ApartmentsRecyclerViewAdapter(list, currentUserPositionInList);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerView.setAdapter(apartmentsRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Objects.requireNonNull(this.getContext()), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);


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
                    tempApartment = list.get(position);


                    if (direction == ItemTouchHelper.LEFT && position == currentUserPositionInList) {
                        deletedApartment = list.get(position); // used for undo action
                        reAddDeletedApartment = new ApartmentAdd(state.get(position), price.get(position),
                                area.get(position));

                        list.remove(position);
                        apartmentsRecyclerViewAdapter.notifyItemRemoved(position);

                        final DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("ApartmentsAll").child(users.get(position));
                        reference.removeValue();

                        final DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                .getReference("ApartmentsCategorized")
                                .child(building.get(position)).child(users.get(position));
                        reference1.removeValue();

                        Snackbar.make(recyclerView, "Apartment advertisement removed!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        list.add(position, deletedApartment);
                                        apartmentsRecyclerViewAdapter.notifyItemInserted(position);

                                        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                .getReference("ApartmentsAll").child(users.get(position));
                                        reference1.setValue(reAddDeletedApartment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.i("undo", "success");
                                                } else {
                                                    Log.i("undo", "failed");
                                                }
                                            }

                                        });

                                        DatabaseReference reference2 = FirebaseDatabase.getInstance()
                                                .getReference("ApartmentsCategorized")
                                                .child(building.get(position)).child(users.get(position));
                                        reference2.setValue(reAddDeletedApartment).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Log.i("undo cat", "success");
                                                } else {
                                                    Log.i("undo cat", "failed");
                                                }
                                            }

                                        });

                                    }
                                }).show();
                    }
                    else {
                        list.remove(position);
                        apartmentsRecyclerViewAdapter.notifyItemRemoved(position);
                        list.add(position, tempApartment);
                        apartmentsRecyclerViewAdapter.notifyItemInserted(position);
                        Toast.makeText(getActivity(), "Cannot delete an apartment other than yours!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }

    }

    public static void getUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    checkIfCM = snapshot.child("userType").getValue().equals("Committee member");

                    getUserBuilding = snapshot.child("buildingChosen").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Log.i("error getting user data", error.getMessage());
                }
            });
        }

    }

}