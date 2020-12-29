package com.example.lebsmart.Others;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.lebsmart.RandomFragments.LoginTabFragment;
import com.example.lebsmart.RandomFragments.SignUpTabFragment;

public class LoginAdapter extends FragmentPagerAdapter {

    int totalTabs;

    public LoginAdapter(FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }



    @NonNull
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                //Log.i("page", "0");
                return new LoginTabFragment();
            case 1:
                //Log.i("page", "1");
                return new SignUpTabFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        //Log.i("totaltabs", "" + totalTabs);
        return totalTabs;
    }

}
