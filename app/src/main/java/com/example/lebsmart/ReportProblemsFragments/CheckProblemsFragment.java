package com.example.lebsmart.ReportProblemsFragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebsmart.Activities.ProblemsActivity;
import com.example.lebsmart.MeetingsFragments.MeetingRVA;
import com.example.lebsmart.MeetingsFragments.Meetings;
import com.example.lebsmart.R;

import java.util.ArrayList;
import java.util.List;

public class CheckProblemsFragment extends Fragment {

    CardView yourBuildingProblems, theSmartCityProblems;

    ViewGroup root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.check_problems, container, false);

        yourBuildingProblems = root.findViewById(R.id.yourBuildingProblems);
        theSmartCityProblems = root.findViewById(R.id.theSmartCityProblems);

        yourBuildingProblems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from database and pass them to the activity
                Intent intent = new Intent(getActivity(), ProblemsActivity.class);
                intent.putExtra("problemWithin", "Your Building");
                startActivity(intent);
            }
        });

        theSmartCityProblems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from database and pass them to the activity
                Intent intent = new Intent(getActivity(), ProblemsActivity.class);
                intent.putExtra("problemWithin", "The Smart City");
                startActivity(intent);
            }
        });


        return root;
    }

}
