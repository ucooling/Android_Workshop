package com.thoughtworks.wechat;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lbma on 7/18/15.
 */
public class WechatServiceBroadcast extends BroadcastReceiver {

    private final static String WECHAT_RESULT="com.thoughtworks.wechat.service.WECHAT";

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if(action.equals(WECHAT_RESULT))
        {
            intent.setClass(context,ServiceActivity.class);
            context.startActivity(intent);
        }
    }
}
