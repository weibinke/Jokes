package com.kb.jokes.ui;

import java.util.logging.FileHandler;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.kb.jokes.R;
import com.kb.jokes.business.core.FileConfig;
import com.kb.platform.update.PlatformHelper;
import com.kb.platform.util.FileHelper;
import com.kb.platform.util.ShareUtil;
import com.kb.platform.util.Util;

public class SettingsActivity extends BaseActivity implements OnClickListener{
	private View checkUpdateView;
	private View feedbackView;
	private View recommendView;
	private View clearCacheView;
	private View rateView;
	private View aboutView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		initView();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void initView(){
		TextView titleTextView = (TextView) findViewById(R.id.titlebar_text_title);
		titleTextView.setText(R.string.settings_title);
		
		ImageButton titleLeftButton = (ImageButton) findViewById(R.id.titlebar_btn_left);
		titleLeftButton.setVisibility(View.VISIBLE);
		titleLeftButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		checkUpdateView = findViewById(R.id.settings_checkupdate);
		checkUpdateView.setOnClickListener(this);
		
		feedbackView = findViewById(R.id.settings_feedback);
		feedbackView.setOnClickListener(this);
		
		recommendView = findViewById(R.id.settings_recommend);
		recommendView.setOnClickListener(this);
		
		clearCacheView = findViewById(R.id.settings_clearcache);
		clearCacheView.setOnClickListener(this);
		
		rateView = findViewById(R.id.settings_rate);
		rateView.setOnClickListener(this);
		
		aboutView = findViewById(R.id.settings_about);
		aboutView.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(v == checkUpdateView){
		}else if(v == feedbackView){
			
		}else if(v== recommendView){
			ShareUtil.share(this, getString(R.string.settings_share_content), getString(R.string.share_title));
		}else if(v== clearCacheView){
			clearCache();
		}else if(v==rateView){
			Util.jumpToMarket(this);
		}else if(v==aboutView){
			
		}
		
	}
	
	private void clearCache(){
		FileHelper.removeDirectory(FileConfig.getRootPath());
		FileConfig.initFilePath(this);
		Toast.makeText(this, R.string.settings_clearcache_suc, Toast.LENGTH_SHORT).show();
	}

}
