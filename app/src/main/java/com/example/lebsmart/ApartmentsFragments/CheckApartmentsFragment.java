package com.example.lebsmart.ApartmentsFragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lebsmart.R;

import java.util.ArrayList;
import java.util.List;


public class CheckApartmentsFragment extends Fragment {

    RecyclerView recyclerView;
    ApartmentsRecyclerViewAdapter apartmentsRecyclerViewAdapter;

    ViewGroup root;

    List<Apartment> list;


    public CheckApartmentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.fragment_check_apartments, container, false);

        list = new ArrayList<>();
        list.add(new Apartment("rent", "500$", "100m^2", "Zohoor", "78421354", "2nd"));
        list.add(new Apartment("sale", "700$", "200m^2", "bbA", "26626267", "3rd"));
        list.add(new Apartment("rent", "900$", "400m^2", "kkk", "784212424", "7th"));
        list.add(new Apartment("sale", "200$", "300m^2", "qwer", "78741258", "1st"));

        recyclerView = root.findViewById(R.id.recyclerView);
        apartmentsRecyclerViewAdapter = new ApartmentsRecyclerViewAdapter(list);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerView.setAdapter(apartmentsRecyclerViewAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Inflate the layout for this fragment
        return root;
    }


}