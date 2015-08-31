package com.kb.jokes.data.storage;

import android.content.Context;

import com.activeandroid.ActiveAndroid;

/**
 * DB对外接口
 * @author weibinke
 *
 */
public class DbService {
	private IDbAdapter dbAdapter;
	private ConfigStorage configStorage;
	
	public DbService(){
		
	}
	
	public boolean init(Context context){
		ActiveAndroid.initialize(context);
		dbAdapter = new DbAdpterImp();
		configStorage = new ConfigStorage(context);
		return true;
	}
	
	public void unInit(){
		
	}
	
	public IDbAdapter getDbAdapter(){
		return dbAdapter;
	}
	
	public ConfigStorage getConfigStorage(){
		return configStorage;
	}
}
