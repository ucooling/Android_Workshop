package com.thoughtworks.wechat;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.widget.Toast;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.thoughtworks.wechat.model.Tweet;
import com.thoughtworks.wechat.model.User;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class StorageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.write_pref)
    void writePrefsandFiles(){
        writeData();

    }

    private void writeData() {
        new AsyncTask<Void, Void, Pair<User, List<Tweet>>>() {
            @Override
            protected Pair<User, List<Tweet>> doInBackground(Void... params) {
                List<Tweet> tweetList = new ArrayList<>();
                User user = null;
                try {
                    String url_tweet = "http://thoughtworks-ios.herokuapp.com/user/jsmith/tweets";
                    String url_user = "http://thoughtworks-ios.herokuapp.com/user/jsmith/";
                    Request request_tweet = new Request.Builder().get().url(url_tweet).build();
                    Request request_user = new Request.Builder().get().url(url_user).build();
                    OkHttpClient okHttpClient = new OkHttpClient();
                    Response response_tweet = okHttpClient.newCall(request_tweet).execute();
                    Response response_user = okHttpClient.newCall(request_user).execute();
                    if (response_tweet.isSuccessful() && response_user.isSuccessful()) {
                        tweetList = new Gson().fromJson(response_tweet.body().string(), new TypeToken<List<Tweet>>() {
                        }.getType());
                        writeFile(response_tweet.body().string());
                        user = new Gson().fromJson(response_user.body().string(), User.class);
                        writeuser(user);
                    }
                }catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        }.execute();

    }

    private void writeuser(User user) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(Constants.Users.PROFILEIMAGE,user.getProfileImage());
        editor.putString(Constants.Users.AVATAR,user.getAvatar());
        editor.putString(Constants.Users.NICK, user.getNick());
        editor.putString(Constants.Users.USERNAME, user.getUsername());
        editor.apply();
    }

    private void writeFile(String tweetList){
        System.out.println("&&&&&&&&&&&&&&&&&&&&&&&&&&");
        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput("HomeWork", Context.MODE_PRIVATE);
            outputStream.write(tweetList.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        Toast.makeText(StorageActivity.this, "successfully", Toast.LENGTH_SHORT).show();
    }

}

