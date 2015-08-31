package com.kb.jokes.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.State;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.kb.jokes.R;
import com.kb.jokes.business.core.AppCore;
import com.kb.jokes.business.manager.JokesDataManager.AddJokeCommentEvent;
import com.kb.jokes.business.manager.JokesDataManager.FetchJokeCommentEvent;
import com.kb.jokes.business.manager.JokesDataManager.FetchJokeDetailEvent;
import com.kb.jokes.business.manager.JokesDataManager.UpdateJokeEvent;
import com.kb.jokes.data.protocol.CGIConfig;
import com.kb.jokes.data.protocol.CommentItem;
import com.kb.jokes.data.storage.model.JokeInfo;
import com.kb.jokes.ui.ImageJokesListAdapter.ViewHolder;
import com.kb.platform.util.MLog;
import com.kb.platform.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import de.greenrobot.event.EventBus;

public class JokeDetailActivity extends BaseActivity{
	private static final String TAG = "JokeDetailActivity";
	public final static String KEY_JOKE_ID = "jokeid";
	
	private long jokeId;
	private JokeInfo jokeInfo;
	private View jokeInfoView;
	private ImageJokesListAdapter.ViewHolder holder;
	private static DisplayImageOptions displayImageOptions = null;
	
	private static final int COMENT_COUNT_PAGE = 20;
	
	private PullToRefreshListView listview;
	private View emptyView;
	private TextView emptyTextView;
	private View btnSendComment;
	private EditText editText;
	
	private List<CommentItem> commentItems = new ArrayList<CommentItem>();
	
	private CommentsListViewAdater adapter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		jokeId = getIntent().getLongExtra(KEY_JOKE_ID, 0);
		if(jokeId > 0){
			jokeInfo = AppCore.getDbService().getDbAdapter().getJokeInfoByJokeId(jokeId);
		}
		
		if(jokeInfo == null){
			MLog.e(TAG, "joke not found.jokeId=" + jokeId);
			finish();
			return;
		}
		
		Drawable defaultBitmapDrawable = getResources().getDrawable(R.drawable.default_image);
		displayImageOptions = new DisplayImageOptions.Builder()
		.showImageForEmptyUri(defaultBitmapDrawable)
		.showImageOnFail(defaultBitmapDrawable)
		.showImageOnLoading(defaultBitmapDrawable)
		.resetViewBeforeLoading(false)  // default
		.delayBeforeLoading(1000)
		.cacheInMemory(true) // default
		.cacheOnDisk(true) // default
		.considerExifParams(false) // default
		.imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
		.bitmapConfig(Bitmap.Config.ARGB_8888) // default
//		.decodingOptions(...)
//		.displayer(new SimpleBitmapDisplayer()) // default
//		.handler(new Handler()) // default
		.build();
		
		initView();
		
		EventBus.getDefault().register(this);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	private void updateJokeView(boolean needUpdateImage){
		jokeInfo = AppCore.getDbService().getDbAdapter().getJokeInfoByJokeId(jokeId);
		
		holder.textContent.setText(jokeInfo.getContent());

		if(jokeInfo.getType() == JokeInfo.TYPE_NOPIC_JOKES || Util.isNullOrNil(jokeInfo.getImageurl())){
			holder.imageView.setVisibility(View.GONE);
		}else{
			holder.imageView.setVisibility(View.VISIBLE);
			if(needUpdateImage){
				ImageLoader.getInstance().displayImage(jokeInfo.getImageurl(),holder.imageView,displayImageOptions);
				holder.imageView.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent = new Intent(JokeDetailActivity.this, BigImageActivity.class);
						intent.putExtra(BigImageActivity.JOKE_ID, jokeId);
						JokeDetailActivity.this.startActivity(intent);
					}
				});
			}
		}
		
		holder.thumbupArea.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MLog.i(TAG, "thumbup id=" + jokeInfo.getJokeId() + ",isThumbup=" + jokeInfo.getIsThumbup());
				AppCore.getDataManager().thumbup(jokeInfo);
				updateJokeView(false);
			}
		});
		
		holder.textThumbupCount.setText("" + jokeInfo.getThumbUpCount());
		holder.textThumbupCount.setSelected(jokeInfo.getIsThumbup());
		holder.btnThumbup.setSelected(jokeInfo.getIsThumbup());

		holder.addFavoriteArea.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MLog.i(TAG, "add favorit id=" + jokeInfo.getJokeId() + ",isFavorite=" + jokeInfo.getIsFavorite());
				AppCore.getDataManager().addFavorite(jokeInfo);
				updateJokeView(false);
			}
		});
		
		holder.btnAddFavorite.setSelected(jokeInfo.getIsFavorite());
		
		holder.commentArea.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MLog.i(TAG, "comment id=" + jokeInfo.getJokeId());
				editText.requestFocus();
				InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
				imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);   
			}
		});
		
		holder.textCommentCount.setText("" + jokeInfo.getCommentCount());
		
		holder.shareArea.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MLog.i(TAG, "share id=" + jokeInfo.getJokeId());
				ImageJokesListAdapter.shareJoke(JokeDetailActivity.this,jokeInfo, holder.imageView.getDrawable());
			}
		});
	}
	
	private void initView(){
		setContentView(R.layout.joke_detail);
		
		ImageButton backButton = (ImageButton) findViewById(R.id.titlebar_btn_left);
		backButton.setVisibility(View.VISIBLE);

		ImageButton refreshButton = (ImageButton) findViewById(R.id.titlebar_btn_right);
		refreshButton.setVisibility(View.GONE);
		
		backButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		TextView titleTextView = (TextView) findViewById(R.id.titlebar_text_title);
		titleTextView.setText(R.string.detail_title);
		
		holder = new ViewHolder();
		jokeInfoView = View.inflate(this, R.layout.imagejokes_list_item, null);
		holder.initView(jokeInfoView);
		updateJokeView(true);
		initCommentListView();

		AppCore.getDataManager().fetchJokeDetail(jokeId);
		loadMoreComments();
	}
	
	private void initCommentListView(){
		listview = (PullToRefreshListView)findViewById(R.id.my_list);
		
		listview.setMode(Mode.PULL_FROM_END);
		
		btnSendComment = findViewById(R.id.btn_comment_send);
		editText = (EditText) findViewById(R.id.comment_edittext);
		btnSendComment.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String content = editText.getText().toString();
				if(Util.isNullOrNil(content)){
					Toast.makeText(JokeDetailActivity.this, R.string.detail_comment_emptytips, Toast.LENGTH_SHORT).show();
					return;
				}
				editText.setText("");
				hideKeyboard();
				
				addMyComment(content);
			}
		});
		
		listview.getRefreshableView().addHeaderView(jokeInfoView);
		
		emptyView = View.inflate(this, R.layout.list_empty_tips, null);
		emptyTextView = (TextView) emptyView.findViewById(R.id.emptyText);
		emptyTextView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MLog.i(TAG, "onClick emptyTextView");
				emptyTextView.setText(R.string.detail_loading_comments);
				AppCore.getDataManager().fetchJokeComment(jokeInfo, 0, COMENT_COUNT_PAGE);
			}
		});
		emptyTextView.setText(R.string.detail_loading_comments);
//		listview.setEmptyView(emptyView);
		
		listview.setOnPullEventListener(new PullToRefreshBase.OnPullEventListener<ListView>() {

			@Override
			public void onPullEvent(PullToRefreshBase<ListView> refreshView,
					State state, Mode direction) {

				MLog.i(TAG, "onPullEvent state=" + state + ",mode=" + direction);
				if (state.equals(State.PULL_TO_REFRESH)
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
		
		listview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ListView> refreshView) {
				MLog.i(TAG, "onRefresh");
				loadMoreComments();
				
			}
		});
		adapter = new CommentsListViewAdater(null,this,emptyView);
		listview.setAdapter(adapter);
	}
	
	private void loadMoreComments(){
		long start = 0;
		if(commentItems.size() != 0){
			start = commentItems.get(commentItems.size() - 1).getPubDate();
		}
		
		AppCore.getDataManager().fetchJokeComment(jokeInfo, start, COMENT_COUNT_PAGE);
	}
	
	private void addMyComment(String content){
		AppCore.getDataManager().addComment(jokeInfo, content);
		
		CommentItem item = new CommentItem();
		item.setAuthor(JokeDetailActivity.this.getString(R.string.detail_default_comment_author));
		item.setContent(content);
		item.setJokeId(jokeId);
		item.setPubDate(Util.currentTick());
		commentItems.add(0, item);
		adapter.setList(commentItems);
		listview.getRefreshableView().setSelectionAfterHeaderView();
		
		emptyView.setVisibility(View.VISIBLE);
	}
	private void updateComments(final List<CommentItem> list){
		commentItems.addAll(list);
		if(list == null || list.size() == 0){
			emptyTextView.setText(R.string.detail_nocomment);
			emptyView.setVisibility(View.VISIBLE);
		}else{
			emptyView.setVisibility(View.GONE);
			adapter.setList(commentItems);
		}
	}
	
	public void onEvent(FetchJokeDetailEvent event){
		MLog.i(TAG, "FetchJokeDetailEvent");
		updateJokeView(false);
	}
	
	public void onEvent(FetchJokeCommentEvent event){
		MLog.i(TAG, "FetchJokeDetailEvent");
		listview.onRefreshComplete();
		if(!event.isSuc || event.response.getRet() != CGIConfig.RET_OK){
			MLog.e(TAG, "onEvent failed");
			
			Toast.makeText(this, R.string.detail_comment_fetch_failed, Toast.LENGTH_SHORT).show();
			return;
		}
		if(commentItems.size() != 0 && (event.response.getItems() == null || event.response.getItems().size() == 0)){
			Toast.makeText(this, R.string.listview_tips_loadmore_nomoredatata, Toast.LENGTH_SHORT).show();
			return;
		}
		updateComments(event.response.getItems());
	}
	
	public void onEvent(AddJokeCommentEvent event){
		if(event.isSuc){
			Toast.makeText(this, R.string.detail_comment_send_suc, Toast.LENGTH_SHORT).show();
		}else{
			Toast.makeText(this, R.string.detail_comment_send_failed, Toast.LENGTH_SHORT).show();
		}
	}
	
	public void onEvent(UpdateJokeEvent event){
		if(event.list != null && event.list.length > 0){
			updateJokeView(false);
		}
	}
}
