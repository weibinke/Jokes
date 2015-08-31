package com.kb.jokes.business.core;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.kb.platform.util.MLog;

public class FileConfig {
	private static final String PATH_NAME_ROOT = "joke";
	private static final String PATH_NAME_CACHE = "cache";
	private static final String PATH_NAME_SHARE = "share";
	private static final String PATH_NAME_SAVE = "save";
	private static final String TAG = "FileConfig";
	private static String APP_DATA_ROOT;

	public static void initFilePath(Context context){
		try {
			APP_DATA_ROOT = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).applicationInfo.dataDir;
			new File(getRootPath()).mkdirs();
			new File(getCachePath()).mkdirs();
			new File(getSharePath()).mkdir();
			new File(getSavePath()).mkdir();
		} catch (Exception e) {
			MLog.e(TAG,"initFilePath failed.",e);
		}
	}
	
	public static String getRootPath(){
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			return Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + PATH_NAME_ROOT + "/";
		}
		
		return APP_DATA_ROOT + "/" + PATH_NAME_ROOT + "/";
	}
	public static String getCachePath(){
		return getRootPath() + PATH_NAME_CACHE + "/";
	}
	
	public static String getSharePath(){
		return getRootPath() + PATH_NAME_SHARE + "/";
	}
	
	public static String getSavePath(){
		return getRootPath() + PATH_NAME_SAVE+ "/";
	}
}
