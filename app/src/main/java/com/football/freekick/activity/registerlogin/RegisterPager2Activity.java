package com.football.freekick.activity.registerlogin;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Area;
import com.football.freekick.utils.ImageUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.StringUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.RangeSeekBar;
import com.football.freekick.views.RoundImageView;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.football.freekick.views.loopview.LoopView;
import com.football.freekick.views.loopview.OnItemSelectedListener;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;

import org.joda.time.DateTime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterPager2Activity extends BaseActivity {

    private static final int RESULT_LOAD_IMAGE = 0x1235;
    private static final int RESULT_CAMERA_IMAGE = 0x4322;
    @Bind(R.id.tv_upload_pic)
    TextView mTvUploadPic;
    @Bind(R.id.tv_next)
    TextView mTvNext;
    @Bind(R.id.tv_back)
    TextView mTvBack;
    @Bind(R.id.iv_logo)
    RoundImageView mIvLogo;
    @Bind(R.id.seek_bar)
    RangeSeekBar mSeekBar;
    @Bind(R.id.nestedScrollView)
    NestedScrollView mNestedScrollView;
    @Bind(R.id.tv_height_1)
    TextView mTvHeight1;
    @Bind(R.id.tv_height_2)
    TextView mTvHeight2;
    @Bind(R.id.tv_height_3)
    TextView mTvHeight3;
    @Bind(R.id.tv_height_4)
    TextView mTvHeight4;
    @Bind(R.id.tv_change)
    TextView mTvChange;
    @Bind(R.id.ll_btn)
    LinearLayout mLlBtn;
    @Bind(R.id.edt_team_name)
    EditText mEdtTeamName;
    @Bind(R.id.tv_team_area)
    TextView mTvTeamArea;
    @Bind(R.id.tv_year)
    TextView mTvYear;
    @Bind(R.id.ll_year)
    LinearLayout mLlYear;
    @Bind(R.id.tv_reduce)
    TextView mTvReduce;
    @Bind(R.id.tv_people_num)
    TextView mTvPeopleNum;
    @Bind(R.id.tv_add)
    TextView mTvAdd;
    @Bind(R.id.tv_team_style)
    TextView mTvTeamStyle;
    @Bind(R.id.ll_team_style)
    LinearLayout mLlTeamStyle;
    @Bind(R.id.tv_team_like)
    TextView mTvTeamLike;
    @Bind(R.id.ll_team_like)
    LinearLayout mLlTeamLike;
    @Bind(R.id.rl_parent)
    RelativeLayout mRlParent;
    private Context mContext;
    private int num;
    private List<String> mYears;
    private List<String> mTeamStyle;
    private List<String> mTeamLike;
    private int mYearPos;
    private int mTeamStylePos;
    private int mLikePos;
    private String mPicLoaclUrl;
    //权限标示
    private static final int PERMISSION_WRITE_EXTERNAL_STORAGE = 3;
    private static final int CAMERA = 4;
    private Uri imageUri;
    private List<String> mRegions;//大區
    private List<Area.RegionsBean> mAreaRegions;//解析出的大區
    private List<Area.RegionsBean.DistrictsBean> mDistricts;//解析出的小區
    private List<String> districtList;//小區
    private int regionPos;
    private int districtPos;

    private String average_height = "";
    private String age_range_min = "";
    private String age_range_max = "";
    private String image = "";
    private String district="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pager2);
        mContext = RegisterPager2Activity.this;
        ButterKnife.bind(this);
        initView();
        Logger.d("access-token--->"+ PrefUtils.getString(App.APP_CONTEXT, "access_token", null));
        Logger.d("client--->"+PrefUtils.getString(App.APP_CONTEXT, "client", null));
        Logger.d("uid--->"+PrefUtils.getString(App.APP_CONTEXT, "uid", null));
        Logger.d("expiry--->"+PrefUtils.getString(App.APP_CONTEXT, "expiry", null));
    }

    private void initView() {
        num = 7;
        mTvUploadPic.setTypeface(App.mTypeface);
        mTvBack.setTypeface(App.mTypeface);
        mTvChange.setTypeface(App.mTypeface);
        age_range_min = "12";
        age_range_max = "85";
        mSeekBar.setValue(12, 85);
        mSeekBar.setOnRangeChangedListener(new RangeSeekBar.OnRangeChangedListener() {
            @Override
            public void onRangeChanged(RangeSeekBar view, float min, float max, boolean isFromUser) {
                ToastUtil.toastShort("小值" + (int) min + ",大值" + (int) max);
                age_range_min = (int) min + "";
                age_range_max = (int) max + "";
            }
        });
        mTvPeopleNum.setText(num + "");

        mYears = new ArrayList<>();//年份
        DateTime dateTime = new DateTime();
        int year = dateTime.getYear() - 30;
        for (int i = 0; i < 30; i++) {
            mYears.add((year + i) + " 年");
        }
        mYears.add("今年");

        mTeamStyle = new ArrayList<>();//風格
        mTeamStyle.add(getString(R.string.main_attack));
        mTeamStyle.add(getString(R.string.long_pass));
        mTeamStyle.add(getString(R.string.short_pass));

        mTeamLike = new ArrayList<>();
        mTeamLike.add(getString(R.string.for_fun));
        mTeamLike.add(getString(R.string.become_strong));

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
    }

    @OnClick({R.id.tv_back, R.id.tv_team_area, R.id.tv_upload_pic, R.id.iv_logo, R.id.ll_year, R.id.tv_reduce, R.id
            .tv_add, R.id.tv_height_1, R.id.tv_height_2, R.id.tv_height_3, R.id.tv_height_4, R.id.ll_team_style, R.id
            .ll_team_like, R.id.tv_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;
            case R.id.tv_team_area:
                chooseArea();
                break;
            case R.id.tv_upload_pic:
                ToastUtil.toastShort("照片");
//                takePic();
                break;
            case R.id.iv_logo:
                takePic();
                break;
            case R.id.ll_year:
                chooseYear();
                break;
            case R.id.tv_reduce:
                if (num > 1)
                    num -= 1;
                mTvPeopleNum.setText(num + "");
                break;
            case R.id.tv_add:
                if (num < 99)
                    num += 1;
                mTvPeopleNum.setText(num + "");
                break;
            case R.id.tv_height_1:
                mTvHeight1.setBackgroundResource(R.drawable.shape_corner_green);
                mTvHeight2.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight3.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight4.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight1.setTextColor(Color.WHITE);
                mTvHeight2.setTextColor(Color.BLACK);
                mTvHeight3.setTextColor(Color.BLACK);
                mTvHeight4.setTextColor(Color.BLACK);
                average_height = "1";
                break;
            case R.id.tv_height_2:
                mTvHeight1.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight2.setBackgroundResource(R.drawable.shape_corner_green);
                mTvHeight3.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight4.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight1.setTextColor(Color.BLACK);
                mTvHeight2.setTextColor(Color.WHITE);
                mTvHeight3.setTextColor(Color.BLACK);
                mTvHeight4.setTextColor(Color.BLACK);
                average_height = "2";
                break;
            case R.id.tv_height_3:
                mTvHeight1.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight2.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight3.setBackgroundResource(R.drawable.shape_corner_green);
                mTvHeight4.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight1.setTextColor(Color.BLACK);
                mTvHeight2.setTextColor(Color.BLACK);
                mTvHeight3.setTextColor(Color.WHITE);
                mTvHeight4.setTextColor(Color.BLACK);
                average_height = "3";
                break;
            case R.id.tv_height_4:
                mTvHeight1.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight2.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight3.setBackgroundResource(R.drawable.shape_corner_white);
                mTvHeight4.setBackgroundResource(R.drawable.shape_corner_green);
                mTvHeight1.setTextColor(Color.BLACK);
                mTvHeight2.setTextColor(Color.BLACK);
                mTvHeight3.setTextColor(Color.BLACK);
                mTvHeight4.setTextColor(Color.WHITE);
                average_height = "4";
                break;
            case R.id.ll_team_style:
                chooseStyle();
                break;
            case R.id.ll_team_like:
                chooseLike();
                break;
            case R.id.tv_next:
                next();
                break;
        }
    }

    /**
     * 將數據傳到下一個界面請求接口
     */
    private void next() {
        if (StringUtils.isEmpty(mEdtTeamName)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_team_name));
            return;
        }
        if (StringUtils.isEmpty(mTvTeamArea)) {
            ToastUtil.toastShort(getString(R.string.please_enter_your_team_area));
            return;
        }
        if (StringUtils.isEmpty(mTvYear)) {
            ToastUtil.toastShort(getString(R.string.please_choose_the_year_established));
            return;
        }
        if (StringUtils.isEmpty(average_height)) {
            ToastUtil.toastShort(getString(R.string.please_select_a_team_average_height));
            return;
        }

        String team_name = StringUtils.getEditText(mEdtTeamName);
        String establish_year;
        // TODO: 2017/11/19 是否要把這裡的漢字改成雙語言?
        if (StringUtils.getEditText(mTvYear).equals("今年")){
            DateTime time = new DateTime();
            establish_year = time.getYear()+"";
        }else {
            establish_year = StringUtils.getEditText(mTvYear).replace(" 年","");
        }
        String style = "";
        if (StringUtils.getEditText(mTvTeamStyle).equals(getString(R.string.short_pass))) {
            style = "short_pass";
        } else if (StringUtils.getEditText(mTvTeamStyle).equals(getString(R.string.long_pass))) {
            style = "long_pass";
        } else if (StringUtils.getEditText(mTvTeamStyle).equals(getString(R.string.main_attack))) {
            style = "attack";
        }
        String battle_preference = "";
        if (StringUtils.getEditText(mTvTeamLike).equals(getString(R.string.for_fun))) {
            battle_preference = "for_fun";
        } else if (StringUtils.getEditText(mTvTeamLike).equals(getString(R.string.become_strong))) {
            battle_preference = "become_strong";
        }
        String size = StringUtils.getEditText(mTvPeopleNum);

        Intent intent = new Intent(mContext, RegisterPager3Activity.class);
        intent.putExtra("team_name",team_name);
        intent.putExtra("district",district);
        intent.putExtra("establish_year",establish_year);
        intent.putExtra("average_height",average_height);
        intent.putExtra("age_range_min",age_range_min);
        intent.putExtra("age_range_max",age_range_max);
        intent.putExtra("style",style);
        intent.putExtra("battle_preference",battle_preference);
        intent.putExtra("size",size);
        intent.putExtra("status","a");//?這是個什麼東東??
        intent.putExtra("image",image);
        startActivity(intent);
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
                mTvTeamArea.setText(mAreaRegions.get(regionPos).getDistricts().get(districtPos).getDistrict().replace("$", " "));
                district = district_id;
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
        popupWindow.showAtLocation(mRlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 獲取照片
     */
    private void takePic() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_take_pic, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        Button btnChooseImg = (Button) contentView.findViewById(R.id.btn_choose_img);
        Button btnChoosePhone = (Button) contentView.findViewById(R.id.btn_choose_phone);
        Button btnCancel = (Button) contentView.findViewById(R.id.btn_choose_cancel);
        btnChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                //权限检查
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager
                                .PERMISSION_GRANTED) {
                    //没有权限，请求权限
                    ActivityCompat.requestPermissions(RegisterPager2Activity.this, new String[]{Manifest.permission
                                    .WRITE_EXTERNAL_STORAGE},
                            PERMISSION_WRITE_EXTERNAL_STORAGE);
                } else {
                    //打开相册
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                }

            }
        });
        btnChoosePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                getImageFromCamera();
//                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
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
        popupWindow.showAtLocation(mRlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 通過照相機獲取圖片
     */
    private void getImageFromCamera() {
        //创建一个File对象用于存储拍照后的照片
        mPicLoaclUrl = getExternalCacheDir() + "/" + System.currentTimeMillis() + ".jpg";
        File outputImage = new File(mPicLoaclUrl);
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //判断Android版本是否是Android7.0以上
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            imageUri = FileProvider.getUriForFile(RegisterPager2Activity.this, "com.football.freekick.fileprovider",
                    outputImage);
        } else {
            imageUri = Uri.fromFile(outputImage);
        }
        try {
            final String permission = Manifest.permission.CAMERA;  //相机权限
//            final String permission1 = Manifest.permission.WRITE_EXTERNAL_STORAGE; //写入数据权限
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED
//                    || ContextCompat.checkSelfPermission(this, permission1) != PackageManager.PERMISSION_GRANTED
                    ) {  //先判断是否被赋予权限，没有则申请权限
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {  //给出权限申请说明
                    ActivityCompat.requestPermissions(RegisterPager2Activity.this, new String[]{Manifest.permission
                            .CAMERA}, 1);
                } else { //直接申请权限
                    ActivityCompat.requestPermissions(RegisterPager2Activity.this, new String[]{Manifest.permission
                            .CAMERA}, CAMERA); //申请权限，可同时申请多个权限，并根据用户是否赋予权限进行判断
                }
            } else {  //赋予过权限，则直接调用相机拍照
                //启动相机程序
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                startActivityForResult(intent, RESULT_CAMERA_IMAGE);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }

    }

    /**
     * 選擇建立時間
     */
    private void chooseYear() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_year, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        LoopView loopView = (LoopView) contentView.findViewById(R.id.loop_view);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);

        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mYearPos = index;
            }
        });
        loopView.setItems(mYears);
        loopView.setCurrentPosition(mYears.size() - 1);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvYear.setText(mYears.get(mYearPos));
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
        popupWindow.showAtLocation(mRlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 選擇風格
     */
    private void chooseStyle() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_team_style, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        LoopView loopView = (LoopView) contentView.findViewById(R.id.loop_view);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);

        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mTeamStylePos = index;
            }
        });
        loopView.setItems(mTeamStyle);
        loopView.setCurrentPosition(mTeamStyle.size() - 1);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvTeamStyle.setText(mTeamStyle.get(mTeamStylePos));
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
        popupWindow.showAtLocation(mRlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 選擇對戰喜好
     */
    private void chooseLike() {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_choose_like, null);
        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        LoopView loopView = (LoopView) contentView.findViewById(R.id.loop_view);
        TextView tvConfirm = (TextView) contentView.findViewById(R.id.tv_confirm);

        loopView.setListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
                mLikePos = index;
            }
        });
        loopView.setItems(mTeamLike);
        loopView.setCurrentPosition(mTeamLike.size() - 1);

        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                mTvTeamLike.setText(mTeamLike.get(mLikePos));
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
        popupWindow.showAtLocation(mRlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getWindow().setAttributes(lp);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESULT_CAMERA_IMAGE
                && resultCode == Activity.RESULT_OK) {
            Logger.d(mPicLoaclUrl);
            ImageLoaderUtils.displayImage("file:/" + mPicLoaclUrl, mIvLogo);
            uploadImageToBase64(mPicLoaclUrl);

        } else if (requestCode == RESULT_LOAD_IMAGE
                && resultCode == Activity.RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            String picturePath = null;
            if (cursor == null) {
                if (selectedImage != null) {
                    String tmpPath = selectedImage.getPath();
                    if (tmpPath != null
                            && (tmpPath.endsWith(".jpg")
                            || tmpPath.endsWith(".jpeg")
                            || tmpPath.endsWith(".png") || tmpPath
                            .endsWith(".gif"))) {
                        picturePath = tmpPath;
                    } else {
                        ToastUtil.toastShort("相片格式不支持");
                    }
                } else {
                    ToastUtil.toastShort("相片不能选取");
                }
            } else {
                if (cursor.moveToFirst()) {
                    int columnIndex = cursor
                            .getColumnIndexOrThrow(filePathColumn[0]);
                    picturePath = cursor.getString(columnIndex);
                } else {
                    ToastUtil.toastShort("云相册相片不能选取");
                }
                cursor.close();
            }
            if (picturePath != null && !picturePath.equals("")) {
                ImageLoaderUtils.displayImage("file:/" + picturePath, mIvLogo);
                uploadImageToBase64(picturePath);
            } else {
                ToastUtil.toastShort("获取图片失败");
            }

        }
    }

    /**
     * 操作圖片toBase64
     *
     * @param picLoaclUrl
     */
    private void uploadImageToBase64(String picLoaclUrl) {
        Bitmap bitmap = ImageUtil.getimage(picLoaclUrl);
        image = ImageUtil.bitmapToBase64(bitmap);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[]
            grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //用户给予授权，打开相册
                    //打开相册
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, RESULT_LOAD_IMAGE);
                    overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                } else {
                    ToastUtil.toastShort("您没有给予授权");
                }
                break;
            case CAMERA:
                int length = grantResults.length;
                final boolean isGranted = length >= 1 && PackageManager.PERMISSION_GRANTED == grantResults[length - 1];
                if (isGranted) {  //如果用户赋予权限，则调用相机
                    getImageFromCamera();
                } else { //未赋予权限，则做出对应提示
                    ToastUtil.toastShort("您没有给予授权");
                }

                break;
        }
    }

}
