package com.kb.platform.update;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.preference.PreferenceManager;

import com.kb.platform.update.UpadateHelper.CheckAdConfigResult;
import com.kb.platform.update.UpadateHelper.UpdateResult;
import com.kb.platform.util.MLog;

public class PlatformHelper {
	private static PlatformHelper sInstance = null;
	private Context mContext = null;
	private String mAppId = null;
	private String mChannelId = null;
	private UpdateResult mUpateResult = null;
	
	private static final String PRENAME_LASTUPDATETIME= "lastupdatetime";
	private static final String PRENAME_CANSHOW_YOUMIAD= "canshowyoumiad";
	protected static final String TAG = "PlatformHelper";
	
	private PlatformHelper(){
		
	}
	
	public static PlatformHelper getInstance(){
		if (sInstance == null) {
			sInstance = new PlatformHelper();
		}
		
		return sInstance;
	}
	
	public void init(Context context,String appId,String channelId){
		if (mContext == null) {
			mContext = context.getApplicationContext();
			
			mAppId = appId;
			mChannelId = channelId;
			UIDFactory.init(mContext);
		}
		
		startAutoUpdate();
		if (!canShowYoumiOfferAd()) {
			//如果当前已经放开了积分墙，则不去server检查了，减少点流量消耗
			startCheckAdConfig();
		}
	}
	
	public UpdateResult getUpdateResult(){
		return mUpateResult;
	}
	
	/**
	 * 自动更新
	 */
	private void startAutoUpdate(){

		UpadateHelper.checkNewVersion(mContext,mAppId,mChannelId,new UpadateHelper.UpdateCallback() {
			
			@Override
			public void onCheckUpdateResult(UpdateResult result) {
				MLog.i(TAG,"handleCheckUpdateResult.");
				
				mUpateResult = result;
				if (result != null && result.ret == 0) {
					if(result.hasUpdate == true && result.downloadUrl != null && result.downloadUrl.length() > 0)
					{
						SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
						long now = System.currentTimeMillis();
						long lastUpdateTime = sharedPreferences.getLong(PRENAME_LASTUPDATETIME, 0);
						if (lastUpdateTime == 0 || (now - lastUpdateTime) > 24 * 3600 * 1000) {	//24小时内只显示一次更新提示
							//由于不知道当前最前面的Activity引用，无法直接弹对话框，所以采用Dialog风格的activity
							Intent intent = new Intent(mContext,UpdateResultView.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							mContext.startActivity(intent);
						}
						
						sharedPreferences.edit().putLong(PRENAME_LASTUPDATETIME, now).commit();
						

					}else {
						MLog.e(TAG,"onCheckUpdateResult error");
					}
				}
			}
		});
		
	}
	
	/**
	 * 检查积分墙广告的设置
	 */
	private void startCheckAdConfig(){
		UpadateHelper.checkAdConfigVersion(mContext,mAppId,mChannelId,new UpadateHelper.CheckConfigCallback() {
			
			@Override
			public void onCallback(CheckAdConfigResult result) {
				// TODO Auto-generated method stub
				if (result != null && result.ret == 0) {
					PackageManager  pm = mContext.getPackageManager();
					int versionCode = 0;
					try {
						versionCode = pm.getPackageInfo(mContext.getPackageName(), 0).versionCode;
					} catch (NameNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
					String adConfigString = result.canShowYoumiOfferAd ? "" + versionCode + "_yes" : "" + versionCode + "_no";
					sharedPreferences.edit().putString(PRENAME_CANSHOW_YOUMIAD, adConfigString).commit();
				}
				
			}
		});
	}
	
	//是否可以显示积分墙，为了绕过市场审核，在指定时间后才显示积分墙
	public boolean canShowYoumiOfferAd(){
//		Date date = new Date(112, 4, 31, 18, 30, 0);
//		long time = date.getTime();
//		long now = System.currentTimeMillis();
//		
//		if (now > time) {
//			return true;
//		}
		
		//还没到硬编码的时间，去server拉取配置
		PackageManager  pm = mContext.getPackageManager();
		int versionCode = 0;
		try {
			versionCode = pm.getPackageInfo(mContext.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
		
		SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
		String adConfigString = sharedPreferences.getString(PRENAME_CANSHOW_YOUMIAD, "");
		if (adConfigString == null || adConfigString.equals("")) {
			//还没有去检查过
			return false;
		}else if (adConfigString.equalsIgnoreCase("" + versionCode + "_yes") ) {
			return true;
		}else if (adConfigString.equalsIgnoreCase("" + versionCode + "_no") ) {
			return false;
		}
		
		return false;
	}
	

	private Activity getTopActivity(){
		Activity topActivity = null;
			ActivityManager am = (ActivityManager) mContext.getSystemService(Activity.ACTIVITY_SERVICE);
		ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
		
		
		return topActivity;
	}
}
