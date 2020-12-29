package com.example.lebsmart.TheftsFragments;

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

import com.example.lebsmart.R;

import java.util.ArrayList;
import java.util.List;

public class CheckTheftsFragment extends Fragment {

    RecyclerView recyclerViewThefts;
    TheftsRVA theftsRVA;

    ViewGroup root;

    List<Thefts> thefts;

    public CheckTheftsFragment () {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.check_theft_fragment, container, false);

        // expandable recyclerview as in apartments fragment

        thefts = new ArrayList<>();
        thefts.add(new Thefts("hadiz", "car stolen", "friday august 7th 2020", "11 : 15", "2nd car stolen in the area, one of them is mine; take care!!", "3a2isha bakkar, beirut, lebanon"));
        thefts.add(new Thefts("khaled", "money stolen", "friday august 7th 2020", "11 : 15", "2nd car stolen in the area, one of them is mine; take care!!", "3a2isha bakkar, beirut, lebanon"));
        thefts.add(new Thefts("ramzi", "house stolen", "friday august 7th 2020", "11 : 15", "2nd car stolen in the area, one of them is mine; take care!!", "3a2isha bakkar, beirut, lebanon"));
        thefts.add(new Thefts("hamdi", "motorcycle stolen", "friday august 7th 2020", "11 : 15", "2nd car stolen in the area, one of them is mine; take care!!", "3a2isha bakkar, beirut, lebanon"));
        thefts.add(new Thefts("rola", "bike stolen", "friday august 7th 2020", "11 : 15", "2nd car stolen in the area, one of them is mine; take care!!", "3a2isha bakkar, beirut, lebanon"));

        recyclerViewThefts = root.findViewById(R.id.theftsRecyclerView);
        theftsRVA = new TheftsRVA(thefts);

        recyclerViewThefts.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerViewThefts.setAdapter(theftsRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewThefts.addItemDecoration(dividerItemDecoration);

        return root;

    }
}
