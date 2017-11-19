package com.football.freekick;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;

import com.football.freekick.app.BaseActivity;
import com.football.freekick.fragment.EstablishFragment;
import com.football.freekick.fragment.MineFragment;
import com.football.freekick.fragment.PartakeFragment;
import com.football.freekick.fragment.RecordFragment;
import com.football.freekick.fragment.SetUpFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 主界面(替代FragmentTabHosts)
 */
public class MainActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener {

    @Bind(R.id.xian)
    View           mXian;
    @Bind(R.id.main_establish)
    RadioButton    mMainEstablish;
    @Bind(R.id.main_partake)
    RadioButton    mMainPartake;
    @Bind(R.id.main_record)
    RadioButton    mMainRecord;
    @Bind(R.id.main_mine)
    RadioButton    mMainMine;
    @Bind(R.id.main_set_up)
    RadioButton    mMainSetUp;
    @Bind(R.id.main_radiogroup)
    RadioGroup     mainRadiogroup;
    @Bind(R.id.main_group)
    FrameLayout    mMainGroup;
    @Bind(R.id.rl_content)
    RelativeLayout mRlContent;

    private FragmentManager     fm;
    private FragmentTransaction ft;

    private String TAG = "MainActivity";

    private EstablishFragment mEstablishFragment;
    private PartakeFragment   mPartakeFragment;
    private RecordFragment    mRecordFragment;
    private MineFragment      mMineFragment;
    private SetUpFragment     mSetUpFragment;

    private int save;
    private Context mContext;
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

    private void initView() {
        fm = getSupportFragmentManager();
        mainRadiogroup.setOnCheckedChangeListener(this);
        int which = getIntent().getIntExtra("which", 1);
        switch (which){
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
                if (mPartakeFragment == null) {
                    mPartakeFragment = new PartakeFragment();
                    ft.add(R.id.main_group, mPartakeFragment);
                } else {
                    ft.show(mPartakeFragment);
                }
                break;
            case R.id.main_record:
                if (mRecordFragment == null) {
                    mRecordFragment = new RecordFragment();
                    ft.add(R.id.main_group, mRecordFragment);
                } else {
                    ft.show(mRecordFragment);
                }
                break;
            case R.id.main_mine:
                if (mMineFragment == null) {
                    mMineFragment = new MineFragment();
                    ft.add(R.id.main_group, mMineFragment);
                } else {
                    ft.show(mMineFragment);
                }
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
        ft.commitAllowingStateLoss();
    }

    private void hideFragment() {
        ft = fm.beginTransaction();
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
