package com.kb.jokes.data.protocol;

import com.google.gson.annotations.SerializedName;


public class CommentItem{

    private static final String FIELD_JOKE_ID = "joke_id";
    private static final String FIELD_ID = "id";
    private static final String FIELD_STATUS = "status";
    private static final String FIELD_PUB_DATE = "pub_date";
    private static final String FIELD_CONTENT = "content";
    private static final String FIELD_AUTHOR = "author";


    @SerializedName(FIELD_JOKE_ID)
    private long mJokeId;
    @SerializedName(FIELD_ID)
    private long mId;
    @SerializedName(FIELD_STATUS)
    private int mStatus;
    @SerializedName(FIELD_PUB_DATE)
    private long mPubDate;
    @SerializedName(FIELD_CONTENT)
    private String mContent;
    @SerializedName(FIELD_AUTHOR)
    private String mAuthor;


    public CommentItem(){

    }

    public void setJokeId(long jokeId) {
        mJokeId = jokeId;
    }

    public long getJokeId() {
        return mJokeId;
    }

    public void setId(long id) {
        mId = id;
    }

    public long getId() {
        return mId;
    }

    public void setStatus(int status) {
        mStatus = status;
    }

    public int getStatus() {
        return mStatus;
    }

    public void setPubDate(long pubDate) {
        mPubDate = pubDate;
    }

    public long getPubDate() {
        return mPubDate;
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

    @Override
    public boolean equals(Object obj){
        if(obj instanceof CommentItem){
            return ((CommentItem) obj).getId() == mId;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return ((Long)mId).hashCode();
    }

    @Override
    public String toString(){
        return "jokeId = " + mJokeId + ", id = " + mId + ", status = " + mStatus + ", pubDate = " + mPubDate + ", content = " + mContent + ", author = " + mAuthor;
    }


}