package com.football.freekick.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.AvailableMatches;
import com.football.freekick.beans.JoinMatch;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 參與球賽界面(球賽內容頁風格)
 */
public class JoinMatchActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.tv_friend)
    TextView mTvFriend;
    @Bind(R.id.tv_notice)
    TextView mTvNotice;
    @Bind(R.id.ll_top)
    LinearLayout mLlTop;
    @Bind(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.tv_icon_location)
    TextView mTvIconLocation;
    @Bind(R.id.ll_location)
    LinearLayout mLlLocation;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.iv_home_dress)
    ImageView mIvHomeDress;
    @Bind(R.id.tv_home_name)
    TextView mTvHomeName;
    @Bind(R.id.tv_home_num)
    TextView mTvHomeNum;
    @Bind(R.id.tv_visitor_name)
    TextView mTvVisitorName;
    @Bind(R.id.tv_reduce)
    TextView mTvReduce;
    @Bind(R.id.tv_visitor_num)
    TextView mTvVisitorNum;
    @Bind(R.id.tv_add)
    TextView mTvAdd;
    @Bind(R.id.tv_confirm)
    TextView mTvConfirm;
    @Bind(R.id.tv_location)
    TextView mTvLocation;
    @Bind(R.id.iv_visitor_dress)
    ImageView mIvVisitorDress;
    private AvailableMatches.MatchesBean mMatchesBean;
    private int visitorNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_match);
        ButterKnife.bind(this);
        mMatchesBean = (AvailableMatches.MatchesBean) getIntent().getSerializableExtra("matchesBean");
        initView();
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLocation.setTypeface(App.mTypeface);
        mTvDate.setText(JodaTimeUtil.getDate(mMatchesBean.getPlay_start()));
        mTvLocation.setText(mMatchesBean.getLocation());
        mTvTime.setText(JodaTimeUtil.getTimeHourMinutes(mMatchesBean.getPlay_start()) + "-" + JodaTimeUtil
                .getTimeHourMinutes(mMatchesBean.getPlay_end()));

        mTvHomeNum.setText(mMatchesBean.getSize() + "");
        mIvHomeDress.setBackgroundColor(MyUtil.getColorInt(mMatchesBean.getHome_team_color()));
        mIvVisitorDress.setBackgroundColor(MyUtil.getColorInt(PrefUtils.getString(App.APP_CONTEXT, "color2", null)));
        mTvVisitorName.setText(PrefUtils.getString(App.APP_CONTEXT, "team_name", null));
        mTvVisitorNum.setText(PrefUtils.getString(App.APP_CONTEXT, "size", null));

        visitorNum = Integer.parseInt(StringUtils.getEditText(mTvVisitorNum));
    }

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.tv_icon_location, R.id.ll_location, R.id.tv_reduce,
            R.id.tv_add, R.id.tv_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_friend:
                break;
            case R.id.tv_notice:
                break;
            case R.id.ll_location:
                ToastUtil.toastShort("定位");
                break;
            case R.id.tv_reduce:
                if (visitorNum <= 1) {
                    return;
                } else {
                    visitorNum -= 1;
                }
                mTvVisitorNum.setText(visitorNum + "");
                break;
            case R.id.tv_add:
                visitorNum += 1;
                mTvVisitorNum.setText(visitorNum + "");
                break;
            case R.id.tv_confirm:
                joinMatch();//參與球賽
                break;
        }
    }

    /**
     * 參與約
     */
    private void joinMatch() {
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("match_id", mMatchesBean.getId() + "");
        object1.addProperty("join_team_id", PrefUtils.getString(App.APP_CONTEXT, "team_id", null));
        object1.addProperty("join_team_color", PrefUtils.getString(App.APP_CONTEXT, "color2", null));
        object1.addProperty("size", PrefUtils.getString(App.APP_CONTEXT, "size", null));
        object.add("join_match", object1);
        Logger.json(object.toString());
        OkGo.post(Url.JOIN_MATCHES)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        JoinMatch joinMatch = gson.fromJson(s, JoinMatch.class);
                        if (joinMatch.getJoin_match() != null) {
                            ToastUtil.toastShort(R.string.match_success);
                        } else {

                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }
}
