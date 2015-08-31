package com.kb.jokes.ui;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.kb.jokes.R;
import com.viewpagerindicator.IconPagerAdapter;

public class MainTabAdapter extends FragmentPagerAdapter implements IconPagerAdapter{
	private String[] CONTENT;
	private BaseFragment[] fragments;
	private static final int[] ICONS = new int[] {
        R.drawable.perm_group_calendar,
        R.drawable.perm_group_camera,
        R.drawable.perm_group_device_alarms,
        R.drawable.perm_group_location,
	};
	
	public MainTabAdapter(Context context,BaseFragment[] fragments,FragmentManager fm){
		super(fm);
		CONTENT = new String[]{
				context.getString(R.string.tab_title_pic),
				context.getString(R.string.tab_title_nopic),
				context.getString(R.string.tab_title_news),
				context.getString(R.string.tab_title_homepage),
				
		};
		
		this.fragments = fragments;
	}

	@Override
    public Fragment getItem(int position) {
		return fragments[position % CONTENT.length];
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return CONTENT[position % CONTENT.length].toUpperCase();
    }

    @Override public int getIconResId(int index) {
      return ICONS[index];
    }

  @Override
    public int getCount() {
      return CONTENT.length;
    }
}
