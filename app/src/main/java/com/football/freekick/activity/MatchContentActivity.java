package com.football.freekick.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;

/**
 * 球賽內容頁
 */
public class MatchContentActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_content);
    }
}
