package com.kb.jokes.data.protocol;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class GetJokesListJsonResponse /*implements Parcelable*/{

    private static final String FIELD_START = "start";
    private static final String FIELD_COUNT = "count";
    private static final String FIELD_ITEMS = "items";
    private static final String FIELD_RET = "ret";
    private static final String FIELD_NEXT_POS = "next_pos";


    @SerializedName(FIELD_START)
    private long mStart;
    @SerializedName(FIELD_COUNT)
    private int mCount;
    @SerializedName(FIELD_ITEMS)
    private List<Item> mItems;
    @SerializedName(FIELD_RET)
    private int mRet;
    @SerializedName(FIELD_NEXT_POS)
    private int mNextPo;


    public GetJokesListJsonResponse(){

    }

    public void setStart(long start) {
        mStart = start;
    }

    public long getStart() {
        return mStart;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public int getCount() {
        return mCount;
    }

    public void setItems(List<Item> items) {
        mItems = items;
    }

    public List<Item> getItems() {
        return mItems;
    }

    public void setRet(int ret) {
        mRet = ret;
    }

    public int getRet() {
        return mRet;
    }

    public void setNextPo(int nextPo) {
        mNextPo = nextPo;
    }

    public int getNextPo() {
        return mNextPo;
    }

//    public GetJokesListJsonResponse(Parcel in) {
//        mStart = in.readInt();
//        mCount = in.readInt();
//        mItems = new ArrayList<Item>();
//        in.readTypedList(mItems, Item.CREATOR);
//        mRet = in.readInt();
//        mNextPo = in.readInt();
//    }
//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    public static final Parcelable.Creator<GetJokesListJsonResponse> CREATOR = new Parcelable.Creator<GetJokesListJsonResponse>() {
//        public GetJokesListJsonResponse createFromParcel(Parcel in) {
//            return new GetJokesListJsonResponse(in);
//        }
//
//        public GetJokesListJsonResponse[] newArray(int size) {
//        return new GetJokesListJsonResponse[size];
//        }
//    };
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//        dest.writeInt(mStart);
//        dest.writeInt(mCount);
//        dest.writeTypedList(mItems);
//        dest.writeInt(mRet);
//        dest.writeInt(mNextPo);
//    }

    @Override
    public String toString(){
        return "start = " + mStart + ", count = " + mCount + ", items = " + mItems + ", ret = " + mRet + ", nextPo = " + mNextPo;
    }
    public static class Item /*implements Parcelable*/{

    	private static final String FIELD_ID = "id";
        private static final String FIELD_TYPE = "type";
        private static final String FIELD_COMMENT_COUNT = "comment_count";
        private static final String FIELD_THUMB_UP_COUNT = "thumb_up_count";
        private static final String FIELD_AVATAR = "avatar";
        private static final String FIELD_IMAGEURL = "imageurl";
        private static final String FIELD_TIME = "time";
        private static final String FIELD_UPDATETIME = "update_time";
        private static final String FIELD_CONTENT = "content";
        private static final String FIELD_AUTHOR = "author";
        private static final String FIELD_ISFAVORITE = "is_favorite";
        private static final String FIELD_ISTHUMBUP = "is_thumbup";

        @SerializedName(FIELD_ID)
        private long mJokeId;
        @SerializedName(FIELD_TYPE)
        private int mType;
        @SerializedName(FIELD_COMMENT_COUNT)
        private int mCommentCount;
        @SerializedName(FIELD_THUMB_UP_COUNT)
        private int mThumbUpCount;
        @SerializedName(FIELD_AVATAR)
        private String mAvatar;
        @SerializedName(FIELD_IMAGEURL)
        private String mImageurl;
        @SerializedName(FIELD_TIME)
        private long mTime;
        @SerializedName(FIELD_UPDATETIME)
        private long mUpdateTime;
        @SerializedName(FIELD_CONTENT)
        private String mContent;
        @SerializedName(FIELD_AUTHOR)
        private String mAuthor;
        @SerializedName(FIELD_ISFAVORITE)
        private boolean mIsFavorite;
        @SerializedName(FIELD_ISTHUMBUP)
        private boolean mIsThumbup;


        public Item(){

        }

        public void setJokeId(long id) {
            mJokeId = id;
        }

        public long getJokeId() {
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

//        public Item(Parcel in) {
//        	mId = in.readLong();
//            mType = in.readInt();
//            mCommentCount = in.readInt();
//            mThumbUpCount = in.readInt();
//            mAvatar = in.readString();
//            mImageurl = in.readString();
//            mTime = in.readLong();
//            mContent = in.readString();
//            mAuthor = in.readString();
//        }
//
//        @Override
//        public int describeContents() {
//            return 0;
//        }
//
//        public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
//            public Item createFromParcel(Parcel in) {
//                return new Item(in);
//            }
//
//            public Item[] newArray(int size) {
//            return new Item[size];
//            }
//        };
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//        	dest.writeLong(mId);
//            dest.writeInt(mType);
//            dest.writeInt(mCommentCount);
//            dest.writeInt(mThumbUpCount);
//            dest.writeString(mAvatar);
//            dest.writeString(mImageurl);
//            dest.writeLong(mTime);
//            dest.writeString(mContent);
//            dest.writeString(mAuthor);
//        }

        @Override
        public String toString(){
            return "type = " + mType + ", commentCount = " + mCommentCount + ", thumbUpCount = " + mThumbUpCount + ", avatar = " + mAvatar + ", imageurl = " + mImageurl + ", time = " + mTime + ", content = " + mContent + ", author = " + mAuthor;
        }
    }
}