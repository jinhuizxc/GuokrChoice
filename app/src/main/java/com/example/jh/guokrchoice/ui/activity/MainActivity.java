package com.example.jh.guokrchoice.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import com.example.jh.guokrchoice.R;
import com.example.jh.guokrchoice.adapter.ArticleListAdapter;
import com.example.jh.guokrchoice.bean.Article;
import com.example.jh.guokrchoice.support.http.HttpParams;
import com.example.jh.guokrchoice.task.GetArticleListTask;
import com.example.jh.guokrchoice.task.GetCarouselTask;
import com.example.jh.guokrchoice.ui.draw.DividerItemDecoration;
import com.example.jh.guokrchoice.ui.view.ImageBannerView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements GetArticleListTask.TaskStateListener,
        SwipeRefreshLayout.OnRefreshListener,
        ArticleListAdapter.OnItemOnClickListener {
    public static final String TAG = "MainActivity";


    public static final int GET_HEADER = 0;
    public static final int GET_ARTICLE = 1;
    public static final int UPDATE_ARTICLE = 2;

    private List<Article> articleList = new ArrayList<>();

    private int lastArticle = 0;

    private boolean needRefresh = false;
    private boolean needUpdate = true;
    private boolean isRefreshing = false;
    private boolean isLoading = false;

    RecyclerView mRecyclerView;
    SwipeRefreshLayout mSwipeRefresh;
    ArticleListAdapter mAdapter;
    Toolbar mToolbar;
    DrawerLayout mDrawerLayout;
    ActionBarDrawerToggle mDrawerToggle;

    ImageBannerView bannerView;
    View footerView;

    RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            LinearLayoutManager linearLayoutManager = ((LinearLayoutManager) recyclerView.getLayoutManager());
            int lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
            int totalItemCount = linearLayoutManager.getItemCount();

            if (lastVisibleItem >= totalItemCount - 2 && dy > 0) {
                if (isLoading) {
                    Log.d(TAG, "ignore manually update!");
                } else {
                    doRefresh(true);
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initView();
    }

    private void initView() {
        mAdapter = new ArticleListAdapter(this, R.layout.item_article, articleList);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawLayout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, android.R.string.ok, android.R.string.cancel);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        bannerView = new ImageBannerView(this);
        configView();
    }

    private void configView() {
        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mSwipeRefresh.setOnRefreshListener(this);
        mRecyclerView.setHasFixedSize(true);
        DividerItemDecoration itemDecoration = new DividerItemDecoration(DividerItemDecoration.VERTICAL);
        mRecyclerView.addItemDecoration(itemDecoration);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setOnScrollListener(scrollListener);

        footerView = LayoutInflater.from(this).inflate(R.layout.footer_view, null, false);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        footerView.setLayoutParams(params);
        footerView.setVisibility(View.GONE);
        mAdapter.setFooterView(footerView);
        mAdapter.setOnItemClickListener(this);
        mSwipeRefresh.post(new Runnable() {
            @Override
            public void run() {
                mSwipeRefresh.setRefreshing(true);
            }
        });
        mAdapter.setHeaderView(bannerView);
    }

    private void refresh() {
        if (!isRefreshing && needRefresh) {
            doRefresh(needUpdate);
        }
    }

    public void doRefresh(boolean isUpdate) {
        if (isUpdate) {
            new GetArticleListTask(HttpParams.getUpdateParams(lastArticle), this, UPDATE_ARTICLE).execute();
        } else {
            new GetCarouselTask(this, GET_HEADER).execute();
            new GetArticleListTask(HttpParams.getDefaultParams(), this, GET_ARTICLE).execute();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (articleList.size() == 0) {
            needRefresh = true;
            needUpdate = false;
        } else {
            needRefresh = false;
        }
        refresh();
        Log.d(TAG, "needRefresh=" + needRefresh + ",needUpdate=" + needUpdate);
    }

    @Override
    public void beforeTaskStart(int actionId) {
        switch (actionId) {
            case GET_HEADER:
            case GET_ARTICLE:
                isRefreshing = true;
                break;
            case UPDATE_ARTICLE:
                isLoading = true;
                footerView.setVisibility(View.VISIBLE);
                break;
        }

    }

    @Override
    public void afterTaskFinished(List<Article> resultList, boolean isSuccess, int actionId) {
        if (isSuccess) {
            switch (actionId) {
                case GET_ARTICLE:
                    articleList.clear();
                    lastArticle = resultList.get(resultList.size() - 1).getDate_picked();
                    articleList.addAll(resultList);
                    mAdapter.notifyDataSetChanged();
                    isRefreshing = false;
                    break;
                case GET_HEADER:

                    bannerView.addAllData(resultList);
                    break;
                case UPDATE_ARTICLE:
                    footerView.setVisibility(View.GONE);
                    lastArticle = resultList.get(resultList.size() - 1).getDate_picked();
                    articleList.addAll(resultList);
                    mAdapter.notifyDataSetChanged();
                    isLoading = false;
                    break;
            }
        } else {
            Snackbar.make(mRecyclerView, "网络错误,请检查网络连接", Snackbar.LENGTH_SHORT).show();
        }
        mSwipeRefresh.setRefreshing(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        needRefresh = true;
        refresh();
    }

    @Override
    public void OnItemClick(View v, int position) {
        Intent intent = new Intent(this, ContainerActivity.class);
        int length = 3;
        int defpage = 1;
        if (position == 1 || articleList.size() - 1 == position - 1) length = 2;
        if (position == 1) defpage = 0;
        int id[] = new int[length];
        for (int i = 0; i < length; i++) {
            id[i] = articleList.get(position + (i - 1) - defpage).getId();
            Log.d(TAG, "OnItemClick: " + (position + (i - 1)));
        }
        intent.putExtra("ids", id);
        intent.putExtra("default", defpage);
        startActivity(intent);
    }
}
