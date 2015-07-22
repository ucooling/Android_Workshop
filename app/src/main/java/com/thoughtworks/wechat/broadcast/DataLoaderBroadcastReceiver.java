package com.thoughtworks.wechat.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.thoughtworks.wechat.database.DataBaseContract;
import com.thoughtworks.wechat.database.DataBaseHelper;
import com.thoughtworks.wechat.service.TweetService;

public class DataLoaderBroadcastReceiver extends BroadcastReceiver {

    private DataCallBack mCallBack;

    public DataLoaderBroadcastReceiver(DataCallBack callBack) {
        this.mCallBack = callBack;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SQLiteDatabase database = new DataBaseHelper(context).getReadableDatabase();
        final String action = intent.getAction();
        if (action.equals(TweetService.ACTION)) {
            final String status = intent.getStringExtra(TweetService.EXTRA_STATUS);
            if (status.equals(TweetService.COMPLETE)) {
                Cursor userCursor = database.query(DataBaseContract.UserEntry.TABLE_NAME, null, null, null, null, null, null);
                Cursor tweetCursor = database.query(DataBaseContract.TweetEntry.TABLE_NAME, null, null, null, null, null, null);
                mCallBack.onDataLoaderComplete(userCursor, tweetCursor);
            }
        }
    }
}
