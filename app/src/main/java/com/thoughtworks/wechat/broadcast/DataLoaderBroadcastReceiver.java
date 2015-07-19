package com.thoughtworks.wechat.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.View;

import com.thoughtworks.wechat.adapter.TweetAdapter;
import com.thoughtworks.wechat.database.DataBaseContract;
import com.thoughtworks.wechat.database.DataBaseHelper;
import com.thoughtworks.wechat.model.User;
import com.thoughtworks.wechat.service.TweetService;
import com.thoughtworks.wechat.utils.DatabaseUtils;
import com.thoughtworks.wechat.viewholder.TweetHeaderHolder;

/**
 * Created by lbma on 7/19/15.
 */
public class DataLoaderBroadcastReceiver extends BroadcastReceiver {

    public View mHeaderView;
    public TweetAdapter mTweetAdapter;
    public DataBaseHelper dataBaseHelper;

    @Override
    public void onReceive(Context context, Intent intent) {
        final String action = intent.getAction();
        if (action.equals(TweetService.ACTION)) {
            final String status = intent.getStringExtra(TweetService.EXTRA_STATUS);
            if (status.equals(TweetService.COMPLETE)) {
                Cursor cursor = dataBaseHelper.getReadableDatabase().query(DataBaseContract.UserEntry.TABLE_NAME, null, null, null, null, null,null);
                if (cursor != null) {
                    User user = DatabaseUtils.cursor2User(cursor);
                    TweetHeaderHolder holder = new TweetHeaderHolder(context, mHeaderView);
                    holder.populate(user);
                    cursor.close();
                }else{
                    Log.i("User load error", "========User load error========");
                }

                mTweetAdapter.changeCursor(dataBaseHelper.getReadableDatabase().query(DataBaseContract.TweetEntry.TABLE_NAME, null, null, null, null, null, null));
            }
        }
    }
}
