package com.example.lebsmart.MeetingsFragments;

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

public class MeetingsFragment extends Fragment {

    ViewGroup root;

    TabLayout tabLayoutMeetings;

    TabItem tabCheck;
    TabItem tabSchedule;

    ViewPager viewPagerMeetings;

    PagerAdapter pagerAdapterMeetings;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.meetings_fragment, container, false);

        tabLayoutMeetings = root.findViewById(R.id.tabLayoutMeetings);
        tabCheck = root.findViewById(R.id.checkMeetingsTab);
        tabSchedule = root.findViewById(R.id.scheduleMeetingTab);
        viewPagerMeetings = root.findViewById(R.id.viewPagerMeetings);

        pagerAdapterMeetings = new PagerAdapterMeetings(getChildFragmentManager(), tabLayoutMeetings.getTabCount());

        viewPagerMeetings.setAdapter(pagerAdapterMeetings);

        tabLayoutMeetings.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerMeetings.setCurrentItem(tab.getPosition());
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
