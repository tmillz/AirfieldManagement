package com.tmillz.airfieldmanagement;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ViewPagerFragment extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.view_pager, container, false);
        ViewPager viewPager = view.findViewById(R.id.pager);
        viewPager.setAdapter(buildAdapter());

        TabLayout tabLayout = view.findViewById(R.id.tab_layout);
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		tabLayout.setTabMode(TabLayout.MODE_FIXED);
		tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private PagerAdapter buildAdapter() {
        return new ViewPagerAdapter(getChildFragmentManager());
    }
}