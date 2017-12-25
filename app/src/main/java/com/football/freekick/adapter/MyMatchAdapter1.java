package com.football.freekick.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.TeamDetailActivity;
import com.football.freekick.beans.MatchesComing;
import com.football.freekick.utils.JodaTimeUtil;
import com.football.freekick.utils.MyUtil;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 90516 on 2017/11/22.
 */

public class MyMatchAdapter1 extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    //    private List<String> datas;
    private ArrayList<MatchesComing.MatchesBean> mMatches;
    private Context                              mContext;
    private static final int TYPE_1 = 1111;//邀請
    private static final int TYPE_2 = 2222;//已邀請
    private final String team_id;
    private final Gson   gson;

    public MyMatchAdapter1(ArrayList<MatchesComing.MatchesBean> datas, Context mContext) {
        this.mMatches = datas;
        this.mContext = mContext;
        team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
        gson = new Gson();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_1) {
            View      view1     = LayoutInflater.from(mContext).inflate(R.layout.item_my_need_match, parent, false);
            MyHolder1 myHolder1 = new MyHolder1(view1);
            AutoUtils.auto(view1);
            return myHolder1;
        } else if (viewType == TYPE_2) {
            View      view2     = LayoutInflater.from(mContext).inflate(R.layout.item_my_already_matched, parent, false);
            MyHolder2 myHolder2 = new MyHolder2(view2);
            AutoUtils.auto(view2);
            return myHolder2;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final MatchesComing.MatchesBean matchesBean = mMatches.get(position);
        if (holder instanceof MyHolder1) {
            final MyHolder1 myHolder1 = (MyHolder1) holder;
            myHolder1.tvIconDelete.setTypeface(App.mTypeface);
            myHolder1.tvHomeName.setText(matchesBean.getHome_team().getTeam_name());
            ImageLoaderUtils.displayImage(MyUtil.getImageUrl(matchesBean.getHome_team().getImage().getUrl()),
                    myHolder1.ivHomeLogo);

            String date = JodaTimeUtil.getDate2(matchesBean.getPlay_start());
            myHolder1.tvDate.setText(date);
            String start = JodaTimeUtil.getTime2(matchesBean.getPlay_start());
            String end   = JodaTimeUtil.getTime2(matchesBean.getPlay_end());
            myHolder1.tvTime.setText(start + " - " + end);
            List<MatchesComing.MatchesBean.JoinMatchesBean> join_matches = matchesBean.getJoin_matches();
            myHolder1.tvLocation.setText(matchesBean.getPitch_name());
            if (matchesBean.getHome_team().getId() != Integer.parseInt(team_id)) {
                //主隊位置不是自己,那自己應該在另一邊,屬於主動參與進來的
                myHolder1.tvState.setText(R.string.confirmation_pending);
                myHolder1.tvState.setBackgroundResource(R.drawable.shape_corner_gray_bg);
                myHolder1.tvState.setClickable(false);
                myHolder1.tvIconDelete.setVisibility(View.VISIBLE);
                int secondPos = 0;
                for (int i = 0; i < join_matches.size(); i++) {
                    if (join_matches.get(i).getJoin_team_id() == Integer.parseInt(team_id)) {
                        secondPos = i;
                    }
                }
                final int finalSecondPos = secondPos;
                myHolder1.tvIconDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //參與進來而待確認,可取消
                        click.Click(1, myHolder1.tvIconDelete, position, finalSecondPos);
                    }
                });
                myHolder1.lLContent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        click.Click(7, myHolder1.lLContent, position, 0);//球賽詳情頁(我主動參與別人的球賽)
                    }
                });
            } else {
                //主隊位置是自己,那右側要麼是有邀請過,要麼是有隊伍參與進來
                myHolder1.tvState.setBackgroundResource(R.drawable.selector_round_green_gray_bg);
                myHolder1.tvState.setClickable(true);
                if (join_matches.size() > 0) {//有邀請的隊伍優先,其次有隊伍參與的
                    String toJson = gson.toJson(join_matches);
                    if (toJson.contains("invited")) {//有邀請的隊伍(取消邀請時要刪除)
                        myHolder1.tvState.setText(R.string.invited_already);
                        myHolder1.tvIconDelete.setVisibility(View.VISIBLE);
                        for (int i = 0; i < join_matches.size(); i++) {
                            if (join_matches.get(i).getStatus().equals("invited")) {
                                final int finalI = i;
                                myHolder1.tvState.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        click.Click(2, myHolder1.tvState, position, finalI);//已邀請,i是join_match的位置
                                    }
                                });
                                myHolder1.tvIconDelete.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        click.Click(5, myHolder1.tvIconDelete, position, finalI);//取消邀請
                                    }
                                });
                                myHolder1.lLContent.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        click.Click(8, myHolder1.lLContent, position, 0);//球賽詳情頁(有邀請了的隊伍)
                                    }
                                });
                            }
                        }
                    } else if (!toJson.contains("invited") && toJson.contains("confirmation_pending")) {//無邀請的隊伍
                        // (有主動參與的隊伍)
                        Logger.d("走了嗎  這裡");
                        myHolder1.tvIconDelete.setVisibility(View.GONE);
                        myHolder1.tvState.setText(R.string.confirmation_pending);
                        myHolder1.tvState.setBackgroundResource(R.drawable.selector_round_green_gray_bg);
//                        for (int i = 0; i < join_matches.size(); i++) {
//                            if (join_matches.get(i).getStatus().equals("confirmation_pending")) {
//                                final int finalI = i;
                        final int finalI = 0;
                        myHolder1.tvState.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                click.Click(3, myHolder1.tvState, position, finalI);//待確認,i是join_match的位置
                            }
                        });
//                            }
//                        }
                        myHolder1.lLContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                click.Click(6, myHolder1.lLContent, position, 0);//球賽詳情頁(有主動參與的隊伍)
                            }
                        });
                    } else {//無邀請,無主動參與隊伍
                        myHolder1.tvState.setText(R.string.invite);
                        myHolder1.tvIconDelete.setVisibility(View.GONE);
                        myHolder1.tvState.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                click.Click(4, myHolder1.tvState, position, 0);//邀請(無邀請隊,無參與隊)
                            }
                        });
                        myHolder1.lLContent.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                click.Click(9, myHolder1.lLContent, position, 0);//球賽詳情頁(無邀請,無主動參與隊伍)
                            }
                        });
                    }
                } else {
                    myHolder1.tvState.setText(R.string.invite);
                    myHolder1.tvIconDelete.setVisibility(View.GONE);
                    myHolder1.tvState.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            click.Click(4, myHolder1.tvState, position, 0);//邀請(無邀請隊,無參與隊)
                        }
                    });
                }
            }
            //主隊進入球隊詳情
            myHolder1.lLHomeTeam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(mContext, TeamDetailActivity.class);
                    intent.putExtra("id", matchesBean.getHome_team().getId() + "");
                    mContext.startActivity(intent);
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
        private LinearLayout lLContent,lLHomeTeam;
        private ImageView    ivHomeLogo;

        public MyHolder1(View itemView) {
            super(itemView);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvHomeName = (TextView) itemView.findViewById(R.id.tv_home_name);
            tvLocation = (TextView) itemView.findViewById(R.id.tv_location);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);
            ivHomeLogo = (ImageView) itemView.findViewById(R.id.iv_home_logo);
            tvState = (TextView) itemView.findViewById(R.id.tv_state);
            lLContent = (LinearLayout) itemView.findViewById(R.id.ll_content);
            lLHomeTeam = (LinearLayout) itemView.findViewById(R.id.ll_home_team);
            tvIconDelete = (TextView) itemView.findViewById(R.id.tv_icon_delete);
        }
    }

    public static class MyHolder2 extends RecyclerView.ViewHolder {
        private TextView tvDate, tvHomeName, tvLocation, tvTime, tvIconDelete, tvVisitorName, tvState;
        private LinearLayout lLContent;
        private ImageView    ivHomeLogo, ivVisitorLogo;

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
