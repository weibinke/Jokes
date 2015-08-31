package com.kb.jokes.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.kb.jokes.R;
import com.kb.jokes.business.core.AppCore;
import com.kb.jokes.business.core.FileConfig;
import com.kb.jokes.data.storage.model.JokeInfo;
import com.kb.platform.util.BitmapUtil;
import com.kb.platform.util.MLog;
import com.kb.platform.util.Util;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ortiz.touch.TouchImageView;


public class BigImageActivity extends BaseActivity {
	public static final String JOKE_ID = "jokeid";
	private static final String TAG = "BigImageActivity";
	private TouchImageView imageView;
	private JokeInfo jokeInfo;
	private Button save;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		long jokeid = getIntent().getLongExtra(JOKE_ID,0);
		jokeInfo = AppCore.getDbService().getDbAdapter().getJokeInfoByJokeId(jokeid);
		if(jokeInfo == null){
			MLog.e(TAG, "onCreate joke not found,id" + jokeid);
			return;
		}
		initView();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void initView(){
		setContentView(R.layout.view_image);
		imageView = (TouchImageView) findViewById(R.id.img);
		ImageLoader.getInstance().displayImage(jokeInfo.getImageurl(),imageView);
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		save = (Button) findViewById(R.id.saveimage);
		save.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MLog.i(TAG, "save image jokeid=" + jokeInfo.getJokeId());
				ImageLoader.getInstance().loadImage(jokeInfo.getImageurl(),new ImageLoadingListener() {
					
					@Override
					public void onLoadingStarted(String imageUri, View view) {
					}
					
					@Override
					public void onLoadingFailed(String imageUri, View view,
							FailReason failReason) {
						Toast.makeText(BigImageActivity.this, R.string.viewimage_saveimage_failed, Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
						String filePath = FileConfig.getSavePath() + Util.currentTick() + ".jpg";
						BitmapUtil.saveBitmapToDisk(loadedImage, filePath);
						String tips = BigImageActivity.this.getString(R.string.viewimage_saveimage_suc) + filePath;
						Toast.makeText(BigImageActivity.this, tips, Toast.LENGTH_SHORT).show();
					}
					
					@Override
					public void onLoadingCancelled(String imageUri, View view) {
						
					}
				});
				
			}
		});
	}
	
}
