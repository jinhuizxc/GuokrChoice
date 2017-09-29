package com.example.jh.guokrchoice.task;

import android.os.AsyncTask;



import java.util.List;

/**
 * Created by Dazz on 2016/4/4.
 */
public abstract class BaseGetTask<Params, Progress, Result> extends AsyncTask<Params, Progress, Result>{

    protected boolean isSucceed = true;

    protected int actionId;

    public BaseGetTask(int actionId) {
        this.actionId = actionId;
    }


}
