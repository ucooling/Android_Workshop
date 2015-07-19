package com.thoughtworks.wechat;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;

import com.thoughtworks.wechat.adapter.TweetAdapter;
import com.thoughtworks.wechat.broadcast.DataLoaderBroadcastReceiver;
import com.thoughtworks.wechat.database.DataBaseContract;
import com.thoughtworks.wechat.database.DataBaseHelper;
import com.thoughtworks.wechat.service.TweetService;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class ServiceActivity extends AppCompatActivity {

    @InjectView(R.id.listview)
    ListView mTweetListView;
    private View mHeaderView;
    private TweetAdapter mTweetAdapter;
    private Intent srviceIntent = null;
    private DataBaseHelper dataBaseHelper;
    DataLoaderBroadcastReceiver mDataLoaderReceiver = new DataLoaderBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        ButterKnife.inject(this);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter();
        filter.addAction(TweetService.ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mDataLoaderReceiver, filter);
        startService();
    }

    @Override
    protected void onPause() {
        super.onPause();

        LocalBroadcastManager.getInstance(this).unregisterReceiver(mDataLoaderReceiver);
    }

    private void startService() {
        srviceIntent = new Intent(TweetService.ACTION);
        srviceIntent.setClass(this, TweetService.class);
        startService(srviceIntent);
    }

    private void initViews() {
        dataBaseHelper = new DataBaseHelper(this);
        Cursor cursor = dataBaseHelper.getReadableDatabase().query(DataBaseContract.TweetEntry.TABLE_NAME, null, null, null, null, null, null);
        mTweetAdapter = new TweetAdapter(this, cursor, false);
        mHeaderView = LayoutInflater.from(this).inflate(R.layout.tweet_header, mTweetListView, false);
        mTweetListView.addHeaderView(mHeaderView);
        mTweetListView.setAdapter(mTweetAdapter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mDataLoaderReceiver);
        stopService(srviceIntent);
    }
}
