package com.example.lebsmart.About;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lebsmart.LostFoundAnnouncements.LFAsRVA;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ListOfResidentsFragment extends Fragment {

    ViewGroup viewGroup;

    RecyclerView recyclerViewLORs;
    LORsRVA loRsRVA;

    List<LORs> loRs;

    ProgressDialog progressDialog;

    String currentUserBuilding;

    ArrayList<String> users = new ArrayList<>(); // users IDs inside the building of current user
    ArrayList<String> mails = new ArrayList<>();
    ArrayList<String> fullNames = new ArrayList<>();
    ArrayList<String> phones = new ArrayList<>();
    ArrayList<String> userTypes = new ArrayList<>(); // make fullname in bold for committee members

    SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.list_of_residents_frag, container, false);

        progressDialog = new ProgressDialog(getActivity());

        loRs = new ArrayList<>();

        //dbGetLORs();
        getCurrentUserBuilding();

        swipeRefreshLayout = viewGroup.findViewById(R.id.residentsListRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ListOfResidentsFragment()).commit();

                swipeRefreshLayout.setRefreshing(false);
            }
        });

        return viewGroup;
    }



    public void getCurrentUserBuilding () {

        CommonMethods.displayLoadingScreen(progressDialog);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserBuilding = snapshot.child("buildingChosen").getValue().toString();
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
                dbGetLORs();
            }
        }, 1777);

    }

    public void dbGetLORs () {

        users.clear();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Buildings")
                .child(currentUserBuilding);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dataSnapshot : snapshot.child("Committee member").getChildren()) {
                    users.add(dataSnapshot.getKey());
                    mails.add(Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString());
                    fullNames.add(Objects.requireNonNull(dataSnapshot.child("fullName").getValue()).toString());
                    phones.add(Objects.requireNonNull(dataSnapshot.child("phone").getValue()).toString());
                    userTypes.add(Objects.requireNonNull(dataSnapshot.child("userType").getValue()).toString());

                }

                for (DataSnapshot dataSnapshot : snapshot.child("Normal resident").getChildren()) {
                    users.add(dataSnapshot.getKey());
                    mails.add(Objects.requireNonNull(dataSnapshot.child("email").getValue()).toString());
                    fullNames.add(Objects.requireNonNull(dataSnapshot.child("fullName").getValue()).toString());
                    phones.add(Objects.requireNonNull(dataSnapshot.child("phone").getValue()).toString());
                    userTypes.add(Objects.requireNonNull(dataSnapshot.child("userType").getValue()).toString());

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
                for (int j=0; j<users.size(); j++) {
                    loRs.add(new LORs(fullNames.get(j), mails.get(j), phones.get(j), userTypes.get(j)));
                }
                CommonMethods.dismissLoadingScreen(progressDialog);
                setLORsRVA();
            }
        }, 3555);

    }

    public void setLORsRVA () {
        recyclerViewLORs = viewGroup.findViewById(R.id.residentsListRecyclerView);
        loRsRVA = new LORsRVA(loRs, getActivity(), FirebaseAuth.getInstance().getCurrentUser().getUid(), users);

        recyclerViewLORs.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerViewLORs.setAdapter(loRsRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewLORs.addItemDecoration(dividerItemDecoration);
    }

}
