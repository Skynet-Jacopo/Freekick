package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.ArticleActivity;
import com.football.freekick.activity.MatchContentActivity1;
import com.football.freekick.adapter.MyMatchAdapter2;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.baseadapter.recyclerview.OnItemClickListener;
import com.football.freekick.beans.Article;
import com.football.freekick.beans.ConfirmInvite;
import com.football.freekick.beans.MatchesComing;
import com.football.freekick.http.Url;
import com.football.freekick.utils.PrefUtils;
import com.football.freekick.utils.ToastUtil;
import com.football.freekick.views.imageloader.ImageLoaderUtils;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.orhanobut.logger.Logger;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

/**
 * 未落實球賽
 */
public class MyMatchFragment2 extends LazyLoadFragment {


    public static final int REQUEST_CODE_TO_1 = 1;
    @Bind(R.id.recycler_my_match)
    RecyclerView mRecyclerMyMatch;
    @Bind(R.id.tv_icon_lines)
    TextView mTvIconLines;
    @Bind(R.id.recycler_lines)
    RecyclerView mRecyclerLines;
    @Bind(R.id.tv_icon_focus)
    TextView mTvIconFocus;
    @Bind(R.id.recycler_focus)
    RecyclerView mRecyclerFocus;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean mHasLoadedOnce;
    private LinearLayout mFragmentView;


    private List<Article.ArticleBean> news = new ArrayList<>();
    private List<Article.ArticleBean> point_of_view = new ArrayList<>();
    private Context mContext;
    private String picUrl = "http://www.cnr.cn/china/xwwgf/201111/W020111128658021231674.jpg";
    private CommonAdapter mLineAdapter;
    private CommonAdapter mFocusAdapter;
    private ArrayList<MatchesComing.MatchesBean> mMatches = new ArrayList<>();
    private ArrayList<MatchesComing.MatchesBean> mListInvite = new ArrayList<>();
    private MyMatchAdapter2 mMatchAdapter;
    private String team_id;
    private int pos;

    public MyMatchFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        if (mFragmentView == null) {
            mFragmentView = (LinearLayout) inflater.inflate(R.layout.fragment_my_match_fragment0,
                    container, false);
            ButterKnife.bind(this, mFragmentView);
            isPrepared = true;
            lazyLoad();
        }
        //共用一个视图，需要先移除以前的view
        ViewGroup parent = (ViewGroup) mFragmentView.getParent();
        if (parent != null) parent.removeView(mFragmentView);
        ButterKnife.bind(this, mFragmentView);
        return mFragmentView;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || mHasLoadedOnce) {
            //已加载的fragment不需要重新加载
            return;
        }
        initView();
        initMyMatches();
        initData();
        initLines();
        initFocus();
    }

    private void initFocus() {
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerFocus.setLayoutManager(manager);
        mRecyclerFocus.setNestedScrollingEnabled(false);
        mFocusAdapter = new CommonAdapter<Article.ArticleBean>(mContext, R.layout.item_focus, point_of_view) {

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, Article.ArticleBean articleBean) {
                ImageView ivPic = holder.getView(R.id.iv_pic);
                ImageLoaderUtils.displayImage(articleBean.getImage(), ivPic);
                holder.setText(R.id.tv_title, articleBean.getSubject());
            }
        };
        mFocusAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, ArticleActivity.class);
                intent.putExtra("Article", point_of_view.get(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerFocus.setAdapter(mFocusAdapter);
    }

    private void initLines() {
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        manager.setOrientation(GridLayoutManager.VERTICAL);
        mRecyclerLines.setLayoutManager(manager);
        mRecyclerLines.setNestedScrollingEnabled(false);
        mLineAdapter = new CommonAdapter<Article.ArticleBean>(mContext, R.layout.item_lines, news) {

            @Override
            public void onBindViewHolder(ViewHolder holder, int position) {
                super.onBindViewHolder(holder, position);
                AutoUtils.auto(holder.getConvertView());
            }

            @Override
            public void convert(ViewHolder holder, Article.ArticleBean articleBean) {
                ImageView ivPic = holder.getView(R.id.iv_pic);
                ImageLoaderUtils.displayImage(articleBean.getImage(), ivPic);
                holder.setText(R.id.tv_title, articleBean.getSubject());
                holder.setText(R.id.tv_date, articleBean.getCreated_at());
            }
        };
        mLineAdapter.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(ViewGroup parent, View view, Object o, int position) {
                Intent intent = new Intent(mContext, ArticleActivity.class);
                intent.putExtra("Article", news.get(position));
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(ViewGroup parent, View view, Object o, int position) {
                return false;
            }
        });
        mRecyclerLines.setAdapter(mLineAdapter);
    }

    private void initMyMatches() {
        mRecyclerMyMatch.setLayoutManager(new LinearLayoutManager(mContext));
        mRecyclerMyMatch.setNestedScrollingEnabled(false);
        mMatchAdapter = new MyMatchAdapter2(mListInvite, mContext);
        mRecyclerMyMatch.setAdapter(mMatchAdapter);
        mMatchAdapter.setClick(new MyMatchAdapter2.Click() {
            @Override
            public void Click(int state, View view, int position, int secondPos) {
                /**
                 * 1.接受邀請
                 * 2.球賽內容頁
                 */
                Intent intent = new Intent();
                pos = position;
                switch (state) {
                    case 1:
                        confirmInvite(position);
                        break;
                    case 2:
                        intent.setClass(mContext, MatchContentActivity1.class);
                        intent.putExtra("id", mListInvite.get(position).getId() + "");
                        intent.putExtra("type", 5);
                        startActivityForResult(intent, REQUEST_CODE_TO_1);
                        break;
                }
            }
        });
    }

    /**
     * 接受邀請
     * @param position
     */
    private void confirmInvite(final int position) {
//        http://api.freekick.hk/api/en/teams/<team ID>/confirm_invite
        String confirmInviteUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "teams/" + team_id + "/confirm_invite";
        JsonObject object = new JsonObject();
        object.addProperty("match_id",mListInvite.get(position).getId()+"");
        Logger.d(confirmInviteUrl);
        loadingShow();
        OkGo.put(confirmInviteUrl)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        ConfirmInvite fromJson = gson.fromJson(s, ConfirmInvite.class);
                        if (fromJson.getJoin_match()!=null){
                            ToastUtil.toastShort(getString(R.string.comfirm_success));
                            ((MineFragment)getParentFragment()).mViewpager.setCurrentItem(0,true);
                            ((MineFragment)getParentFragment()).setRefreshFragment1();
                            mListInvite.remove(position);
                            mMatchAdapter.notifyDataSetChanged();
                        }else if (fromJson.getError()!=null){
                            ToastUtil.toastShort(fromJson.getError());
                        }else {
                            ToastUtil.toastShort(getString(R.string.confirm_failed));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        ToastUtil.toastShort(getString(R.string.confirm_failed));
                        loadingDismiss();
                    }
                });
    }

    private void initData() {
        mHasLoadedOnce = true;
//        mMatches = getArguments().getParcelableArrayList("mMatches");

        if (point_of_view != null) {
            point_of_view.clear();
        }
        if (news != null) {
            news.clear();
        }
        if (point_of_view != null) {
            point_of_view.clear();
        }
        if (news != null) {
            news.clear();
        }
        news = getArguments().getParcelableArrayList("news");
        point_of_view = getArguments().getParcelableArrayList("point_of_view");
        getMatchList();
    }

    /**
     * 獲取球賽列表
     */
    private void getMatchList() {
        if (mMatches != null) {
            mMatches.clear();
        }
        if (mListInvite != null) {
            mListInvite.clear();
        }
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/coming";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson gson = new Gson();
                        MatchesComing json = gson.fromJson(s, MatchesComing.class);
                        mMatches.addAll(json.getMatches());
                        team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
                        for (int i = 0; i < mMatches.size(); i++) {
                            for (int j = 0; j < App.mPitchesBeanList.size(); j++) {
                                if (mMatches.get(i).getPitch_id() == App.mPitchesBeanList.get(j).getId()) {
                                    mMatches.get(i).setLocation(App.mPitchesBeanList.get(j).getLocation());
                                    mMatches.get(i).setPitch_name(App.mPitchesBeanList.get(j).getName());
                                }
                            }
                            if (mMatches.get(i).getStatus().equals("i")) {
                                List<MatchesComing.MatchesBean.JoinMatchesBean> join_matches = mMatches.get(i)
                                        .getJoin_matches();
                                for (int j = 0; j < join_matches.size(); j++) {
                                    if (join_matches.get(j).getJoin_team_id() == Integer.parseInt(team_id)) {
                                        mListInvite.add(mMatches.get(i));
                                    }
                                }
                            }
                        }
                        mMatchAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }

    private void initView() {
        mTvIconFocus.setTypeface(App.mTypeface);
        mTvIconLines.setTypeface(App.mTypeface);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setRefresh() {
        getMatchList();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TO_1&&resultCode == RESULT_OK){
            ((MineFragment)getParentFragment()).mViewpager.setCurrentItem(0,true);
            ((MineFragment)getParentFragment()).setRefreshFragment1();
            mListInvite.remove(pos);
            mMatchAdapter.notifyDataSetChanged();
        }
    }
}
