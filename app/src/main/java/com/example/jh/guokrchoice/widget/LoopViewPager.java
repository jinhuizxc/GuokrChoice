package com.example.jh.guokrchoice.widget;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dazz on 2016/4/5.
 */
public class LoopViewPager extends ViewPager {
    private static final boolean DEFAULT_BOUNDARY_CASHING = false;
    public static final int SCROLL_WHAT = 0;

    /**
     * do nothing when sliding at the last or first item
     **/
    public static final int SLIDE_BORDER_MODE_NONE = 0;
    /**
     * cycle when sliding at the last or first item
     **/
    public static final int SLIDE_BORDER_MODE_CYCLE = 1;
    /**
     * deliver event to parent when sliding at the last or first item
     **/
    public static final int SLIDE_BORDER_MODE_TO_PARENT = 2;

    private double autoScrollFactor = 1.0;
    private double swipeScrollFactor = 1.0;

    private List<OnPageChangeListener> mOnPageChangeListeners = new ArrayList<>();
    private LoopPagerAdapter mAdapter;
    private boolean mBoundaryCaching = DEFAULT_BOUNDARY_CASHING;
    CustomDurationScroller scroller;

    private Handler handler;

    private OnPageChangeListener onPageChangeListener = new OnPageChangeListener() {
        private float mPreviousOffset = -1;
        private float mPreviousPosition = -1;

        @Override
        public void onPageSelected(int position) {

            int realPosition = mAdapter.toRealPosition(position);
            if (mPreviousPosition != realPosition) {
                mPreviousPosition = realPosition;
                for (OnPageChangeListener listener : mOnPageChangeListeners) {
                    listener.onPageSelected(realPosition);
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
            int realPosition = position;
            if (mAdapter == null) {
                return;
            }
            if (mAdapter != null) {
                realPosition = mAdapter.toRealPosition(position);

                if (positionOffset == 0
                        && mPreviousOffset == 0
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }
            mPreviousOffset = positionOffset;

            for (OnPageChangeListener listener : mOnPageChangeListeners) {
                if (realPosition != mAdapter.getRealCount() - 1) {
                    listener.onPageScrolled(realPosition,
                            positionOffset, positionOffsetPixels);
                } else {
                    if (positionOffset > .5) {
                        listener.onPageScrolled(0, 0, 0);
                    } else {
                        listener.onPageScrolled(realPosition,
                                0, 0);
                    }
                }
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            if (mAdapter != null) {
                int position = LoopViewPager.super.getCurrentItem();
                int realPosition = mAdapter.toRealPosition(position);
                if (state == ViewPager.SCROLL_STATE_IDLE
                        && (position == 0 || position == mAdapter.getCount() - 1)) {
                    setCurrentItem(realPosition, false);
                }
            }
            for (OnPageChangeListener listener : mOnPageChangeListeners) {
                listener.onPageScrollStateChanged(state);
            }
        }
    };
    private boolean isBorderAnimation = true;
    private boolean isCycle = true;
    private int interval = 2000;
    private boolean isAutoScroll = false;
    private boolean stopScrollWhenTouch = true;
    private boolean isStopByTouch = false;
    private int slideBorderMode = SLIDE_BORDER_MODE_NONE;
    private float touchX = 0f, downX = 0f;
    private String TAG = "LoopViewPager";

    public LoopViewPager(Context context) {
        super(context);
        init();
    }

    public LoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * helper function which may be used when implementing FragmentPagerAdapter
     *
     * @param position
     * @param count
     * @return (position-1)%count
     */
    public static int toRealPosition(int position, int count) {
        position = position - 1;
        if (position < 0) {
            position += count;
        } else {
            position = position % count;
        }
        return position;
    }

    /**
     * If set to true, the boundary views (i.e. first and last) will never be destroyed
     * This may help to prevent "blinking" of some views
     *
     * @param flag
     */
    public void setBoundaryCaching(boolean flag) {
        mBoundaryCaching = flag;
        if (mAdapter != null) {
            mAdapter.setBoundaryCaching(flag);
        }
    }

    public void startAutoScroll() {
        isAutoScroll = true;
        sendScrollMessage((long) (interval + scroller.getDuration() / autoScrollFactor * swipeScrollFactor));
    }

    /**
     * stop auto scroll
     */
    public void stopAutoScroll() {
        isAutoScroll = false;
        handler.removeMessages(SCROLL_WHAT);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);


        if (stopScrollWhenTouch) {
            if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
                isStopByTouch = true;
                stopAutoScroll();
            } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
                startAutoScroll();
            }
        }

        if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT || slideBorderMode == SLIDE_BORDER_MODE_CYCLE) {
            touchX = ev.getX();
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                downX = touchX;
            }
            int currentItem = super.getCurrentItem();
            int pageCount = mAdapter == null ? 0 : mAdapter.getCount();
            /**
             * current index is first one and slide to right or current index is last one and slide to left.<br/>
             * if slide border mode is to parent, then requestDisallowInterceptTouchEvent false.<br/>
             * else scroll to last one when current item is first one, scroll to first one when current item is last
             * one.
             */
            if ((currentItem == 0 && downX <= touchX) || (currentItem == pageCount - 1 && downX >= touchX)) {
                if (slideBorderMode == SLIDE_BORDER_MODE_TO_PARENT) {
                    getParent().requestDisallowInterceptTouchEvent(false);
                } else {
                    if (pageCount > 1) {
                        setCurrentItem(pageCount - currentItem - 1, isBorderAnimation);
                    }
                    getParent().requestDisallowInterceptTouchEvent(true);
                }
                return super.dispatchTouchEvent(ev);
            }
        }
        getParent().requestDisallowInterceptTouchEvent(true);

        return super.dispatchTouchEvent(ev);
    }

    @Override
    public PagerAdapter getAdapter() {
        return mAdapter != null ? mAdapter.getRealAdapter() : mAdapter;
    }

    @Override
    public void setAdapter(PagerAdapter adapter) {
        mAdapter = new LoopPagerAdapter(adapter);
        mAdapter.setBoundaryCaching(mBoundaryCaching);
        super.setAdapter(mAdapter);
        setCurrentItem(0, false);
    }

    @Override
    public int getCurrentItem() {
        return mAdapter != null ? mAdapter.toRealPosition(super.getCurrentItem()) : 0;
    }

    private void sendScrollMessage(long delayTimeInMills) {
        /** remove messages before, keeps one message is running at most **/
        handler.removeMessages(SCROLL_WHAT);
        handler.sendEmptyMessageDelayed(SCROLL_WHAT, delayTimeInMills);
    }


    /**
     * scroll only once
     */
    public void scrollOnce() {
        int currentItem = super.getCurrentItem()-1;
        int totalCount;
        if (mAdapter == null || (totalCount = mAdapter.getCount()) <= 1) {
            return;
        }

        int nextItem = ++currentItem;
        if (nextItem < 0) {
            if (isCycle) {
                setCurrentItem(totalCount - 1, isBorderAnimation);
            }
        } else if (nextItem == totalCount) {
            if (isCycle) {
                setCurrentItem(0, isBorderAnimation);
            }
        } else {
            setCurrentItem(nextItem, true);
        }
    }

    @Override
    public void setCurrentItem(int item) {
        if (getCurrentItem() != item) {
            setCurrentItem(item, true);
        }
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        int realItem = mAdapter.toInnerPosition(item);
        super.setCurrentItem(realItem, smoothScroll);
    }

    @Override
    public void addOnPageChangeListener(OnPageChangeListener listener) {
        mOnPageChangeListeners.add(listener);
    }

    private void init() {
        handler = new MyHandler(this);
        setViewPagerScroller();
        super.addOnPageChangeListener(onPageChangeListener);
    }

    private void setViewPagerScroller() {
        try {
            Field scrollerField = ViewPager.class.getDeclaredField("mScroller");
            scrollerField.setAccessible(true);
            Field interpolatorField = ViewPager.class.getDeclaredField("sInterpolator");
            interpolatorField.setAccessible(true);

            scroller = new CustomDurationScroller(getContext(), (Interpolator) interpolatorField.get(null));
            scrollerField.set(this, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private static class MyHandler extends Handler {

        private final WeakReference<LoopViewPager> loopViewPager;

        public MyHandler(LoopViewPager loopViewPager) {
            this.loopViewPager = new WeakReference<LoopViewPager>(loopViewPager);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what) {
                case SCROLL_WHAT:
                    LoopViewPager pager = this.loopViewPager.get();
                    if (pager != null) {
                        pager.scroller.setScrollDurationFactor(pager.autoScrollFactor);
                        pager.scrollOnce();
                        pager.scroller.setScrollDurationFactor(pager.swipeScrollFactor);
                        pager.sendScrollMessage(pager.interval + pager.scroller.getDuration());
                    }
                default:
                    break;
            }
        }
    }

    public class CustomDurationScroller extends Scroller {

        private double scrollFactor = 1;

        public CustomDurationScroller(Context context) {
            super(context);
        }

        public CustomDurationScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        /**
         * not exist in android 2.3
         *
         * @param context
         * @param interpolator
         * @param flywheel
         */
        // @SuppressLint("NewApi")
        // public CustomDurationScroller(Context context, Interpolator interpolator, boolean flywheel){
        // super(context, interpolator, flywheel);
        // }

        /**
         * Set the factor by which the duration will change
         */
        public void setScrollDurationFactor(double scrollFactor) {
            this.scrollFactor = scrollFactor;
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, (int) (duration * scrollFactor));
        }
    }

}
