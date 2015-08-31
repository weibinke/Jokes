package com.kb.jokes.business.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.Header;

import com.google.gson.Gson;
import com.kb.jokes.business.core.AppCore;
import com.kb.jokes.data.protocol.CGIConfig;
import com.kb.jokes.data.protocol.GetCommentsJsonResponse;
import com.kb.jokes.data.protocol.GetJokesListJsonResponse;
import com.kb.jokes.data.storage.model.JokeInfo;
import com.kb.platform.util.MLog;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import de.greenrobot.event.EventBus;

public class JokesDataManager {
	protected static final String TAG = "JokesDataManager";
	
	public static class FetchJokeDetailEvent{
		public boolean isSuc;
	}
	
	public static class FetchJokeCommentEvent{
		public boolean isSuc;
		public GetCommentsJsonResponse response;
	}
	
	public static class AddJokeCommentEvent{
		public boolean isSuc;
	}
	
	public static class FetchJokeListSuccessEvent{
		public int type;
		public boolean isHead;
		public int count;
	}
	
	public static class FetchJokeListFailedEvent{
		public int type;
		public boolean isHead;
	}
	
	public static class UpdateJokeEvent{
		public JokeInfo[] list;
	}
	
	public static interface ILoadDataCallback{
		void onFailed(int type,boolean isHead);
		void onSuccess(int type,boolean isHead,int count);
	}

	private List<ILoadDataCallback> dataCallbacks = new ArrayList<JokesDataManager.ILoadDataCallback>();
	
	public JokesDataManager() {
	}
	
	public void init(){
		MLog.i(TAG, "init");
	}
	
	public void unInit(){
		MLog.i(TAG, "unInit");
	}
	
	public void registerCallback(ILoadDataCallback callback){
		if(!dataCallbacks.contains(callback)){
			dataCallbacks.add(callback);
		}
	}
	
	public void unRegisterCallback(ILoadDataCallback callback){
		dataCallbacks.remove(callback);
	}
	
	public void invokeCallback(boolean isSuc,boolean isHead,int type,int count){
		for (ILoadDataCallback callback : dataCallbacks) {
			if(isSuc){
				callback.onSuccess(type,isHead,count);
			}else{
				callback.onFailed(type,isHead);
			}
		}
	}
	
	public List<JokeInfo> getImageJokesList(int type,int maxCount){
		List<JokeInfo> results = readFromDb(type, maxCount);
		return results;
	}
	
	public List<JokeInfo> getMyFavoriteJokesList(){
		List<JokeInfo> results = AppCore.getDbService().getDbAdapter().getMyFavoriteList();
		return results;
	}
	
	public List<JokeInfo> getMyThumbupJokesList(){
		List<JokeInfo> results = AppCore.getDbService().getDbAdapter().getMyThumbupList();
		return results;
	}

	/**
	 * fecth joke list from server
	 * @return
	 */
	public void fetchJokesList(final int type,long start,final int count,final boolean isHeader){
		//refresh from server
		RequestParams params = new RequestParams();
		params.put("ishead", isHeader ? 1 : 0);
		params.put("count", count);
		params.put("start", start);
		params.put("type", type);
		AppCore.getCommunicator().get(CGIConfig.getJokesListCgiUrl(), params, new AsyncHttpResponseHandler() {
			
			@Override
			public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
				MLog.i(TAG, "onSuccess statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onSuccess statusCode=" + statusCode + ",result=" + result);
				Gson gson = new Gson();
				GetJokesListJsonResponse response = gson.fromJson(result, GetJokesListJsonResponse.class);
				
				int count = response.getCount();
				List<JokeInfo> jokeInfos = new ArrayList<JokeInfo>();
				if(response.getRet()== CGIConfig.RET_OK){
					if(count > 0 && response.getItems() != null){
						
						for (GetJokesListJsonResponse.Item item: response.getItems()) {
							JokeInfo joke = new JokeInfo();
							joke.converFrom(item);
							jokeInfos.add(joke);
						}
						
						saveJokesResults(jokeInfos);
					}
					
					invokeCallback(true,isHeader, type,jokeInfos.size());
				}else{
					invokeCallback(false,isHeader, type,0);
				}
			}
			
			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				MLog.i(TAG, "onFailure statusCode=" + statusCode);
				invokeCallback(false,isHeader,type,0);
			}
		});
	}
	
	/**
	 * thumbup or cancel thumbup a Joke
	 * @param joke
	 */
	public void thumbup(JokeInfo joke){
		if(!joke.getIsThumbup()){
			joke.setThumbUpCount(joke.getThumbUpCount() + 1);
			joke.setIsThumbup(true);
		}else{
			joke.setThumbUpCount(joke.getThumbUpCount() - 1);
			joke.setIsThumbup(false);
		}
		
		saveJokes(joke);
		
		//send request to server
		RequestParams params = new RequestParams();
		params.put("joke_id", joke.getJokeId());
		params.put("deviceid", AppCore.getInstance().getDeviceId());
		
		AppCore.getCommunicator().get(CGIConfig.getThumbUpCgiUrl(), params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				MLog.i(TAG, "onSuccess statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onSuccess statusCode=" + statusCode + ",result=" + result);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				MLog.i(TAG, "onFailure statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onFailure statusCode=" + statusCode + ",result=" + result);
			}
			
		});
	}
	
	/**
	 * add favorite
	 * @param joke
	 */
	public void addFavorite(JokeInfo joke){
		if(!joke.getIsFavorite()){
			joke.setIsFavorite(true);
		}else{
			joke.setIsFavorite(false);
		}
		
		saveJokes(joke);
		
		//send request to server
		RequestParams params = new RequestParams();
		params.put("joke_id", joke.getJokeId());
		params.put("is_cancel", joke.getIsFavorite() ? 0 : 1);
		
		AppCore.getCommunicator().get(CGIConfig.getAddFavoriteCgiUrl(), params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				MLog.i(TAG, "onSuccess statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onSuccess statusCode=" + statusCode + ",result=" + result);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				MLog.i(TAG, "onFailure statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onFailure statusCode=" + statusCode + ",result=" + result);
			}
			
		});
	}
	
	/**
	 * send add comment request
	 * @param joke
	 * @param content
	 */
	public void addComment(JokeInfo joke,String content){
		joke.setCommentCount(joke.getCommentCount() + 1);
		saveJokes(joke);
		
		RequestParams params = new RequestParams();
		params.put("joke_id", joke.getJokeId());
		params.put("content", content);
		
		AppCore.getCommunicator().get(CGIConfig.getAddCommentCgiUrl(), params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				MLog.i(TAG, "onSuccess statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onSuccess statusCode=" + statusCode + ",result=" + result);
				AddJokeCommentEvent event = new AddJokeCommentEvent();
				event.isSuc = true;
				EventBus.getDefault().post(event);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				MLog.i(TAG, "onFailure statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onFailure statusCode=" + statusCode + ",result=" + result);
				
				AddJokeCommentEvent event = new AddJokeCommentEvent();
				event.isSuc = true;
				EventBus.getDefault().post(event);
			}
			
		});
	}
	
	/**
	 * fetch joke comments from server
	 * @param joke
	 * @param start
	 * @param count
	 */
	public void fetchJokeComment(JokeInfo joke,long start,int count){
		RequestParams params = new RequestParams();
		params.put("joke_id", joke.getJokeId());
		params.put("start", start);
		params.put("count", count);
		
		AppCore.getCommunicator().get(CGIConfig.getGetCommentCgiUrl(), params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO dealwith result here
				
				MLog.i(TAG, "onSuccess statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onSuccess statusCode=" + statusCode + ",result=" + result);
				
				Gson gson = new Gson();
				GetCommentsJsonResponse response = gson.fromJson(result, GetCommentsJsonResponse.class);
				
				FetchJokeCommentEvent event = new FetchJokeCommentEvent();
				event.isSuc = true;
				event.response = response;
				EventBus.getDefault().post(event);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				MLog.i(TAG, "onFailure statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onFailure statusCode=" + statusCode + ",result=" + result);
				FetchJokeCommentEvent event = new FetchJokeCommentEvent();
				event.isSuc = false;
				EventBus.getDefault().post(event);
			}
			
		});
	}
	
	public void saveJokes(JokeInfo joke){
		MLog.i(TAG, "saveJokes jokeid=" + joke.getJokeId());
		AppCore.getDbService().getDbAdapter().updateJoke(joke);
		UpdateJokeEvent event = new UpdateJokeEvent();
		event.list = new JokeInfo[]{joke};
		EventBus.getDefault().post(event);
	}
	
	public void saveJokesResults(List<JokeInfo> results){
		if(results == null || results.size() <=0){
			return;
		}
		MLog.i(TAG, "saveJokes count=" + results.size());
		AppCore.getDbService().getDbAdapter().saveJokes(results);
		UpdateJokeEvent event = new UpdateJokeEvent();
		event.list = new JokeInfo[results.size()];
		results.toArray(event.list);
		EventBus.getDefault().post(event);
	}
	
	private List<JokeInfo> readFromDb(int type,int maxCount){
		return AppCore.getDbService().getDbAdapter().getJokesList(type, maxCount);
	}
	
	/**
	 * get joke detail
	 * @param jokeId
	 */
	public void fetchJokeDetail(long jokeId){
		RequestParams params = new RequestParams();
		params.put("joke_id", jokeId);
		
		AppCore.getCommunicator().get(CGIConfig.getGetJokeDetailCgiUrl(), params, new AsyncHttpResponseHandler() {

			@Override
			public void onSuccess(int statusCode, Header[] headers,
					byte[] responseBody) {
				// TODO dealwith result here
				
				MLog.i(TAG, "onSuccess statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onSuccess statusCode=" + statusCode + ",result=" + result);
				
				Gson gson = new Gson();
				GetJokesListJsonResponse response = gson.fromJson(result, GetJokesListJsonResponse.class);
				
				FetchJokeDetailEvent event = new FetchJokeDetailEvent();
				List<JokeInfo> jokeInfos = new ArrayList<JokeInfo>();
				if(response.getRet() == CGIConfig.RET_OK && response.getCount() >0 && response.getItems() != null && response.getItems().size() > 0){
					for (GetJokesListJsonResponse.Item item: response.getItems()) {
						JokeInfo joke = new JokeInfo();
						joke.converFrom(item);
						jokeInfos.add(joke);
					}
					
					saveJokesResults(jokeInfos);
					event.isSuc = true;
				}else {
					event.isSuc = false;
				}
				
				EventBus.getDefault().post(event);
			}

			@Override
			public void onFailure(int statusCode, Header[] headers,
					byte[] responseBody, Throwable error) {
				MLog.i(TAG, "onFailure statusCode=" + statusCode);
				String result = new String(responseBody);
				MLog.d(TAG, "onFailure statusCode=" + statusCode + ",result=" + result);
				FetchJokeDetailEvent event = new FetchJokeDetailEvent();
				event.isSuc = false;
				EventBus.getDefault().post(event);
			}
			
		});
	}
	
	
}
