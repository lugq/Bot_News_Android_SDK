package com.kido.ucmaindemo.widget.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kido.ucmaindemo.R;
import com.kido.ucmaindemo.widget.main.behavior.BarBehavior;
import com.kido.ucmaindemo.widget.main.behavior.BarFollowerBehavior;
import com.kido.ucmaindemo.widget.main.behavior.BarFooterBehavior;
import com.kido.ucmaindemo.widget.main.behavior.BarHeaderBehavior;


/**
 * 新闻列表的顶部Layout
 * </p>
 * 使用时可指定app:unhl_closing_footer, app:unhl_closing_header做为合拢时的头部和尾部，起到合拢动画效果。<br>
 * 另，Bar底部如果要跟内容（比如一个ViewPager）需指定对应的behavior为BarFollowerBehavior，达到嵌套动画效果。
 *
 * @author Kido
 */
@CoordinatorLayout.DefaultBehavior(BarBehavior.class)
public class UcNewsBarLayout extends FrameLayout {

    private BarBehavior mBehavior;
    private OnBarStateListener mBarStateListener;
    private Context mContext;


    private static final int INVALID_SCROLL_RANGE = -1;
    private static final int INVALID_RESOURCE_ID = -1;

    private int mOffsetRange = INVALID_SCROLL_RANGE;

    private int mHeaderId = INVALID_RESOURCE_ID;
    private int mFooterId = INVALID_RESOURCE_ID;
    private int mFollowerId = INVALID_RESOURCE_ID;
    private View mHeaderView;
    private View mFooterView;
    private View mFollowerView;

    public UcNewsBarLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public UcNewsBarLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public UcNewsBarLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.UcNewsBarLayout);

        mOffsetRange = a.getDimensionPixelSize(R.styleable.UcNewsBarLayout_unbl_offset_range, INVALID_SCROLL_RANGE);
        mHeaderId = a.getResourceId(R.styleable.UcNewsBarLayout_unbl_closing_header, INVALID_RESOURCE_ID);
        mFooterId = a.getResourceId(R.styleable.UcNewsBarLayout_unbl_closing_footer, INVALID_RESOURCE_ID);
        mFollowerId = a.getResourceId(R.styleable.UcNewsBarLayout_unbl_closing_follower, INVALID_RESOURCE_ID);

        a.recycle();

        init(context);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        ensureCoSiblings();
        ensureBehavior();
    }

    /**
     * 找到指定的header, footer, follower (若有)，然后赋予对应的Behavior（若无）
     */
    private void ensureCoSiblings() {
        mHeaderView = findCoSibling(mHeaderId, BarHeaderBehavior.class);
        mFooterView = findCoSibling(mFooterId, BarFooterBehavior.class);
        mFollowerView = findCoSibling(mFollowerId, BarFollowerBehavior.class);
    }


    private View findCoSibling(int id, Class<? extends CoordinatorLayout.Behavior> behaviorType) {
        View sibling = null;
        ViewGroup parent = (ViewGroup) getParent();
        if (parent instanceof CoordinatorLayout) { // 只在CoordinatorLayout中生效
            if (id != INVALID_RESOURCE_ID) {
                sibling = parent.findViewById(id);
            }
            if (sibling == null) { // findView 找不到的话则遍历parent底下的设置了对应behavior的View(兼容没有指定unbl_closing_xxx之类的情况)
                for (int i = 0; i < parent.getChildCount(); i++) {
                    View child = parent.getChildAt(i);
                    CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
                    if (layoutParams.getBehavior() != null && layoutParams.getBehavior().getClass() == behaviorType) {
                        sibling = child;
                        break;
                    }
                }
            }
            if (sibling != null) { // 若找到对应的可以协助兄弟，再看看是否设置了Behavior
                CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) sibling.getLayoutParams();
                if (layoutParams.getBehavior() == null || layoutParams.getBehavior().getClass() != behaviorType) {
                    try {
                        layoutParams.setBehavior(behaviorType.newInstance());
                    } catch (InstantiationException e) {
                    } catch (IllegalAccessException e) {
                    }
                }
            }
        }

        return sibling;

    }

    private void ensureBehavior() {
        if (mBehavior == null) {
            if (getLayoutParams() instanceof CoordinatorLayout.LayoutParams) {
                final CoordinatorLayout.LayoutParams coParams = (CoordinatorLayout.LayoutParams) getLayoutParams();
                if (coParams.getBehavior() instanceof BarBehavior) {
                    mBehavior = (BarBehavior) coParams.getBehavior();
                    mBehavior.setPagerStateListener(new BarBehavior.OnPagerStateListener() {
                        @Override
                        public void onBarStartClosing() {
                            if (mBarStateListener != null) {
                                mBarStateListener.onBarStartClosing();
                            }
                        }

                        @Override
                        public void onBarStartOpening() {
                            if (mBarStateListener != null) {
                                mBarStateListener.onBarStartOpening();
                            }
                        }

                        @Override
                        public void onBarClosed() {
                            if (mBarStateListener != null) {
                                mBarStateListener.onBarClosed();
                            }
                        }

                        @Override
                        public void onBarOpened() {
                            if (mBarStateListener != null) {
                                mBarStateListener.onBarOpened();
                            }
                        }
                    });
                }
            }
        }
    }

    private void init(Context context) {
        mContext = context;
    }

    /**
     * 本layout的header的高度
     *
     * @return
     */
    public int getHeaderHeight() {
        int height = 0;
        if (mHeaderView != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mHeaderView.getLayoutParams();
            height = mHeaderView.getMeasuredHeight() /*+ layoutParams.topMargin + layoutParams.bottomMargin*/; // FIXME: 17/5/29 BarHeaderBehavior中的实现是改变topMargin
        }
        return height;
    }

    /**
     * 本layout的footer的高度
     *
     * @return
     */

    public int getFooterHeight() {
        int height = 0;
        if (mFooterView != null) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) mFooterView.getLayoutParams();
            height = mFooterView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
        }
        return height;
    }

    /**
     * 本layout能向上滑动的最大偏移
     *
     * @return
     */
    public int getBarOffsetRange() {
        if (mOffsetRange != INVALID_SCROLL_RANGE) {
            return mOffsetRange;
        }
        mOffsetRange = -(getHeaderHeight() + getFooterHeight()); // 默认offset为header的高度+footer的高度
        return mOffsetRange;
    }


    public void setBarStateListener(OnBarStateListener listener) {
        mBarStateListener = listener;
    }

    public void openBar() {
        if (mBehavior != null) {
            mBehavior.openPager();
        }
    }

    public void closeBar() {
        if (mBehavior != null) {
            mBehavior.closePager();
        }
    }

    public boolean isClosed() {
        if (mBehavior != null) {
            return mBehavior.isClosed();
        }
        return false;
    }


    /**
     * callback for HeaderPager 's state
     */
    public interface OnBarStateListener extends BarBehavior.OnPagerStateListener {
    }


}
