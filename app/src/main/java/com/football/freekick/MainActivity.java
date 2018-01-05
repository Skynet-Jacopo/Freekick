package com.football.freekick;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.football.freekick.app.BaseActivity;
import com.football.freekick.beans.Pitches;
import com.football.freekick.event.MainEvent;
import com.football.freekick.fragment.EstablishFragment;
import com.football.freekick.fragment.MineFragment;
import com.football.freekick.fragment.PartakeFragment;
import com.football.freekick.fragment.PartakeListFragment;
import com.football.freekick.fragment.RecordFragment;
import com.football.freekick.fragment.SetUpFragment;
import com.football.freekick.utils.ActyUtil;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 主界面(替代FragmentTabHosts)
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.xian)
    View mXian;
    @Bind(R.id.main_establish)
    RadioButton mMainEstablish;
    @Bind(R.id.main_partake)
    RadioButton mMainPartake;
    @Bind(R.id.main_record)
    RadioButton mMainRecord;
    @Bind(R.id.main_mine)
    RadioButton mMainMine;
    @Bind(R.id.main_set_up)
    RadioButton mMainSetUp;
    @Bind(R.id.main_radiogroup)
    RadioGroup mainRadiogroup;
    @Bind(R.id.main_group)
    FrameLayout mMainGroup;
    @Bind(R.id.rl_content)
    RelativeLayout mRlContent;

    private FragmentManager fm;
    private FragmentTransaction ft;

    private String TAG = "MainActivity";

    private EstablishFragment mEstablishFragment;
    private PartakeFragment mPartakeFragment;
    private RecordFragment mRecordFragment;
    private MineFragment mMineFragment;
    private SetUpFragment mSetUpFragment;
    private PartakeListFragment mPartakeListFragment;

    private int save;
    private Context mContext;
    private String msg;
    private int mEventType;

    //    private static String[] PERMISSIONS_STORAGE = {
//            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.WRITE_SETTINGS,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = MainActivity.this;
        ButterKnife.bind(this);
        if (App.mConfig.getLanguageValue().equals("zh")) {
            App.isChinese = true;
        } else {
            App.isChinese = false;
        }
        getPitches();//切換語言后,MainActivity重新創建,獲取到對應語言版本的球場信息.
        Logger.d("language--->"+App.mConfig.getLanguageValue());
        Logger.d("language--->"+App.isChinese);
        EventBus.getDefault().register(this);
        initView();
//        /**
//         * 权限判断
//         */
//        if (Build.VERSION.SDK_INT >= 23) {
//            int permission = ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE);
//            int permission1 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECORD_AUDIO);
//            int permission2 = ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_SETTINGS);
//            if (permission != PackageManager.PERMISSION_GRANTED || permission1 != PackageManager.PERMISSION_GRANTED||
//                    permission2 != PackageManager.PERMISSION_GRANTED){
//                ActivityCompat.requestPermissions(MainActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
//            }
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Api-19的手機底部導航欄在中英文切換時不變,故而手動改變
        if (App.isChinese){
            mMainEstablish.setText("開場");
            mMainPartake.setText("搵場");
            mMainRecord.setText("作賽記錄");
            mMainMine.setText("我的主牆");
            mMainSetUp.setText("設定");
        }else {
            mMainEstablish.setText("Create Match");
            mMainPartake.setText("Join Match");
            mMainRecord.setText("Records");
            mMainMine.setText("My Wall");
            mMainSetUp.setText("Settings");
        }
    }

    /**
     * 獲取場地
     */
    private void getPitches() {
        Logger.d("中文還是英文--->"+App.isChinese);
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "pitches";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson gson = new Gson();
                        Pitches pitches = gson.fromJson(s, Pitches.class);
                        App.mPitchesBeanList = pitches.getPitches();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }
    private void initView() {
        fm = getSupportFragmentManager();
        mainRadiogroup.setOnCheckedChangeListener(this);
        int which = getIntent().getIntExtra("which", 1);
        switch (which) {
            case 1:
                mainRadiogroup.check(R.id.main_establish);
                break;
            case 2:
                mainRadiogroup.check(R.id.main_partake);
                break;
            case 3:
                mainRadiogroup.check(R.id.main_record);
                break;
            case 4:
                mainRadiogroup.check(R.id.main_mine);
                break;
            case 5:
                mainRadiogroup.check(R.id.main_set_up);
                break;
        }

        //因切圖尺寸不一,故而設置drawable邊界
        Drawable drawableEstablish = getResources().getDrawable(R.drawable.selector_establish);
        drawableEstablish.setBounds(0, 0, 52, 40);
        mMainEstablish.setCompoundDrawables(null, drawableEstablish, null, null);

        Drawable drawablePartake = getResources().getDrawable(R.drawable.selector_partake);
        drawablePartake.setBounds(0, 0, 40, 40);
        mMainPartake.setCompoundDrawables(null, drawablePartake, null, null);

        Drawable drawableRecord = getResources().getDrawable(R.drawable.selector_record);
        drawableRecord.setBounds(0, 0, 52, 40);
        mMainRecord.setCompoundDrawables(null, drawableRecord, null, null);

        Drawable drawableMine = getResources().getDrawable(R.drawable.selector_mine);
        drawableMine.setBounds(0, 0, 40, 40);
        mMainMine.setCompoundDrawables(null, drawableMine, null, null);

        Drawable drawableSetUp = getResources().getDrawable(R.drawable.selector_set_up);
        drawableSetUp.setBounds(0, 0, 40, 40);
        mMainSetUp.setCompoundDrawables(null, drawableSetUp, null, null);
        mMainPartake.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEventType = -1;
                mainRadiogroup.check(R.id.main_establish);
                mainRadiogroup.check(R.id.main_partake);
            }
        });
    }

    private void showFragment(int id) {
        hideFragment();
        ft = fm.beginTransaction();
        switch (id) {
            case R.id.main_establish:
                if (mEstablishFragment == null) {
                    mEstablishFragment = new EstablishFragment();
                    ft.add(R.id.main_group, mEstablishFragment);
                } else {
                    ft.show(mEstablishFragment);
                }
                break;
            case R.id.main_partake:
                if (mEventType == 1) {
                    if (mPartakeListFragment == null) {
                        mPartakeListFragment = new PartakeListFragment();
                        ft.add(R.id.main_group, mPartakeListFragment,"PartakeListFragment");
                    } else {
                        ft.show(mPartakeListFragment);
                    }

                } else {
                    if (mPartakeFragment == null) {
                        mPartakeFragment = new PartakeFragment();
                        ft.add(R.id.main_group, mPartakeFragment,"PartakeFragment");
                    } else {
                        ft.show(mPartakeFragment);
                    }
                }

                break;
            case R.id.main_record://應要求,記錄頁和我的主牆頁每次點擊都刷新
//                if (mRecordFragment == null) {
                    mRecordFragment = new RecordFragment();
                    ft.add(R.id.main_group, mRecordFragment);
//                } else {
//                    ft.show(mRecordFragment);
//                }
                break;
            case R.id.main_mine:
//                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    ft.add(R.id.main_group, mMineFragment,"mMineFragment");
//                } else {
//                    ft.show(mMineFragment);
//                }
                break;
            case R.id.main_set_up:
                if (mSetUpFragment == null) {
                    mSetUpFragment = new SetUpFragment();
                    ft.add(R.id.main_group, mSetUpFragment);
                } else {
                    ft.show(mSetUpFragment);
                }
                break;
        }
        ft.setCustomAnimations(R.anim.in_from_right,R.anim.out_to_left,R.anim.in_from_left,R.anim.out_to_right);
        ft.commitAllowingStateLoss();
    }

    private void hideFragment() {
        ft = fm.beginTransaction();
        if (mPartakeListFragment != null) {
            ft.hide(mPartakeListFragment);
        }
        if (mEstablishFragment != null) {
            ft.hide(mEstablishFragment);
        }
        if (mPartakeFragment != null) {
            ft.hide(mPartakeFragment);
        }
        if (mRecordFragment != null) {
            ft.hide(mRecordFragment);
        }
        if (mMineFragment != null) {
            ft.hide(mMineFragment);
        }
        if (mSetUpFragment != null) {
            ft.hide(mSetUpFragment);
        }
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
        switch (i) {
            case R.id.main_establish:
                showFragment(i);
                break;
            case R.id.main_partake:
                showFragment(i);
                break;
            case R.id.main_record:
                showFragment(i);
                break;
            case R.id.main_mine:
                showFragment(i);
                break;
            case R.id.main_set_up:
                showFragment(i);
                break;
        }
        save = i;
    }

    @Subscribe(threadMode = ThreadMode.MAIN) //在ui线程执行
    public void onDataSynEvent(MainEvent event) {
        Log.e(TAG, "onDataSynEvent: " + event.getType());
        switch (event.getType()) {
            case 1:
                mEventType = event.getType();
                mainRadiogroup.check(R.id.main_establish);
                mainRadiogroup.check(R.id.main_partake);
                break;
            case 2:

                break;
            case 3:

                break;
            case 4:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 菜单、返回键响应
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if(keyCode == KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();      //调用双击退出函数
        }
        return false;
    }
    /**
     * 双击退出函数
     */
    private static Boolean isExit = false;

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true; // 准备退出
            Toast.makeText(this, R.string.press_again_exit, Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false; // 取消退出
                }
            }, 2000); // 如果2秒钟内没有按下返回键，则启动定时器取消掉刚才执行的任务

        } else {
            finish();
//            System.exit(0);
            ActyUtil.finishAllActivity();
        }
    }

    //
//    @Override
//    protected void onSaveInstanceState(Bundle outState) {
//        // super.onSaveInstanceState(outState);
//        outState.putInt("fragment", save);
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//        showFragment(savedInstanceState.getInt("fragment"));
//    }

}
