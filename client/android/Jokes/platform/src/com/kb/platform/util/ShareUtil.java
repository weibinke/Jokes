package com.kb.platform.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class ShareUtil {
	public static void share(Context context,String text,String filePath,String title){
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("image/jpeg");
        String dir = "file://" + filePath;
        sendIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(dir));
        sendIntent.putExtra(Intent.EXTRA_TEXT,text);
        context.startActivity(Intent.createChooser(sendIntent, title));
	}
	
	public static void share(Context context,String text,String tilte){
		Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT,text);
        context.startActivity(Intent.createChooser(sendIntent, tilte));
	}
}
