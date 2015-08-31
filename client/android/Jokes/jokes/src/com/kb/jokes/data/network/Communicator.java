package com.kb.jokes.data.network;

import com.kb.jokes.business.core.AppCore;
import com.kb.jokes.data.protocol.CGIConfig;
import com.kb.platform.util.MLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestHandle;
import com.loopj.android.http.RequestParams;

/**
 * ÍøÂç²ã½Ó¿Ú
 * @author weibinke
 *
 */
public class Communicator {
	private static final String TAG = "Communicator";
	private AsyncHttpClient client;

	public Communicator() {
		client = new AsyncHttpClient();
		client.setEnableRedirects(true, true, true);
	}

	public void appendCommonParams(RequestParams params){
		params.put("deviceid", AppCore.getInstance().getDeviceId());
		params.put("cv", CGIConfig.CLIENT_VERSION);
		params.put("model", CGIConfig.MODEL);
		params.put("os", CGIConfig.OS);
		params.put("platform", CGIConfig.PLATFORM);
		
	}
	
	public RequestHandle get(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		appendCommonParams(params);
		MLog.d(TAG,"get url=" + url + ",params=" + params);
		return client.get(url, params, responseHandler);
	}

	public RequestHandle post(String url, RequestParams params,
			AsyncHttpResponseHandler responseHandler) {
		appendCommonParams(params);
		MLog.d(TAG,"post url=" + url + ",params=" + params);
		return client.post(url, params, responseHandler);
	}
}
