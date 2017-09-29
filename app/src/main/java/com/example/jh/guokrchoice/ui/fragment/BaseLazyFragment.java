package com.example.jh.guokrchoice.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/*
*
* 懒加载Fragment
* */
public abstract class BaseLazyFragment extends Fragment {
    private boolean isVisible;

    private boolean isPrepared;

    private boolean isFirstLoad = true;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        isFirstLoad = true;
        View view = initView(inflater,container,savedInstanceState);
        isPrepared = true;
        lazyLoad();
        return view;
    }

    protected void lazyLoad(){
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initData();
    }

    protected abstract void initData();

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected void onVisible() {
        lazyLoad();
    }

    protected void onInvisible() {
    }
}
