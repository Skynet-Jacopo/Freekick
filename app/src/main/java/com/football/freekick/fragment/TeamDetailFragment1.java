package com.football.freekick.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.football.freekick.R;
import com.football.freekick.beans.TeamDetail;
import com.football.freekick.views.PolygonsView;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 球隊詳情左側頁面
 */
public class TeamDetailFragment1 extends Fragment {

    @Bind(R.id.PolygonsView1)
    PolygonsView mPolygonsView1;
    @Bind(R.id.PolygonsView2)
    PolygonsView mPolygonsView2;
    @Bind(R.id.iv_1_1_1)
    ImageView mIv111;
    @Bind(R.id.iv_1_1_2)
    ImageView mIv112;
    @Bind(R.id.iv_1_1_3)
    ImageView mIv113;
    @Bind(R.id.iv_1_1_4)
    ImageView mIv114;
    @Bind(R.id.iv_1_1_5)
    ImageView mIv115;
    @Bind(R.id.iv_1_2_1)
    ImageView mIv121;
    @Bind(R.id.iv_1_2_2)
    ImageView mIv122;
    @Bind(R.id.iv_1_2_3)
    ImageView mIv123;
    @Bind(R.id.iv_1_2_4)
    ImageView mIv124;
    @Bind(R.id.iv_1_2_5)
    ImageView mIv125;
    @Bind(R.id.iv_1_3_1)
    ImageView mIv131;
    @Bind(R.id.iv_1_3_2)
    ImageView mIv132;
    @Bind(R.id.iv_1_3_3)
    ImageView mIv133;
    @Bind(R.id.iv_1_3_4)
    ImageView mIv134;
    @Bind(R.id.iv_1_3_5)
    ImageView mIv135;
    @Bind(R.id.iv_1_4_1)
    ImageView mIv141;
    @Bind(R.id.iv_1_4_2)
    ImageView mIv142;
    @Bind(R.id.iv_1_4_3)
    ImageView mIv143;
    @Bind(R.id.iv_1_4_4)
    ImageView mIv144;
    @Bind(R.id.iv_1_4_5)
    ImageView mIv145;
    @Bind(R.id.iv_1_5_1)
    ImageView mIv151;
    @Bind(R.id.iv_1_5_2)
    ImageView mIv152;
    @Bind(R.id.iv_1_5_3)
    ImageView mIv153;
    @Bind(R.id.iv_1_5_4)
    ImageView mIv154;
    @Bind(R.id.iv_1_5_5)
    ImageView mIv155;
    @Bind(R.id.iv_2_1_1)
    ImageView mIv211;
    @Bind(R.id.iv_2_1_2)
    ImageView mIv212;
    @Bind(R.id.iv_2_1_3)
    ImageView mIv213;
    @Bind(R.id.iv_2_1_4)
    ImageView mIv214;
    @Bind(R.id.iv_2_1_5)
    ImageView mIv215;
    @Bind(R.id.iv_2_2_1)
    ImageView mIv221;
    @Bind(R.id.iv_2_2_2)
    ImageView mIv222;
    @Bind(R.id.iv_2_2_3)
    ImageView mIv223;
    @Bind(R.id.iv_2_2_4)
    ImageView mIv224;
    @Bind(R.id.iv_2_2_5)
    ImageView mIv225;
    @Bind(R.id.iv_2_3_1)
    ImageView mIv231;
    @Bind(R.id.iv_2_3_2)
    ImageView mIv232;
    @Bind(R.id.iv_2_3_3)
    ImageView mIv233;
    @Bind(R.id.iv_2_3_4)
    ImageView mIv234;
    @Bind(R.id.iv_2_3_5)
    ImageView mIv235;
    @Bind(R.id.iv_2_4_1)
    ImageView mIv241;
    @Bind(R.id.iv_2_4_2)
    ImageView mIv242;
    @Bind(R.id.iv_2_4_3)
    ImageView mIv243;
    @Bind(R.id.iv_2_4_4)
    ImageView mIv244;
    @Bind(R.id.iv_2_4_5)
    ImageView mIv245;
    @Bind(R.id.iv_2_5_1)
    ImageView mIv251;
    @Bind(R.id.iv_2_5_2)
    ImageView mIv252;
    @Bind(R.id.iv_2_5_3)
    ImageView mIv253;
    @Bind(R.id.iv_2_5_4)
    ImageView mIv254;
    @Bind(R.id.iv_2_5_5)
    ImageView mIv255;
    @Bind(R.id.tv_district)
    TextView mTvDistrict;
    @Bind(R.id.tv_establish_time)
    TextView mTvEstablishTime;
    @Bind(R.id.tv_team_num)
    TextView mTvTeamNum;
    @Bind(R.id.tv_team_average_height)
    TextView mTvTeamAverageHeight;
    @Bind(R.id.tv_team_average_age)
    TextView mTvTeamAverageAge;
    @Bind(R.id.tv_team_style)
    TextView mTvTeamStyle;
    @Bind(R.id.tv_vs_hobby)
    TextView mTvVsHobby;
    private TeamDetail.TeamBean mTeam;

    public TeamDetailFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_team_detail_fragment1, container, false);
        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTeam = (TeamDetail.TeamBean) getArguments().getSerializable("mTeam");
        initView();
    }

    private void initView() {
        mPolygonsView1.setVertexs(5);//設置頂點個數
        mPolygonsView1.setPolygonCount(5);//多邊形個數
        mPolygonsView1.setDiagonalsLineEnable(false);//对角线是否开启
        mPolygonsView1.setProgressLineColor(Color.BLACK);//進度縣的顏色
        mPolygonsView1.setProgressLineWidth(2f);//進度縣的寬度
        //顏色寬度
        mPolygonsView1.setEdgeWidth(0, 5f);
        mPolygonsView1.setEdgeWidth(1, 5f);
        mPolygonsView1.setEdgeWidth(2, 5f);
        mPolygonsView1.setEdgeWidth(3, 5f);
        mPolygonsView1.setEdgeWidth(4, 5f);
        //文字
        mPolygonsView1.setVertexText(0, getString(R.string.attack));
        mPolygonsView1.setVertexText(1, getString(R.string.defend));
        mPolygonsView1.setVertexText(2, getString(R.string.ability));
        mPolygonsView1.setVertexText(3, getString(R.string.quality));
        mPolygonsView1.setVertexText(4, getString(R.string.punctuality));
        //指示器進度
        mPolygonsView1.setProgress(0, 10);
        mPolygonsView1.setProgress(1, 60);
        mPolygonsView1.setProgress(2, 70);
        mPolygonsView1.setProgress(3, 80);
        mPolygonsView1.setProgress(4, 100);
        //多邊形顏色
        mPolygonsView1.setPolygonColor(0, Color.parseColor("#93bc69"));
        mPolygonsView1.setPolygonColor(1, Color.parseColor("#c1e69b"));
        mPolygonsView1.setPolygonColor(2, Color.parseColor("#e5f2a0"));
        mPolygonsView1.setPolygonColor(3, Color.parseColor("#f1c05f"));
        mPolygonsView1.setPolygonColor(4, Color.parseColor("#e9776f"));
        //設置文字大小
        mPolygonsView1.setVertexTextSize(0, 20f);
        mPolygonsView1.setVertexTextSize(1, 20f);
        mPolygonsView1.setVertexTextSize(2, 20f);
        mPolygonsView1.setVertexTextSize(3, 20f);
        mPolygonsView1.setVertexTextSize(4, 20f);


        mPolygonsView2.setVertexs(5);//設置頂點個數
        mPolygonsView2.setPolygonCount(5);//多邊形個數
        mPolygonsView2.setDiagonalsLineEnable(false);//对角线是否开启
        mPolygonsView2.setProgressLineColor(Color.BLACK);//進度縣的顏色
        mPolygonsView2.setProgressLineWidth(2f);//進度縣的寬度
        //顏色寬度
        mPolygonsView2.setEdgeWidth(0, 5f);
        mPolygonsView2.setEdgeWidth(1, 5f);
        mPolygonsView2.setEdgeWidth(2, 5f);
        mPolygonsView2.setEdgeWidth(3, 5f);
        mPolygonsView2.setEdgeWidth(4, 5f);
        //文字
        mPolygonsView2.setVertexText(0, getString(R.string.attack));
        mPolygonsView2.setVertexText(1, getString(R.string.defend));
        mPolygonsView2.setVertexText(2, getString(R.string.ability));
        mPolygonsView2.setVertexText(3, getString(R.string.quality));
        mPolygonsView2.setVertexText(4, getString(R.string.punctuality));
        //指示器進度
        mPolygonsView2.setProgress(0, 10);
        mPolygonsView2.setProgress(1, 60);
        mPolygonsView2.setProgress(2, 70);
        mPolygonsView2.setProgress(3, 80);
        mPolygonsView2.setProgress(4, 100);
        //多邊形顏色
        mPolygonsView2.setPolygonColor(0, Color.parseColor("#93bc69"));
        mPolygonsView2.setPolygonColor(1, Color.parseColor("#c1e69b"));
        mPolygonsView2.setPolygonColor(2, Color.parseColor("#e5f2a0"));
        mPolygonsView2.setPolygonColor(3, Color.parseColor("#f1c05f"));
        mPolygonsView2.setPolygonColor(4, Color.parseColor("#e9776f"));
        //設置文字大小
        mPolygonsView2.setVertexTextSize(0, 20f);
        mPolygonsView2.setVertexTextSize(1, 20f);
        mPolygonsView2.setVertexTextSize(2, 20f);
        mPolygonsView2.setVertexTextSize(3, 20f);
        mPolygonsView2.setVertexTextSize(4, 20f);

        mTvDistrict.setText(mTeam.getDistrict().getRegion() + mTeam.getDistrict().getDistrict());
        mTvEstablishTime.setText(mTeam.getEstablish_year() + "");
        mTvTeamNum.setText(mTeam.getSize() + "");
        int average_height = mTeam.getAverage_height();
        switch (average_height) {
            case 1:
                mTvTeamAverageHeight.setText(getString(R.string.under_160cm));
                break;
            case 2:
                mTvTeamAverageHeight.setText("161cm - 170cm");
                break;
            case 3:
                mTvTeamAverageHeight.setText("171cm - 180cm");
                break;
            case 4:
                mTvTeamAverageHeight.setText(getString(R.string.above_180cm));
                break;
        }
        String style = mTeam.getStyle().get(0);
        switch (style) {
            case "short_pass":
                mTvTeamStyle.setText(getString(R.string.short_pass));
                break;
            case "long pass":
                mTvTeamStyle.setText(getString(R.string.long_pass));
                break;
            case "attack":
                mTvTeamStyle.setText(getString(R.string.main_attack));
                break;
            default:
                mTvTeamStyle.setText(getString(R.string.short_pass));
                break;
        }
        String battle_preference = mTeam.getBattle_preference().get(0);
        switch (battle_preference) {
            case "for_fun":
                mTvVsHobby.setText(getString(R.string.for_fun));
                break;
            case "become_strong":
                mTvVsHobby.setText(getString(R.string.become_strong));
                break;
            default:
                mTvVsHobby.setText(getString(R.string.for_fun));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
