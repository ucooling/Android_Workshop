package com.thoughtworks.wechat.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.content.LocalBroadcastManager;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thoughtworks.wechat.database.DataBaseContract;
import com.thoughtworks.wechat.database.DataBaseHelper;
import com.thoughtworks.wechat.model.Tweet;
import com.thoughtworks.wechat.model.User;
import com.thoughtworks.wechat.utils.DatabaseUtils;
import com.thoughtworks.wechat.utils.FileUtils;

import java.io.IOException;
import java.util.List;

public class TweetService extends IntentService {

    private static final String TAG="TweetService";
    private DataBaseHelper dataBaseHelper;
    public static final String ACTION = "com.thoughtworks.wechat.service.WECHAT";
    public static final String START = "start";
    public static final String COMPLETE = "complete";
    public static final String EXTRA_STATUS = "status";

    private String action;


    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public TweetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        action = intent.getAction();
        if(action.equals(ACTION)) {
            sendStatusBroadcast(START);
            initData();
            sendStatusBroadcast(COMPLETE);
        }

    }

    private void initData() {
        dataBaseHelper = new DataBaseHelper(TweetService.this);
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        try {
            dataBaseHelper.getReadableDatabase().delete(DataBaseContract.TweetEntry.TABLE_NAME, null, null);
            String headerSource = FileUtils.readAssetTextFile(TweetService.this, "user.json");
            String tweetSource = FileUtils.readAssetTextFile(TweetService.this, "tweets.json");
            User user = new Gson().fromJson(headerSource, User.class);
            List<Tweet> tweetList = new Gson().fromJson(tweetSource, new TypeToken<List<Tweet>>() {
            }.getType());
            System.out.println(tweetList);
            for (Tweet tweet : tweetList) {
                boolean noError = tweet.getError() == null && tweet.getUnknownError() == null;
                boolean shouldDisplay = tweet.getContent() != null && tweet.getImages() != null;
                if (noError && shouldDisplay) {
                    ContentValues contentValues = DatabaseUtils.getWeChatTweetContentValues(tweet);
                    database.insert(DataBaseContract.TweetEntry.TABLE_NAME, null, contentValues);
                }
            }

            if (null != user){
                ContentValues contentUser = DatabaseUtils.getWeChatUserContentValues(user);
                database.insert(DataBaseContract.UserEntry.TABLE_NAME, null, contentUser);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendStatusBroadcast(String status) {
       Intent intent = new Intent(action);
        intent.putExtra(EXTRA_STATUS, status);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
