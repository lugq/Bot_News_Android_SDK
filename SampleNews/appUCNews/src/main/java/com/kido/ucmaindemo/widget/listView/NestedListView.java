package com.kido.ucmaindemo.widget.listView;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ListView;

import com.kido.ucmaindemo.utils.Logger;

/**
 * 支持嵌套滑动的ListView
 * <p>
 * 通过查看AbsListView源码，发现其已对嵌套滑动做了一些处理，但是是api21的时候才加的。
 * <p>为了兼容api21之前达到嵌套滑动的效果，此处针对api21之前的情况重写了onTouchEvent事件。
 *
 * @author Kido
 */

public class NestedListView extends ListView implements NestedScrollingChild {

    private static final String TAG = "NestedListView";
    private static final int INVALID_POINTER = -1;

    private NestedScrollingChildHelper mChildHelper;
    private int mScrollPointerId = INVALID_POINTER;
    private int mLastTouchY;
    private final int[] mScrollOffset = new int[2];
    private final int[] mScrollConsumed = new int[2];
    private int mNestedOffsetY;

    private boolean mScrollable = true;

    public NestedListView(Context context) {
        super(context);
        init();
    }

    public NestedListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public NestedListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
        setTouchScrollable(true);
    }

    /**
     * 该方法用于设置是否允许触摸滚动（取消move事件传递）
     *
     * @param scrollable
     */
    public void setTouchScrollable(boolean scrollable) {
        mScrollable = scrollable;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return onInterceptTouchEventUnder21(ev);
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return onTouchEventUnder21(event);
        }
        return super.onTouchEvent(event);

    }

    private boolean onInterceptTouchEventUnder21(MotionEvent event) {

        return super.onInterceptTouchEvent(event);
    }


    private boolean onTouchEventUnder21(MotionEvent event) {

        final MotionEvent vtev = MotionEvent.obtain(event);
        final int action = MotionEventCompat.getActionMasked(event);
        final int actionIndex = MotionEventCompat.getActionIndex(event);
        if (action == MotionEvent.ACTION_DOWN) {
            mNestedOffsetY = 0;
        }
        vtev.offsetLocation(0, mNestedOffsetY);

        Logger.d(TAG, "event.action=%s, mNestedOffsetY=%s", action, mNestedOffsetY);

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mScrollPointerId = event.getPointerId(0);
                mLastTouchY = (int) (event.getY() + 0.5f);
                Logger.d(TAG, "ACTION_DOWN-> mLastTouchY=%s, mNestedOffsetY=%s", mLastTouchY, mNestedOffsetY);
                startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
                break;
            case MotionEventCompat.ACTION_POINTER_DOWN:
                mScrollPointerId = event.getPointerId(actionIndex);
                mLastTouchY = (int) (event.getX(actionIndex) + 0.5f);

                break;
            case MotionEvent.ACTION_MOVE:
                final int index = event.findPointerIndex(mScrollPointerId);
                if (index < 0) {
                    Log.e(TAG, "Error processing scroll; pointer index for id " +
                            mScrollPointerId + " not found. Did any MotionEvents get skipped?");
                    return false;
                }
                final int y = (int) (event.getY(index) + 0.5f);
                int originDy = mLastTouchY - y;
                int dy = originDy;
                int oldY = getScrollY();

                Logger.d(TAG, "ACTION_MOVE-> mLastTouchY=%s, y=%s, dy=%s, oldY=%s, mNestedOffsetY=%s", mLastTouchY, y, dy, oldY, mNestedOffsetY);

                if (dispatchNestedPreScroll(0, dy, mScrollConsumed, mScrollOffset)) {
                    dy -= mScrollConsumed[1];
                    vtev.offsetLocation(0, -mScrollOffset[1]);
                    mNestedOffsetY += mScrollOffset[1];
                    Logger.i(TAG, "dispatchNestedPreScroll-> mScrollConsumed[1]=%s, mScrollOffset[1]=%s, dy=%s,  mNestedOffsetY=%s",
                            mScrollConsumed[1], mScrollOffset[1], dy, mNestedOffsetY);
                } else {
                    Logger.e(TAG, "dispatchNestedPreScroll-> false)");
                }
                mLastTouchY = y - mScrollOffset[1];
                Logger.d(TAG, "dispatchNestedPreScroll outer -> y=%s, mScrollOffset[1]=%s, mLastTouchY=%s, dy=%s", y, mScrollOffset[1], mLastTouchY, dy);
                if (dy < 0) {
                    int newScrollY = Math.max(0, oldY + dy);
                    dy -= newScrollY - oldY;
                    Logger.d(TAG, "dy < 0 -> newScrollY=%s, dy=%s", y, newScrollY, dy);
                    if (dispatchNestedScroll(0, newScrollY - dy, 0, dy, mScrollOffset)) {
                        vtev.offsetLocation(0, mScrollOffset[1]);
                        mNestedOffsetY += mScrollOffset[1];
                        mLastTouchY -= mScrollOffset[1];
                        Logger.i(TAG, "dispatchNestedScroll-> mScrollOffset[1]=%s, mLastTouchY=%s,  mNestedOffsetY=%s",
                                mScrollOffset[1], mLastTouchY, mNestedOffsetY);
                    } else {
                        Logger.e(TAG, "dispatchNestedScroll-> false)");
                    }
                }

                if (!mScrollable && Math.abs(originDy) >= 1) {
                    event.setAction(MotionEvent.ACTION_CANCEL); // 取消move事件传递
                }

                break;
            case MotionEventCompat.ACTION_POINTER_UP: {
                onPointerUp(event);
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stopNestedScroll();
                break;
        }
        vtev.recycle();
        super.onTouchEvent(event);
        return true;
    }

    private void onPointerUp(MotionEvent e) {
        final int actionIndex = MotionEventCompat.getActionIndex(e);
        if (e.getPointerId(actionIndex) == mScrollPointerId) {
            // Pick a new pointer to pick up the slack.
            final int newIndex = actionIndex == 0 ? 1 : 0;
            mScrollPointerId = e.getPointerId(newIndex);
            mLastTouchY = (int) (e.getY(newIndex) + 0.5f);
        }
    }


    // NestedScrollingChild
    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mChildHelper.setNestedScrollingEnabled(enabled);
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
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
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