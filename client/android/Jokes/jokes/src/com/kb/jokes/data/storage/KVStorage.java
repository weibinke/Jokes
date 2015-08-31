package com.kb.jokes.data.storage;

import android.content.Context;
import android.content.SharedPreferences;

public abstract class KVStorage {
	private SharedPreferences sp;
	
	public KVStorage(Context context,String name) {
		sp = context.getSharedPreferences(name, Context.MODE_WORLD_READABLE | Context.MODE_WORLD_WRITEABLE);
	}
	
	public void putString(String key,String value){
		sp.edit().putString(key, value).commit();
	}
	
	public String getString(String key,String defValue){
		return sp.getString(key, defValue);
	}
	
	public void putInt(String key,int value){
		sp.edit().putInt(key, value).commit();
	}
	
	public int getInt(String key,int defValue){
		return sp.getInt(key, defValue);
	}
	
	public void putLong(String key,int value){
		sp.edit().putLong(key, value).commit();
	}
	
	public long getLong(String key,long defValue){
		return sp.getLong(key, defValue);
	}
	
	public void putBoolean(String key,boolean value){
		sp.edit().putBoolean(key, value).commit();
	}
	
	public boolean getBoolean(String key,boolean defValue){
		return sp.getBoolean(key, defValue);
	}
}
