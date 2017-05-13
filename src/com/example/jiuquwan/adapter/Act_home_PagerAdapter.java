package com.example.jiuquwan.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class Act_home_PagerAdapter extends FragmentPagerAdapter {
	//数据源就是Fragment的集合
	private List<Fragment> frags;
	
	public Act_home_PagerAdapter(FragmentManager fm,List<Fragment> frags) {
		super(fm);
		this.frags=frags;
	}

	@Override
	public Fragment getItem(int position) {
		// TODO Auto-generated method stub
		return frags.get(position);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return frags.size();
	}

}
