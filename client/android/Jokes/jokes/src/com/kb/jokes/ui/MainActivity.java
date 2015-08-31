package com.kb.jokes.ui;

import java.io.IOException;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kb.jokes.R;
import com.kb.jokes.business.core.AppCore;
import com.kb.platform.util.MLog;
import com.viewpagerindicator.IconTabPageIndicator;

public class MainActivity extends FragmentActivity {
	private static final String TAG = "MainActivity";
	private ViewPager pager;
	private MainTabAdapter adapter;
	private TextView titleText; 
	private ImageButton btnTitlebarRight;

	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		MLog.i(TAG, "onCreate");
		setContentView(R.layout.main);
		
		BaseFragment[] fragments = new BaseFragment[]{
			new ImageJokesListView(),
			new NoPicJokesListView(),
			new NewsJokesListView(),
			new HomepageView()			
		};
		
		adapter = new MainTabAdapter(this,fragments,getSupportFragmentManager());

        pager = (ViewPager)findViewById(R.id.pager);
        pager.setAdapter(adapter);

        IconTabPageIndicator indicator = (IconTabPageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager);
        
        btnTitlebarRight = (ImageButton) findViewById(R.id.titlebar_btn_right);
        btnTitlebarRight.setVisibility(View.VISIBLE);
        titleText = (TextView) findViewById(R.id.titlebar_text_title);
		if(btnTitlebarRight != null){
			btnTitlebarRight.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					BaseFragment currentPage = (BaseFragment) adapter.getItem(pager.getCurrentItem());
					currentPage.onClickTitleBarRight();
				}
			});
		}
		
		indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				titleText.setText(adapter.getPageTitle(arg0));
				BaseFragment currentPage = (BaseFragment) adapter.getItem(arg0);
				btnTitlebarRight.setBackgroundResource(currentPage.getTitleBarRightImage());
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				
			}
		});
		
		testXmpp();
	}
	
	private void testXmpp(){
//		XMPPTCPConnection conn1 = new XMPPTCPConnection("test1", "1234","120.24.210.119");
//		try {
//			conn1.connect();
//			
//			conn1.login();
//			conn1.disconnect();
//		} catch (SmackException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (XMPPException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

	protected void onDestroy() {
		super.onDestroy();
		MLog.i(TAG, "onDestroy");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			finish();
			AppCore.getInstance().exitApp();
			System.exit(0);
		}
		
		return super.onKeyDown(keyCode, event);
	}
}
