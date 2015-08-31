package com.kb.jokes.ui;

import com.kb.jokes.data.storage.model.JokeInfo;

public class NoPicJokesListView extends BaseJokesListView{
	protected static final String TAG = "NoPicJokesListView";
	
	@Override
	protected int getType() {
		return JokeInfo.TYPE_NOPIC_JOKES;
	}
}
