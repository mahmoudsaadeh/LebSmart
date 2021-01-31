package com.example.lebsmart.EWSourcesFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.R;

public class EWSources extends Fragment {

    ViewGroup root;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.electricity_water_sources, container, false);

        // make a refresh in the page instead of buttons
        // sources should load directly once the fragment is selected from the drawer

        return root;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }


}
