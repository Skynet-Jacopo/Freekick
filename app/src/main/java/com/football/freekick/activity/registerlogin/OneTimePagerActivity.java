package com.football.freekick.activity.registerlogin;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.MainActivity;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Settings;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

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
    @Bind(R.id.iv_pic1)
    ImageView mIvPic1;
    @Bind(R.id.iv_pic2)
    ImageView mIvPic2;

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_time_pager);
        mContext = OneTimePagerActivity.this;
        ButterKnife.bind(this);
        mIvPic1.setColorFilter(Color.parseColor("#03244F"));
        mIvPic2.setColorFilter(Color.parseColor("#03244F"));
        checkoutToken();
    }

    /**
     * 檢查token是否過期(未過期顯示此頁,若過期則彈窗去登錄頁)
     */
    private void checkoutToken() {
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "settings";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Settings json = gson.fromJson(s, Settings.class);
                        if (json.getErrors()!=null&&json.getErrors().size()>0){
                            showDialog();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    @OnClick({R.id.tv_create_match, R.id.tv_jion_match, R.id.tv_record, R.id.tv_my_wall})
    public void onViewClicked(View view) {
        Intent intent = new Intent(mContext, MainActivity.class);
        switch (view.getId()) {
            case R.id.tv_create_match:
                intent.putExtra("which", 1);
                break;
            case R.id.tv_jion_match:
                intent.putExtra("which", 2);
                break;
            case R.id.tv_record:
                intent.putExtra("which", 3);
                break;
            case R.id.tv_my_wall:
                intent.putExtra("which", 4);
                break;
        }
        startActivity(intent);
        finish();
    }

    private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setTitle(R.string.warning);
        builder.setMessage(R.string.login_has_expired);
        builder.setPositiveButton(getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(mContext,LoginPager2Activity.class);
                startActivity(intent);
                finish();
            }
        });
        builder.create();
        builder.show();
    }
}
