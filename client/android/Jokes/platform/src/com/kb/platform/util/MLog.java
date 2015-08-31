package com.kb.platform.util;

import android.util.Log;

public class MLog {
	public static void i(final String tag,final String msg)
	{
		Log.i(tag,msg);
	}
	
	public static void e(final String tag,String msg)
	{
		Log.e(tag, msg);
	}
	
	public static void e(final String tag,String msg,Exception e)
	{
		e.printStackTrace();
		Log.e(tag, msg);
	}
	
	public static void e(final String tag,String msg,Error e)
	{
		e.printStackTrace();
		Log.e(tag, msg);
	}
	
	public static void d(final String tag,String msg)
	{
		Log.d(tag, msg);
	}
}
