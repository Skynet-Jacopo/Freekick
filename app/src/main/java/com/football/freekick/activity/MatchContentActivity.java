package com.football.freekick.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.app.BaseActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 球賽內容頁
 */
public class MatchContentActivity extends BaseActivity {

    @Bind(R.id.tv_back)
    TextView     mTvBack;
    @Bind(R.id.tv_friend)
    TextView     mTvFriend;
    @Bind(R.id.tv_notice)
    TextView     mTvNotice;
    @Bind(R.id.ll_top)
    LinearLayout mLlTop;
    @Bind(R.id.ll_bottom)
    LinearLayout mLlBottom;
    @Bind(R.id.tv_date)
    TextView     mTvDate;
    @Bind(R.id.tv_icon_location)
    TextView     mTvIconLocation;
    @Bind(R.id.ll_location)
    LinearLayout mLlLocation;
    @Bind(R.id.tv_time)
    TextView     mTvTime;
    @Bind(R.id.iv_1_1)
    ImageView    mIv11;
    @Bind(R.id.iv_1_2)
    ImageView    mIv12;
    @Bind(R.id.iv_1_3)
    ImageView    mIv13;
    @Bind(R.id.iv_1_4)
    ImageView    mIv14;
    @Bind(R.id.iv_1_5)
    ImageView    mIv15;
    @Bind(R.id.iv_2_1)
    ImageView    mIv21;
    @Bind(R.id.iv_2_2)
    ImageView    mIv22;
    @Bind(R.id.iv_2_3)
    ImageView    mIv23;
    @Bind(R.id.iv_2_4)
    ImageView    mIv24;
    @Bind(R.id.iv_2_5)
    ImageView    mIv25;
    @Bind(R.id.iv_3_1)
    ImageView    mIv31;
    @Bind(R.id.iv_3_2)
    ImageView    mIv32;
    @Bind(R.id.iv_3_3)
    ImageView    mIv33;
    @Bind(R.id.iv_3_4)
    ImageView    mIv34;
    @Bind(R.id.iv_3_5)
    ImageView    mIv35;
    @Bind(R.id.iv_4_1)
    ImageView    mIv41;
    @Bind(R.id.iv_4_2)
    ImageView    mIv42;
    @Bind(R.id.iv_4_3)
    ImageView    mIv43;
    @Bind(R.id.iv_4_4)
    ImageView    mIv44;
    @Bind(R.id.iv_4_5)
    ImageView    mIv45;
    @Bind(R.id.iv_5_1)
    ImageView    mIv51;
    @Bind(R.id.iv_5_2)
    ImageView    mIv52;
    @Bind(R.id.iv_5_3)
    ImageView    mIv53;
    @Bind(R.id.iv_5_4)
    ImageView    mIv54;
    @Bind(R.id.iv_5_5)
    ImageView    mIv55;
    @Bind(R.id.tv_home_num)
    TextView     mTvHomeNum;
    @Bind(R.id.tv_visitor_num)
    TextView     mTvVisitorNum;
    @Bind(R.id.tv_confirm)
    TextView     mTvConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match_content);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        mTvBack.setTypeface(App.mTypeface);
        mTvFriend.setTypeface(App.mTypeface);
        mTvNotice.setTypeface(App.mTypeface);
        mTvIconLocation.setTypeface(App.mTypeface);
    }

    @OnClick({R.id.tv_back, R.id.tv_friend, R.id.tv_notice, R.id.ll_location, R.id.iv_1_1, R.id.iv_1_2, R.id.iv_1_3, R.id.iv_1_4, R.id.iv_1_5, R.id.iv_2_1, R.id.iv_2_2, R.id.iv_2_3, R.id.iv_2_4, R.id.iv_2_5, R.id.iv_3_1, R.id.iv_3_2, R.id.iv_3_3, R.id.iv_3_4, R.id.iv_3_5, R.id.iv_4_1, R.id.iv_4_2, R.id.iv_4_3, R.id.iv_4_4, R.id.iv_4_5, R.id.iv_5_1, R.id.iv_5_2, R.id.iv_5_3, R.id.iv_5_4, R.id.iv_5_5, R.id.tv_confirm})
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
                break;
            case R.id.iv_1_1:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_unselected);
                mIv13.setImageResource(R.drawable.icon_star_unselected);
                mIv14.setImageResource(R.drawable.icon_star_unselected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_1_2:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_unselected);
                mIv14.setImageResource(R.drawable.icon_star_unselected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_1_3:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_selected);
                mIv14.setImageResource(R.drawable.icon_star_unselected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_1_4:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_selected);
                mIv14.setImageResource(R.drawable.icon_star_selected);
                mIv15.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_1_5:
                mIv11.setImageResource(R.drawable.icon_star_selected);
                mIv12.setImageResource(R.drawable.icon_star_selected);
                mIv13.setImageResource(R.drawable.icon_star_selected);
                mIv14.setImageResource(R.drawable.icon_star_selected);
                mIv15.setImageResource(R.drawable.icon_star_selected);
                break;
            case R.id.iv_2_1:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_unselected);
                mIv23.setImageResource(R.drawable.icon_star_unselected);
                mIv24.setImageResource(R.drawable.icon_star_unselected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_2_2:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_unselected);
                mIv24.setImageResource(R.drawable.icon_star_unselected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_2_3:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_selected);
                mIv24.setImageResource(R.drawable.icon_star_unselected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_2_4:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_selected);
                mIv24.setImageResource(R.drawable.icon_star_selected);
                mIv25.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_2_5:
                mIv21.setImageResource(R.drawable.icon_star_selected);
                mIv22.setImageResource(R.drawable.icon_star_selected);
                mIv23.setImageResource(R.drawable.icon_star_selected);
                mIv24.setImageResource(R.drawable.icon_star_selected);
                mIv25.setImageResource(R.drawable.icon_star_selected);
                break;
            case R.id.iv_3_1:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_unselected);
                mIv33.setImageResource(R.drawable.icon_star_unselected);
                mIv34.setImageResource(R.drawable.icon_star_unselected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_3_2:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_unselected);
                mIv34.setImageResource(R.drawable.icon_star_unselected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_3_3:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_selected);
                mIv34.setImageResource(R.drawable.icon_star_unselected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_3_4:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_selected);
                mIv34.setImageResource(R.drawable.icon_star_selected);
                mIv35.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_3_5:
                mIv31.setImageResource(R.drawable.icon_star_selected);
                mIv32.setImageResource(R.drawable.icon_star_selected);
                mIv33.setImageResource(R.drawable.icon_star_selected);
                mIv34.setImageResource(R.drawable.icon_star_selected);
                mIv35.setImageResource(R.drawable.icon_star_selected);
                break;
            case R.id.iv_4_1:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_unselected);
                mIv43.setImageResource(R.drawable.icon_star_unselected);
                mIv44.setImageResource(R.drawable.icon_star_unselected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_4_2:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_unselected);
                mIv44.setImageResource(R.drawable.icon_star_unselected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_4_3:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_selected);
                mIv44.setImageResource(R.drawable.icon_star_unselected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_4_4:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_selected);
                mIv44.setImageResource(R.drawable.icon_star_selected);
                mIv45.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_4_5:
                mIv41.setImageResource(R.drawable.icon_star_selected);
                mIv42.setImageResource(R.drawable.icon_star_selected);
                mIv43.setImageResource(R.drawable.icon_star_selected);
                mIv44.setImageResource(R.drawable.icon_star_selected);
                mIv45.setImageResource(R.drawable.icon_star_selected);
                break;
            case R.id.iv_5_1:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_unselected);
                mIv53.setImageResource(R.drawable.icon_star_unselected);
                mIv54.setImageResource(R.drawable.icon_star_unselected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_5_2:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_unselected);
                mIv54.setImageResource(R.drawable.icon_star_unselected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_5_3:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_selected);
                mIv54.setImageResource(R.drawable.icon_star_unselected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_5_4:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_selected);
                mIv54.setImageResource(R.drawable.icon_star_selected);
                mIv55.setImageResource(R.drawable.icon_star_unselected);
                break;
            case R.id.iv_5_5:
                mIv51.setImageResource(R.drawable.icon_star_selected);
                mIv52.setImageResource(R.drawable.icon_star_selected);
                mIv53.setImageResource(R.drawable.icon_star_selected);
                mIv54.setImageResource(R.drawable.icon_star_selected);
                mIv55.setImageResource(R.drawable.icon_star_selected);
                break;
            case R.id.tv_confirm:
                break;
        }
    }
}
