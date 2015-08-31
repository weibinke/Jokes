package com.kb.platform.update;

import java.util.UUID;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;

import com.kb.platform.util.MLog;
import com.kb.platform.util.Util;

public class UIDFactory {
	public static final String PRE_NAME = "UID_NAME";
	public static final String PRE_KEY_NAME = "UID_KEY";
	private static final String TAG = "UIDFactory";
	private static String mUID = null;
	
	public static String init(Context context){
		if(Util.isNullOrNil(mUID)){
			SharedPreferences sharedPreferences = context.getSharedPreferences(PRE_NAME, Context.MODE_WORLD_READABLE |Context.MODE_WORLD_WRITEABLE);
			String id = sharedPreferences.getString(PRE_KEY_NAME, null);
			
			if (id == null) {
				id = ((TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE)).getDeviceId();
				if (!isValidID(id)) {
					id = Secure.getString(context.getContentResolver(),Secure.ANDROID_ID);
					if (!isValidID(id)) {
						UUID uuid = UUID.randomUUID();
						id = uuid.toString();
					}
				}
				
				if (isValidID(id)) {
					sharedPreferences.edit().putString(PRE_KEY_NAME, id).commit();
				}
			}
			
			mUID = id;
			
			if (!isValidID(id)) {
				MLog.e(TAG,"UIDFactory error.mUID = " + mUID);
			}
		}
		
		return mUID;
	}

	private static boolean isValidID(String ID){
		if (ID == null || ID.length() <= 10 ||ID.length() > 64) {
			return false;
		}
		
		if (ID != null ) {
			try {
				if (Integer.decode(ID) == 0 || Integer.decode(ID) == -1) {
					return false;
				}
			} catch (NumberFormatException e) {
				// TODO: handle exception
			}
			
		}
		
		return true;
	}
	public static String getUID(){
		return mUID;
	}
}
