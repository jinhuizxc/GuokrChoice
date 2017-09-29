package com.example.jh.guokrchoice.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.media.Image;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.jh.guokrchoice.R;
import com.example.jh.guokrchoice.bean.Article;
import com.example.jh.guokrchoice.widget.LoopViewPager;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class ImageBannerView extends FrameLayout {

    private List<Article> mDatas = new ArrayList<>();
    private LoopViewPager mViewPager;
    private TextView mTvImageTitle;
    private TextView mTvIndicatorNum;
    private ImageView mIvIndicatorImage;
    private ImageLoader mImageLoader;

    private int currentIndex;
    private int totalIndex;

    private String TAG = "BannerView";


    protected List<ImageView> imageViewList = new ArrayList<>();
    private DisplayImageOptions imageOptions;

    private ImageAdapter adapter;

    public ImageBannerView(Context context) {
        this(context, null);

    }

    public ImageBannerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ImageBannerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mImageLoader = ImageLoader.getInstance();
        View mainView = LayoutInflater.from(context).inflate(R.layout.image_banner, this, true);

        mViewPager = (LoopViewPager) mainView.findViewById(R.id.bannerViewpager);

        mViewPager.addOnPageChangeListener(new PageListener());
        mTvImageTitle = (TextView) mainView.findViewById(R.id.bannerImagetitle);
        mTvIndicatorNum = (TextView) mainView.findViewById(R.id.bannerIndicatorNum);
        mIvIndicatorImage = (ImageView) mainView.findViewById(R.id.bannerIndicatorImage);
    }


    private void setup() {
        if (adapter == null) {
            adapter = new ImageAdapter();
            mViewPager.setAdapter(adapter);
        } else {
            adapter.notifyDataSetChanged();
        }
    }

    public void addAllData(List<? extends Article> datas) {
        if (mDatas == datas) return;
        if (!mDatas.isEmpty()) mDatas.clear();
        mDatas.addAll(datas);
        imageViewList.clear();
        totalIndex = mDatas.size();
        for (Article article : mDatas) {
            addItem(article);
        }

        setup();
    }

    private void addItem(Article article) {
        if (imageOptions == null) {
            imageOptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.no_image)
                    .showImageForEmptyUri(R.drawable.no_image)
                    .showImageOnFail(R.drawable.no_image)
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .considerExifParams(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .build();
        }
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        mImageLoader.displayImage(article.getHeadline_img(), imageView, imageOptions);
        imageViewList.add(imageView);
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mViewPager.startAutoScroll();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mViewPager.stopAutoScroll();
    }

    class PageListener implements ViewPager.OnPageChangeListener {
        Matrix matrix = new Matrix();

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Bitmap bitmap = ((BitmapDrawable) getResources().getDrawable(R.drawable.pager_indicator_background)).getBitmap();

            matrix.setRotate((360 / 5) * positionOffset,mIvIndicatorImage.getWidth()/2,mIvIndicatorImage.getHeight() / 2);

            bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
            mIvIndicatorImage.setImageBitmap(bitmap);
        }

        @Override
        public void onPageSelected(int position) {
            Article article = mDatas.get(position);
            mTvImageTitle.setText(article.getTitle());
            mTvIndicatorNum.setText((position + 1) + "/" + adapter.getCount());
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    class ImageAdapter extends PagerAdapter {

        public ImageAdapter() {
        }

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {

            if (object instanceof ImageView) {
                container.removeView((View) object);
            } else {
                container.removeView(imageViewList.get(position));

            }
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {


            try {
                container.addView(imageViewList.get(position));
            } catch (Exception ignore) {
                ImageView imageCopy = new ImageView(container.getContext());
                Bitmap copy = ((BitmapDrawable) imageViewList.get(position).getDrawable()).getBitmap();
                imageCopy.setScaleType(ImageView.ScaleType.FIT_XY);
                imageCopy.setImageBitmap(copy.copy(copy.getConfig(), false));
                container.addView(imageCopy);
                return imageCopy;

            }
            return imageViewList.get(position);
        }
    }


}
