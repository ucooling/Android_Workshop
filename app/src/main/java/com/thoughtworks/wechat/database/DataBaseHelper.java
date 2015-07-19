package com.thoughtworks.wechat.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by lbma on 7/17/15.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "WeChat";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DataBaseContract.TweetEntry.TABLE_NAME + " (" +
                    DataBaseContract.TweetEntry._ID + " INTEGER PRIMARY KEY," +
                    DataBaseContract.TweetEntry.COLUMN_NAME_CONTENT + TEXT_TYPE + COMMA_SEP +
                    DataBaseContract.TweetEntry.COLUMN_NAME_IMAGES + TEXT_TYPE + COMMA_SEP +
                    DataBaseContract.TweetEntry.COLUMN_NAME_SENDER + TEXT_TYPE + COMMA_SEP +
                    DataBaseContract.TweetEntry.COLUMN_NAME_COMMENTS + TEXT_TYPE + COMMA_SEP +
                    DataBaseContract.TweetEntry.COLUMN_NAME_ERROR + TEXT_TYPE + COMMA_SEP +
                    DataBaseContract.TweetEntry.COLUMN_NAME_UNKNOW_ERROR + TEXT_TYPE +
                    " )";

    private static final String SQL_CREATE_USER_ENTRIES =
            "CREATE TABLE " + DataBaseContract.UserEntry.TABLE_NAME + " (" +
                    DataBaseContract.UserEntry._ID + " INTEGER PRIMARY KEY," +
                    DataBaseContract.UserEntry.COLUMN_USER_NAME + TEXT_TYPE + COMMA_SEP +
                    DataBaseContract.UserEntry.COLUMN_NICK  + TEXT_TYPE + COMMA_SEP +
                    DataBaseContract.UserEntry.COLUMN_AVATAR + TEXT_TYPE + COMMA_SEP +
                    DataBaseContract.UserEntry.COLUMN_PROFILE_IMAGE + TEXT_TYPE +
                    ")";

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_USER_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
