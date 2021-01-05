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

    //RecyclerView recyclerViewProblems;
    //ReportProblemRVA reportProblemRVA;

    CardView elec;
    CardView wat;
    CardView bs;

    ViewGroup root;

    //List<Problem> problems;

    public CheckProblemsFragment () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.check_problems, container, false);

        elec = root.findViewById(R.id.elecProblems);
        wat = root.findViewById(R.id.watProblems);
        bs = root.findViewById(R.id.bsProblems);

        elec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from database and pass them to the activity
                Intent intent = new Intent(getActivity(), ProblemsActivity.class);
                intent.putExtra("problemType", "Electricity");
                startActivity(intent);
            }
        });

        wat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from database and pass them to the activity
                Intent intent = new Intent(getActivity(), ProblemsActivity.class);
                intent.putExtra("problemType", "Water");
                startActivity(intent);
            }
        });

        bs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get data from database and pass them to the activity
                Intent intent = new Intent(getActivity(), ProblemsActivity.class);
                intent.putExtra("problemType", "Building Services");
                startActivity(intent);
            }
        });

        /*problems = new ArrayList<>();
        problems.add(new Problem("water", "3otol1", "me1", "today1"));
        problems.add(new Problem("electricity", "3otol2", "me2", "today2"));
        problems.add(new Problem("bs", "3otol3", "me3", "today3"));
        problems.add(new Problem("bs", "3otol4", "me4", "today4"));
        problems.add(new Problem("water", "3otol5", "me5", "today5"));

        recyclerViewProblems = root.findViewById(R.id.problemsRecyclerView);
        reportProblemRVA = new ReportProblemRVA(problems);

        recyclerViewProblems.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerViewProblems.setAdapter(reportProblemRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewProblems.addItemDecoration(dividerItemDecoration);*/


        return root;
    }

}
