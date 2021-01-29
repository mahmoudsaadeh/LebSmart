package com.example.lebsmart.ApartmentsFragments;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

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


public class CheckApartmentsFragment extends Fragment {

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_check_apartments, container, false);

        progressDialog = new ProgressDialog(getActivity());

        Log.i("test1", "pass");

        // add person name and building name as well
        list = new ArrayList<>();

        dbCheckApartments();

        /*list.add(new Apartment("rent", "500$", "100m^2", "Zohoor", "78421354", "fullname));
        list.add(new Apartment("sale", "700$", "200m^2", "Al Burj Al Abiad", "26626267", "3rd"));
        list.add(new Apartment("rent", "900$", "400m^2", "Tubbara", "784212424", "7th"));
        list.add(new Apartment("sale", "200$", "300m^2", "Al Bahij", "78741258", "1st"));*/

        //list.add(new Apartment("test", "test", "test", "test", "test", "t"));

        // added below using a method
        /*recyclerView = root.findViewById(R.id.recyclerView);
        apartmentsRecyclerViewAdapter = new ApartmentsRecyclerViewAdapter(list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerView.setAdapter(apartmentsRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);*/

        swipeRefreshLayout = root.findViewById(R.id.refreshLayoutCheckApartment);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Reload current fragment
                /*Fragment frg = null;
                frg = getFragmentManager().findFragmentByTag("Apartments");
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.detach(frg);
                ft.attach(frg);
                ft.commit();*/

                /*getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragmentCheckApartment,
                        new CheckApartmentsFragment(), "Apartments").commit();*/

                // giving wrong results
                /*FragmentTransaction ft = getFragmentManager().beginTransaction();
                if (Build.VERSION.SDK_INT >= 26) {
                    ft.setReorderingAllowed(false);
                }
                ft.detach(CheckApartmentsFragment.this).attach(CheckApartmentsFragment.this).commit();*/

                // giving wrong results
                /*assert getFragmentManager() != null;
                getFragmentManager()
                        .beginTransaction()
                        .detach(CheckApartmentsFragment.this)
                        .attach(CheckApartmentsFragment.this)
                        .commit();*/

                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ApartmentsFragment()).commit();

                swipeRefreshLayout.setRefreshing(false);
            }
        });





        // Inflate the layout for this fragment
        return root;
    }


    Apartment deletedApartment = null;
    ApartmentAdd reAddDeletedApartment = null;
    Apartment tempApartment = null;


    public void dbCheckApartments() {
        CommonMethods.displayLoadingScreen(progressDialog);

        users.clear();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Apartments");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.hasChildren()) {
                    return;
                }
                Log.i("1", "1");
                int x = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Log.i("snapshot", dataSnapshot.getValue().toString());
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
                Log.i("2", "2");
                //CommonMethods.dismissLoadingScreen(progressDialog);
                databaseReference.removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("error", error.getMessage());
                CommonMethods.dismissLoadingScreen(progressDialog);
            }
        });

        Log.i("3", "3");

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


                Log.i("snp1", snapshot.getValue().toString());
                Log.i("userz", users.toString());
                /*Log.i("snp2", snapshot.child(users.get(i)).getValue().toString());
                Log.i("snp3", snapshot.child(users.get(i)).child("buildingChosen").getValue().toString());*/

                for (int i=0; i< users.size(); i++) {
                    building.add(snapshot.child(users.get(i)).child("buildingChosen").getValue().toString());
                    ownerPhone.add(snapshot.child(users.get(i)).child("phone").getValue().toString());
                    ownerName.add(snapshot.child(users.get(i)).child("fullName").getValue().toString());
                    Log.i("building" + i, building.get(i));
                }
                for (int j=0; j<users.size(); j++) {
                    list.add(new Apartment(state.get(j), price.get(j), area.get(j), building.get(j), ownerPhone.get(j), ownerName.get(j)));
                }
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
        Log.i("setApartmentRv", "entered");
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

                    //AlertDialog d = approveDeletionAlert();

                    if (direction == ItemTouchHelper.LEFT && position == currentUserPositionInList/* && approved*/) {
                        deletedApartment = list.get(position); // used for undo action
                        reAddDeletedApartment = new ApartmentAdd(state.get(position), price.get(position),
                                area.get(position));

                        list.remove(position);
                        apartmentsRecyclerViewAdapter.notifyItemRemoved(position);

                        final DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("Apartments").child(users.get(position));
                        reference.removeValue();

                        Snackbar.make(recyclerView, "Apartment advertisement removed!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        list.add(position, deletedApartment);
                                        apartmentsRecyclerViewAdapter.notifyItemInserted(position);

                                        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                .getReference("Apartments").child(users.get(position));
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

                                    }
                                }).show();
                    }
                    else {
                        //list.add(position, deletedMovie);
                        list.remove(position);
                        apartmentsRecyclerViewAdapter.notifyItemRemoved(position);
                        list.add(position, tempApartment);
                        apartmentsRecyclerViewAdapter.notifyItemInserted(position);
                        Toast.makeText(getActivity(), "Cannot delete an apartment other than yours!", Toast.LENGTH_SHORT).show();
                        /*if (d.isShowing()) {
                            d.dismiss();
                        }*/
                    }
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerView);
        }

    }

    //boolean approved = false;

    /*public AlertDialog approveDeletionAlert() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.delete_item_dialog, null);

        builder.setView(view);

        final AlertDialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        Button cancel = view.findViewById(R.id.cancelButton);
        Button yes = view.findViewById(R.id.yesButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                approved = false;
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                approved = true;
            }
        });

        //builder.setView(view);
        dialog.show();


        return dialog;

    }*/

}