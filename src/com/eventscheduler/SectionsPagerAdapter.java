package com.eventscheduler;

import java.util.Locale;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

public class SectionsPagerAdapter extends FragmentPagerAdapter {

	static final String DEBUG_TAG = "SectionsPagerAdapter";
	GeneralTabFragment genTabFragment;
	ToggleTabFragment togTabFragment;
	Context context ;
	public SectionsPagerAdapter(FragmentManager fm, Context c) {
		super(fm);
		context = c;
		genTabFragment = new GeneralTabFragment();
		togTabFragment = new ToggleTabFragment();
	}

	@Override
	public Fragment getItem(int position) {
		//Log.d(DEBUG_TAG, "getItem()");
		switch (position) {
		case 0:
			return genTabFragment;
		case 1:
			
			return togTabFragment;
		default:
			break;
		}
		return null;

	}

	@Override
	public int getCount() {
		return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		//Log.d(DEBUG_TAG, "getPageTitle()");
		Locale l = Locale.getDefault();
		switch (position) {
		case 0:
			
			return context.getString(R.string.general_tab).toUpperCase(l);
		case 1:
			return context.getString(R.string.toggle_tab).toUpperCase(l);
		}
		return null;
	}
}
