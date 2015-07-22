package com.thoughtworks.wechat.broadcast;

import android.database.Cursor;

public interface DataCallBack {

    void onDataLoaderStart();
    void onDataLoaderComplete(Cursor userCursor, Cursor tweetCursor);
}
