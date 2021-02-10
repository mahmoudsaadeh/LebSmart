package com.example.lebsmart.MeetingsFragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
import com.example.lebsmart.CommitteeDR.CheckCommitteeDRFragment;
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

    LayoutInflater layoutInflater;
    ViewGroup containerVG;

    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        layoutInflater = inflater;
        containerVG = container;

        progressDialog = new ProgressDialog(getActivity());

        if (CheckApartmentsFragment.checkIfCM) {
            setFragmentCommittee();
        }
        else {
            setFragmentNotCommittee();
        }

        return root;
    }

    public void setFragmentCommittee () {
        root = (ViewGroup) layoutInflater.inflate(R.layout.meetings_fragment, containerVG, false);

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
    }

    public void setFragmentNotCommittee () {
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,
                        new CheckMeetingsFragment()).commit();
    }

}
