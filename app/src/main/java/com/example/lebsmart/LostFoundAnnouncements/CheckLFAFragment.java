package com.example.lebsmart.LostFoundAnnouncements;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lebsmart.MeetingsFragments.MeetingRVA;
import com.example.lebsmart.MeetingsFragments.Meetings;
import com.example.lebsmart.R;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckLFAFragment extends Fragment {

    RecyclerView recyclerViewLFA;
    LFAsRVA lfaRVA;

    ViewGroup root;

    List<LFA> lfas;

    String currentDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        root = (ViewGroup) inflater.inflate(R.layout.check_lfa_fragment, container, false);

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        //System.out.println(formatter.format(date));
        currentDate = formatter.format(date);

        lfas = new ArrayList<>();
        lfas.add(new LFA("Found keys on stairs", "" + currentDate, "found 4 keys with tennis portocle", "get current user(the user that added the ann)"));
        lfas.add(new LFA("Found money on stairs", "" + currentDate, "found 4 keys with tennis portocle", "get current user(the user that added the ann)"));
        lfas.add(new LFA("Found bag in garage", "" + currentDate, "found 4 keys with tennis portocle", "get current user(the user that added the ann)"));
        lfas.add(new LFA("Found ID card in eleveator", "" + currentDate, "found 4 keys with tennis portocle", "get current user(the user that added the ann)"));
        lfas.add(new LFA("Found car keys in parking", "" + currentDate, "found 4 keys with tennis portocle", "get current user(the user that added the ann)"));

        recyclerViewLFA = root.findViewById(R.id.lfaRecyclerView);
        lfaRVA = new LFAsRVA(lfas);

        recyclerViewLFA.setLayoutManager(new LinearLayoutManager(this.getContext()));

        recyclerViewLFA.setAdapter(lfaRVA);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this.getContext(), DividerItemDecoration.VERTICAL);
        recyclerViewLFA.addItemDecoration(dividerItemDecoration);

        return root;
    }
}
