package com.kb.jokes.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kb.jokes.R;
import com.kb.jokes.business.core.AppCore;
import com.kb.jokes.business.manager.JokesDataManager.ILoadDataCallback;
import com.kb.jokes.business.manager.JokesDataManager.UpdateJokeEvent;
import com.kb.jokes.data.storage.model.JokeInfo;
import com.kb.platform.util.MLog;

import de.greenrobot.event.EventBus;

public class BaseJokesListView extends BaseFragment implements ILoadDataCallback{
	protected static final String TAG = "BaseJokesListView";

	private static final int COUNT_PER_PAGE = 10; // size for each fetch count
	private static final int DEFAULT_SIZE = COUNT_PER_PAGE; // default size of
															// first page

	private PullToRefreshListView listview;
	private ImageJokesListAdapter adapter;

	protected int getType() {
		return JokeInfo.TYPE_PIC_JOKES;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppCore.getDataManager().registerCallback(this);
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
		AppCore.getDataManager().unRegisterCallback(this);
		EventBus.getDefault().unregister(this);
	}

	private void initView(View rootView) {
		listview = (PullToRefreshListView) rootView.findViewById(R.id.my_list);
		View emptyView = rootView.findViewById(R.id.empty);
		listview.setEmptyView(emptyView);
		listview.setMode(Mode.BOTH);
		listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				MLog.d(TAG, "onItemClick() : pos=" + position + ", id=" + id);
				JokeInfo jokeInfo = (JokeInfo) parent.getAdapter().getItem(position); // @warning: Should not use mAdapter
				Intent intent = new Intent(BaseJokesListView.this.getActivity(),JokeDetailActivity.class);
				intent.putExtra(JokeDetailActivity.KEY_JOKE_ID, jokeInfo.getJokeId());
				startActivity(intent);
			}
		});

		listview.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {

			@Override
			public void onPullEvent(PullToRefreshBase<ListView> refreshView,
					State state, Mode direction) {

				MLog.i(TAG, "onPullEvent state=" + state + ",mode=" + direction);
				if (state.equals(State.PULL_TO_REFRESH)
						&& direction == Mode.PULL_FROM_START) {
					refreshView.getLoadingLayoutProxy().setPullLabel(
							getString(R.string.listview_pulldowntorefress));
					refreshView.getLoadingLayoutProxy().setReleaseLabel(
							getString(R.string.listview_release_to_refresh));
					refreshView.getLoadingLayoutProxy().setRefreshingLabel(
							getString(R.string.listview_loading));

					String label = DateUtils.formatDateTime(getActivity()
							.getApplicationContext(), System
							.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
							| DateUtils.FORMAT_SHOW_DATE
							| DateUtils.FORMAT_ABBREV_ALL);
					refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(
							getString(R.string.listview_updatelabel) + " : "
									+ label);
				} else if (state.equals(State.PULL_TO_REFRESH)
						&& direction == Mode.PULL_FROM_END) {
					refreshView.getLoadingLayoutProxy().setPullLabel(
							getString(R.string.listview_pulluptorefress));
					refreshView.getLoadingLayoutProxy().setReleaseLabel(
							getString(R.string.listview_release_to_refresh));
					refreshView.getLoadingLayoutProxy().setRefreshingLabel(
							getString(R.string.listview_loading));
				}
			}

		});

		listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {

			@Override
			public void onPullDownToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				MLog.i(TAG, "onPullDownToRefresh");
				refresh(true);
			}

			@Override
			public void onPullUpToRefresh(
					PullToRefreshBase<ListView> refreshView) {
				MLog.i(TAG, "onPullDownToRefresh");
				refresh(false);
			}

		});

		// View loadMoreView =
		// View.inflate(getActivity(),R.layout.listview_footer_loadermore,
		// null);
		// loadMoreView =
		// (TextView)loadMoreView.findViewById(R.id.litview_loadmore_text);

		adapter = new ImageJokesListAdapter(getActivity());
		listview.setAdapter(adapter);

		reloadData();

		refresh(true);
	}

	@Override
	public void onClickTitleBarRight() {
		super.onClickTitleBarRight();
		if(listview != null){
			listview.setRefreshing();
		}
	}

	/**
	 * refresh data from server
	 * 
	 * @param isHead
	 *            refresh header or footer
	 */
	private void refresh(boolean isHead) {
		MLog.i(TAG, "refresh start");
		long start = 0;
		if (adapter.getCount() == 0) {
			start = 0;
		} else if (isHead) {
			start = ((JokeInfo) adapter.getItem(0)).getUpdateTime();
		} else {
			start = ((JokeInfo) adapter.getItem(adapter.getCount() - 1)).getUpdateTime();
		}

		AppCore.getDataManager().fetchJokesList(getType(), start, COUNT_PER_PAGE, isHead);
	}

	private void reloadData() {
		int curSize = adapter.getCount();
		int nextSize = 0;
		if (curSize == 0) {
			nextSize = DEFAULT_SIZE;
		} else {
			nextSize = curSize + COUNT_PER_PAGE;
		}

		List<JokeInfo> resultsInfos = AppCore.getDataManager()
				.getImageJokesList(getType(), nextSize);
		adapter.setData(resultsInfos);
	}

	@Override
	public void onFailed(int type, boolean isHead) {
		if (type == getType()) {
			listview.onRefreshComplete();
			if(isResumed()){
				Toast.makeText(getActivity(), R.string.listview_tips_load_failed,
						Toast.LENGTH_SHORT).show();
			}
			
		}
	}

	@Override
	public void onSuccess(int type, boolean isHead, int count) {
		if (type == getType()) {
			listview.onRefreshComplete();
			if(isResumed() && count == 0){
				if (isHead) {
					Toast.makeText(getActivity(),
							R.string.listview_tips_nomore_newdatas,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(getActivity(),
							R.string.listview_tips_nomore_earlydatas,
							Toast.LENGTH_SHORT).show();
				}
			}
		}
	}
	
	public void onEvent(UpdateJokeEvent event){
		if(event.list != null && event.list.length > 0){
			reloadData();
		}
	}
}
