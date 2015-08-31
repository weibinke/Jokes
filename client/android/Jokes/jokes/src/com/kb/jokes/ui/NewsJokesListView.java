package com.kb.jokes.ui;

import com.kb.jokes.data.storage.model.JokeInfo;

public class NewsJokesListView extends BaseJokesListView{
	@Override
	protected int getType() {
		return JokeInfo.TYPE_LONG_JOKES;
	}
}
