package com.kb.jokes.ui;

import com.kb.jokes.data.storage.model.JokeInfo;

public class ImageJokesListView extends BaseJokesListView {
	protected static final String TAG = "ImageJokesListView";
		
	@Override
	protected int getType() {
		return JokeInfo.TYPE_PIC_JOKES;
	}
}
