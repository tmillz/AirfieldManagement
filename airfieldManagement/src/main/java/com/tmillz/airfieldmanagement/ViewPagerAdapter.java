package com.tmillz.airfieldmanagement;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;
 
public class ViewPagerAdapter extends FragmentPagerAdapter {
	SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

    private final int PAGES = 3;
 
    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public int getCount() {
        return PAGES;
    }

    @Override
    public Fragment getItem(int position) {
    	switch (position) {
    	case 0:
    		return Maps.newInstance();
        case 1:
            return EditDisc.newInstance();
        case 2:
            return Disc.newInstance();
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

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        registeredFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    public Fragment getRegisteredFragment(int position) {
        return registeredFragments.get(position);
    }
}