package com.football.freekick;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.football.freekick.app.BaseActivity;
import com.football.freekick.commons.FragmentTabHost;
import com.football.freekick.fragment.EstablishFragment;
import com.football.freekick.fragment.MineFragment;
import com.football.freekick.fragment.PartakeFragment;
import com.football.freekick.fragment.RecordFragment;
import com.football.freekick.fragment.SetUpFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {

    @Bind(R.id.realtabcontent)
    FrameLayout     mRealtabcontent;
    @Bind(R.id.fth_tabhost)
    FragmentTabHost mFthTabhost;
    @Bind(R.id.main_activity_linear)
    LinearLayout    mMainActivityLinear;

    /**
     * Fragment数组界面
     */
    private Class mFragmentArray[] = {EstablishFragment.class, PartakeFragment.class,
            RecordFragment.class, MineFragment.class, SetUpFragment.class};

    /**
     * 布局填充器
     */
    private LayoutInflater mLayoutInflater;

    /**
     * 存放图片数组
     */
    private int    mImageArray[] = {R.drawable.selector_establish,
            R.drawable.selector_partake, R.drawable.selector_record,
            R.drawable.selector_mine,R.drawable.selector_set_up};
    /**
     * 选项卡文字
     */
//    private String mTextArray[]  = {"建立球賽", "參與球賽", "作賽記錄", "我的球賽", "設定"};
    private int mTextArray[]  = {R.string.establish, R.string.partake, R.string.record, R.string.mine, R.string.set_up};
    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mLayoutInflater = LayoutInflater.from(this);
        initView();
    }

    private void initView() {
        // 找到TabHost
        mFthTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        // 得到fragment的个数
        final int count = mFragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 给每个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mFthTabhost.newTabSpec(getResources().getString(mTextArray[i])).setIndicator(
                    getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mFthTabhost.addTab(tabSpec, mFragmentArray[i], null);
            // 设置Tab按钮的背景
//            mFthTabhost.getTabWidget().getChildAt(i)
//             .setBackgroundResource(R.color.cardview_light_background);
            mFthTabhost.getTabWidget().setDividerDrawable(R.color.white);

        }
        mFthTabhost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {

            }
        });
    }
    /**
     * 给每个Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View      view      = mLayoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.bar_iv);
        imageView.setImageResource(mImageArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.bar_tv);
        textView.setText(mTextArray[index]);
        return view;
    }

}
