package com.thoughtworks.wechat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.thoughtworks.wechat.R;
import com.thoughtworks.wechat.utils.DatabaseUtils;
import com.thoughtworks.wechat.viewholder.TweetItemViewHolder;

public class TweetAdapter extends CursorAdapter {


    public TweetAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View convertView = LayoutInflater.from(context).inflate(R.layout.tweet_item_view, parent, false);
        TweetItemViewHolder holder = new TweetItemViewHolder(context, convertView);
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TweetItemViewHolder holder = (TweetItemViewHolder) view.getTag();
        holder.populate(DatabaseUtils.cursor2Tweet(cursor));

//        int imageIndex = cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_IMAGES);
//        Gson gson = new Gson();
//        Tweet tweet = new Tweet();
//        tweet.setImages(gson.fromJson(cursor.getString(imageIndex), new TypeToken<List<Image>>() {
//        }.getType()));
    }

    /***
     *         Gson gson = new Gson();
     Tweet tweet = new Tweet();
     tweet.setContent(cursor.getString(cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_CONTENT)));
     int columnIndex = cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_COMMENTS);
     tweet.setComments(gson.fromJson(cursor.getString(columnIndex),
     new TypeToken<List<Object>>() {
     }.getType()));
     tweet.setError(cursor.getString(cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_ERROR)));
     int imageIndex = cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_IMAGES);
     tweet.setImages(gson.fromJson(cursor.getString(imageIndex), new TypeToken<List<Image>>(){}.getType()));
     tweet.setSender(cursor.getString(cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_SENDER)));
     tweet.setUnknownError(cursor.getString(cursor.getColumnIndex(DataBaseContract.TweetEntry.COLUMN_NAME_UNKNOW_ERROR)));
     */
}
