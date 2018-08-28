package com.tmillz.airfieldmanagement;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

class ViewPagerAdapter extends FragmentStatePagerAdapter {
	SparseArray<Fragment> registeredFragments = new SparseArray<>();

    private final int PAGES = 2;

    private String[] tabTitles = new String[]{"Map", "Markers"};


    ViewPagerAdapter(Context context, FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public int getCount() {
        return PAGES;
    }

    @Override
    public Fragment getItem(int position) {
    	switch (position) {
    	case 0:
    		return new Maps();
        case 1:
            return new MarkersList();
        default:
            throw new IllegalArgumentException("The item position should be less or equal to:" + PAGES);
    	}
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment fragment = (Fragment) super.instantiateItem(container, position);
        registeredFragments.put(position, fragment);
        return fragment;
    }
}