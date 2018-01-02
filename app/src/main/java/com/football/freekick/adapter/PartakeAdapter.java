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
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.views.ClickableImageView;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

/**
 * Created by 90516 on 2017/11/22.
 */

public class PartakeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<AvailableMatches.MatchesBean> mMatchList;
    private Context mContext;
    private Gson gson;
    private String team_id;
    private static final int TYPE_1 = 1111;//搵場列表
    private static final int TYPE_2 = 2222;//廣告

    private String picUrl = "http://www.cnr.cn/china/xwwgf/201111/W020111128658021231674.jpg";


    public PartakeAdapter(List<AvailableMatches.MatchesBean> mMatchList, Context mContext) {
        this.mMatchList = mMatchList;
        this.mContext = mContext;
        gson = new Gson();
        team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        for (int i = 0; i < App.mPitchesBeanList.size(); i++) {
            for (int j = 0; j < mMatchList.size(); j++) {
                if (App.mPitchesBeanList.get(i).getId() == mMatchList.get(j).getPitch_id()) {
                    mMatchList.get(j).setPitch_name(App.mPitchesBeanList.get(i).getName());
                    mMatchList.get(j).setLocation(App.mPitchesBeanList.get(i).getLocation());
                    mMatchList.get(j).setLongitude(App.mPitchesBeanList.get(i).getLongitude());
                    mMatchList.get(j).setLatitude(App.mPitchesBeanList.get(i).getLatitude());
                    mMatchList.get(j).setPitch_image(App.mPitchesBeanList.get(i).getImage().getUrl());
                }
            }
        }
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
        AvailableMatches.MatchesBean matchesBean = mMatchList.get(position);


        if (holder instanceof MyHolder1) {
            final MyHolder1 myHolder1 = (MyHolder1) holder;
            myHolder1.tvIconShare.setTypeface(App.mTypeface);
            myHolder1.tvIconLocation.setTypeface(App.mTypeface);
            myHolder1.tvPitchName.setText(matchesBean.getPitch_name());
            myHolder1.tvLocation.setText(matchesBean.getLocation());
            String start = JodaTimeUtil.getTime2(matchesBean.getPlay_start());
            String end = JodaTimeUtil.getTime2(matchesBean.getPlay_end());

            myHolder1.tvTime.setText(start + "-" + end);
            myHolder1.ivDressHome.setBackgroundColor(Color.parseColor("#" + matchesBean.getHome_team_color()));
            myHolder1.tvHomeName.setText(matchesBean.getHome_team().getTeam_name());
            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(matchesBean.getPitch_image()),
                    myHolder1.ivPic);

            String status = matchesBean.getStatus();
            List<AvailableMatches.MatchesBean.JoinMatchesBean> join_matches = matchesBean.getJoin_matches();
            switch (status) {
                case "i"://已邀請
                case "w":
                    myHolder1.ivDressVisitor.setImageResource(R.drawable.ic_dress_unknow);
                    myHolder1.ivDressVisitor.setBackgroundResource(R.color.black);
                    myHolder1.tvVisitorName.setText("");
                    boolean haveMe = false;
                    for (int i = 0; i < join_matches.size(); i++) {
                        if (join_matches.get(i).getJoin_team_id() == Integer.parseInt(team_id) && join_matches.get(i)
                                .getStatus().equals("confirmation_pending")) {
                            haveMe = true;
                        }
                    }
                    if (gson.toJson(join_matches).contains("confirmation_pending") && haveMe) {
                        myHolder1.tvState.setText(R.string.confirmation_pending);
                        myHolder1.tvState.setBackgroundResource(R.drawable.shape_round_gray_bg);
                        myHolder1.lLContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                click.Clike(6, myHolder1.lLContent, position);
                            }
                        });
                    } else {
                        myHolder1.tvState.setText(R.string.join_match);
                        myHolder1.tvState.setBackgroundResource(R.drawable.selector_round_green_gray_bg);
                        myHolder1.tvState.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                click.Clike(2, myHolder1.tvState, position);
                            }
                        });
                        myHolder1.lLContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                click.Clike(1, myHolder1.lLContent, position);
                            }
                        });
                    }

                    break;
                case "m":
                    for (int i = 0; i < join_matches.size(); i++) {
                        if (join_matches.get(i).getStatus().equals("confirmed")) {
                            myHolder1.ivDressVisitor.setBackgroundColor(MyUtil.getColorInt(join_matches.get(i)
                                    .getJoin_team_color()));
                            myHolder1.tvVisitorName.setText(join_matches.get(i).getTeam().getTeam_name());

                        }
                    }
                    myHolder1.tvState.setText(R.string.match_success);
                    myHolder1.tvState.setBackgroundResource(R.drawable.selector_round_red_gray_bg);
//                    myHolder1.tvState.setBackgroundResource(R.drawable.shape_round_light_red);
                    myHolder1.tvState.setOnClickListener(new View.OnClickListener() {//應該是沒用了
                        @Override
                        public void onClick(View view) {
                            click.Clike(3, myHolder1.tvState, position);
                        }
                    });
                    myHolder1.lLContent.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            click.Clike(7, myHolder1.lLContent, position);
                        }
                    });
                    break;
                case "f":

                    break;
                case "c":

                    break;

            }

            myHolder1.lLShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.Clike(4, myHolder1.lLShare, position);
                }
            });
            myHolder1.tvIconShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.Clike(4, myHolder1.tvIconShare, position);
                }
            });
        } else if (holder instanceof MyHolder2) {
            final MyHolder2 myHolder2 = (MyHolder2) holder;
            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(matchesBean.getDefault_image()),
                    myHolder2.ivAdvertisement);
            myHolder2.lLContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.Clike(5, myHolder2.lLContent, position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mMatchList.size() == 0 ? 0 : mMatchList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mMatchList.size() >= 3) {
            if (position == 2) {
                return TYPE_2;
            } else {
                return TYPE_1;
            }
        } else if (mMatchList.size() == 2) {
            if (position == 1) {
                return TYPE_2;
            } else {
                return TYPE_1;
            }
        } else {
            return TYPE_1;
        }
    }

    public static class MyHolder1 extends RecyclerView.ViewHolder {
        private TextView tvPitchName, tvHomeName, tvVisitorName, tvIconShare;
        private TextView tvLocation;
        private TextView tvIconLocation;
        private TextView tvTime;
        private TextView tvState;
        private LinearLayout lLContent, lLShare;
        private ImageView ivPic;
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
            tvIconShare = (TextView) itemView.findViewById(R.id.tv_icon_share);
            ivDressHome = (ImageView) itemView.findViewById(R.id.iv_dress_home);
            ivDressVisitor = (ImageView) itemView.findViewById(R.id.iv_dress_visitor);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            lLShare = (LinearLayout) itemView.findViewById(R.id.ll_share);
            lLContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
        }
    }

    public static class MyHolder2 extends RecyclerView.ViewHolder {
        private ClickableImageView ivAdvertisement;
        private LinearLayout lLContent;

        public MyHolder2(View itemView) {
            super(itemView);
            ivAdvertisement = (ClickableImageView) itemView.findViewById(R.id.iv_advertisement);
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
