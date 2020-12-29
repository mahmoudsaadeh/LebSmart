package com.example.lebsmart.ReportProblemsFragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lebsmart.MeetingsFragments.CheckMeetingsFragment;
import com.example.lebsmart.MeetingsFragments.ScheduleMeetingFragment;

public class PagerAdapterProblems extends FragmentPagerAdapter {

    private int numOfTabs;

    public PagerAdapterProblems(@NonNull FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ReportProblemFragment();
            case 1:
                return new CheckProblemsFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}
