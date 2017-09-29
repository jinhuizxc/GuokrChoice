package com.example.jh.guokrchoice.util;

import android.content.Context;

import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by Dazz on 2016/4/3.
 */
public class ImageUtils {

    /**
     * dp2px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
