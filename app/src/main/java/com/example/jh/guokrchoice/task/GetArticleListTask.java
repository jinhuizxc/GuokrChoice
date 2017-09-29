package com.example.jh.guokrchoice.task;

import android.os.AsyncTask;


import com.example.jh.guokrchoice.bean.Article;
import com.example.jh.guokrchoice.support.Constants;
import com.example.jh.guokrchoice.support.http.HttpParams;
import com.example.jh.guokrchoice.support.http.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GetArticleListTask extends BaseGetArticleTask {

    private HttpParams httpParams;

    public GetArticleListTask(HttpParams httpParams, TaskStateListener mTaskStateListener, int actionId) {
        super(mTaskStateListener,actionId);
        this.httpParams = httpParams;
    }

    @Override
    protected List<Article> doInBackground(Void... params) {
        List<Article> articleList = new ArrayList<>();

        try {

            String jsonData = HttpUtils.get(Constants.Url.ARTICLE, httpParams);
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonArt = jsonArray.getJSONObject(i);
                Article article = new Article();
                article.setTitle(jsonArt.getString("title"));
                article.setDate_picked(jsonArt.getInt("date_picked"));
                article.setId(jsonArt.getInt("id"));
                article.setSummary(jsonArt.getString("summary"));
                String imageUrl = jsonArt.getString("headline_img_tb");
                if (imageUrl.isEmpty()){
                    imageUrl = jsonArt.getJSONArray("images").getString(0);
                }
                article.setHeadline_img(imageUrl);
                articleList.add(article);
            }
        } catch (JSONException | IOException e) {
            e.printStackTrace();
            isSucceed = false;
        }
        return articleList;
    }




}
