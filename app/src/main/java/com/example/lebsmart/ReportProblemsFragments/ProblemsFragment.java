package com.example.lebsmart.ReportProblemsFragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.lebsmart.MeetingsFragments.PagerAdapterMeetings;
import com.example.lebsmart.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class ProblemsFragment extends Fragment {

    ViewGroup root;

    TabLayout tabLayoutProblems;

    TabItem tabCheck;
    TabItem tabReport;

    ViewPager viewPagerProblems;

    PagerAdapter pagerAdapterProblems;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.problems_fragment, container, false);

        tabLayoutProblems = root.findViewById(R.id.tabLayoutProblems);
        tabCheck = root.findViewById(R.id.checkProblemsTab);
        tabReport = root.findViewById(R.id.reportProblemTab);
        viewPagerProblems = root.findViewById(R.id.viewPagerProblems);

        pagerAdapterProblems = new PagerAdapterProblems(getChildFragmentManager(), tabLayoutProblems.getTabCount());

        viewPagerProblems.setAdapter(pagerAdapterProblems);

        tabLayoutProblems.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerProblems.setCurrentItem(tab.getPosition());
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
