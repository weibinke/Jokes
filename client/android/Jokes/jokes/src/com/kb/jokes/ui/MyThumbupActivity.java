package com.kb.jokes.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.kb.jokes.R;

public class MyThumbupActivity extends FragmentActivity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.myfavorite);
		
		getSupportFragmentManager().beginTransaction().replace(R.id.myfavoirte_fragment, new MyThumbupView()).commit();
		
		initView();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void initView(){
		TextView title = (TextView) findViewById(R.id.titlebar_text_title);
		title.setText(R.string.homepage_mythumbup);
		
		ImageButton backButton = (ImageButton) findViewById(R.id.titlebar_btn_left);
		backButton.setVisibility(View.VISIBLE);
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		ImageButton titleRight = (ImageButton) findViewById(R.id.titlebar_btn_right);
		titleRight.setVisibility(View.GONE);
	}

}
