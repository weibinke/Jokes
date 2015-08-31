package com.kb.jokes.data.storage;

import java.util.List;

import com.kb.jokes.data.storage.model.JokeInfo;

public interface IDbAdapter {
	/**
	 * get jokes list with type
	 * @param type
	 * @param maxCount
	 * @return
	 */
	public List<JokeInfo> getJokesList(int type,int maxCount);
	
	public List<JokeInfo> getMyFavoriteList();

	public List<JokeInfo> getMyThumbupList();
	
	public void saveJokes(List<JokeInfo> jokeInfos);

	public void updateJoke(JokeInfo jokeInfo);
	
	public JokeInfo getJokeInfoByJokeId(long jokeId);
	
	public long getStartPos(int type);
	
	public long getEndPos(int type);
}
