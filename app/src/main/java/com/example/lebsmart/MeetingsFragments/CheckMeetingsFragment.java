package com.example.lebsmart.MeetingsFragments;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lebsmart.ApartmentsFragments.Apartment;
import com.example.lebsmart.ApartmentsFragments.ApartmentAdd;
import com.example.lebsmart.ApartmentsFragments.ApartmentsFragment;
import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
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

public class CheckMeetingsFragment extends Fragment {

    RecyclerView recyclerViewMeetings;
    MeetingRVA meetingRVA;

    ViewGroup root;

    List<Meetings> meetings;


    ProgressDialog progressDialog;

    ArrayList<String> users = new ArrayList<>(); // users IDs in Meetings child in db

    ArrayList<String> title = new ArrayList<>();
    ArrayList<String> description = new ArrayList<>();
    ArrayList<String> place = new ArrayList<>();
    ArrayList<String> date = new ArrayList<>();
    ArrayList<String> time = new ArrayList<>();
    ArrayList<String> scheduledBy = new ArrayList<>(); // users' names

    SwipeRefreshLayout swipeRefreshLayout;

    int currentUserPositionInList = -1;

    boolean checkIfExist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.check_meetings_fragment, container, false);

        progressDialog = new ProgressDialog(getActivity());

        meetings = new ArrayList<>();

        dbCheckMeetings();

        swipeRefreshLayout = root.findViewById(R.id.checkMeetingsRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                /*Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new MeetingsFragment()).commit();*/
                dbCheckMeetings();

                swipeRefreshLayout.setRefreshing(false);
            }
        });


        /*Drawable unwrappedDrawable = AppCompatResources.getDrawable(Objects.requireNonNull(getActivity()), R.drawable.ic_delete);
        assert unwrappedDrawable != null;
        Drawable wrappedDrawable = DrawableCompat.wrap(unwrappedDrawable);
        DrawableCompat.setTint(wrappedDrawable, Color.WHITE);*/

        return root;
    }



    public void dbCheckMeetings() {

        CommonMethods.displayLoadingScreen(progressDialog);

        users.clear();
        date.clear();
        time.clear();
        description.clear();
        place.clear();
        title.clear();

        checkIfExist = true;

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Meetings")
                .child(CheckApartmentsFragment.getUserBuilding);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(getActivity(), "No meetings are scheduled!", Toast.LENGTH_SHORT).show();
                    CommonMethods.dismissLoadingScreen(progressDialog);
                    checkIfExist = false;
                    databaseReference.removeEventListener(this);
                    return;
                }
                int x = 0;
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    //Log.i("snapshotMeetings", Objects.requireNonNull(dataSnapshot.getValue()).toString());
                    users.add(dataSnapshot.getKey());
                    //Log.i("key", dataSnapshot.getKey());
                    date.add(Objects.requireNonNull(dataSnapshot.child("meetingDateAdd").getValue()).toString());
                    time.add(Objects.requireNonNull(dataSnapshot.child("meetingTimeAdd").getValue()).toString());
                    description.add(Objects.requireNonNull(dataSnapshot.child("meetingDescriptionAdd").getValue()).toString());
                    place.add(Objects.requireNonNull(dataSnapshot.child("meetingPlaceAdd").getValue()).toString());
                    title.add(Objects.requireNonNull(dataSnapshot.child("meetingTitleAdd").getValue()).toString());

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
                    getUserInfoMeetings();
                }
            }
        }, 4100);

    }



    public void getUserInfoMeetings() {

        CommonMethods.displayLoadingScreen(progressDialog);

        scheduledBy.clear();

        meetings.clear();

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                /*Log.i("snp1", snapshot.getValue().toString());
                Log.i("userz", users.toString());
                Log.i("snp2", snapshot.child(users.get(i)).getValue().toString());
                Log.i("snp3", snapshot.child(users.get(i)).child("buildingChosen").getValue().toString());*/

                for (int i=0; i< users.size(); i++) {
                    scheduledBy.add(Objects.requireNonNull(snapshot.child(users.get(i)).child("fullName").getValue()).toString());
                }
                for (int j=0; j<users.size(); j++) {
                    meetings.add(new Meetings(time.get(j), date.get(j), place.get(j), title.get(j), description.get(j), scheduledBy.get(j)));
                }
                CommonMethods.dismissLoadingScreen(progressDialog);
                reference.removeEventListener(this);
                setMeetingsRV();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.i("dbError", error.getMessage());
            }
        });

    }




    Meetings deletedMeeting = null;
    MeetingAdd reAddDeletedMeeting = null;
    Meetings tempMeeting = null;

    public void setMeetingsRV () {
        Log.i("setMeetingRv", "entered");
        recyclerViewMeetings = root.findViewById(R.id.meetingsRecyclerView);
        meetingRVA = new MeetingRVA(meetings, currentUserPositionInList);

        recyclerViewMeetings.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerViewMeetings.setAdapter(meetingRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(Objects.requireNonNull(this.getContext()), DividerItemDecoration.VERTICAL);
        recyclerViewMeetings.addItemDecoration(dividerItemDecoration);


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
                    tempMeeting = meetings.get(position);

                    if (direction == ItemTouchHelper.LEFT && position == currentUserPositionInList) {
                        deletedMeeting = meetings.get(position); // used for undo action
                        reAddDeletedMeeting = new MeetingAdd(time.get(position), date.get(position),
                                place.get(position), title.get(position), description.get(position));

                        meetings.remove(position);
                        meetingRVA.notifyItemRemoved(position);

                        final DatabaseReference reference = FirebaseDatabase.getInstance()
                                .getReference("Meetings").child(users.get(position));
                        reference.removeValue();

                        Snackbar.make(recyclerViewMeetings, "Meeting removed!", Snackbar.LENGTH_LONG)
                                .setAction("Undo", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        meetings.add(position, deletedMeeting);
                                        meetingRVA.notifyItemInserted(position);

                                        DatabaseReference reference1 = FirebaseDatabase.getInstance()
                                                .getReference("Meetings").child(users.get(position));
                                        reference1.setValue(reAddDeletedMeeting).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                        meetings.remove(position);
                        meetingRVA.notifyItemRemoved(position);
                        meetings.add(position, tempMeeting);
                        meetingRVA.notifyItemInserted(position);
                        Toast.makeText(getActivity(), "Cannot delete a meeting that you haven't scheduled!", Toast.LENGTH_SHORT).show();
                    }
                }
            };

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
            itemTouchHelper.attachToRecyclerView(recyclerViewMeetings);
        }


    }

}
