package com.example.lebsmart.BestServiceProviders;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;

public class AddBSPFragment extends Fragment {

    ViewGroup root;

    EditText otherET;

    RadioGroup spRadioGroup;
    RadioButton radioButtonElectrician;
    RadioButton radioButtonCarpenter;
    RadioButton radioButtonPainter;
    RadioButton radioButtonPlumber;
    RadioButton radioButtonBlacksmith;
    RadioButton radioButtonOther;

    LinearLayout addBspLL;
    RatingBar spRatingBar;
    Button addSPButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.add_bsp, container, false);

        otherET = root.findViewById(R.id.otherET);

        addBspLL = root.findViewById(R.id.addBspLL);
        spRatingBar = root.findViewById(R.id.spRatingBar);
        addSPButton = root.findViewById(R.id.addSPButton);

        radioButtonElectrician = root.findViewById(R.id.radioButtonElectrician);
        radioButtonCarpenter = root.findViewById(R.id.radioButtonCarpenter);
        radioButtonPainter = root.findViewById(R.id.radioButtonPainter);
        radioButtonPlumber = root.findViewById(R.id.radioButtonPlumber);
        radioButtonBlacksmith = root.findViewById(R.id.radioButtonBlacksmith);
        radioButtonOther = root.findViewById(R.id.radioButtonOther);

        spRadioGroup = root.findViewById(R.id.spRadioGroup);

        addBspLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        spRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        addSPButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });


        radioButtonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.VISIBLE);
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonElectrician.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.GONE);
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonCarpenter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.GONE);
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonPainter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.GONE);
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonPlumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.GONE);
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        radioButtonBlacksmith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.GONE);
                CommonMethods.hideSoftKeyboard(getActivity());
            }
        });

        return root;
    }



}
