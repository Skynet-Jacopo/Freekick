package com.football.freekick.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.football.freekick.R;
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
        mPolygonsView1.setVertexTextSize(0,20f);
        mPolygonsView1.setVertexTextSize(1,20f);
        mPolygonsView1.setVertexTextSize(2,20f);
        mPolygonsView1.setVertexTextSize(3,20f);
        mPolygonsView1.setVertexTextSize(4,20f);


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
        mPolygonsView2.setVertexTextSize(0,20f);
        mPolygonsView2.setVertexTextSize(1,20f);
        mPolygonsView2.setVertexTextSize(2,20f);
        mPolygonsView2.setVertexTextSize(3,20f);
        mPolygonsView2.setVertexTextSize(4,20f);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
