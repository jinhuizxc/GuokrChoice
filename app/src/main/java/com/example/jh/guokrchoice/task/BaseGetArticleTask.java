package com.example.jh.guokrchoice.task;



import com.example.jh.guokrchoice.bean.Article;

import java.util.List;

/**
 * Created by Dazz on 2016/4/9.
 */
public abstract class BaseGetArticleTask extends BaseGetTask<Void,Void,List<Article>> {


    protected TaskStateListener mTaskStateListener;

    public BaseGetArticleTask(TaskStateListener mTaskStateListener,int actionId) {
        super(actionId);
        this.mTaskStateListener = mTaskStateListener;
    }

    @Override
    protected void onPreExecute() {
        if (mTaskStateListener != null){
            mTaskStateListener.beforeTaskStart(actionId);
        }
    }

    @Override
    protected void onPostExecute(List<Article> result) {
        if (mTaskStateListener!=null){
            mTaskStateListener.afterTaskFinished(result,isSucceed,actionId);
        }
    }

    public interface TaskStateListener {
        void beforeTaskStart(int actionId);

        void afterTaskFinished(List<Article> result, boolean isSuccess, int actionId);
    }
}
