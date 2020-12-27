package com.example.lebsmart.ApartmentsFragments;

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

public class ApartmentsFragment extends Fragment {

    ViewGroup root;

    TabLayout tabLayout;

    TabItem tabCheck;
    TabItem tabAdd;

    ViewPager viewPager;

    PagerAdapter pagerAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.apartments_fragment, container, false);

        tabLayout = root.findViewById(R.id.tabLayoutApartments);
        tabCheck = root.findViewById(R.id.checkApartments);
        tabAdd = root.findViewById(R.id.addApartments);
        viewPager = root.findViewById(R.id.viewPagerApartments);

        pagerAdapter = new com.example.lebsmart.others.PagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());

        viewPager.setAdapter(pagerAdapter);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
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
