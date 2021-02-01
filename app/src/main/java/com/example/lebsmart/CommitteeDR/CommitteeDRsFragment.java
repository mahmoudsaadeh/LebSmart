package com.example.lebsmart.CommitteeDR;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.lebsmart.ApartmentsFragments.ApartmentsFragment;
import com.example.lebsmart.ApartmentsFragments.CheckApartmentsFragment;
import com.example.lebsmart.R;
import com.example.lebsmart.RandomFragments.CommonMethods;
import com.example.lebsmart.TheftsFragments.Thefts;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class CommitteeDRsFragment extends Fragment {

    ViewGroup root;

    TabLayout tabLayoutCommittee;

    TabItem tabCheckCommittee;
    TabItem tabAddCommittee;

    ViewPager viewPagerCommittee;

    PagerAdapter pagerAdapterCommittee;

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
        Log.i("test", "5");
        root = (ViewGroup) layoutInflater.inflate(R.layout.committee_dr_fragment, containerVG, false);

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
    }

    public void setFragmentNotCommittee () {
        Log.i("test", "6");
        //root = (ViewGroup) layoutInflater.inflate(R.layout.check_committee_dr, containerVG, false);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container,
                new CheckCommitteeDRFragment()).commit();
    }

}
