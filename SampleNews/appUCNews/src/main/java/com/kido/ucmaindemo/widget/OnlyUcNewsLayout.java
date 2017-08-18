package com.kido.ucmaindemo.widget;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kido.ucmaindemo.NewsTagFragment;
import com.kido.ucmaindemo.R;
import com.kido.ucmaindemo.widget.main.UcNewsContentPager;
import com.kido.ucmaindemo.widget.main.UcNewsTabLayout;
import com.kido.ucmaindemo.widget.main.UcNewsTitleLayout;
import com.kido.ucmaindemo.widget.refresh.KSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 信息流頁面
 *
 * @author Kido
 */

public class OnlyUcNewsLayout extends FrameLayout {

    private UcNewsTitleLayout mTitleLayout;
    private UcNewsTabLayout mTabLayout;
    private UcNewsContentPager mContentPager;
    private ImageView mBottomBar;

    private List<NewsTagFragment> mFragments;

    private Context mContext;

    private OnGobackListener mOnGobackListener;

    public OnlyUcNewsLayout(@NonNull Context context) {
        super(context);
        init(context);
    }

    public OnlyUcNewsLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public OnlyUcNewsLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        initData();
    }

    private void init(Context context) {
        mContext = context;
        LayoutInflater.from(mContext).inflate(R.layout.layout_onlyucnews, this);
        bindViews();
        initTitleAndHeader();
        initTabsAndPager();
    }

    private void initData() {
//        for (NewsTagFragment fragment : mFragments) {
//            fragment.setOpeningState(false);
//        }
    }


    private void bindViews() {

        mTitleLayout = (UcNewsTitleLayout) findViewById(R.id.titlebar_layout);
        mContentPager = (UcNewsContentPager) findViewById(R.id.news_viewPager);
        mTabLayout = (UcNewsTabLayout) findViewById(R.id.news_tabLayout);
        mBottomBar = (ImageView) findViewById(R.id.bottom_bar);
        mBottomBar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnGobackListener != null) {
                    mOnGobackListener.onGoback();
                }
            }
        });

    }

    private void initTitleAndHeader() {
        mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragments.get(mContentPager.getCurrentItem()).scrollToTop(true);
            }
        });
    }


    private void initTabsAndPager() {
        String[] newsTabTitles = getResources().getStringArray(R.array.news_tab_titles);
        mFragments = new ArrayList<>(newsTabTitles.length);
        for (String title : newsTabTitles) {
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
            NewsTagFragment fragment = NewsTagFragment.newInstance(title, false);
            fragment.addOnRefreshListener(new KSwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // do nothing, because something has been done inside the fragment.
                }

                @Override
                public void onTerminal() { // open header to go back home
                    if (mOnGobackListener != null) {
                        mOnGobackListener.onGoback();
                    }
                }
            });
            mFragments.add(fragment);
        }

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mContentPager.setupTabLayout(mTabLayout);
        //mContentPager.setAdapter(new TagFragmentAdapter(((FragmentActivity) getContext()).getSupportFragmentManager(), mFragments));

    }

    public void setOnGobackListener(OnGobackListener listener) {
        this.mOnGobackListener = listener;
    }

    public static interface OnGobackListener {
        void onGoback();
    }

}
