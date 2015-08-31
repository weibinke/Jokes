package com.kb.jokes.ui;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kb.jokes.R;
import com.kb.jokes.business.core.AppCore;
import com.kb.jokes.business.core.FileConfig;
import com.kb.jokes.data.storage.model.JokeInfo;
import com.kb.platform.util.BitmapUtil;
import com.kb.platform.util.MLog;
import com.kb.platform.util.ShareUtil;
import com.kb.platform.util.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class ImageJokesListAdapter extends BaseAdapter{
	private static final String TAG = "ImageJokesListAdapter";
	private Context context;
	private List<JokeInfo> datas;
	private static DisplayImageOptions displayImageOptions = null;
	
	public ImageJokesListAdapter(final Context context) {
		this.context = context;
		Drawable defaultBitmapDrawable = context.getResources().getDrawable(R.drawable.default_image);
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
	}
	
	public void setData(List<JokeInfo> datas){
		this.datas = datas;
		notifyDataSetChanged();
		MLog.i(TAG, "setData count=" + (datas != null ? datas.size() : 0));
	}

	@Override
	public int getCount() {
		return datas != null? datas.size() : 0;
	}

	@Override
	public Object getItem(int position) {
		if(datas != null && position >= 0 && position < datas.size()){
			return datas.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(datas == null || position <0 || position >= datas.size()){
			MLog.e(TAG, "getView index invalid.size=" + getCount() + ",position=" + position);
			return null;
		}
		
		final ViewHolder holder;
		if(convertView == null){
			convertView = View.inflate(context, R.layout.imagejokes_list_item, null);
			holder = new ViewHolder();
			holder.initView(convertView);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		final JokeInfo jokeInfo = datas.get(position);
		holder.textContent.setText(jokeInfo.getContent());

		if(jokeInfo.getType() == JokeInfo.TYPE_NOPIC_JOKES || Util.isNullOrNil(jokeInfo.getImageurl())){
			holder.imageView.setVisibility(View.GONE);
		}else{
			holder.imageView.setVisibility(View.VISIBLE);
			ImageLoader.getInstance().displayImage(jokeInfo.getImageurl(),holder.imageView,displayImageOptions);
		}
		
		holder.thumbupArea.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MLog.i(TAG, "thumbup id=" + jokeInfo.getJokeId() + ",isThumbup=" + jokeInfo.getIsThumbup());
				AppCore.getDataManager().thumbup(jokeInfo);
				notifyDataSetChanged();
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
				notifyDataSetChanged();
			}
		});
		
		holder.btnAddFavorite.setSelected(jokeInfo.getIsFavorite());
		
		holder.commentArea.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MLog.i(TAG, "comment id=" + jokeInfo.getJokeId());
				Intent intent = new Intent(context,JokeDetailActivity.class);
				intent.putExtra(JokeDetailActivity.KEY_JOKE_ID, jokeInfo.getJokeId());
				context.startActivity(intent);
			}
		});
		
		holder.textCommentCount.setText("" + jokeInfo.getCommentCount());
		
		holder.shareArea.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MLog.i(TAG, "share id=" + jokeInfo.getJokeId());
				shareJoke(context,jokeInfo, holder.imageView.getDrawable());
			}
		});
		
		return convertView;
	}
	
	public static boolean shareJoke(Context context,JokeInfo jokeInfo,Drawable drawable){
		if(jokeInfo == null){
			return false;
		}
		MLog.i(TAG, "shareJoke.id=" + jokeInfo.getJokeId());
		
		if(Util.isNullOrNil(jokeInfo.getImageurl()) || drawable == null || !(drawable instanceof BitmapDrawable)){
			ShareUtil.share(context, jokeInfo.getContent(),context.getString(R.string.share_title));
		}else{
			Bitmap bitmap = ((BitmapDrawable)drawable).getBitmap();
			String filePath = FileConfig.getSharePath() + System.currentTimeMillis();
			if(!BitmapUtil.saveBitmapToDisk(bitmap, filePath)){
				MLog.e(TAG, "shareJoke save bitmap failed");
				return false;
			}
			
			ShareUtil.share(context, jokeInfo.getContent(), filePath,context.getString(R.string.share_title));
		}
		
		return true;
	}
	
	public static class ViewHolder{
		public TextView textContent;
		public ImageView imageView;
		public View thumbupArea;
		public ImageView btnThumbup;
		public TextView textThumbupCount;
		public View addFavoriteArea;
		public ImageView btnAddFavorite;
		public View commentArea;
		public ImageView btnComment;
		public TextView textCommentCount;
		public View shareArea;
		public ImageView btnShare;
		
		public void initView(View convertView){
			ViewHolder holder = this;
			holder.textContent = (TextView) convertView.findViewById(R.id.image_jokes_item_content);
			holder.imageView = (ImageView) convertView.findViewById(R.id.image_jokes_item_image);
			
			holder.thumbupArea = convertView.findViewById(R.id.controlarea_thumb_up);
			holder.btnThumbup = (ImageView) convertView.findViewById(R.id.btn_thumb_up);
			holder.textThumbupCount = (TextView) convertView.findViewById(R.id.text_thumbup_count);
			
			holder.addFavoriteArea = convertView.findViewById(R.id.controlarea_like);
			holder.btnAddFavorite = (ImageView) convertView.findViewById(R.id.btn_like);
			
			holder.commentArea = convertView.findViewById(R.id.controlarea_comment);
			holder.btnComment = (ImageView) convertView.findViewById(R.id.btn_comment);
			holder.textCommentCount = (TextView) convertView.findViewById(R.id.text_comment);
			
			holder.shareArea = convertView.findViewById(R.id.controlarea_share);
			holder.btnShare = (ImageView) convertView.findViewById(R.id.btn_share);
		}
	}
}
