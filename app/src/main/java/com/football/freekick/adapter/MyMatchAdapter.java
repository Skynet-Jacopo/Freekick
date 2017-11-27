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

public class MyMatchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<String> datas;
    private Context mContext;
    private static final int TYPE_1 = 1111;//邀請
    private static final int TYPE_2 = 2222;//已邀請

    public MyMatchAdapter(List<String> datas, Context mContext) {
        this.datas = datas;
        this.mContext = mContext;
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
        String s = datas.get(position);
        if (holder instanceof MyHolder1) {
            final MyHolder1 myHolder1 = (MyHolder1) holder;
            myHolder1.tvLocation.setText(s);

        } else if (holder instanceof MyHolder2) {
            final MyHolder2 myHolder2 = (MyHolder2) holder;
            myHolder2.tvLocation.setText(s);
            myHolder2.tvIconDelete.setTypeface(App.mTypeface);
            myHolder2.tvIconDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.Clike(1,myHolder2.tvIconDelete,position);//刪除
                }
            });
            myHolder2.lLContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    click.Clike(2,myHolder2.lLContent,position);//詳情
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return datas.size() == 0 ? 0 : datas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 != 0) {
            return TYPE_1;
        } else {
            return TYPE_2;
//            return TYPE_1;
        }
    }

    public static class MyHolder1 extends RecyclerView.ViewHolder {
        private TextView tvDate, tvHomeName, tvLocation, tvTime, tvState;
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
        void Clike(int state, View view, int position);
    }

}
