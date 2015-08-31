package com.kb.platform.util;

import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;

public class BitmapUtil {
	private static final String TAG = "BitmapUtil";

	public static boolean saveBitmapToDisk(Bitmap bitmap, String filePath) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
			fos.close();
			fos = null;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			MLog.e(TAG, "saveBitmapToDisk failed.filepath=" + filePath,e);
		}finally{
			if(fos!= null){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return false;
	}

}
