package com.example.lebsmart.MeetingsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebsmart.ApartmentsFragments.Apartment;
import com.example.lebsmart.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class CheckMeetingsFragment extends Fragment {

    RecyclerView recyclerViewMeetings;
    MeetingRVA meetingRVA;

    ViewGroup root;

    List<Meetings> meetings;

    public CheckMeetingsFragment () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.check_meetings_fragment, container, false);

        meetings = new ArrayList<>();
        meetings.add(new Meetings("11:22", "25-5-2015", "zohoor building", "an alien", "Meeting Y", "description heree!!"));
        meetings.add(new Meetings("1:55", "25-5-2015", "zohoor building", "an alien", "Meeting X", "description heree!!"));
        meetings.add(new Meetings("4:51", "25-5-2015", "zohoor building", "an alien", "Meeting Z", "description heree!!"));
        meetings.add(new Meetings("2:55", "25-5-2015", "zohoor building", "an alien", "Meeting N", "description heree!!"));
        meetings.add(new Meetings("8:02", "25-5-2015", "zohoor building", "an alien", "Meeting K", "description heree!!"));


        recyclerViewMeetings = root.findViewById(R.id.meetingsRecyclerView);
        meetingRVA = new MeetingRVA(meetings);

        recyclerViewMeetings.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerViewMeetings.setAdapter(meetingRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewMeetings.addItemDecoration(dividerItemDecoration);


        /*ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerViewMeetings);*/

        return root;
    }

    /*Meetings deletedMeeting = null;

    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,
            ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            // used for rearranging items inside the RV only
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            final int position = viewHolder.getAdapterPosition();

            switch (direction) {
                case ItemTouchHelper.LEFT:
                    deletedMeeting = meetings.get(position); // used for undo action

                    meetings.remove(position);
                    meetingRVA.notifyItemRemoved(position);

                    Snackbar.make(recyclerViewMeetings, "Meeting deleted!", Snackbar.LENGTH_LONG)
                            .setAction("Undo", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    meetings.add(position, deletedMeeting);
                                    meetingRVA.notifyItemInserted(position);
                                }
                            }).show();

                    break;
                case ItemTouchHelper.RIGHT:
                    break;
            }
        }
    };*/

}
