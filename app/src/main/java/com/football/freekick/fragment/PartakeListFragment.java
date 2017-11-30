package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import com.football.freekick.activity.FiltrateActivity;
import com.football.freekick.activity.JoinMatchActivity;
import com.football.freekick.activity.MatchContentActivity;
import com.football.freekick.adapter.PartakeAdapter;
import com.football.freekick.beans.AvailableMatches;
import com.football.freekick.beans.JoinMatch;
import com.football.freekick.http.Url;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;
import static com.football.freekick.R.string.average_height;
import static com.football.freekick.R.string.cancel;
import static com.football.freekick.R.string.join;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartakeListFragment extends Fragment {
    public static final int CHOOSE_DATE = 2;
    public static final int FILTRATE_REQUEST_CODE = 1;

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
    private String size = "2";//默認球隊人數
    private String average_height = "2";//默認平均高度
    private String age_range = "2";//默認球隊年齡
    private String style = "1";//默認風格
    private PartakeAdapter mAdapter;
    private List<AvailableMatches.MatchesBean> mMatchList;

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
        initData();
    }

    private void initData() {
        mMatchList = new ArrayList<>();
        mAdapter = new PartakeAdapter(mMatchList, mContext);
        mRecyclerPartake.setLayoutManager(new LinearLayoutManager(mContext));
        if (mTvIconRight != null) {
            mRecyclerPartake.setHasFixedSize(true);
        }
        mRecyclerPartake.setAdapter(mAdapter);
        mAdapter.setClick(new PartakeAdapter.Click() {
            @Override
            public void Clike(int state, View view, int position) {
                //1.點擊item,2.點擊參與約賽;3.點擊成功約賽
                Intent intent = new Intent();
                AvailableMatches.MatchesBean matchesBean = mMatchList.get(position);
                switch (state) {
                    case 1:
                        String status = matchesBean.getStatus();
                        switch (status) {
                            case "i"://已邀請
                            case "w"://不展示右側以及Button

                                break;
                            case "m"://展示或者(如果參與對是自己的話,是不是應該有退出比賽)

                                break;
                        }
                        intent.setClass(mContext, MatchContentActivity.class);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.setClass(mContext, JoinMatchActivity.class);
                        intent.putExtra("matchesBean", matchesBean);
                        startActivity(intent);
//                                            joinMatch(position);//參與球賽
                        break;
                    case 3://成功約賽的,應該是沒啥用了

                        break;
                    case 4://分享
                        // TODO: 2017/11/30 分享
                        ToastUtil.toastShort("分享");
                        break;
                }
            }
        });
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
        if (mPartakeFragment.isPartake) {
            mText.performClick();
            mPartakeFragment.isPartake = false;
        }
        getAvailableAatches();
    }

    private void getAvailableAatches() {
        Logger.d("image--->" + PrefUtils.getString(App.APP_CONTEXT, "logourl", null));

        String play_start = StringUtils.getEditText(mTvDate) + " " + mStartTime + ":00";
        String play_end = StringUtils.getEditText(mTvDate) + " " + mEndTime + ":00";

        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("play_start", play_start);
        object1.addProperty("play_end", play_end);
        object1.addProperty("pitch_size", pitch_size);
        object1.addProperty("district_id", district_id);
        object1.addProperty("size", size);
        object1.addProperty("average_height", average_height);
        object1.addProperty("age_range", age_range);
        object1.addProperty("style", style);
        object.add("get_available_match_input", object1);
        Logger.json(object.toString());
        Logger.d(Url.AVAILABLE_MATCHES + PrefUtils.getString(App.APP_CONTEXT, "team_id", null) + "/available_matches");
        OkGo.post(Url.AVAILABLE_MATCHES + PrefUtils.getString(App.APP_CONTEXT, "team_id", null) + "/available_matches")
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        String str = "{\n" +
                                "    \"matches\": [\n" +
                                "        {\n" +
                                "            \"id\": 7,\n" +
                                "            \"play_start\": \"2017-11-20T06:00:00.000Z\",\n" +
                                "            \"play_end\": \"2017-11-20T09:00:00.000Z\",\n" +
                                "            \"pitch_id\": 1,\n" +
                                "            \"home_team_color\": \"ffffff\",\n" +
                                "            \"status\": \"w\",\n" +
                                "            \"size\": \"5\",\n" +
                                "            \"home_team\": {\n" +
                                "                \"id\": 33,\n" +
                                "                \"image\": {\n" +
                                "                    \"url\": " +
                                "\"/uploads/team/image/33/upload-image-9724761-1510149726.\"\n" +
                                "                }\n" +
                                "            },\n" +
                                "            \"join_matches\": [\n" +
                                "                {\n" +
                                "                    \"join_team_id\": 48,\n" +
                                "                    \"status\": \"confirmation_pending\",\n" +
                                "                    \"join_team_color\": \"ffc300\",\n" +
                                "                    \"team\": {\n" +
                                "                        \"team_name\": \"Lions9dd875\",\n" +
                                "                        \"size\": 5,\n" +
                                "                        \"image\": {\n" +
                                "                            \"url\": \"/uploads/team/image/46/image.jpeg\"\n" +
                                "                        },\n" +
                                "                        \"district\": {\n" +
                                "                            \"id\": 72,\n" +
                                "                            \"district\": \"Yuen Long\",\n" +
                                "                            \"region\": \"New Territories\"\n" +
                                "                        }\n" +
                                "                    }\n" +
                                "                }\n" +
                                "            ]\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"id\": 7,\n" +
                                "            \"play_start\": \"2017-11-20T12:00:00.000Z\",\n" +
                                "            \"play_end\": \"2017-11-20T01:00:00.000Z\",\n" +
                                "            \"pitch_id\": 1,\n" +
                                "            \"home_team_color\": \"ffffff\",\n" +
                                "            \"status\": \"m\",\n" +
                                "            \"size\": \"5\",\n" +
                                "            \"home_team\": {\n" +
                                "                \"id\": 33,\n" +
                                "                \"image\": {\n" +
                                "                    \"url\": \"/uploads/team/image/46/image.jpeg\"\n" +
                                "                }\n" +
                                "            },\n" +
                                "            \"join_matches\": [\n" +
                                "                {\n" +
                                "                    \"join_team_id\": 48,\n" +
                                "                    \"status\": \"confirmed\",\n" +
                                "                    \"join_team_color\": \"ffc300\",\n" +
                                "                    \"team\": {\n" +
                                "                        \"team_name\": \"Lions9dd875\",\n" +
                                "                        \"size\": 5,\n" +
                                "                        \"image\": {\n" +
                                "                            \"url\": \"/uploads/team/image/46/image.jpeg\"\n" +
                                "                        },\n" +
                                "                        \"district\": {\n" +
                                "                            \"id\": 72,\n" +
                                "                            \"district\": \"Yuen Long\",\n" +
                                "                            \"region\": \"New Territories\"\n" +
                                "                        }\n" +
                                "                    }\n" +
                                "                }\n" +
                                "            ]\n" +
                                "        },\n" +
                                "        {\n" +
                                "            \"id\": 7,\n" +
                                "            \"play_start\": \"2017-11-20T12:00:00.000Z\",\n" +
                                "            \"play_end\": \"2017-11-20T01:00:00.000Z\",\n" +
                                "            \"pitch_id\": 1,\n" +
                                "            \"home_team_color\": \"ffffff\",\n" +
                                "            \"status\": \"m\",\n" +
                                "            \"size\": \"5\",\n" +
                                "            \"home_team\": {\n" +
                                "                \"id\": 33,\n" +
                                "                \"image\": {\n" +
                                "                    \"url\": \"/uploads/team/image/46/image.jpeg\"\n" +
                                "                }\n" +
                                "            },\n" +
                                "            \"join_matches\": [\n" +
                                "                {\n" +
                                "                    \"join_team_id\": 48,\n" +
                                "                    \"status\": \"confirmed\",\n" +
                                "                    \"join_team_color\": \"ffc300\",\n" +
                                "                    \"team\": {\n" +
                                "                        \"team_name\": \"Lions9dd875\",\n" +
                                "                        \"size\": 5,\n" +
                                "                        \"image\": {\n" +
                                "                            \"url\": \"/uploads/team/image/46/image.jpeg\"\n" +
                                "                        },\n" +
                                "                        \"district\": {\n" +
                                "                            \"id\": 72,\n" +
                                "                            \"district\": \"Yuen Long\",\n" +
                                "                            \"region\": \"New Territories\"\n" +
                                "                        }\n" +
                                "                    }\n" +
                                "                }\n" +
                                "            ]\n" +
                                "        }\n" +
                                "    ]\n" +
                                "}";

                        Gson gson = new Gson();
                        AvailableMatches matches = gson.fromJson(str, AvailableMatches.class);
                        if (matches.getMatches().size() <= 0) {
                            ToastUtil.toastShort("No records found.");
                        } else {
                            mMatchList.addAll(matches.getMatches());
                            AvailableMatches.MatchesBean matchesBean = new AvailableMatches.MatchesBean();
                            matchesBean.setDefault_image("http:\\/\\/api.freekick" +
                                    ".hk\\/uploads\\/advertisement\\/image\\/2\\/gotravel.jpg");
                            if (mMatchList.size()>=2){
                                mMatchList.add(1,matchesBean);
                            }else if (mMatchList.size() == 1){
                                mMatchList.add(matchesBean);
                            }else if (mMatchList.size() == 0){

                            }
                            mAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
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
                startActivityForResult(intent, FILTRATE_REQUEST_CODE);
                break;
            case R.id.tv_friend:
                ToastUtil.toastShort("朋友");
                break;
            case R.id.tv_notice:
                ToastUtil.toastShort("通知");
                break;
            case R.id.tv_icon_left:
//                ToastUtil.toastShort("左");
                dateTime = DateTime.parse(StringUtils.getEditText(mTvDate));
                dateTime = dateTime.minusDays(1);
                mTvDate.setText(dateTime.toString("yyyy-MM-dd"));
                break;
            case R.id.tv_date:
            case R.id.tv_icon_date:
//                ToastUtil.toastShort("日期");
                startActivityForResult(new Intent(mContext, CalenderActivity.class), CHOOSE_DATE);
                break;
            case R.id.tv_icon_right:
//                ToastUtil.toastShort("右");
                dateTime = DateTime.parse(StringUtils.getEditText(mTvDate));
                dateTime = dateTime.plusDays(1);
                mTvDate.setText(dateTime.toString("yyyy-MM-dd"));
                break;
            case R.id.tv_pitch_size:
            case R.id.tv_icon_down:
                ToastUtil.toastShort("球場大小");
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

        } else if (requestCode == CHOOSE_DATE && resultCode == RESULT_OK) {
            String day = data.getStringExtra("day");
            String month = data.getStringExtra("month");
            String year = data.getStringExtra("year");
            mDateTime = (DateTime) data.getSerializableExtra("dateTime");
//            ToastUtil.toastShort(year + "年" + month + "月" + day + "日");
            mTvDate.setText(year + "-" + month + "-" + day);
        }
    }
}
