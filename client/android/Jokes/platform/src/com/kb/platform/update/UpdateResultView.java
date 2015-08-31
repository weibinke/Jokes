package com.kb.platform.update;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.kb.platform.R;
import com.kb.platform.update.UpadateHelper.UpdateResult;

public class UpdateResultView extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.update_dialog);

		setupViews();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void setupViews(){

		UpdateResult result = PlatformHelper.getInstance().getUpdateResult();
		if (result != null && result.ret == 0 && result.hasUpdate == true && result.downloadUrl != null && result.downloadUrl.length() > 0) {
			final String urlString = result.downloadUrl;
			String text = getString(R.string.update_hasupdate_text1) + result.newVersionName + ".\n";
			text = text + getString(R.string.update_hasupdate_text2) + "\n\n" + getString(R.string.update_hasupdate_text3) + "\n";
			text = text + result.versionDesc + "\n";
			
//			text = "dsfsdafsafdsafsdffsd\nsdfsdfs\nfsdfasfdf\ndfasdfas\n";

			TextView textView = (TextView)findViewById(R.id.update_hasupdate);
			textView.setText(text);
			Button btnYes = (Button)findViewById(R.id.update_btn_update);
			Button btnNo = (Button)findViewById(R.id.update_btn_no);
			btnYes.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					//Æô¶¯ä¯ÀÀÆ÷È¥ÏÂÔØ
					Intent viewIntent = new Intent("android.intent.action.VIEW",Uri.parse(urlString));
					startActivity(viewIntent);
					finish();
				}
			});

			btnNo.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					finish();
				}
			});

		}
	}
}
