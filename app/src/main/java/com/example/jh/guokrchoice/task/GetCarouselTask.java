package com.example.jh.guokrchoice.task;



import com.example.jh.guokrchoice.bean.Article;
import com.example.jh.guokrchoice.support.Constants;
import com.example.jh.guokrchoice.support.http.HttpUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dazz on 2016/4/4.
 */
public class GetCarouselTask extends BaseGetArticleTask {

    public GetCarouselTask(TaskStateListener mTaskStateListener,int actionId) {
        super(mTaskStateListener, actionId);
    }

    @Override
    protected List<Article> doInBackground(Void... params) {
        List<Article> articleList = new ArrayList<>();
        try {
            String jsonData = HttpUtils.get(Constants.Url.HANDPICK_CAROUSEL);
            JSONObject jsonObject = new JSONObject(jsonData);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonArt = jsonArray.getJSONObject(i);
                Article article = new Article();
                String imageUrl = jsonArt.getString("picture");
                if (imageUrl.isEmpty()) continue;
                article.setTitle(jsonArt.getString("custom_title"));
                article.setId(jsonArt.getInt("article_id"));

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
