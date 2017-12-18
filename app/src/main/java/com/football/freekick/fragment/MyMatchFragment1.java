package com.football.freekick.fragment;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.football.freekick.App;
import com.football.freekick.R;
import com.football.freekick.activity.ArticleActivity;
import com.football.freekick.activity.ConfirmationPendingActivity;
import com.football.freekick.activity.MatchContentActivity1;
import com.football.freekick.activity.MatchInviteActivity;
import com.football.freekick.adapter.MyMatchAdapter1;
import com.football.freekick.baseadapter.ViewHolder;
import com.football.freekick.baseadapter.recyclerview.CommonAdapter;
import com.football.freekick.baseadapter.recyclerview.OnItemClickListener;
import com.football.freekick.beans.Article;
import com.football.freekick.beans.Invite;
import com.football.freekick.beans.MatchesComing;
import com.football.freekick.beans.NoMatches;
import com.football.freekick.beans.WithDraw;
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
public class MyMatchFragment1 extends LazyLoadFragment {


    public static final int REQUEST_CODE_INVITE = 1;
    public static final int REQUEST_CODE_TO_1   = 2;
    public static final int REQUEST_CODE_DETAIL   = 3;
    @Bind(R.id.recycler_my_match)
    RecyclerView mRecyclerMyMatch;
    @Bind(R.id.tv_icon_lines)
    TextView     mTvIconLines;
    @Bind(R.id.recycler_lines)
    RecyclerView mRecyclerLines;
    @Bind(R.id.tv_icon_focus)
    TextView     mTvIconFocus;
    @Bind(R.id.recycler_focus)
    RecyclerView mRecyclerFocus;
    @Bind(R.id.ll_parent)
    LinearLayout mLlParent;

    /**
     * 标志位，标志已经初始化完成
     */
    private boolean      isPrepared;
    /**
     * 是否已被加载过一次，第二次就不再去请求数据了
     */
    private boolean      mHasLoadedOnce;
    private LinearLayout mFragmentView;

    private Context mContext;
    private String picUrl = "http://www.cnr.cn/china/xwwgf/201111/W020111128658021231674.jpg";
    private CommonAdapter mLineAdapter;
    private CommonAdapter mFocusAdapter;
    private ArrayList<MatchesComing.MatchesBean> mMatches  = new ArrayList<>();
    private ArrayList<MatchesComing.MatchesBean> mListWait = new ArrayList<>();
    private MyMatchAdapter1 mMatchAdapter;
    private List<Article.ArticleBean> news          = new ArrayList<>();
    private List<Article.ArticleBean> point_of_view = new ArrayList<>();

    public MyMatchFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mContext = getActivity();
        if (mFragmentView == null) {
            mFragmentView = (LinearLayout) inflater.inflate(R.layout.fragment_my_match,
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
        mMatchAdapter = new MyMatchAdapter1(mListWait, mContext);
        mRecyclerMyMatch.setAdapter(mMatchAdapter);
        mMatchAdapter.setClick(new MyMatchAdapter1.Click() {
            @Override
            public void Click(int state, View view, int position, int secondPos) {
                /**
                 * 1.參與進來而待確認,可取消
                 * 2.已邀請,secondPos是join_match中的位置
                 * 3.待確認,secondPos是join_match中的位置
                 * 4.邀請(無邀請隊,無參與隊)
                 * 5.取消邀請
                 * 6.球賽詳情頁
                 */
                Intent intent = new Intent();
                switch (state) {
                    case 1:
                        withdrawJoin(position, secondPos);
                        break;
                    case 2://有×號代替吧
                        break;
                    case 3:
                        intent.setClass(mContext, ConfirmationPendingActivity.class);
                        intent.putExtra("id", mListWait.get(position).getId() + "");
                        startActivityForResult(intent, REQUEST_CODE_TO_1);
                        break;
                    case 4:
                        intent.setClass(mContext, MatchInviteActivity.class);
                        intent.putExtra("match_id", mListWait.get(position).getId() + "");
                        startActivityForResult(intent, REQUEST_CODE_INVITE);
                        break;
                    case 5:
                        showCancelInvitePop(position, secondPos);
                        break;
                    case 6://球賽詳情頁(有主動參與的隊伍)
                        intent.setClass(mContext, MatchContentActivity1.class);
                        intent.putExtra("id", mListWait.get(position).getId() + "");
                        intent.putExtra("type", 4);
                        startActivityForResult(intent, REQUEST_CODE_TO_1);
                        break;
                    case 7://球賽詳情頁(我主動參與別人的球賽)
                        intent.setClass(mContext, MatchContentActivity1.class);
                        intent.putExtra("id", mListWait.get(position).getId() + "");
                        intent.putExtra("type", 3);
                        startActivityForResult(intent, REQUEST_CODE_DETAIL);
                        break;
                    case 8://球賽詳情頁(我主動邀請過了別的隊伍)
                        intent.setClass(mContext, MatchContentActivity1.class);
                        intent.putExtra("id", mListWait.get(position).getId() + "");
                        intent.putExtra("type", 6);
                        startActivityForResult(intent, REQUEST_CODE_DETAIL);
                        break;
                }
            }
        });
    }

    private void showCancelInvitePop(final int position, final int secondPos) {
        View contentView = LayoutInflater.from(mContext).inflate(
                R.layout.view_cancel_invite, null);

        final PopupWindow popupWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        TextView tvno  = (TextView) contentView.findViewById(R.id.tv_no);
        TextView tvyes = (TextView) contentView.findViewById(R.id.tv_yes);
        tvno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });
        tvyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
                cancelInvite(position, secondPos);
            }
        });
        popupWindow.setTouchable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                backgroundAlpha(1f);
            }
        });
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置好参数之后再show
        popupWindow.showAtLocation(mLlParent, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    /**
     * 參與隊取消參與
     *
     * @param position
     * @param secondPos
     */
    private void withdrawJoin(int position, int secondPos) {
//        http://api.freekick.hk/api/en/join_matches/<joinmatchID>/withdraw
        String withdrawUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "join_matches/" + mListWait.get
                (position).getJoin_matches().get(secondPos).getJoin_team_id() + "/withdraw";
        Logger.d(withdrawUrl);
        loadingShow();
        OkGo.put(withdrawUrl)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        loadingDismiss();
                        Logger.json(s);
                        Gson     gson     = new Gson();
                        WithDraw fromJson = gson.fromJson(s, WithDraw.class);
                        if (fromJson.getJoin_match() != null) {
                            ToastUtil.toastShort(getString(R.string.withdraw_success));

                        } else {
                            ToastUtil.toastShort(getString(R.string.withdraw_failed));
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                        loadingDismiss();
                    }
                });
    }

    /**
     * 取消邀請
     *
     * @param position
     * @param secondPos
     */
    private void cancelInvite(final int position, final int secondPos) {
        JsonObject object = new JsonObject();
        object.addProperty("invite_team_id", mListWait.get(position).getJoin_matches().get(secondPos).getJoin_team_id
                () + "");
        String cancelInviteUrl = Url.BaseUrl + (App.isChinese ? Url.ZH_HK : Url.EN) + "matches/" + mListWait.get
                (position).getId() +
                "/withdraw_invite";
        Logger.d(cancelInviteUrl);
        Logger.d(object.toString());
        OkGo.put(cancelInviteUrl)
                .upJson(object.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        Gson   gson   = new Gson();
                        Invite invite = gson.fromJson(s, Invite.class);
                        if (invite.getSuccess() != null) {
                            ToastUtil.toastShort(invite.getSuccess());
                            List<MatchesComing.MatchesBean.JoinMatchesBean> join_matches = mListWait.get(position)
                                    .getJoin_matches();
                            join_matches.remove(secondPos);
                            mMatchAdapter.notifyDataSetChanged();
                        } else if (invite.getErrors() != null) {
                            ToastUtil.toastShort(invite.getErrors());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Logger.d(e.getMessage());
                    }
                });
    }

    private void initData() {
        mHasLoadedOnce = true;

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
        if (mListWait != null) {
            mListWait.clear();
        }
        loadingShow();
        String url = BaseUrl + (App.isChinese ? ZH_HK : EN) + "matches/coming";
        Logger.d(url);
        OkGo.get(url)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Logger.json(s);
                        loadingDismiss();
                        Gson          gson = new Gson();
                        if (!s.contains("[") && !s.contains("]")) {
                            NoMatches noMatches = gson.fromJson(s, NoMatches.class);
                            ToastUtil.toastShort(noMatches.getMatches());
                        } else {
                            MatchesComing json = gson.fromJson(s, MatchesComing.class);
                            mMatches.addAll(json.getMatches());
                            String team_id = PrefUtils.getString(App.APP_CONTEXT, "team_id", null);
                            for (int i = 0; i < mMatches.size(); i++) {
                                for (int j = 0; j < App.mPitchesBeanList.size(); j++) {
                                    if (mMatches.get(i).getPitch_id() == App.mPitchesBeanList.get(j).getId()) {
                                        mMatches.get(i).setLocation(App.mPitchesBeanList.get(j).getLocation());
                                        mMatches.get(i).setPitch_name(App.mPitchesBeanList.get(j).getName());
                                        mMatches.get(i).setLongitude(App.mPitchesBeanList.get(j).getLongitude());
                                        mMatches.get(i).setLatitude(App.mPitchesBeanList.get(j).getLatitude());
                                        mMatches.get(i).setPitch_image(App.mPitchesBeanList.get(j).getImage().getUrl());
                                    }
                                }
                                if (mMatches.get(i).getStatus().equals("w")) {
                                    mListWait.add(mMatches.get(i));
                                }
                                if (mMatches.get(i).getStatus().equals("i")) {
                                    List<MatchesComing.MatchesBean.JoinMatchesBean> join_matches = mMatches.get(i)
                                            .getJoin_matches();
                                    for (int j = 0; j < join_matches.size(); j++) {
                                        if (join_matches.get(j).getStatus().equals("invited")) {
                                            if (join_matches.get(j).getJoin_team_id() != Integer.parseInt(team_id)) {
                                                //客隊不是自己,添加
                                                mListWait.add(mMatches.get(i));
                                            } else {
                                                //客隊是自己,不能添加
                                            }
                                        }
                                    }
                                }
                            }
                            mMatchAdapter.notifyDataSetChanged();
                        }
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

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getActivity().getWindow().getAttributes();
        lp.alpha = bgAlpha; //0.0-1.0
        getActivity().getWindow().setAttributes(lp);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_INVITE && resultCode == RESULT_OK) {
            setRefresh();
        } else if (requestCode == REQUEST_CODE_TO_1 && resultCode == RESULT_OK) {
            setRefresh();
            ((MineFragment)getParentFragment()).mViewpager.setCurrentItem(0,true);
            ((MineFragment)getParentFragment()).setRefreshFragment1();
        } else if (requestCode == REQUEST_CODE_DETAIL && resultCode == RESULT_OK) {
            setRefresh();
        }
    }
}
