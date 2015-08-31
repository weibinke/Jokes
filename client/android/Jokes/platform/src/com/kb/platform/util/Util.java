package com.kb.platform.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class Util {
	public static boolean isNullOrNil(String value){
		return value == null || value.length() == 0;
	}
	
	public static long currentTick(){
		return System.currentTimeMillis();
	}
	
	public static long tickToNow(long start){
		return currentTick() - start;
	}
	
	public static String getDateCurrentTimeZone(long timestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd hh:mm");
		String dateString = formatter.format(new Date(timestamp));
        return dateString;
    }
	
	public static void jumpToMarket(Context context){
		Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=" + context.getPackageName()));
		context.startActivity(intent);
	}
}
