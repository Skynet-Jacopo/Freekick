package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.CalenderActivity;
import com.football.freekick.R;
import com.football.freekick.activity.AdvertisementDetailActivity;
import com.football.freekick.activity.FiltrateActivity;
import com.football.freekick.activity.FriendActivity;
import com.football.freekick.activity.JoinMatchActivity;
import com.football.freekick.activity.MatchContentActivity1;
import com.football.freekick.activity.NoticeActivity;
import com.football.freekick.adapter.PartakeAdapter;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.beans.AvailableMatches;
import com.football.freekick.beans.JoinMatch;
import com.football.freekick.beans.NoMatches;
import com.football.freekick.http.Url;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.MypopupWindow;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartakeListFragment extends BaseFragment {
    public static final int CHOOSE_DATE = 2;
    public static final int FILTRATE_REQUEST_CODE = 1;
    public static final int REQUEST_CODE_DETAIL = 3;

    public static PartakeListFragment mPartakeListFragment;
    @Bind(R.id.text)
    TextView mText;
    @Bind(R.id.tv_icon_filtrate)
    TextView mTvIconFiltrate;
    @Bind(R.id.tv_friend)
    TextView mTvFriend;
    @Bind(R.id.tv_notice)
    TextView mTvNotice;
    @Bind(R.id.tv_icon_left)
    TextView mTvIconLeft;
    @Bind(R.id.tv_icon_date)
    TextView mTvIconDate;
    @Bind(R.id.tv_icon_right)
    TextView mTvIconRight;
    @Bind(R.id.tv_pitch_size)
    TextView mTvPitchSize;
    @Bind(R.id.tv_icon_down)
    TextView mTvIconDown;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.recycler_partake)
    RecyclerView mRecyclerPartake;
    @Bind(R.id.ll_content)
    LinearLayout mLlContent;
    private PartakeFragment mPartakeFragment;
    private Context mContext;


    private DateTime mDateTime;
    private String pitch_size;
    private String district_id;
    private MypopupWindow mPopupWindow;
    private String mStartTime = "00:00";
    private String mEndTime = "00:00";
    private String size = "";//默認球隊人數
    private String average_height = "";//默認平均高度
    private String age_range = "";//默認球隊年齡
    private String style = "";//默認風格
    private PartakeAdapter mAdapter;
    private List<AvailableMatches.MatchesBean> mMatchList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_partake_list, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPartakeFragment = (PartakeFragment) getFragmentManager().findFragmentByTag("PartakeFragment");

        initView();
    }

    private void initView() {
        mTvIconFiltrate.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLeft.setTypeface(App.mTypeface);
        mTvIconDate.setTypeface(App.mTypeface);
        mTvIconRight.setTypeface(App.mTypeface);
        mTvIconDown.setTypeface(App.mTypeface);
    }

    @Override
    public void onResume() {
        super.onResume();
        Logger.d("onResume走了么");
        if (mPartakeFragment.isPartake) {//因為有可能回退到上個界面修改數據,然後這個Fragment只是被隱藏了,不是被銷毀了,所以在onResume里執行此操作
            mText.performClick();
            mPartakeFragment.isPartake = false;
            getAvailableAatches();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Logger.d("可見嗎--->"+hidden);
        if (mPartakeFragment.isPartake) {//因為有可能回退到上個界面修改數據,然後這個Fragment只是被隱藏了,不是被銷毀了,所以在onResume里執行此操作
            mText.performClick();
            mPartakeFragment.isPartake = false;
            getAvailableAatches();
        }
        super.onHiddenChanged(hidden);
    }

    private void getAvailableAatches() {
        if (mMatchList != null) {
            mMatchList.clear();
        }
        Logger.d("image--->" + PrefUtils.getString(App.APP_CONTEXT, "logourl", null));

        String play_start = StringUtils.getEditText(mTvDate) + " " + mStartTime + ":00";
        String play_end = StringUtils.getEditText(mTvDate) + " " + mEndTime + ":00";

        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("play_start", play_start);
        object1.addProperty("play_end", play_end);
        object1.addProperty("pitch_size", pitch_size);
        object1.addProperty("district_id", district_id);
        if (!size.equals("")) {
            object1.addProperty("size", size);
        }
        if (!average_height.equals(""))
            object1.addProperty("average_height", average_height);
        if (!age_range.equals(""))
            object1.addProperty("age_range", age_range);
        if (!style.equals(""))
            object1.addProperty("style", style);
        object.add("get_available_match_input", object1);
        Logger.json(object.toString());
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "teams/";
        Logger.d(url + PrefUtils.getString(App.APP_CONTEXT, "team_id", null) + "/available_matches");
        OkGo.post(url + PrefUtils.getString(App.APP_CONTEXT, "team_id", null) + "/available_matches")
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        if (!s.contains("[") && !s.contains("]")) {
                            NoMatches noMatches = gson.fromJson(s, NoMatches.class);
                            mMatchList.clear();
                            if (mAdapter!=null){
                                mAdapter.notifyDataSetChanged();
                            }else {
                                mAdapter = new PartakeAdapter(mMatchList, mContext);
                                mRecyclerPartake.setLayoutManager(new LinearLayoutManager(mContext));
                                if (mTvIconRight != null) {
                                    mRecyclerPartake.setHasFixedSize(true);
                                }
                                mRecyclerPartake.setAdapter(mAdapter);
                            }
                            ToastUtil.toastShort(noMatches.getMatches());
                        } else {
                            AvailableMatches matches = gson.fromJson(s, AvailableMatches.class);
                            if (matches.getMatches().size() <= 0) {
                                mMatchList.clear();
                                mAdapter.notifyDataSetChanged();
                                ToastUtil.toastShort("No records found.");
                            } else {
                                mMatchList.addAll(matches.getMatches());
                                AvailableMatches.MatchesBean matchesBean = new AvailableMatches.MatchesBean();
                                for (int i = 0; i < App.mAdvertisementsBean.size(); i++) {
                                    if (App.mAdvertisementsBean.get(i).getScreen().equals(Url.SEARCH_RESULT_001)) {
                                        matchesBean.setDefault_image(MyUtil.getImageUrl(App.mAdvertisementsBean.get(i)
                                                .getImage()));
                                    }
                                }
                                if (mMatchList.size() >= 2) {
                                    mMatchList.add(2, matchesBean);
                                } else if (mMatchList.size() == 1) {
                                    mMatchList.add(matchesBean);
                                } else if (mMatchList.size() == 0) {

                                }

                                mAdapter = new PartakeAdapter(mMatchList, mContext);
                                mRecyclerPartake.setLayoutManager(new LinearLayoutManager(mContext));
                                if (mTvIconRight != null) {
                                    mRecyclerPartake.setHasFixedSize(true);
                                }
                                mRecyclerPartake.setAdapter(mAdapter);
                                mAdapter.setClick(new PartakeAdapter.Click() {
                                    @Override
                                    public void Clike(int state, View view, int position) {
                                        /**
                                         * 1.參與約賽(詳情頁)
                                         * 2.點擊參與約賽;
                                         * 3.點擊成功約賽
                                         * 4.分享
                                         * 5.廣告
                                         * 6.已參與,待確認
                                         * 7.已參與,且已確認
                                         */
                                        Intent intent = new Intent();
                                        AvailableMatches.MatchesBean matchesBean = mMatchList.get(position);
                                        switch (state) {
                                            case 1:
                                                String status = matchesBean.getStatus();
                                                switch (status) {
                                                    case "i"://已邀請
                                                    case "w"://不展示右側以及Button
                                                        intent.setClass(mContext, JoinMatchActivity.class);
                                                        intent.putExtra("matchesBean", matchesBean);
                                                        break;
//                                                    case "m"://展示或者(如果參與對是自己的話,是不是應該有退出比賽)
//                                                        intent.setClass(mContext, ShowMatchActivity.class);
//                                                        intent.putExtra("matchesBean", matchesBean);
//                                                        break;
                                                }
                                                startActivity(intent);
                                                break;
                                            case 2:
                                                intent.setClass(mContext, JoinMatchActivity.class);
                                                intent.putExtra("matchesBean", matchesBean);
                                                startActivityForResult(intent, REQUEST_CODE_DETAIL);
//                                            joinMatch(position);//參與球賽
                                                break;
                                            case 3://成功約賽的,應該是沒啥用了
                                                intent.setClass(mContext,MatchContentActivity1.class);
                                                intent.putExtra("id",matchesBean.getId()+"");
                                                intent.putExtra("type",10);
                                                startActivity(intent);
                                                break;
                                            case 4://分享
                                                // TODO: 2017/11/30 分享
                                                shareMsg(getString(R.string.share_to),"快來看一看","這比賽不錯哦\u3000\u3000"+matchesBean.getMatch_url(),null);
                                                break;
                                            case 5://廣告
                                                // TODO: 2017/11/30 分享
                                                for (int i = 0; i < App.mAdvertisementsBean.size(); i++) {
                                                    if (App.mAdvertisementsBean.get(i).getScreen().equals(Url
                                                            .SEARCH_RESULT_001)) {
                                                        intent.setClass(mContext, AdvertisementDetailActivity.class);
                                                        intent.putExtra("name", App.mAdvertisementsBean.get(i)
                                                                .getName());
                                                        intent.putExtra("url", App.mAdvertisementsBean.get(i).getUrl());
                                                        startActivity(intent);
                                                    }
                                                }
                                                break;
                                            case 6://已參與,待確認
                                                intent.setClass(mContext, MatchContentActivity1.class);
                                                intent.putExtra("id", mMatchList.get(position).getId() + "");
                                                intent.putExtra("type", 3);
                                                startActivityForResult(intent, REQUEST_CODE_DETAIL);
                                                break;
                                            case 7://已參與,且已確認(自己是客隊)
                                                intent.setClass(mContext, MatchContentActivity1.class);
                                                intent.putExtra("id", mMatchList.get(position).getId() + "");
                                                intent.putExtra("type", 2);
                                                startActivityForResult(intent, REQUEST_CODE_DETAIL);
                                                break;
                                            case 8://已確認(不包含自己)
                                                intent.setClass(mContext,MatchContentActivity1.class);
                                                intent.putExtra("id",matchesBean.getId()+"");
                                                intent.putExtra("type",10);
                                                startActivity(intent);
                                                break;
                                            case 9://按鈕:接收邀請
                                                intent.setClass(mContext, MatchContentActivity1.class);
                                                intent.putExtra("id", matchesBean.getId() + "");
                                                intent.putExtra("type", 5);
                                                startActivity(intent);
                                                break;
                                            case 10://已被邀請,進入球賽詳情頁
                                                intent.setClass(mContext, MatchContentActivity1.class);
                                                intent.putExtra("id", matchesBean.getId() + "");
                                                intent.putExtra("type", 5);
                                                startActivity(intent);
                                                break;
                                        }
                                    }
                                });
                            }
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }

    /**
     * 參與約賽
     *
     * @param position
     */
    private void joinMatch(int position) {
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("match_id", mMatchList.get(position).getId() + "");
        object1.addProperty("join_team_id", PrefUtils.getString(App.APP_CONTEXT, "team_id", null));
        object1.addProperty("join_team_color", PrefUtils.getString(App.APP_CONTEXT, "color2", null));
        object1.addProperty("size", PrefUtils.getString(App.APP_CONTEXT, "size", null));
        object.add("join_match", object1);
        Logger.json(object.toString());
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "join_matches";
        Logger.d(url);
        OkGo.post(url)
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.tv_date, R.id.tv_icon_filtrate, R.id.tv_friend, R.id.tv_notice, R.id.tv_icon_left, R.id
            .tv_icon_date, R.id.tv_icon_right, R.id.tv_pitch_size, R.id.tv_icon_down, R.id.text})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        DateTime dateTime = null;
        switch (view.getId()) {
            case R.id.tv_icon_filtrate:
                intent.setClass(mContext, FiltrateActivity.class);//篩選界面
                intent.putExtra("start_time", mStartTime);
                intent.putExtra("end_time", mEndTime);
                intent.putExtra("district_id", district_id);
                intent.putExtra("size", size);
                intent.putExtra("average_height", average_height);
                intent.putExtra("age_range", age_range);
                intent.putExtra("style", style);
                startActivityForResult(intent, FILTRATE_REQUEST_CODE);
                break;
            case R.id.tv_friend:
                intent.setClass(mContext, FriendActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_notice:
                intent.setClass(mContext, NoticeActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_icon_left:
//                ToastUtil.toastShort("左");
                dateTime = DateTime.parse(StringUtils.getEditText(mTvDate));
                dateTime = dateTime.minusDays(1);
                mTvDate.setText(dateTime.toString("yyyy-MM-dd"));
                getAvailableAatches();
                break;
            case R.id.tv_date:
            case R.id.tv_icon_date:
//                ToastUtil.toastShort("日期");
                intent.setClass(mContext, CalenderActivity.class);
                intent.putExtra("dateTime", mDateTime);
                startActivityForResult(intent, CHOOSE_DATE);
                break;
            case R.id.tv_icon_right:
//                ToastUtil.toastShort("右");
                dateTime = DateTime.parse(StringUtils.getEditText(mTvDate));
                dateTime = dateTime.plusDays(1);
                mTvDate.setText(dateTime.toString("yyyy-MM-dd"));
                getAvailableAatches();
                break;
            case R.id.tv_pitch_size:
            case R.id.tv_icon_down:
//                ToastUtil.toastShort("球場大小");
                choosePitchSize();
                break;
            case R.id.text://模擬點擊獲取上頁數據
                Logger.d(mPartakeFragment.mStr);
                getDataFromFrontPage();
                break;
        }
    }

    /**
     * 模擬點擊獲取上頁數據
     */
    private void getDataFromFrontPage() {
        if (mPartakeFragment.dateTime == null) {
            mDateTime = new DateTime();
        } else {
            mDateTime = mPartakeFragment.dateTime;
        }
        mStartTime = mPartakeFragment.mStartTime;
        mEndTime = mPartakeFragment.mEndTime;
        pitch_size = mPartakeFragment.pitch_size;
        switch (mPartakeFragment.pitch_size) {
            case "5":
                mTvPitchSize.setText(getString(R.string.pitch_size_5));
                break;
            case "7":
                mTvPitchSize.setText(getString(R.string.pitch_size_7));
                break;
            case "11":
                mTvPitchSize.setText(getString(R.string.pitch_size_11));
                break;
        }
        mTvDate.setText(mDateTime.toString("yyyy-MM-dd"));
        district_id = mPartakeFragment.district_id;

        Logger.d(mPartakeFragment.mStartTime);
        Logger.d(mPartakeFragment.mEndTime);
        Logger.d(mPartakeFragment.pitch_size);
        Logger.d(mDateTime.toString("yyyy-MM-dd") + " " + mPartakeFragment.mStartTime);
        Logger.d(mPartakeFragment.district_id);
    }

    /**
     * 選擇場地大小
     */
    private void choosePitchSize() {
        // 一个自定义的布局，作为显示的内容
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_pitch_size_2, null);

        mPopupWindow = new MypopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        final TextView tv11 = (TextView) contentView.findViewById(R.id.tv_11);

        final TextView tv7 = (TextView) contentView.findViewById(R.id.tv_7);
        final TextView tv5 = (TextView) contentView.findViewById(R.id.tv_5);
        tv5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvPitchSize.setText(StringUtils.getEditText(tv5));
                pitch_size = "5";
                mPopupWindow.dismiss();
            }
        });
        tv7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvPitchSize.setText(StringUtils.getEditText(tv7));
                pitch_size = "7";
                mPopupWindow.dismiss();
            }
        });
        tv11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTvPitchSize.setText(StringUtils.getEditText(tv11));
                pitch_size = "11";
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setTouchable(true);
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                getAvailableAatches();
            }
        });
        // 设置好参数之后再show
        mPopupWindow.showAsDropDown(mLlContent);
//        backgroundAlpha(0.5f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }
    /**
     * 分享功能(文字圖片無法調和)
     * 微信朋友圈:用圖片可加文字,但此時其他三方應用就只有圖片,沒有文字,故而
     *
     * @param activityTitle
     *            Activity的名字
     * @param msgTitle
     *            消息标题
     * @param msgText
     *            消息内容
     * @param imgPath
     *            图片路径，不分享图片则传null
     */
    public void shareMsg(String activityTitle, String msgTitle, String msgText,
                         String imgPath) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        if (imgPath == null || imgPath.equals("")) {
            intent.setType("text/plain"); // 纯文本
        } else {
            File f = new File(imgPath);
            if (f != null && f.exists() && f.isFile()) {
                intent.setType("image/*");
                Uri u = Uri.fromFile(f);
                intent.putExtra(Intent.EXTRA_STREAM, u);
            }
        }
        intent.putExtra(Intent.EXTRA_SUBJECT, msgTitle);
        intent.putExtra(Intent.EXTRA_TEXT, msgText);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, activityTitle));
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTRATE_REQUEST_CODE && resultCode == RESULT_OK) {//篩選返回數據
            if (!StringUtils.isEmpty(data.getStringExtra("mStartTime"))) {
                mStartTime = data.getStringExtra("mStartTime");
            }
            if (!StringUtils.isEmpty(data.getStringExtra("mEndTime")))
                mEndTime = data.getStringExtra("mEndTime");
            if (!StringUtils.isEmpty(data.getStringExtra("district_id")))
                district_id = data.getStringExtra("district_id");
            if (!StringUtils.isEmpty(data.getStringExtra("size")))
                size = data.getStringExtra("size");
            if (!StringUtils.isEmpty(data.getStringExtra("average_height")))
                average_height = data.getStringExtra("average_height");
            if (!StringUtils.isEmpty(data.getStringExtra("age_range")))
                age_range = data.getStringExtra("age_range");
            if (!StringUtils.isEmpty(data.getStringExtra("style")))
                style = data.getStringExtra("style");
            getAvailableAatches();
        } else if (requestCode == CHOOSE_DATE && resultCode == RESULT_OK) {
            String day = data.getStringExtra("day");
            String month = data.getStringExtra("month");
            String year = data.getStringExtra("year");
            mDateTime = (DateTime) data.getSerializableExtra("dateTime");
//            ToastUtil.toastShort(year + "年" + month + "月" + day + "日");
//            mTvDate.setText(year + "-" + month + "-" + day);
            mTvDate.setText(mDateTime.toString("yyyy-MM-dd"));
        } else if (requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_OK) {//回來刷新
            getAvailableAatches();
        }
    }
}
