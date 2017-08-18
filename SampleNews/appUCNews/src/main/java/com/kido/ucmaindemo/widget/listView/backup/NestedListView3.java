package com.kido.ucmaindemo.widget.listView.backup;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

/**
 * @author Kido
 */

public class NestedListView3 extends ListView implements NestedScrollingChild {

    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];

    private final float[] mTmpPoint = new float[2];

    // Used for offsetting MotionEvents that we feed to the VelocityTracker.
    // In the future it would be nice to be able to give this to the VelocityTracker
    // directly, or alternatively put a VT into absolute-positioning mode that only
    // reads the raw screen-coordinate x/y values.
    private int mNestedYOffset = 0;
    private int mLastY;

    private NestedScrollingChildHelper mChildHelper;

    public NestedListView3(Context context) {
        this(context, null);
    }

    public NestedListView3(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedListView3(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return super.onInterceptTouchEvent(ev);
        }

        return onInterceptTouchEvent21(ev);
    }

    public boolean onInterceptTouchEvent21(MotionEvent ev) {
        final int actionMasked = ev.getActionMasked();
        switch (actionMasked) {
            case MotionEvent.ACTION_DOWN:
                mLastY = Integer.MIN_VALUE;
                mNestedYOffset = 0;
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEvent.ACTION_MOVE:

                break;
        }
        return false;
    }

    /**
     * @see android.support.v4.widget.NestedScrollView#onTouchEvent(MotionEvent)
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return super.onTouchEvent(ev);
        }
        return onTouEvent21(ev);
    }

    public boolean onTouEvent21(MotionEvent ev) {
        if (!isEnabled()) {
            // A disabled view that is clickable still consumes the touch
            // events, it just doesn't respond to them.
            return isClickable() || isLongClickable();
        }

        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        final MotionEvent vtev = MotionEvent.obtain(ev);

        final int actionMasked = ev.getActionMasked();
        if (actionMasked == MotionEvent.ACTION_DOWN) {
            mLastY = Integer.MIN_VALUE;
            mNestedYOffset = 0;
        }
        vtev.offsetLocation(0, mNestedYOffset);


        vtev.recycle();
        return true;
    }


    // NestedScrollingChild
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed,
                                        int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed,
                                           int[] offsetInWindow) {
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

}