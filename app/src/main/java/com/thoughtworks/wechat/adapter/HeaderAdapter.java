package com.thoughtworks.wechat.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.thoughtworks.wechat.R;
import com.thoughtworks.wechat.utils.DatabaseUtils;
import com.thoughtworks.wechat.viewholder.TweetHeaderHolder;

/**
 * Created by lbma on 7/18/15.
 */
public class HeaderAdapter extends CursorAdapter {

    public HeaderAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        System.out.println("==================");
        View convertView = LayoutInflater.from(context).inflate(R.layout.tweet_header, parent, false);
        TweetHeaderHolder holder = new TweetHeaderHolder(context, convertView);
        convertView.setTag(holder);
        return convertView;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TweetHeaderHolder holder = (TweetHeaderHolder) view.getTag();
        holder.populate(DatabaseUtils.cursor2User(cursor));
    }
}
