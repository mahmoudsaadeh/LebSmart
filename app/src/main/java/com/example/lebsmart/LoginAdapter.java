package com.example.lebsmart;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

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
