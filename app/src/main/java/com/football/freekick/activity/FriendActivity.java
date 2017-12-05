package com.football.freekick.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;

/**
 * 邀請頁面(應該叫分享界面吧?)
 */
public class FriendActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);
    }
}
