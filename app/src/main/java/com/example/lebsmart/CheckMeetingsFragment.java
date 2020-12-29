package com.example.lebsmart;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebsmart.MeetingRVA;
import com.example.lebsmart.Meetings;
import com.example.lebsmart.R;

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
        meetings.add(new Meetings("11:22", "25-5-2015", "zohoor building", "an alien", "mafiye ellak1", "description heree!!"));
        meetings.add(new Meetings("1:55", "25-5-2015", "zohoor building", "an alien", "mafiye ellak2", "description heree!!"));
        meetings.add(new Meetings("4:51", "25-5-2015", "zohoor building", "an alien", "mafiye ellak3", "description heree!!"));
        meetings.add(new Meetings("2:55", "25-5-2015", "zohoor building", "an alien", "mafiye ellak4", "description heree!!"));
        meetings.add(new Meetings("8:02", "25-5-2015", "zohoor building", "an alien", "mafiye ellak5", "description heree!!"));


        recyclerViewMeetings = root.findViewById(R.id.meetingsRecyclerView);
        meetingRVA = new MeetingRVA(meetings);

        recyclerViewMeetings.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerViewMeetings.setAdapter(meetingRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewMeetings.addItemDecoration(dividerItemDecoration);

        return root;
    }
}
