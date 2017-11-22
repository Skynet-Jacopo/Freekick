package com.football.freekick.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.R;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by 90516 on 2017/11/22.
 */

public class PartakeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> mList;
    private Context      mContext;
    private static final int TYPE_1 = 1111;//搵場列表
    private static final int TYPE_2 = 2222;//廣告

    private String picUrl = "http://www.cnr.cn/china/xwwgf/201111/W020111128658021231674.jpg";

    public PartakeAdapter(List<String> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_1) {
            View      view1     = LayoutInflater.from(mContext).inflate(R.layout.item_partake, parent, false);
            MyHolder1 myHolder1 = new MyHolder1(view1);
            AutoUtils.auto(view1);
            return myHolder1;
        } else if (viewType == TYPE_2) {
            View      view2     = LayoutInflater.from(mContext).inflate(R.layout.item_partake_advertisement, parent, false);
            MyHolder2 myHolder2 = new MyHolder2(view2);
            AutoUtils.auto(view2);
            return myHolder2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final String s = mList.get(position);

        if (holder instanceof MyHolder1) {
            final MyHolder1 myHolder1 = (MyHolder1) holder;
            myHolder1.tvPitchName.setText(s);
            myHolder1.lLContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.Clike(myHolder1.lLContent,position);
                }
            });
            myHolder1.tvState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.Clike(myHolder1.tvState,position);
                }
            });
        } else if (holder instanceof MyHolder2) {
            MyHolder2 myHolder2 = (MyHolder2) holder;
        }
    }

    @Override
    public int getItemCount() {
        return mList.size() == 0 ? 0 : mList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 3 != 0) {
            return TYPE_1;
        } else {
            return TYPE_2;
        }
    }

    public static class MyHolder1 extends RecyclerView.ViewHolder {
        private TextView     tvPitchName;
        private TextView     tvLocation;
        private TextView     tvIconLocation;
        private TextView     tvTime;
        private TextView     tvState;
        private LinearLayout lLContent;
        private ImageView    ivPic;
        private ImageView    ivDressHome;
        private ImageView    ivDressVisitor;

        public MyHolder1(View itemView) {
            super(itemView);
            tvPitchName = (TextView) itemView.findViewById(R.id.tv_pitch_name);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvIconLocation = (TextView) itemView.findViewById(R.id.tv_icon_location);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
            ivDressHome = (ImageView) itemView.findViewById(R.id.iv_dress_home);
            ivDressVisitor = (ImageView) itemView.findViewById(R.id.iv_dress_visitor);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            lLContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }
    }

    public static class MyHolder2 extends RecyclerView.ViewHolder {
        private ImageView    ivAdvertisement;
        private LinearLayout lLContent;

        public MyHolder2(View itemView) {
            super(itemView);
            ivAdvertisement = (ImageView) itemView.findViewById(R.id.iv_advertisement);
            lLContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }
    }
    private Click click;
    public void setClick(Click click){
        this.click=click;
    }
    public  interface Click{
        void Clike(View view,int position);
    }

}
