package com.thoughtworks.wechat;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.thoughtworks.wechat.adapter.TweetAdapter;
import com.thoughtworks.wechat.database.DataBaseContract;
import com.thoughtworks.wechat.database.DataBaseHelper;
import com.thoughtworks.wechat.model.Tweet;
import com.thoughtworks.wechat.model.User;
import com.thoughtworks.wechat.utils.DatabaseUtils;
import com.thoughtworks.wechat.utils.FileUtils;
import com.thoughtworks.wechat.viewholder.TweetHeaderHolder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SecondActivity extends AppCompatActivity {

    @InjectView(R.id.listview)
    ListView mTweetListView;
    private TweetAdapter mTweetAdapter;
    private View mHeaderView;
    private DataBaseHelper dataBaseHelper;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.inject(this);

        dataBaseHelper = new DataBaseHelper(SecondActivity.this);

        initViews();
        initData();
    }

    private void initViews() {
        Cursor cursor = query();
        mTweetAdapter = new TweetAdapter(this, cursor, true);
        mTweetListView.setAdapter(mTweetAdapter);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.tweet_header, mTweetListView, false);
        mTweetListView.addHeaderView(mHeaderView);
    }

    private Cursor query() {
        return dataBaseHelper.getReadableDatabase().query(DataBaseContract.TweetEntry.TABLE_NAME, null, null, null, null, null, null);
    }

    private void initData() {
        AsyncTask<Void, Void, User> asyncTask = new AsyncTask<Void, Void, User>() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected User doInBackground(Void... params) {
                List<Tweet> tweetList = new ArrayList<>();
                User user = null;
                try {
                    dataBaseHelper.getReadableDatabase().delete(DataBaseContract.TweetEntry.TABLE_NAME, null, null);
                    String headerSource = FileUtils.readAssetTextFile(SecondActivity.this, "user.json");
                    String tweetSource = FileUtils.readAssetTextFile(SecondActivity.this, "tweets.json");
                    user = new Gson().fromJson(headerSource, User.class);
                    tweetList = new Gson().fromJson(tweetSource, new TypeToken<List<Tweet>>() {
                    }.getType());

                    for (Tweet tweet : tweetList) {
                        boolean noError = tweet.getError() == null && tweet.getUnknownError() == null;
                        boolean shouldDisplay = tweet.getContent() != null && tweet.getImages() != null;
                        if (noError && shouldDisplay) {
                            SQLiteDatabase database = dataBaseHelper.getWritableDatabase();
                            ContentValues contentValues = DatabaseUtils.getWeChatTweetContentValues(tweet);
                            database.insert(DataBaseContract.TweetEntry.TABLE_NAME, null, contentValues);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return user;
            }

            @Override
            protected void onProgressUpdate(Void... values) {
                super.onProgressUpdate(values);
            }

            @Override
            protected void onPostExecute(User user) {
                if (user != null) {
                    TweetHeaderHolder holder = new TweetHeaderHolder(SecondActivity.this, mHeaderView);
                    holder.populate(user);
                }
            }

        };

        asyncTask.execute();
    }

}
