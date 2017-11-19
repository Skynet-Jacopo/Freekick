package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.CalenderActivity;
import com.football.freekick.R;
import com.football.freekick.activity.ChooseTimeActivity;
import com.football.freekick.activity.MatchInviteActivity;
import com.football.freekick.app.BaseFragment;
import com.football.freekick.beans.Area;
import com.football.freekick.beans.Matches;
import com.football.freekick.http.Url;
import com.football.freekick.utils.DateUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.loopview.LoopView;
import com.football.freekick.views.loopview.OnItemSelectedListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.AutoLinearLayout;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 建立球賽頁
 */
public class EstablishFragment extends BaseFragment {


    public static final int CHOOSE_DATE = 1;
    public static final int CHOOSE_TIME = 2;
    @Bind(R.id.tv_icon_date)
    TextView mTvIconDate;
    @Bind(R.id.ll_match_date)
    LinearLayout mLlMatchDate;
    @Bind(R.id.tv_time)
    TextView mTvTime;
    @Bind(R.id.ll_match_time)
    LinearLayout mLlMatchTime;
    @Bind(R.id.tv_pitch_size)
    TextView mTvPitchSize;
    @Bind(R.id.ll_pitch_size)
    LinearLayout mLlPitchSize;
    @Bind(R.id.tv_area)
    TextView mTvArea;
    @Bind(R.id.ll_area)
    LinearLayout mLlArea;
    @Bind(R.id.tv_pitch_name)
    TextView mTvPitchName;
    @Bind(R.id.ll_pitch_name)
    LinearLayout mLlPitchName;
    @Bind(R.id.tv_reduce)
    TextView mTvReduce;
    @Bind(R.id.tv_people_num)
    TextView mTvPeopleNum;
    @Bind(R.id.tv_add)
    TextView mTvAdd;
    @Bind(R.id.tv_establish)
    TextView mTvEstablish;
    @Bind(R.id.tv_date)
    TextView mTvDate;
    @Bind(R.id.ll_parent)
    AutoLinearLayout mLlParent;
    private Context mContext;
    private String mStartTime;
    private String mEndTime;
    private List<String> mRegions;//大區
    private List<Area.RegionsBean> mAreaRegions;//解析出的大區
    private List<Area.RegionsBean.DistrictsBean> mDistricts;//解析出的小區
    private List<String> districtList;//小區
    private int regionPos;
    private int districtPos;

    private int defaultNum = 7;//默認作賽人數
    private int mPitchPos;//球場pos
    private int pitch_id;
    private String home_team_id;
    private String home_team_color;

    public EstablishFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_establish, container, false);
        mContext = getActivity();
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTvIconDate.setTypeface(App.mTypeface);
        initData();

    }

    private void initData() {
        //初始人數
        mTvPeopleNum.setText(defaultNum + getString(R.string.peoson));
        //初始化地區數據
        String string = getString(R.string.text_area).trim();
        Gson gson = new Gson();
        Area area = gson.fromJson(string, Area.class);
        mRegions = new ArrayList<>();
        mAreaRegions = area.getRegions();
        for (int i = 0; i < mAreaRegions.size(); i++) {
            String region = mAreaRegions.get(i).getRegion();
            String s = null;
            if (region.contains("$")) {
                s = region.replace("$", " ");
                mRegions.add(s);
            } else {
                mRegions.add(region);
            }

        }
        mDistricts = mAreaRegions.get(0).getDistricts();

        //創建球隊所需
        // TODO: 2017/11/19 該從數據庫中取還是在SP中
        home_team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", "");
        home_team_color = PrefUtils.getString(App.APP_CONTEXT, "color1", "");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick({R.id.ll_pitch_name, R.id.ll_match_date, R.id.ll_match_time, R.id.ll_pitch_size, R.id.ll_area, R.id
            .tv_reduce, R.id.tv_add, R.id.tv_establish})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_match_date:
                startActivityForResult(new Intent(mContext, CalenderActivity.class), CHOOSE_DATE);
                break;
            case R.id.ll_match_time:
                startActivityForResult(new Intent(mContext, ChooseTimeActivity.class), CHOOSE_TIME);
                break;
            case R.id.ll_pitch_size:
                choosePitchSize();
                break;
            case R.id.ll_area:
                chooseArea();
                break;
            case R.id.ll_pitch_name:
                choosePitchName();
                break;
            case R.id.tv_reduce:
                if (defaultNum > 1) {
                    defaultNum -= 1;
                }
                mTvPeopleNum.setText(defaultNum + getString(R.string.peoson));
                break;
            case R.id.tv_add:
                defaultNum += 1;
                mTvPeopleNum.setText(defaultNum + getString(R.string.peoson));
                break;
            case R.id.tv_establish:
                submitEstablish();
                break;
        }
    }

    /**
     * 點擊 開場(創建比賽)
     */
    private void submitEstablish() {
        if (StringUtils.isEmpty(mTvDate)) {
            ToastUtil.toastShort(getString(R.string.please_choose_match_date));
            return;
        }
        if (StringUtils.isEmpty(mTvTime)) {
            ToastUtil.toastShort(getString(R.string.please_choose_match_time));
            return;
        }
        if (StringUtils.isEmpty(mTvPitchName)) {
            ToastUtil.toastShort(getString(R.string.please_choose_pitch));
            return;
        }
        String size = "";
        if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_5))) {
            size = "5";
        } else if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_7))) {
            size = "7";
        } else if (StringUtils.getEditText(mTvPitchSize).equals(getString(R.string.pitch_size_11))) {
            size = "11";
        }
        String play_start = "";
        play_start = StringUtils.getEditText(mTvDate) + " " + mStartTime + ":00";
        String play_end = StringUtils.getEditText(mTvDate) + " " + mEndTime + ":00";
        String confirm_end = DateUtil.getPreTime(play_start, "1");//play_start前一天

        loadingShow();
        JsonObject object = new JsonObject();
        JsonObject object1 = new JsonObject();
        object1.addProperty("home_team_id", home_team_id);
        object1.addProperty("home_team_color", home_team_color);
        object1.addProperty("size", size);
        object1.addProperty("play_start", play_start);
        object1.addProperty("play_end", play_end);
        object1.addProperty("confirm_end", confirm_end);
        object1.addProperty("pitch_id", pitch_id);
        object.add("match", object1);
        Logger.json(object.toString());
        OkGo.post(Url.MATCHES)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        Matches matches = gson.fromJson(s, Matches.class);
                        if (matches.getMatch() != null) {
                            Matches.MatchBean match = matches.getMatch();
                            String status = match.getStatus();
                            switch (status) {
                                case "w":
                                    ToastUtil.toastShort("未成功匹配到");
                                    break;
                                case "m":
                                    ToastUtil.toastShort("成功匹配");
                                    break;
                                case "f":
                                    ToastUtil.toastShort("已完成");
                                    break;
                                case "c":
                                    ToastUtil.toastShort("已取消");
                                    break;
                                case "i":
                                    ToastUtil.toastShort("已邀請");
                                    break;
                            }
                            int id = match.getId();
                            Intent intent = new Intent(mContext, MatchInviteActivity.class);
                            intent.putExtra("match_id", id + "");
                            startActivity(intent);
                        } else if (matches.getMatch() == null && matches.getErrors() != null) {
                            ToastUtil.toastShort(matches.getErrors().get(0));
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
     * 選擇球場(App中請求下來了)
     */
    private void choosePitchName() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_pitch, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        LoopView loopView = (LoopView) contentView.findViewById(R.id.loop_view);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        List<String> pitches = new ArrayList<>();
        for (int i = 0; i < App.mPitchesBeanList.size(); i++) {
            pitches.add(App.mPitchesBeanList.get(i).getName());
        }

        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mPitchPos = index;
            }
        });
        loopView.setItems(pitches);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvPitchName.setText(App.mPitchesBeanList.get(mPitchPos).getName());
                pitch_id = App.mPitchesBeanList.get(mPitchPos).getId();
                Logger.d("pitch_id--->" + pitch_id);
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        popupWindow.showAtLocation(mLlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 選擇地區
     */
    private void chooseArea() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_area, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        LoopView loopView1 = (LoopView) contentView.findViewById(R.id.loop_view1);
        final LoopView loopView2 = (LoopView) contentView.findViewById(R.id.loop_view2);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);

        loopView1.setItems(mRegions);
        loopView1.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                regionPos = index;
                districtList = new ArrayList<String>();
                mDistricts = mAreaRegions.get(index).getDistricts();
                for (int i = 0; i < mDistricts.size(); i++) {
                    String district = mDistricts.get(i).getDistrict();
                    String s = null;
                    if (district.contains("$")) {
                        s = district.replace("$", " ");
                        districtList.add(s);
                    } else {
                        districtList.add(district);
                    }
                }
                loopView2.setItems(districtList);
            }
        });
        districtList = new ArrayList<>();
        mDistricts = mAreaRegions.get(0).getDistricts();
        for (int i = 0; i < mDistricts.size(); i++) {
            String district = mDistricts.get(i).getDistrict();
            String s = null;
            if (district.contains("$")) {
                s = district.replace("$", " ");
                districtList.add(s);
            } else {
                districtList.add(district);
            }
        }
        loopView2.setItems(districtList);
        loopView2.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                districtPos = index;
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                String district_id = mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict_id();
                mTvArea.setText(mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict());
                ToastUtil.toastShort(district_id);
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        popupWindow.showAtLocation(mLlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 選擇球場大小
     */
    private void choosePitchSize() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_pitch_size, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);
        final RadioButton radio11 = (RadioButton) contentView.findViewById(R.id.radio_11);
        final RadioButton radio7 = (RadioButton) contentView.findViewById(R.id.radio_7);
        final RadioButton radio5 = (RadioButton) contentView.findViewById(R.id.radio_5);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                if (radio5.isChecked()) {
                    mTvPitchSize.setText(radio5.getText());
                } else if (radio7.isChecked()) {
                    mTvPitchSize.setText(radio7.getText());
                } else if (radio11.isChecked()) {
                    mTvPitchSize.setText(radio11.getText());
                }
                mTvPitchName.setText("");
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        popupWindow.showAtLocation(mLlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_DATE && resultCode == RESULT_OK) {
            String day = data.getStringExtra("day");
            String month = data.getStringExtra("month");
            String year = data.getStringExtra("year");
//            ToastUtil.toastShort(year + "年" + month + "月" + day + "日");
            mTvDate.setText(year + "-" + month + "-" + day);
        } else if (requestCode == CHOOSE_TIME && resultCode == RESULT_OK) {
            mStartTime = data.getStringExtra("start_time");
            mEndTime = data.getStringExtra("end_time");
            mTvTime.setText(getString(R.string.from) + " " + mStartTime + "\u3000" + getString(R.string.to) + " " +
                    mEndTime);
        }
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
}
