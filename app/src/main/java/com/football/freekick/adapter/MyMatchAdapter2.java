package com.football.freekick.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.beans.MatchesComing;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 90516 on 2017/11/22.
 */

public class MyMatchAdapter2 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //    private List<String> datas;
    private ArrayList<MatchesComing.MatchesBean> mMatches;
    private Context mContext;
    private static final int TYPE_1 = 1111;//邀請
    private static final int TYPE_2 = 2222;//已邀請
    private final String team_id;
    private final Gson gson;

    public MyMatchAdapter2(ArrayList<MatchesComing.MatchesBean> datas, Context mContext) {
        this.mMatches = datas;
        this.mContext = mContext;
        team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        gson = new Gson();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_1) {
            View view1 = LayoutInflater.from(mContext).inflate(R.layout.item_my_need_match, parent, false);
            MyHolder1 myHolder1 = new MyHolder1(view1);
            AutoUtils.auto(view1);
            return myHolder1;
        } else if (viewType == TYPE_2) {
            View view2 = LayoutInflater.from(mContext).inflate(R.layout.item_my_already_matched, parent, false);
            MyHolder2 myHolder2 = new MyHolder2(view2);
            AutoUtils.auto(view2);
            return myHolder2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MatchesComing.MatchesBean matchesBean = mMatches.get(position);
        if (holder instanceof MyHolder1) {
            final MyHolder1 myHolder1 = (MyHolder1) holder;
            myHolder1.tvIconDelete.setTypeface(App.mTypeface);
            myHolder1.tvIconDelete.setVisibility(View.GONE);
            myHolder1.tvHomeName.setText(matchesBean.getHome_team().getTeam_name());
            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(matchesBean.getHome_team().getImage().getUrl()),
                    myHolder1.ivHomeLogo);

            String date = JodaTimeUtil.getDate2(matchesBean.getPlay_start());
            myHolder1.tvDate.setText(date);
            String start = JodaTimeUtil.getTime2(matchesBean.getPlay_start());
            String end = JodaTimeUtil.getTime2(matchesBean.getPlay_end());
            myHolder1.tvTime.setText(start + " - " + end);
            List<MatchesComing.MatchesBean.JoinMatchesBean> join_matches = matchesBean.getJoin_matches();
            myHolder1.tvLocation.setText(matchesBean.getLocation());
            myHolder1.tvState.setBackgroundResource(R.drawable.selector_round_green_gray_bg);
            myHolder1.tvState.setText(R.string.accept_the_invitation);
            myHolder1.tvState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //接受邀請
                    click.Click(1, myHolder1.tvState, position, 0);
                }
            });
            myHolder1.lLContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //詳情
                    click.Click(2, myHolder1.lLContent, position, 0);
                }
            });

        } else if (holder instanceof MyHolder2) {
            final MyHolder2 myHolder2 = (MyHolder2) holder;
            myHolder2.tvIconDelete.setTypeface(App.mTypeface);

        }
    }

    @Override
    public int getItemCount() {
        return mMatches.size() == 0 ? 0 : mMatches.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 != 0) {
            return TYPE_1;
        } else {
//            return TYPE_2;
            return TYPE_1;
        }
    }

    public static class MyHolder1 extends RecyclerView.ViewHolder {
        private TextView tvDate, tvHomeName, tvLocation, tvTime, tvState, tvIconDelete;
        private LinearLayout lLContent;
        private ImageView ivHomeLogo;

        public MyHolder1(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvHomeName = (TextView) itemView.findViewById(R.id.tv_home_name);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivHomeLogo = (ImageView) itemView.findViewById(R.id.iv_home_logo);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            lLContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            tvIconDelete = (TextView) itemView.findViewById(R.id.tv_icon_delete);
        }
    }

    public static class MyHolder2 extends RecyclerView.ViewHolder {
        private TextView tvDate, tvHomeName, tvLocation, tvTime, tvIconDelete, tvVisitorName, tvState;
        private LinearLayout lLContent;
        private ImageView ivHomeLogo, ivVisitorLogo;

        public MyHolder2(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvHomeName = (TextView) itemView.findViewById(R.id.tv_home_name);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            tvIconDelete = (TextView) itemView.findViewById(R.id.tv_icon_delete);
            tvVisitorName = (TextView) itemView.findViewById(R.id.tv_visitor_name);
            ivHomeLogo = (ImageView) itemView.findViewById(R.id.iv_home_logo);
            ivVisitorLogo = (ImageView) itemView.findViewById(R.id.iv_visitor_logo);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            lLContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }
    }

    private Click click;

    public void setClick(Click click) {
        this.click = click;
    }

    public interface Click {
        void Click(int state, View view, int position, int secondPos);
    }

}
