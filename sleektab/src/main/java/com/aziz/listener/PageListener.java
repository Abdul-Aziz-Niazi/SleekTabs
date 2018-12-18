package com.aziz.listener;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.aziz.sleektablayout.SleekTabLayout;

public class PageListener implements ViewPager.OnPageChangeListener {
    private Context context;
    private SleekTabLayout sleekTabLayout;
    private int lastTitle = 0;
    private float startSize;
    private float endSize;
    private final long animationDuration = 150;

    public PageListener(SleekTabLayout sleekTabLayout) {
        this.sleekTabLayout = sleekTabLayout;
        startSize = sleekTabLayout.getMinTextSize();
        endSize = sleekTabLayout.getMaxTextSize();
        lastTitle = sleekTabLayout.getStartAt();
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {
    }

    private void resetTitle(int i) {
        ((TextView) sleekTabLayout.getChildAt(lastTitle)).setTextSize(startSize);
        ((TextView) sleekTabLayout.getChildAt(lastTitle)).setTextColor(sleekTabLayout.getTextColor());
        lastTitle = i;
    }

    @Override
    public void onPageSelected(int i) {
        pageChanged(i);
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }


    private void pageChanged(final int i) {
        resetTitle(i);
        animateTitle(i);
    }

    private void animateTitle(final int i) {
        ValueAnimator animator = ValueAnimator.ofFloat(startSize, endSize);
        animator.setDuration(animationDuration);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float animatedValue = (float) valueAnimator.getAnimatedValue();
                ((TextView) sleekTabLayout.getChildAt(i)).setTextSize(animatedValue);
                (sleekTabLayout.getChildAt(i)).setAlpha((animatedValue / 100) + 0.68f);
                if (animatedValue == endSize) {
                    ((TextView) sleekTabLayout.getChildAt(i)).setTextColor(sleekTabLayout.getSelectedColor());
                }
            }
        });
        animator.start();
    }
}
