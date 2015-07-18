package com.thoughtworks.wechat.utils;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.thoughtworks.wechat.database.DataBaseContract;
import com.thoughtworks.wechat.model.Image;
import com.thoughtworks.wechat.model.Sender;
import com.thoughtworks.wechat.model.Tweet;

import java.util.List;

public class DatabaseUtils {

    private static Gson gson = new Gson();

    public static ContentValues getWeChatTweetContentValues(Tweet tweet) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(DataBaseContract.TweetEntry.COLUMN_NAME_IMAGES, gson.toJson(tweet.getImages()));
        contentValues.put(DataBaseContract.TweetEntry.COLUMN_NAME_COMMENTS, gson.toJson(tweet.getComments()));
        contentValues.put(DataBaseContract.TweetEntry.COLUMN_NAME_CONTENT, tweet.getContent());
        contentValues.put(DataBaseContract.TweetEntry.COLUMN_NAME_ERROR, tweet.getError());
        contentValues.put(DataBaseContract.TweetEntry.COLUMN_NAME_SENDER, gson.toJson(tweet.getSender()));
        contentValues.put(DataBaseContract.TweetEntry.COLUMN_NAME_UNKNOW_ERROR, tweet.getUnknownError());
        return contentValues;
    }

    public static Tweet cursor2Tweet(Cursor cursor) {
        Tweet tweet = new Tweet();
        tweet.setContent(cursor.getString(cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_CONTENT)));
        List<Image> images = gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_IMAGES)), new TypeToken<List<Image>>() {
        }.getType());
        tweet.setImages(images);
        List<Object> comments = gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_COMMENTS)), new TypeToken<List<Image>>() {
        }.getType());
        tweet.setComments(comments);
        Sender sender = gson.fromJson(cursor.getString(cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_SENDER)), Sender.class);
        tweet.setSender(sender);
        tweet.setError(cursor.getString(cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_ERROR)));
        tweet.setUnknownError(cursor.getString(cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_UNKNOW_ERROR)));
        return tweet;
    }
}
