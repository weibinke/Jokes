package com.kb.jokes.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kb.jokes.R;
import com.kb.jokes.business.core.AppCore;
import com.kb.jokes.business.manager.JokesDataManager.UpdateJokeEvent;
import com.kb.jokes.data.storage.model.JokeInfo;
import com.kb.platform.util.MLog;

import de.greenrobot.event.EventBus;

public class MyFavoriteView extends BaseFragment{
	protected static final String TAG = "BaseJokesListView";

	protected PullToRefreshListView listview;
	protected ImageJokesListAdapter adapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		super.onCreateView(inflater, container, savedInstanceState);

		if (rootView == null) {
			rootView = inflater.inflate(R.layout.image_jokes_list, null);
			initView(rootView);
		}

		return rootView;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	private void initView(View rootView) {
		listview = (PullToRefreshListView) rootView.findViewById(R.id.my_list);
		View emptyView = rootView.findViewById(R.id.empty);
		listview.setEmptyView(emptyView);
		listview.setMode(Mode.DISABLED);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MLog.d(TAG, "onItemClick() : pos=" + position + ", id=" + id);
				JokeInfo jokeInfo = (JokeInfo) parent.getAdapter().getItem(position); // @warning: Should not use mAdapter
				Intent intent = new Intent(MyFavoriteView.this.getActivity(),JokeDetailActivity.class);
				intent.putExtra(JokeDetailActivity.KEY_JOKE_ID, jokeInfo.getJokeId());
				startActivity(intent);
			}
		});

		adapter = new ImageJokesListAdapter(getActivity());
		listview.setAdapter(adapter);

		reloadData();
	}

	@Override
	public void onClickTitleBarRight() {
		super.onClickTitleBarRight();
		if(listview != null){
			listview.setRefreshing();
		}
	}

	protected void reloadData() {
		List<JokeInfo> resultsInfos = AppCore.getDataManager().getMyFavoriteJokesList();
		adapter.setData(resultsInfos);
	}
	
	public void onEvent(UpdateJokeEvent event){
		if(event.list != null && event.list.length > 0){
			reloadData();
		}
	}

}
