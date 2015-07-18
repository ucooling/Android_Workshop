package com.thoughtworks.wechat.database;

import android.provider.BaseColumns;

/**
 * Created by lbma on 7/17/15.
 */
public class DataBaseContract {

    private DataBaseContract() {
    }

    public interface TweetEntry extends BaseColumns {
        String TABLE_NAME = "Android_dev";
        String COLUMN_NAME_CONTENT = "content";
        String COLUMN_NAME_IMAGES = "images";
        String COLUMN_NAME_SENDER = "sender";
        String COLUMN_NAME_COMMENTS = "comments";
        String COLUMN_NAME_ERROR = "error";
        String COLUMN_NAME_UNKNOW_ERROR = "unknow_error";

    }
}