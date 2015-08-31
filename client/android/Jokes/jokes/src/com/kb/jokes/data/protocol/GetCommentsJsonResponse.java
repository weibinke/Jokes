package com.kb.jokes.data.protocol;

import java.util.List;

import com.google.gson.annotations.SerializedName;


public class GetCommentsJsonResponse{

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
    private List<CommentItem> mItems;
    @SerializedName(FIELD_RET)
    private int mRet;
    @SerializedName(FIELD_NEXT_POS)
    private int mNextPo;


    public GetCommentsJsonResponse(){

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

    public void setItems(List<CommentItem> items) {
        mItems = items;
    }

    public List<CommentItem> getItems() {
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

    @Override
    public String toString(){
        return "start = " + mStart + ", count = " + mCount + ", items = " + mItems + ", ret = " + mRet + ", nextPo = " + mNextPo;
    }


}