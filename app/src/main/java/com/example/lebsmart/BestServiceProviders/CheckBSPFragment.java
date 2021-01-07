package com.example.lebsmart.BestServiceProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.lebsmart.Activities.BSPsActivity;
import com.example.lebsmart.R;

public class CheckBSPFragment extends Fragment {

    ViewGroup root;

    CardView electriciansCardView;
    CardView paintersCardView;
    CardView carpentersCardView;
    CardView blacksmithsCardView;
    CardView plumbersCardView;
    CardView othersCardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.check_bsp, container, false);

        electriciansCardView = root.findViewById(R.id.electriciansCardView);
        paintersCardView = root.findViewById(R.id.paintersCardView);
        carpentersCardView = root.findViewById(R.id.carpentersCardView);
        blacksmithsCardView = root.findViewById(R.id.blacksmithsCardView);
        plumbersCardView = root.findViewById(R.id.plumbersCardView);
        othersCardView = root.findViewById(R.id.othersCardView);

        electriciansCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BSPsActivity.class);
                intent.putExtra("spCategory", "Electricians");
                startActivity(intent);
            }
        });

        paintersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BSPsActivity.class);
                intent.putExtra("spCategory", "Painters");
                startActivity(intent);
            }
        });

        carpentersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BSPsActivity.class);
                intent.putExtra("spCategory", "Carpenters");
                startActivity(intent);
            }
        });

        blacksmithsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BSPsActivity.class);
                intent.putExtra("spCategory", "Blacksmiths");
                startActivity(intent);
            }
        });

        plumbersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BSPsActivity.class);
                intent.putExtra("spCategory", "Plumbers");
                startActivity(intent);
            }
        });

        othersCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), BSPsActivity.class);
                intent.putExtra("spCategory", "Others");
                startActivity(intent);
            }
        });

        return root;
    }


}
