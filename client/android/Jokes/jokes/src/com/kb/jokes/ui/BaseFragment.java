package com.kb.jokes.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kb.jokes.R;
import com.kb.platform.util.MLog;

public class BaseFragment extends Fragment{
	private static final String TAG = "BaseFragment";
	
	protected View rootView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		MLog.i(TAG, "onCreate class=" + getClass().getName());
		super.onCreate(savedInstanceState);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		MLog.i(TAG, "onCreateView class=" + getClass().getName());
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onDestroyView() {
		MLog.i(TAG, "onDestroyView class=" + getClass().getName());
		if(rootView != null && rootView.getParent() != null){
			((ViewGroup)rootView.getParent()).removeView(rootView);
		}
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		MLog.i(TAG, "onDestroy class=" + getClass().getName());
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		MLog.i(TAG, "onResume class=" + getClass().getName());
		super.onResume();
	}
	
	@Override
	public void onPause() {
		MLog.i(TAG, "onPause class=" + getClass().getName());
		super.onPause();
	}
	
	protected View findViewById(int id){
		return getView().findViewById(id);
	}
	
	public void onClickTitleBarRight(){
		MLog.i(TAG, "onClickTitleBarRefresh class=" + getClass().getName());
	}
	
	public int getTitleBarRightImage(){
		return R.drawable.titlebar_refresh;
	}
}
