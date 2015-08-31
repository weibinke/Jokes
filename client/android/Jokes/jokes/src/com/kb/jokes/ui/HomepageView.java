package com.kb.jokes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kb.jokes.R;
import com.kb.platform.util.MLog;

public class HomepageView extends BaseFragment {
	private static final String TAG = "HomepageView";
	
	private View myFavorite;
	private View myThumbup;
	private View myComments;

	@Override
	public View onCreateView(LayoutInflater inflater,
			ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		if(rootView == null){
			rootView = inflater.inflate(R.layout.homepage, null);
			initView(rootView);
		}

		return rootView;
	}
	
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
	
	private void initView(View parent){
		MLog.i(TAG, "initView");
		myFavorite = parent.findViewById(R.id.homepage_myfavorite);
		myThumbup = parent.findViewById(R.id.homepage_mythumbup);
		myComments = parent.findViewById(R.id.homepage_mycomments);
		
		myFavorite.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomepageView.this.getActivity(),MyFavoritActivity.class);
				startActivity(intent);
			}
		});
		
		myThumbup.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomepageView.this.getActivity(),MyThumbupActivity.class);
				startActivity(intent);
			}
		});
		
		myComments.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(HomepageView.this.getActivity(),MyFavoritActivity.class);
				startActivity(intent);
			}
		});
	}
	
	@Override
	public int getTitleBarRightImage() {
		return R.drawable.settings_icon;
	}
	
	@Override
	public void onClickTitleBarRight() {
		Intent intent = new Intent(HomepageView.this.getActivity(),SettingsActivity.class);
		startActivity(intent);
	}
}
