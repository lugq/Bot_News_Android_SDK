package com.kido.ucmaindemo.widget.main.helper;

import android.view.View;

import com.kido.ucmaindemo.widget.main.UcNewsBarLayout;

/**
 * @author Kido
 * @email everlastxgb@gmail.com
 * @create_time 17/6/4 17:10
 */

public class BarHelper {
    public static int getBarOffsetRange(View dependency) {
        if (dependency instanceof UcNewsBarLayout) {
            return ((UcNewsBarLayout) dependency).getBarOffsetRange();
        }
        return 0;
    }

    public static int getHeaderHeight(View dependency) {
        if (dependency instanceof UcNewsBarLayout) {
            UcNewsBarLayout barLayout = (UcNewsBarLayout) dependency;
            return barLayout.getHeaderHeight();
        }
        return 0;
    }

    public static int getFooterHeight(View dependency) {
        if (dependency instanceof UcNewsBarLayout) {
            UcNewsBarLayout barLayout = (UcNewsBarLayout) dependency;
            return barLayout.getFooterHeight();
        }
        return 0;
    }


    public static boolean isClosed(View dependency) {
        if (dependency instanceof UcNewsBarLayout) {
            UcNewsBarLayout barLayout = ((UcNewsBarLayout) dependency);
            return barLayout.isClosed();
        }
        return false;
    }

    /**
     * 确保value在[min, max]取值范围内，若越界，则取边缘值。
     *
     * @param value 原始值
     * @param r1 左区临界值
     * @param r2 右区临界值
     * @return
     */
    public static float ensureValueInRange(float value, float r1, float r2) {
        if (r1 > r2) { // 确保左小又大
            float temp = r1;
            r1 = r2;
            r2 = temp;
        }

        return value < r1 ? r1 :
                value > r2 ? r2 : value;
    }

}
