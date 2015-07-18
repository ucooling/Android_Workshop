package com.thoughtworks.wechat;

import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.thoughtworks.wechat.adapter.HeaderAdapter;
import com.thoughtworks.wechat.adapter.TweetAdapter;
import com.thoughtworks.wechat.database.DataBaseContract;
import com.thoughtworks.wechat.database.DataBaseHelper;
import com.thoughtworks.wechat.service.TweetService;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class ServiceActivity extends AppCompatActivity {

    WechatServiceBroadcast wechatServiceBroadcast;
    ListView mTweetListView;
    private TweetAdapter mTweetAdapter;
    private HeaderAdapter mHeaderAdapter;
    private DataBaseHelper dataBaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service);
        dataBaseHelper = new DataBaseHelper(ServiceActivity.this);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.start_service)
    void startService() {
        Log.i("startService","==========startService==========");
        Intent intent = new Intent(ServiceActivity.this, TweetService.class);
        startService(intent);
    }

    @Override
    protected void onStart() {
        initViews();
        super.onStart();
    }

    @Override
    protected void onResume() {
        wechatServiceBroadcast = new WechatServiceBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.thoughtworks.wechat.service.WECHAT");
        registerReceiver(wechatServiceBroadcast, filter);
        Log.i("ServiceActivity", "*****************registerReceiver**************");
    }

    private void initViews() {
        initHeaderView();
        initItemView();

    }

    private void initHeaderView() {
//        Cursor cursor = dataBaseHelper.getReadableDatabase().query(DataBaseContract.UserEntry.TABLE_NAME, null, null, null, null, null,null);
//        mHeaderAdapter = new HeaderAdapter(this, cursor,true);
//        mTweetListView.setAdapter(mHeaderAdapter);
//        System.out.println(cursor);
//        System.out.println("======================");


    }

    private void initItemView() {
        Cursor cursor = dataBaseHelper.getReadableDatabase().query(DataBaseContract.TweetEntry.TABLE_NAME, null, null, null, null, null, null);
        System.out.println(cursor);
        System.out.println("======================");
        mTweetAdapter = new TweetAdapter(this, cursor, true);
        mTweetListView.setAdapter(mTweetAdapter);
    }




}
