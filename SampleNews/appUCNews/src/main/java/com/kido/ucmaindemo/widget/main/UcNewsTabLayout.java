package com.kido.ucmaindemo.widget.main;

import android.content.Context;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;

/**
 * 新闻便签栏
 *
 * @author Kido
 */
public class UcNewsTabLayout extends TabLayout {

    public UcNewsTabLayout(Context context) {
        super(context);
        init(context);
    }

    public UcNewsTabLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UcNewsTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
    }


}
