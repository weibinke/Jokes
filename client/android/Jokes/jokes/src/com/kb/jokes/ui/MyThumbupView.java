package com.kb.jokes.ui;

import java.util.List;

import com.kb.jokes.business.core.AppCore;
import com.kb.jokes.data.storage.model.JokeInfo;

public class MyThumbupView extends MyFavoriteView{
	
	@Override
	protected void reloadData() {
		List<JokeInfo> resultsInfos = AppCore.getDataManager().getMyThumbupJokesList();
		adapter.setData(resultsInfos);
	}

}
