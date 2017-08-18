package com.kido.ucmaindemo.widget.main.behavior;


import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.kido.ucmaindemo.utils.Logger;
import com.kido.ucmaindemo.widget.main.UcNewsBarLayout;
import com.kido.ucmaindemo.widget.main.base.HeaderScrollingViewBehavior;
import com.kido.ucmaindemo.widget.main.helper.BarHelper;

import java.util.List;

/**
 * Behavior for Bar Follower.
 * <p>
 * e.g. ViewPager
 *
 * @author Kido
 */

public class BarFollowerBehavior extends HeaderScrollingViewBehavior {
    private static final String TAG = "UNBL_FollowerBehavior";

    public BarFollowerBehavior() {
    }

    public BarFollowerBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return isDependOn(dependency);
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        offsetChildAsNeeded(parent, child, dependency);
        return false;
    }

    private void offsetChildAsNeeded(CoordinatorLayout parent, View child, View dependency) {
        int dependencyOffsetRange = BarHelper.getBarOffsetRange(dependency);
        float childOffsetRange = -getScrollRange(dependency);

        float childTransY = dependency.getTranslationY() == 0 ? 0 :
                dependency.getTranslationY() == dependencyOffsetRange ? childOffsetRange :
                        (dependency.getTranslationY() / (dependencyOffsetRange * 1.0f) * childOffsetRange);
        float realChildTransY = BarHelper.ensureValueInRange(childTransY, 0, childOffsetRange);
        Logger.d(TAG, "offsetChildAsNeeded-> dependency.getTranslationY()=%s, dependencyOffsetRange=%s, childOffsetRange=%s, childTransY=%s, realChildTransY=%s",
                dependency.getTranslationY(), dependencyOffsetRange, childOffsetRange, childTransY, realChildTransY);
        ViewCompat.setTranslationY(child, realChildTransY);
    }


    @Override
    protected View findFirstDependency(List<View> views) {
        for (int i = 0, z = views.size(); i < z; i++) {
            View view = views.get(i);
            if (isDependOn(view))
                return view;
        }
        return null;
    }

    @Override
    protected int getScrollRange(View v) {
        if (isDependOn(v)) {
            return Math.max(0, v.getMeasuredHeight() - BarHelper.getHeaderHeight(v) - BarHelper.getFooterHeight(v));
        } else {
            return super.getScrollRange(v);
        }
    }


    private boolean isDependOn(View dependency) {
        return dependency instanceof UcNewsBarLayout;
    }
}