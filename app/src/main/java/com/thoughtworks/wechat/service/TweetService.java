package com.thoughtworks.wechat.service;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.IBinder;
import android.util.Log;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thoughtworks.wechat.database.DataBaseContract;
import com.thoughtworks.wechat.database.DataBaseHelper;
import com.thoughtworks.wechat.model.Tweet;
import com.thoughtworks.wechat.model.User;
import com.thoughtworks.wechat.utils.DatabaseUtils;
import com.thoughtworks.wechat.utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TweetService extends IntentService {

    private static final String TAG="TweetService";
    private DataBaseHelper dataBaseHelper;
    private final static String WECHAT_RESULT="com.thoughtworks.wechat.service.WECHAT";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public TweetService(String name) {
        super(name);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

    @Override
    public void onCreate(){
        Log.i(TAG, "=======onCreate=======");
        Intent intent = new Intent();
        intent.setAction(WECHAT_RESULT);
        sendBroadcast(intent);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        Log.i(TAG, "==========onStartCommand==========");
        initData();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy(){
        Log.i(TAG, "========onDestroy=======");
        super.onDestroy();
    }

    private void initData() {
        Log.i(TAG, "============start storage db=============");

        List<Tweet> tweetList = new ArrayList<>();
        dataBaseHelper = new DataBaseHelper(TweetService.this);
        SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
        try {
            dataBaseHelper.getReadableDatabase().delete(DataBaseContract.TweetEntry.TABLE_NAME, null, null);
            String headerSource = FileUtils.readAssetTextFile(TweetService.this, "user.json");
            String tweetSource = FileUtils.readAssetTextFile(TweetService.this, "tweets.json");
            User user = new Gson().fromJson(headerSource, User.class);
            System.out.println(user);
            tweetList = new Gson().fromJson(tweetSource, new TypeToken<List<Tweet>>() {
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
}
