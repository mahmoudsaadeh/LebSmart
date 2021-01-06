package com.example.lebsmart.BestServiceProviders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.R;

public class AddBSPFragment extends Fragment implements View.OnClickListener {

    ViewGroup root;

    EditText otherET;

    RadioGroup spRadioGroup;
    RadioButton radioButtonElectrician;
    RadioButton radioButtonCarpenter;
    RadioButton radioButtonPainter;
    RadioButton radioButtonPlumber;
    RadioButton radioButtonBlacksmith;
    RadioButton radioButtonOther;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.add_bsp, container, false);

        otherET = root.findViewById(R.id.otherET);

        radioButtonElectrician = root.findViewById(R.id.radioButtonElectrician);
        radioButtonCarpenter = root.findViewById(R.id.radioButtonCarpenter);
        radioButtonPainter = root.findViewById(R.id.radioButtonPainter);
        radioButtonPlumber = root.findViewById(R.id.radioButtonPlumber);
        radioButtonBlacksmith = root.findViewById(R.id.radioButtonBlacksmith);
        radioButtonOther = root.findViewById(R.id.radioButtonOther);

        spRadioGroup = root.findViewById(R.id.spRadioGroup);

        /*radioButtonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                otherET.setVisibility(View.VISIBLE);
            }
        });*/

        /*if (spRadioGroup.getCheckedRadioButtonId() == other.getId()) {
            otherET.setVisibility(View.VISIBLE);
        }
        else {
            otherET.setVisibility(View.GONE);
        }*/

        return root;
    }


    @Override
    public void onClick(View v) {
        /*int checkedRB = spRadioGroup.getCheckedRadioButtonId();
        if (v.getId() == spRadioGroup.getCheckedRadioButtonId()) {
            otherET.setVisibility(View.VISIBLE);
        }
        if (v.getId() != spRadioGroup.getCheckedRadioButtonId()) {
            otherET.setVisibility(View.GONE);
        }*/
    }


}
