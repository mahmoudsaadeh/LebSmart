package com.example.lebsmart.TheftsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.lebsmart.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class TheftFragment extends Fragment {

    ViewGroup root;

    TabLayout tabLayoutThefts;

    TabItem tabCheck;
    TabItem tabReport;

    ViewPager viewPagerThefts;

    PagerAdapter pagerAdapter;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.thefts_fragment, container, false);

        tabLayoutThefts = root.findViewById(R.id.tabLayoutTheft);
        tabCheck = root.findViewById(R.id.checkThefts);
        tabReport = root.findViewById(R.id.reportTheft);
        viewPagerThefts = root.findViewById(R.id.viewPagerThefts);

        pagerAdapter = new PagerAdapterThefts(getChildFragmentManager(), tabLayoutThefts.getTabCount());

        viewPagerThefts.setAdapter(pagerAdapter);

        tabLayoutThefts.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerThefts.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return root;
        //return super.onCreateView(inflater, container, savedInstanceState);
    }
}
