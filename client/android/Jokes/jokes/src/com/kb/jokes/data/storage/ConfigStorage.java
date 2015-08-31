package com.kb.jokes.data.storage;

import android.content.Context;

public class ConfigStorage extends KVStorage{
	private static final String NAME = "AppConfig";
	public ConfigStorage(Context context) {
		super(context, NAME);
	}

}
