package com.aziz.motion;

import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.aziz.sleektablayout.SleekTabLayout;


public class SleekGestures extends GestureDetector.SimpleOnGestureListener {

    private static final String TAG = SleekGestures.class.getSimpleName();
    private final ViewPager pager;
    private final SleekTabLayout sleekTabLayout;
    private int totalPages, currentPage;
    private boolean verticalFlung;
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public SleekGestures(SleekTabLayout sleekTabLayout) {
        this.sleekTabLayout = sleekTabLayout;
        this.pager = sleekTabLayout.getPager();
        totalPages = pager.getAdapter().getCount();
        currentPage = pager.getCurrentItem();
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.d(TAG, "onFling: x1:" + e1.getX() + ", y1:" + e1.getY() + " x2:" + e2.getX() + " y2:" + e2.getY());

        // right to left swipe
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE) {
//            Log.w(TAG, "Swipe <<< LEFT");
            pageDown();
            return false;
        }
        // left to right swipe
        else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE) {
//            Log.w(TAG, "Swipe >>> Right");
            pageUp();
            return false;
        }
        // right to left swipe
        if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE) {
//            Log.w(TAG, "Swipe ^^^ Top");
            pageUp();
            return false;
        }
        // left to right swipe
        else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE) {
//            Log.w(TAG, "Swipe vvv Bottom");
            pageDown();
            return false;
        }
        return false;
    }

    private void pageUp() {
        if (currentPage == totalPages)
            return;
        pager.setCurrentItem(++currentPage);
    }

    private void pageDown() {
        if (currentPage == 0)
            return;
        pager.setCurrentItem(--currentPage);
    }
}
