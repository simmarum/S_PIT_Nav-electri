package com.example.nav.nav_electri;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                TabNavigation tabNavigation = new TabNavigation();
                return tabNavigation;
            case 1:
                TabStations tabStations = new TabStations();
                return tabStations;
            case 2:
                TabAttractions tabAttractions = new TabAttractions();
                return tabAttractions;
            case 3:
                TabSettings tabSettings = new TabSettings();
                return tabSettings;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}