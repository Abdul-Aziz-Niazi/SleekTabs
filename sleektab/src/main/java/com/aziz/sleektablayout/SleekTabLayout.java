package com.aziz.sleektablayout;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.aziz.listener.PageListener;
import com.aziz.motion.SleekGestures;
import com.aziz.sleektab.R;


//TODO : Height Measure/ Touch Gestures/ Horizontal Orientation
public class SleekTabLayout extends LinearLayout {

    private static final String TAG = SleekTabLayout.class.getSimpleName();
    private Context context;
    private int mTextColor;
    private int mSelectedColor;
    private int mBGColor;
    private int mStartAt = 0;
    private int mOrientation = 1;
    private float mMaxTextSize = 36;
    private float mMinTextSize = 12;
    private int layoutHeight = 120;
    private SpaceMode spaceMode = SpaceMode.COMPAT;
    private GestureDetectorCompat mGestureDetector;
    private ViewPager pager;
    private boolean gestureEnabled;

    public SleekTabLayout(Context context) {
        super(context);
        init(context, null);
    }

    public SleekTabLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init(context, attrs);
    }

    public void init(Context context, AttributeSet attributeSet) {
        this.context = context;
        int bgColor = ContextCompat.getColor(context, R.color.defaultBackground);
        int selectedColor = ContextCompat.getColor(context, R.color.defaultSelectedColor);
        int textColor = ContextCompat.getColor(context, R.color.defaultTextColor);
        setOrientation();
        LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        if (attributeSet != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attributeSet, R.styleable.SleekTabLayout, 0, 0);
            mSelectedColor = typedArray.getColor(R.styleable.SleekTabLayout_selectedTextColor, selectedColor);
            mTextColor = typedArray.getColor(R.styleable.SleekTabLayout_textColor, textColor);
            mBGColor = typedArray.getColor(R.styleable.SleekTabLayout_android_background, bgColor);
            mMaxTextSize = typedArray.getDimension(R.styleable.SleekTabLayout_selectedFontSize, mMaxTextSize);
            mMinTextSize = typedArray.getDimension(R.styleable.SleekTabLayout_fontSize, mMinTextSize);
            mStartAt = typedArray.getInteger(R.styleable.SleekTabLayout_startingPosition, mStartAt);
            mOrientation = typedArray.getInteger(R.styleable.SleekTabLayout_android_orientation, 1);
            gestureEnabled = typedArray.getBoolean(R.styleable.SleekTabLayout_gestureEnabled, true);
            spaceMode.mode = typedArray.getInteger(R.styleable.SleekTabLayout_mode, 1);
            setGravity(typedArray.getInteger(R.styleable.SleekTabLayout_android_gravity, 0));
            validateAndSetRange(mMaxTextSize, mMinTextSize);
            typedArray.recycle();
        }
        setOrientation();
        adjustHeightToSpaceModeOrientation();
        setBackgroundColor(mBGColor);
        setPadding(Math.round(convertDpToPixel(20)), Math.round(convertDpToPixel(5)), 0, Math.round(convertDpToPixel(5)));
    }

    private void adjustHeightToSpaceModeOrientation() {
        switch (spaceMode.getMode()) {
            case 0: //COZY
                layoutHeight = getOrientation() == VERTICAL ? 140 : 70;
                break;
            case 1://COMPAT
                layoutHeight = getOrientation() == VERTICAL ? 120 : 60;
                break;
            case 2://JUMBO
                layoutHeight = getOrientation() == VERTICAL ? 160 : 80;
                break;
        }
    }

    private void setOrientation() {
        setOrientation(mOrientation == 1 ? VERTICAL : HORIZONTAL);
    }

    @Override
    public void setGravity(int gravity) {
        if (gravity == Gravity.BOTTOM || gravity == Gravity.TOP || gravity == Gravity.START)
            super.setGravity(gravity);
        else
            super.setGravity(Gravity.CENTER);
        requestLayout();
    }

    public void setupWithViewPager(ViewPager pager) {
        this.pager = pager;
        if (pager == null) {
            throw new NullPointerException("View Pager is null");
        }
        if (pager.getAdapter() == null) {
            throw new NullPointerException("Adapter not set. Please set view pager adapter before calling this method");
        }
        PagerAdapter adapter = pager.getAdapter();

        if (adapter.getCount() == 0)
            return;

        if (adapter.getCount() > 4)
            throw new IllegalStateException("Adapter should not have more than 4 tabs");

        setTitles(adapter);
        adjustWithOrientation(getOrientation());
        pager.setCurrentItem(mStartAt);
        pager.addOnPageChangeListener(new PageListener(this));
        mGestureDetector = new GestureDetectorCompat(context, new SleekGestures(this));

    }

    private void setTitles(PagerAdapter adapter) {
        for (int i = 0; i < adapter.getCount(); i++) {
            TextView textView = new TextView(context);
            textView.setTextColor(mTextColor);
            textView.setTextSize(mMinTextSize);
            CharSequence title = adapter.getPageTitle(i);
            if (title == null)
                title = "Tab-" + (i + 1);
            if (getOrientation() == HORIZONTAL)
                handleTitlesForHorizontal(i, textView);
            textView.setText(title);
            addView(textView);
        }
        if (mStartAt > adapter.getCount())
            mStartAt = 0;
        ((TextView) getChildAt(mStartAt)).setTextSize(mMaxTextSize);
        ((TextView) getChildAt(mStartAt)).setTextColor(mSelectedColor);
    }

    private void handleTitlesForHorizontal(final int position, TextView textView) {
        int width = 0;
        if (pager.getAdapter().getCount() % 2 != 0 && pager.getAdapter().getCount() > 2) {
            if (position == (pager.getAdapter().getCount() / 2) + 1) {
                width = ViewGroup.LayoutParams.WRAP_CONTENT;
                Log.d(TAG, "handleTitlesForHorizontal: setting wrap content on " + position);
            }
        }
        LayoutParams layoutParams = new LayoutParams(width, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        textView.setLayoutParams(layoutParams);
        textView.setGravity(Gravity.CENTER);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                pager.setCurrentItem(position);
            }
        });
    }

    private void adjustWithOrientation(int orientation) {
        if (orientation == HORIZONTAL) {
            setGravity(Gravity.CENTER);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        Log.v("Chart onMeasure w", MeasureSpec.toString(widthMeasureSpec));
//        Log.v("Chart onMeasure h", MeasureSpec.toString(heightMeasureSpec));
        int height = MeasureSpec.makeMeasureSpec(Math.round(convertDpToPixel(layoutHeight)), MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, height);
    }

    private int measureDimension(int desiredSize, int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else {
            result = desiredSize;
            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        if (result < desiredSize) {
            Log.e("ChartView", "The view is too small, the content might get cut");
        }
        return result;
    }

    private void validateAndSetRange(float MaxTextSize, float MinTextSize) {
        MaxTextSize = convertPixelsToDp(MaxTextSize);
        MinTextSize = convertPixelsToDp(MinTextSize);
        if (MaxTextSize > 46 || MaxTextSize < 12)
            mMaxTextSize = 46.0f;
        else
            mMaxTextSize = MaxTextSize;
        if (MinTextSize < 12 || MinTextSize > mMaxTextSize)
            mMinTextSize = 12.0f;
        else
            mMinTextSize = MinTextSize;
    }

    public int getSelectedColor() {
        return mSelectedColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public float getMaxTextSize() {
        return mMaxTextSize;
    }

    public float getMinTextSize() {
        return mMinTextSize;
    }

    public int getStartAt() {
        return mStartAt;
    }

    public int getOrientation() {
        return mOrientation==1?VERTICAL:HORIZONTAL;
    }

    public ViewPager getPager() {
        return pager;
    }

    public float convertPixelsToDp(float px) {
        Resources r = context.getResources();
        DisplayMetrics metrics = r.getDisplayMetrics();
        return px / (metrics.densityDpi / 160f);
    }

    public float convertDpToPixel(float dp) {
        Resources r = context.getResources();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (gestureEnabled)
            mGestureDetector.onTouchEvent(event);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            performClick();
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }

    private enum SpaceMode {
        COZY(0),
        COMPAT(1),
        JUMBO(2);
        int mode;

        SpaceMode(int mode) {
            this.mode = mode;
        }

        public int getMode() {
            return mode;
        }
    }
}
