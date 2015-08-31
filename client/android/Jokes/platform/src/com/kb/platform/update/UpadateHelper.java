package com.kb.platform.update;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Handler;
import android.os.Message;

import com.kb.platform.R;
import com.kb.platform.util.MLog;

public class UpadateHelper {
	
	private static final String CHECKUPDATE_URL = "http://mobileplatform.codeshome.com/platform/checkupdate.php";
	private static final String CHECKADCONFIG_URL = "http://mobileplatform.codeshome.com/platform/checkadconfig.php";
	protected static final String TAG = "UpadateHelper";
	
	public static class UpdateResult{
		public int ret = 0;
		public String errmsg = null;
		public boolean hasUpdate = false;
		public boolean isForce = false;
		public int newVersionCode = -1;
		public String newVersionName = null;
		public String versionDesc = null;
		public String downloadUrl = null;
	}
	
	public static class CheckAdConfigResult{
		public int ret = 0;
		public String errmsg = null;
		public boolean canShowYoumiOfferAd = false;			//是否能显示有米积分墙广告
	}
	
	public static interface UpdateCallback{
		abstract void onCheckUpdateResult(UpdateResult result);
	}
	
	public static interface CheckConfigCallback{
		void onCallback(CheckAdConfigResult result);
	}
	/**
	 * 检查升级
	 * @param context
	 */
	public static void checkNewVersion(final Context context,final String appId,final String channelid,final UpdateCallback callback){
		final Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 100) {
					UpdateResult updateResult = (UpdateResult)msg.obj;
					callback.onCheckUpdateResult(updateResult);
				}
			};
		};
		
		new Thread(){
			public void run() {
				String requestUrl = getChecknewUrl(context, appId,channelid);
				MLog.i(TAG,"requestUrl = " + requestUrl);
				URL url = null;
				InputStream is = null;
				
				UpdateResult updateResult = new UpdateResult();
				
				try {
					url = new URL(requestUrl);

					is = (InputStream)url.getContent();
					if (is == null) {
						MLog.e(TAG,"checkNewVersion url.getContent() == null");
					}else {
						ByteArrayOutputStream baos = new ByteArrayOutputStream();
						byte bytes[] = new byte[1024];		
						int len = -1;
						while(true){
							if((len = is.read(bytes)) != -1){
								baos.write(bytes,0,len);
							}else {
								break;
							}
						}
						
						String result = new String(baos.toByteArray());
						JSONObject jsonObject;
						try {
							jsonObject = new JSONObject(result);
							updateResult.ret = jsonObject.getInt("ret");
							updateResult.errmsg = jsonObject.getString("errmsg");
							updateResult.hasUpdate = jsonObject.getString("new_version").equalsIgnoreCase("TRUE") ? true : false;
							if (updateResult.hasUpdate) {
							updateResult.isForce = jsonObject.getString("force_update").equalsIgnoreCase("TRUE") ? true : false;
							updateResult.newVersionCode = jsonObject.getInt("new_ver_code");
							updateResult.newVersionName = jsonObject.getString("new_ver_name");
							updateResult.versionDesc = jsonObject.getString("desc");
							updateResult.downloadUrl = jsonObject.getString("dl_url");						
							}
							
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

				} catch (IOException e) {
					e.printStackTrace();
				}finally{
					if (is != null) {
						try {
							is.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				
				
				Message message = handler.obtainMessage(100, updateResult);
				handler.sendMessage(message);
				
			};
		}.start();
	}
	
	public static void checkAdConfigVersion(final Context context,final String appId,final String channelid,final CheckConfigCallback callback){
		final Handler handler = new Handler(){
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 101) {
					CheckAdConfigResult result = (CheckAdConfigResult)msg.obj;
					callback.onCallback(result);
				}
			};
		};
		
		new Thread(){
			public void run() {
		String requestUrl = getCheckAdConfigUrl(context, appId,channelid);
		MLog.i(TAG,"requestUrl = " + requestUrl);
		URL url = null;
		InputStream is = null;
		
		CheckAdConfigResult configResult = new CheckAdConfigResult();
		
		try {
			url = new URL(requestUrl);

			is = (InputStream)url.getContent();
			if (is == null) {
				MLog.e(TAG,"checkNewVersion url.getContent() == null");
			}else {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				byte bytes[] = new byte[1024];		
				int len = -1;
				while(true){
					if((len = is.read(bytes)) != -1){
						baos.write(bytes,0,len);
					}else {
						break;
					}
				}
				
				String result = new String(baos.toByteArray());
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					configResult.ret = jsonObject.getInt("ret");
					configResult.errmsg = jsonObject.getString("errmsg");
					configResult.canShowYoumiOfferAd = jsonObject.getString("canShowYoumiOfferAd").equalsIgnoreCase("TRUE") ? true : false;					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		
		Message message = handler.obtainMessage(101, configResult);
		handler.sendMessage(message);
		
	};
}.start();
	}
	
	public static String getChecknewUrl(final Context context,final String appId,final String channelid){
		PackageManager  pm = context.getPackageManager();
		PackageInfo packageInfo = null;
		String url = null;
		try {
			packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			
			url = CHECKUPDATE_URL + "?ver_name=" 
				+ versionName 
				+ "&ver_code="
				+ versionCode 
				+ "&uid="
				+ UIDFactory.getUID()
				+ "&channelid="
				+ channelid
				+ "&appid="
				+ appId
				+ "&adscore="
				+ 0
				+ "&device="
				+ android.os.Build.MODEL
				+ "&os="
				+ android.os.Build.VERSION.SDK;
		} catch (NameNotFoundException e) {
			MLog.e(TAG,"getChecknewUrl",e);
		}

		return url;
	}
	
	public static String getCheckAdConfigUrl(final Context context,final String appId,final String channelid){
		PackageManager  pm = context.getPackageManager();
		PackageInfo packageInfo = null;
		String url = null;
		try {
			packageInfo = pm.getPackageInfo(context.getPackageName(), 0);
			
			int versionCode = packageInfo.versionCode;
			String versionName = packageInfo.versionName;
			
			url = CHECKADCONFIG_URL + "?ver_name=" 
				+ versionName 
				+ "&ver_code="
				+ versionCode 
				+ "&uid="
				+ UIDFactory.getUID()
				+ "&channelid="
				+ channelid
				+ "&appid="
				+ appId
				+ "&adscore="
				+ 0
				+ "&device="
				+ android.os.Build.MODEL
				+ "&os="
				+ android.os.Build.VERSION.SDK;
		} catch (NameNotFoundException e) {
			MLog.e(TAG,"getChecknewUrl");
		}
		
		return url;
	}
	
}
