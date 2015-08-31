package com.kb.jokes.business.core;

import android.app.Application;
import android.content.res.Configuration;

import com.kb.platform.util.MLog;

public class JokesApplication extends Application{

	private static final String TAG = "JokesApplication";

	@Override
	public void onCreate() {
		super.onCreate();
		MLog.i(TAG, "onCreate");
		
		AppCore.getInstance().init(this);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		MLog.i(TAG, "onConfigurationChanged");
	}
	
	@Override
	public void onTrimMemory(int level) {
		super.onTrimMemory(level);
		MLog.i(TAG, "onTrimMemory level=" + level);
	}
	
	@Override
	public void onLowMemory() {
		super.onLowMemory();
		MLog.i(TAG, "onLowMemory");
	}
	
	@Override
	public void onTerminate() {
		super.onTerminate();
		MLog.i(TAG, "onTerminate");
	}
}
