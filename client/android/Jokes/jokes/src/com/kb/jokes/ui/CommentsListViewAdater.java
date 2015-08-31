package com.kb.jokes.ui;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kb.jokes.R;
import com.kb.jokes.data.protocol.CommentItem;
import com.kb.platform.util.Util;

public class CommentsListViewAdater extends BaseAdapter{
	private List<CommentItem> list;
	private Context mContext;
	private View emptyView;
	public CommentsListViewAdater(List<CommentItem> list,Context context,View emptyView){
		this.list = list;
		this.mContext = context;
		this.emptyView = emptyView;
	}
	
	public void setList(List<CommentItem> list){
		this.list = list;
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if(list != null && list.size() > 0){
			return list.size();
		}
		return 1;
	}

	@Override
	public Object getItem(int position) {
		if(list != null && position >= 0 && position < list.size()){
			return list.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	
	private String getDisplayAuthor(CommentItem item){
		if(Util.isNullOrNil(item.getAuthor())){
			return mContext.getString(R.string.detail_default_comment_author);
		}
		return item.getAuthor();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(list == null || list.size() == 0){
			return emptyView;
		}
		
		final ViewHolder holder;
		if(convertView == null || convertView.getTag() == null){
			holder = new ViewHolder();
			convertView = View.inflate(mContext, R.layout.comment_listview_item, null);
			holder.textAuthor = (TextView) convertView.findViewById(R.id.comment_item_author);
			holder.textTime = (TextView) convertView.findViewById(R.id.comment_item_time);
			holder.textContent = (TextView) convertView.findViewById(R.id.comment_item_content);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		CommentItem commentItem = (CommentItem) getItem(position);
		if(commentItem != null){
			
			holder.textAuthor.setText(getDisplayAuthor(commentItem));
			holder.textTime.setText(Util.getDateCurrentTimeZone(commentItem.getPubDate()));
			holder.textContent.setText(commentItem.getContent());
		}
		return convertView;
	}
	
	private static class ViewHolder{
		public TextView textAuthor;
		public TextView textTime;
		public TextView textContent;
	}

}
