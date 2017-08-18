package com.kido.ucmaindemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.kido.ucmaindemo.widget.OnlyUcNewsLayout;
import com.kido.ucmaindemo.widget.main.UcNewsBarLayout;
import com.kido.ucmaindemo.widget.main.UcNewsContentPager;
import com.kido.ucmaindemo.widget.main.UcNewsTabLayout;
import com.kido.ucmaindemo.widget.main.UcNewsTitleLayout;
import com.kido.ucmaindemo.widget.refresh.KSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 仿UC首页。此处测试代码模拟动态添加信息流页面view的情况。
 *
 * @author Kido
 */

public class AddViewActivity extends AppCompatActivity {

    private FrameLayout mRootContainer;
    private OnlyUcNewsLayout mOnlyUcNewsLayout;
    private KSwipeRefreshLayout mRefreshLayout;
    private UcNewsTitleLayout mTitleLayout;
    private UcNewsBarLayout mBarLayout;
    private UcNewsTabLayout mTabLayout;
    private UcNewsContentPager mContentPager;

    private ImageView bottomBar;

    private List<NewsTagFragment> mFragments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addview);
        bindViews();
        initTitleAndHeader();
        initTabsAndPager();
        initRefreshLayout();
    }

    private void bindViews() {

        mRootContainer = (FrameLayout) findViewById(R.id.root_container);
        mRefreshLayout = (KSwipeRefreshLayout) findViewById(R.id.root_refresh_layout);
        mTitleLayout = (UcNewsTitleLayout) findViewById(R.id.titlebar_layout);
        mBarLayout = (UcNewsBarLayout) findViewById(R.id.news_header_layout);
        mContentPager = (UcNewsContentPager) findViewById(R.id.news_viewPager);
        mTabLayout = (UcNewsTabLayout) findViewById(R.id.news_tabLayout);
        bottomBar = (ImageView) findViewById(R.id.bottom_bar);

    }

    private void initTitleAndHeader() {
        mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFragments.get(mContentPager.getCurrentItem()).scrollToTop(true);
            }
        });
        mBarLayout.setBarStateListener(new UcNewsBarLayout.OnBarStateListener() {
            @Override
            public void onBarStartClosing() {
                mContentPager.setPagingEnabled(true);
                mRefreshLayout.setEnabled(false);
                mFragments.get(0).setOpeningState(false);
            }

            @Override
            public void onBarStartOpening() {
                mContentPager.setCurrentItem(0, false);
                mContentPager.setPagingEnabled(false);
                mRefreshLayout.setEnabled(true);
                mFragments.get(0).setOpeningState(true);
            }

            @Override
            public void onBarClosed() {
//                bottomBar.setImageResource(R.drawable.bottom_bar_toutiao);
//                mOnlyUcNewsLayout.setVisibility(View.VISIBLE); // 模拟动态添加View
                gotoUcNews();
            }

            @Override
            public void onBarOpened() {
//                bottomBar.setImageResource(R.drawable.bottom_bar_home);
//                mOnlyUcNewsLayout.setVisibility(View.GONE);// 模拟动态移除View
//                gobackHome();
            }
        });
    }

    private void gotoUcNews() { //测试代码，仅供模拟测试
        try {
            mRootContainer.removeView(mRefreshLayout);
            if (mOnlyUcNewsLayout != null) {
                mRootContainer.removeView(mOnlyUcNewsLayout);
            }
            mOnlyUcNewsLayout = new OnlyUcNewsLayout(AddViewActivity.this);
            mOnlyUcNewsLayout.setOnGobackListener(new OnlyUcNewsLayout.OnGobackListener() {
                @Override
                public void onGoback() {
                    gobackHome();
                }
            });
            mRootContainer.addView(mOnlyUcNewsLayout, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    private void gobackHome() { //测试代码，仅供模拟测试
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                try {
                    mRootContainer.removeView(mOnlyUcNewsLayout);
                    mRootContainer.addView(mRefreshLayout);
                    mBarLayout.openBar();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void initTabsAndPager() {
        String[] newsTabTitles = getResources().getStringArray(R.array.news_tab_titles);
        mFragments = new ArrayList<>(newsTabTitles.length);
        for (String title : newsTabTitles) {
            mTabLayout.addTab(mTabLayout.newTab().setText(title));
            NewsTagFragment fragment = NewsTagFragment.newInstance(title);
            fragment.addOnRefreshListener(new KSwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    // do nothing, because something has been done inside the fragment.
                }

                @Override
                public void onTerminal() { // open header to go back home
                    mBarLayout.openBar();
                }
            });
            mFragments.add(fragment);
        }

        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mContentPager.setupTabLayout(mTabLayout);
        //mContentPager.setAdapter(new TagFragmentAdapter(getSupportFragmentManager(), mFragments));
        mContentPager.setPagingEnabled(false);

    }

    private void initRefreshLayout() {
        mRefreshLayout.setTerminalRate(1.5f);
        mRefreshLayout.setOnRefreshListener(new KSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onTerminal() { // close header to go to news list
                mBarLayout.closeBar();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mBarLayout.isClosed()) {
            gobackHome();
        } else {
            super.onBackPressed();
        }
    }
}
