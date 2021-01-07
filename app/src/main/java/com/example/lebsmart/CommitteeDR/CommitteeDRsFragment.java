package com.example.lebsmart.CommitteeDR;

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

public class CommitteeDRsFragment extends Fragment {

    ViewGroup root;

    TabLayout tabLayoutCommittee;

    TabItem tabCheckCommittee;
    TabItem tabAddCommittee;

    ViewPager viewPagerCommittee;

    PagerAdapter pagerAdapterCommittee;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.committee_dr_fragment, container, false);


        tabLayoutCommittee = root.findViewById(R.id.tabLayoutCommittee);
        tabCheckCommittee = root.findViewById(R.id.checkCommitteeTab);
        tabAddCommittee = root.findViewById(R.id.addCommitteeTab);
        viewPagerCommittee = root.findViewById(R.id.viewPagerCommittee);

        pagerAdapterCommittee = new PagerAdapterCommittee(getChildFragmentManager(), tabLayoutCommittee.getTabCount());

        viewPagerCommittee.setAdapter(pagerAdapterCommittee);

        tabLayoutCommittee.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerCommittee.setCurrentItem(tab.getPosition());
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
