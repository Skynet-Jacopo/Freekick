package com.football.freekick.activity.registerlogin;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.football.freekick.MainActivity;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登錄后首頁(只出現一次)
 */
public class OneTimePagerActivity extends BaseActivity {

    @Bind(R.id.tv_create_match)
    TextView mTvCreateMatch;
    @Bind(R.id.tv_jion_match)
    TextView mTvJionMatch;
    @Bind(R.id.tv_record)
    TextView mTvRecord;
    @Bind(R.id.tv_my_wall)
    TextView mTvMyWall;

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_pager);
        mContext = OneTimePagerActivity.this;
        ButterKnife.bind(this);
    }

    @OnClick({R.id.tv_create_match, R.id.tv_jion_match, R.id.tv_record, R.id.tv_my_wall})
    public void onViewClicked(View view) {
        Intent intent = new Intent(mContext, MainActivity.class);
        switch (view.getId()) {
            case R.id.tv_create_match:
                intent.putExtra("which",1);
                break;
            case R.id.tv_jion_match:
                intent.putExtra("which",2);
                break;
            case R.id.tv_record:
                intent.putExtra("which",3);
                break;
            case R.id.tv_my_wall:
                intent.putExtra("which",4);
                break;
        }
        startActivity(intent);
        finish();
    }
}
