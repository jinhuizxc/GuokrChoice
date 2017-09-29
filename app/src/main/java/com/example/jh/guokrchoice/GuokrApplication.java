package com.example.jh.guokrchoice;

import android.app.Application;
import android.content.Context;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Dazz on 2016/4/3.
 */
public class GuokrApplication extends Application {

    public static final String IMAGE_CACHE_DIR = "caches/images";

    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader(getApplicationContext());
    }

    private void initImageLoader(Context context) {
        File cacheDir = StorageUtils.getIndividualCacheDirectory(context, IMAGE_CACHE_DIR);
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCache(new UnlimitedDiskCache(cacheDir));
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024);
        config.diskCacheFileCount(50);
        config.writeDebugLogs();
        ImageLoader.getInstance().init(config.build());
    }
}
