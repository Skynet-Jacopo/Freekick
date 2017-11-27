package com.football.freekick.fragment;

import com.football.freekick.app.BaseFragment;

/**
 * Created by LiuQun on 2017/7/28.
 * ViewPager预加载处理时继承使用
 */

public abstract class LazyLoadFragment extends BaseFragment {

    protected boolean isVisible;

    protected boolean refresh;
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()){
            isVisible=true;
            onVisible();
        }else {
            isVisible=false;
            onInvisible();
        }
    }


    /**
     * visible->lazyLoad
     */
    private void onVisible() {
        lazyLoad();
    }

    private void onInvisible() {

    }

    protected abstract void lazyLoad();


}

