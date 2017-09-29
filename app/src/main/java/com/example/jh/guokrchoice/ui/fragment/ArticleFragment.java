package com.example.jh.guokrchoice.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.example.jh.guokrchoice.R;
import com.example.jh.guokrchoice.support.Constants;


/**
 * Created by Dazz on 2016/4/7.
 */
public class ArticleFragment extends BaseLazyFragment implements View.OnClickListener {

    WebView webView;
    ImageView backView;
    ImageView favoriteView;
    int id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        id = getArguments().getInt("id");

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_article, container, false);
        webView = (WebView) view.findViewById(R.id.articleView);
        backView = (ImageView) view.findViewById(R.id.action_back);
        backView.setOnClickListener(this);

        favoriteView = (ImageView) view.findViewById(R.id.action_favorite);
        favoriteView.setOnClickListener(this);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAppCacheEnabled(true);
        return view;
    }

    @Override
    protected void initData() {
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return false;
            }
        });
        webView.loadUrl(Constants.Url.ARTICLE_LINK + id);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.action_back:
                getActivity().finish();
                break;
            case R.id.action_favorite:

                break;
        }
    }
}
