package com.football.freekick.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.beans.AvailableMatches;
import com.football.freekick.http.Url;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by 90516 on 2017/11/22.
 */

public class PartakeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private AvailableMatches.MatchesBean mMatchesBean;
    private Context mContext;
    private static final int TYPE_1 = 1111;//搵場列表
    private static final int TYPE_2 = 2222;//廣告

    private String picUrl = "http://www.cnr.cn/china/xwwgf/201111/W020111128658021231674.jpg";
    private String mPitchName;
    private String mLocation;

    public PartakeAdapter(AvailableMatches.MatchesBean mMatchesBean, Context mContext) {
        this.mMatchesBean = mMatchesBean;
        this.mContext = mContext;
        for (int i = 0; i < App.mPitchesBeanList.size(); i++) {
            if (App.mPitchesBeanList.get(i).getId() == mMatchesBean.getPitch_id()) {
                mPitchName = App.mPitchesBeanList.get(i).getName();
                mLocation = App.mPitchesBeanList.get(i).getLocation();
            }
        }
        mMatchesBean.getPitch_id();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_1) {
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_partake, parent, false);
            MyHolder1 myHolder1 = new MyHolder1(view1);
            AutoUtils.auto(view1);
            return myHolder1;
        } else if (viewType == TYPE_2) {
            View view2 = LayoutInflater.from(mContext).inflate(R.layout.item_partake_advertisement, parent, false);
            MyHolder2 myHolder2 = new MyHolder2(view2);
            AutoUtils.auto(view2);
            return myHolder2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AvailableMatches.MatchesBean.JoinMatchesBean joinMatchesBean = mMatchesBean.getJoin_matches().get(position);


        if (holder instanceof MyHolder1) {
            final MyHolder1 myHolder1 = (MyHolder1) holder;
            myHolder1.tvPitchName.setText(mPitchName);
            myHolder1.tvLocation.setText(mLocation);
            String start = JodaTimeUtil.getTimeHourMinutes(mMatchesBean.getPlay_start());
            String end = JodaTimeUtil.getTimeHourMinutes(mMatchesBean.getPlay_end());

            myHolder1.tvTime.setText(start + "-" + end);
            myHolder1.ivDressHome.setBackgroundColor(Color.parseColor("#" + mMatchesBean.getHome_team_color()));
            myHolder1.ivDressVisitor.setBackgroundColor(Color.parseColor("#" + joinMatchesBean.getJoin_team_color()));
            myHolder1.tvHomeName.setText(PrefUtils.getString(App.APP_CONTEXT, "team_name", null));
            myHolder1.tvVisitorName.setText(joinMatchesBean.getTeam().getTeam_name());
            ImageLoaderUtils.displayImage(Url.BaseImageUrl+mMatchesBean.getHome_team().getImage().getUrl(),myHolder1.ivPic);
            String status = joinMatchesBean.getStatus();
            switch (status) {
                case "w":
                    myHolder1.tvState.setText(R.string.join_match);
                    myHolder1.tvState.setBackgroundResource(R.drawable.selector_round_green_gray_bg);
                    myHolder1.tvState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            click.Clike(2, myHolder1.tvState, position);
                        }
                    });
                    break;
                case "m":
                    myHolder1.tvState.setText(R.string.match_success);
                    myHolder1.tvState.setBackgroundResource(R.drawable.selector_round_red_gray_bg);
                    myHolder1.tvState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            click.Clike(3, myHolder1.tvState, position);
                        }
                    });
                    break;
                case "f":

                    break;
                case "c":

                    break;
                case "i":

                    break;
            }

            myHolder1.lLContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.Clike(1, myHolder1.lLContent, position);
                }
            });
            myHolder1.ivShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.Clike(4, myHolder1.lLContent, position);
                }
            });
        } else if (holder instanceof MyHolder2) {
            MyHolder2 myHolder2 = (MyHolder2) holder;
        }
    }

    @Override
    public int getItemCount() {
        return mMatchesBean.getJoin_matches().size() == 0 ? 0 : mMatchesBean.getJoin_matches().size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 3 != 0) {
            return TYPE_1;
        } else {
//            return TYPE_2;
            return TYPE_1;
        }
    }

    public static class MyHolder1 extends RecyclerView.ViewHolder {
        private TextView tvPitchName, tvHomeName, tvVisitorName;
        private TextView tvLocation;
        private TextView tvIconLocation;
        private TextView tvTime;
        private TextView tvState;
        private LinearLayout lLContent;
        private ImageView ivPic,ivShare;
        private ImageView ivDressHome;
        private ImageView ivDressVisitor;

        public MyHolder1(View itemView) {
            super(itemView);
            tvPitchName = (TextView) itemView.findViewById(R.id.tv_pitch_name);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvIconLocation = (TextView) itemView.findViewById(R.id.tv_icon_location);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvHomeName = (TextView) itemView.findViewById(R.id.tv_home_name);
            tvVisitorName = (TextView) itemView.findViewById(R.id.tv_visitor_name);
            ivPic = (ImageView) itemView.findViewById(R.id.iv_pic);
            ivShare = (ImageView) itemView.findViewById(R.id.iv_share);
            ivDressHome = (ImageView) itemView.findViewById(R.id.iv_dress_home);
            ivDressVisitor = (ImageView) itemView.findViewById(R.id.iv_dress_visitor);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            lLContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }
    }

    public static class MyHolder2 extends RecyclerView.ViewHolder {
        private ImageView ivAdvertisement;
        private LinearLayout lLContent;

        public MyHolder2(View itemView) {
            super(itemView);
            ivAdvertisement = (ImageView) itemView.findViewById(R.id.iv_advertisement);
            lLContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }
    }

    private Click click;

    public void setClick(Click click) {
        this.click = click;
    }

    public interface Click {
        void Clike(int state, View view, int position);
    }

}
