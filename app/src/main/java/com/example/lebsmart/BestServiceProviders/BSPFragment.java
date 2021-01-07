package com.example.lebsmart.BestServiceProviders;

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
import com.example.lebsmart.ReportProblemsFragments.PagerAdapterProblems;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class BSPFragment extends Fragment {

    ViewGroup root;

    TabLayout tabLayoutBSP;

    TabItem tabCheck;
    TabItem tabAdd;

    ViewPager viewPagerBSP;

    PagerAdapter pagerAdapterBSP;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.bsp_fragment, container, false);


        tabLayoutBSP = root.findViewById(R.id.tabLayoutBSP);
        tabCheck = root.findViewById(R.id.checkSPTab);
        tabAdd = root.findViewById(R.id.addSPTab);
        viewPagerBSP = root.findViewById(R.id.viewPagerBSP);

        pagerAdapterBSP = new PagerAdapterBSP(getChildFragmentManager(), tabLayoutBSP.getTabCount());

        viewPagerBSP.setAdapter(pagerAdapterBSP);

        tabLayoutBSP.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerBSP.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        return root;
    }
}
