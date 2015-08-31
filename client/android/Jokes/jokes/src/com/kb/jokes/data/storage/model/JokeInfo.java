package com.kb.jokes.data.storage.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.kb.jokes.data.protocol.GetJokesListJsonResponse;
import com.kb.platform.util.MLog;

@Table(name = "JokeInfo")
public class JokeInfo extends Model{
	
    private static final String TAG = "JokeInfo";
    
    public static final int TYPE_NOPIC_JOKES = 0;
    public static final int TYPE_PIC_JOKES = 1;
    public static final int TYPE_LONG_JOKES = 2;

    @Column(name = "JokeId",index=true,unique=true)
    private long mJokeId;

    @Column(name = "Type",index=true)
	private int mType;
    
    @Column(name = "CommentCount")
    private int mCommentCount;
    
    @Column(name = "ThumbUpCount")
    private int mThumbUpCount;

    @Column(name = "Avatar")
    private String mAvatar;
    
    @Column(name = "Imageurl")
    private String mImageurl;
    
    @Column(name = "Time")
    private long mTime;
    
    @Column(name="UpdateTime",index=true)
    private long mUpdateTime;
    
    @Column(name = "Content")
    private String mContent;

    @Column(name = "Author")
    private String mAuthor;
    
    @Column(name = "IsFavorite")
    private boolean mIsFavorite;
    
    @Column(name = "IsThumbup")
    private boolean mIsThumbup;
    
    //Ô¤Áô×Ö¶Î
    @Column(name = "reserve1")
    private long reserve1;

    //Ô¤Áô×Ö¶Î
    @Column(name = "reserve2")
    private long reserve2;
    
    //Ô¤Áô×Ö¶Î
    @Column(name = "reserve3")
    private long reserve3;

    //Ô¤Áô×Ö¶Î
    @Column(name = "reserve4")
    private String reserve4;
    
    //Ô¤Áô×Ö¶Î
    @Column(name = "reserve5")
    private String reserve5;
    
    //Ô¤Áô×Ö¶Î
    @Column(name = "reserve6")
    private String reserve6;
    
    public JokeInfo(){

    }

    public static boolean isJokeIdVailed(long jokeId){
    	return jokeId > 0;
    }
    
    public void converFrom(GetJokesListJsonResponse.Item item){
    	if(item == null){
    		MLog.e(TAG, "converFrom item is null");
    		
    		return;
    	}
    	this.setJokeId(item.getJokeId());
    	this.setType(item.getType());
    	this.setCommentCount(item.getCommentCount());
    	this.setThumbUpCount(item.getThumbUpCount());
    	this.setAvatar(item.getAvatar());
    	this.setImageurl(item.getImageurl());
    	this.setTime(item.getTime());
    	this.setUpdateTime(item.getUpdateTime());
    	this.setContent(item.getContent());
    	this.setAuthor(item.getAuthor());
    	this.setIsFavorite(item.getIsFavorite());
    	this.setIsThumbup(item.getIsThumbup());
    	
    }
    
    /**
     * update fields except inner id
     * @param item
     */
    public void updateField(JokeInfo item){
    	if(item == null){
    		MLog.e(TAG, "converFrom item is null");
    		
    		return;
    	}
    	
    	this.setJokeId(item.getJokeId());
    	this.setType(item.getType());
    	this.setCommentCount(item.getCommentCount());
    	this.setThumbUpCount(item.getThumbUpCount());
    	this.setAvatar(item.getAvatar());
    	this.setImageurl(item.getImageurl());
    	this.setTime(item.getTime());
    	this.setUpdateTime(item.getUpdateTime());
    	this.setContent(item.getContent());
    	this.setAuthor(item.getAuthor());
    	this.setIsFavorite(item.getIsFavorite());
    	this.setIsThumbup(item.getIsThumbup());
    	
    }
    
    public void setJokeId(long id){
    	mJokeId = id;
    }
    
    public long getJokeId(){
    	return mJokeId;
    }
    
    public void setType(int type) {
        mType = type;
    }

    public int getType() {
        return mType;
    }

    public void setCommentCount(int commentCount) {
        mCommentCount = commentCount;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setThumbUpCount(int thumbUpCount) {
        mThumbUpCount = thumbUpCount;
    }

    public int getThumbUpCount() {
        return mThumbUpCount;
    }

    public void setAvatar(String avatar) {
        mAvatar = avatar;
    }

    public String getAvatar() {
        return mAvatar;
    }

    public void setImageurl(String imageurl) {
        mImageurl = imageurl;
    }

    public String getImageurl() {
        return mImageurl;
    }

    public void setTime(long time) {
        mTime = time;
    }

    public long getTime() {
        return mTime;
    }
    
    public void setUpdateTime(long time) {
        mUpdateTime = time;
    }

    public long getUpdateTime() {
        return mUpdateTime;
    }

    public void setContent(String content) {
        mContent = content;
    }

    public String getContent() {
        return mContent;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    public String getAuthor() {
        return mAuthor;
    }
    
    public void setIsFavorite(boolean isFavorite){
    	mIsFavorite = isFavorite;
    }
    
    public boolean getIsFavorite(){
    	return mIsFavorite;
    }
    
    public void setIsThumbup(boolean isThumbup){
    	mIsThumbup = isThumbup;
    }

    public boolean getIsThumbup(){
    	return mIsThumbup;
    }

    @Override
    public String toString(){
        return "type = " + mType + ", commentCount = " + mCommentCount + ", thumbUpCount = " + mThumbUpCount + ", avatar = " + mAvatar + ", imageurl = " + mImageurl + ", time = " + mTime + ", content = " + mContent + ", author = " + mAuthor;
    }
}
