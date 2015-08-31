package com.kb.jokes.data.protocol;

import com.kb.platform.util.Util;

public class CGIConfig {
	private final static String HOST_BASE = "http://www.codeshome.com:9001/jokes/";

	public static final long CLIENT_VERSION = 0x01000000;
	public static final String MODEL = android.os.Build.MODEL;
	public static final String OS = android.os.Build.VERSION.RELEASE;
	public static final String PLATFORM = "Android";
	
	// return code define
	public static final int RET_OK=0;
	public static final int RET_ERROR=-1;
	public static final int RET_INVAILED_ARG=-2;
	public static final int RET_JOKE_ID_NOTFOUND=-3;
	public static final int RET_FORBIDDEN=-4;
	
	private static String cgi_getjokes_list;
	public static String getJokesListCgiUrl(){
		if(Util.isNullOrNil(cgi_getjokes_list)){
			cgi_getjokes_list = HOST_BASE + "get_jokes_list";
		}
		
		return cgi_getjokes_list;
	}
	
	private static String cgi_thumbup;
	public static String getThumbUpCgiUrl(){
		if(Util.isNullOrNil(cgi_thumbup)){
			cgi_thumbup = HOST_BASE + "thumbup";
		}
		
		return cgi_thumbup;
	}
	
	private static String cgi_addfavorite;
	public static String getAddFavoriteCgiUrl(){
		if(Util.isNullOrNil(cgi_addfavorite)){
			cgi_addfavorite = HOST_BASE + "like";
		}
		
		return cgi_addfavorite;
	}
	
	private static String cgi_addcomment;
	public static String getAddCommentCgiUrl(){
		if(Util.isNullOrNil(cgi_addcomment)){
			cgi_addcomment = HOST_BASE + "comment";
		}
		
		return cgi_addcomment;
	}
	
	private static String cgi_getcomment;
	public static String getGetCommentCgiUrl(){
		if(Util.isNullOrNil(cgi_getcomment)){
			cgi_getcomment = HOST_BASE + "get_comments";
		}
		
		return cgi_getcomment;
	}
	
	private static String cgi_getjokedetail;
	public static String getGetJokeDetailCgiUrl(){
		if(Util.isNullOrNil(cgi_getjokedetail)){
			cgi_getjokedetail = HOST_BASE + "joke";
		}
		
		return cgi_getjokedetail;
	}
}
