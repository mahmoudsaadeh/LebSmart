package com.example.lebsmart.LostFoundAnnouncements;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.lebsmart.BestServiceProviders.PagerAdapterBSP;
import com.example.lebsmart.R;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class LostFoundAnnouncementFragment extends Fragment {

    ViewGroup root;

    TabLayout tabLayoutLFA;

    TabItem tabCheckLFA;
    TabItem tabAddLFA;

    ViewPager viewPagerLFA;

    PagerAdapter pagerAdapterLFA;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        root = (ViewGroup) inflater.inflate(R.layout.lost_found_announcements_fragment, container, false);

        tabLayoutLFA = root.findViewById(R.id.tabLayoutLFA);
        tabCheckLFA = root.findViewById(R.id.checkLFATab);
        tabAddLFA = root.findViewById(R.id.addLFATab);
        viewPagerLFA = root.findViewById(R.id.viewPagerLFA);

        pagerAdapterLFA = new PagerAdapterLFA(getChildFragmentManager(), tabLayoutLFA.getTabCount());

        viewPagerLFA.setAdapter(pagerAdapterLFA);

        tabLayoutLFA.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPagerLFA.setCurrentItem(tab.getPosition());
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
