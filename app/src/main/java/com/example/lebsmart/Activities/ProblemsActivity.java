package com.example.lebsmart.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lebsmart.R;
import com.example.lebsmart.ReportProblemsFragments.Problem;
import com.example.lebsmart.ReportProblemsFragments.ReportProblemRVA;

import java.util.ArrayList;
import java.util.List;

public class ProblemsActivity extends AppCompatActivity {

    RecyclerView recyclerViewProblems;
    ReportProblemRVA reportProblemRVA;

    TextView problemTV;

    List<Problem> problems;

    String problemType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_problems);

        problemTV = findViewById(R.id.reportedPrblmTV);

        problemType = "";
        Intent intent = getIntent();
        problemType = intent.getStringExtra("problemType");

        problemTV.setText(problemType + " Reported Problems:");

        problems = new ArrayList<>();
        // get info from db and fill them here
        // should check the type of problems clicked, and get the ones with the associated type only
        problems.add(new Problem("water", "3otol1", "me1", "today1"));
        problems.add(new Problem("electricity", "3otol2", "me2", "today2"));
        problems.add(new Problem("bs", "3otol3", "me3", "today3"));
        problems.add(new Problem("bs", "3otol4", "me4", "today4"));
        problems.add(new Problem("water", "3otol5", "me5", "today5"));

        recyclerViewProblems = findViewById(R.id.problemsRecyclerView);
        reportProblemRVA = new ReportProblemRVA(problems);

        recyclerViewProblems.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewProblems.setAdapter(reportProblemRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerViewProblems.addItemDecoration(dividerItemDecoration);

    }
}