package com.kido.ucmaindemo.widget.main;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * 顶部标题栏
 *
 * @author Kido
 */
public class UcNewsTitleLayout extends FrameLayout {

    public UcNewsTitleLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public UcNewsTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UcNewsTitleLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
    }


}
