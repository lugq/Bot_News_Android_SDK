package com.kido.ucmaindemo.widget.main.behavior;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

import com.kido.ucmaindemo.utils.Logger;
import com.kido.ucmaindemo.widget.main.UcNewsBarLayout;
import com.kido.ucmaindemo.widget.main.helper.BarHelper;

/**
 * Behavior for Bar Header.
 * <p>
 * e.g. TitleLayout
 *
 * @author Kido
 */

public class BarHeaderBehavior extends CoordinatorLayout.Behavior<View> {
    private static final String TAG = "UNBL_HeaderBehavior";

    public BarHeaderBehavior() {
    }

    public BarHeaderBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean onLayoutChild(CoordinatorLayout parent, View child, int layoutDirection) {
        ((CoordinatorLayout.LayoutParams) child.getLayoutParams()).topMargin = -child.getMeasuredHeight(); //
        parent.onLayoutChild(child, layoutDirection);
        Logger.d(TAG, "layoutChild-> top=%s, height=%s", child.getTop(), child.getHeight());
        return true;
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
        int childOffsetRange = BarHelper.getHeaderHeight(dependency);

        float childTransY = dependency.getTranslationY() == 0 ? 0 :
                dependency.getTranslationY() == dependencyOffsetRange ? childOffsetRange :
                        (dependency.getTranslationY() / (dependencyOffsetRange * 1.0f) * childOffsetRange);
        float realChildTransY = BarHelper.ensureValueInRange(childTransY, 0, childOffsetRange);
        Logger.d(TAG, "offsetChildAsNeeded-> dependency.getTranslationY()=%s, dependencyOffsetRange=%s, childOffsetRange=%s, childTransY=%s, realChildTransY=%s",
                dependency.getTranslationY(), dependencyOffsetRange, childOffsetRange, childTransY, realChildTransY);
        ViewCompat.setTranslationY(child, realChildTransY);
    }

    private boolean isDependOn(View dependency) {
        return dependency instanceof UcNewsBarLayout;
    }
}
