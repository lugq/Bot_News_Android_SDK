package com.kido.ucmaindemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.kido.ucmaindemo.widget.main.UcNewsBarLayout;
import com.kido.ucmaindemo.widget.main.UcNewsTitleLayout;
import com.kido.ucmaindemo.widget.refresh.KSwipeRefreshLayout;

import ai.botbrain.ttcloud.sdk.model.TopTitleEntity;
import ai.botbrain.ttcloud.sdk.view.fragment.GraphicFragment;
import ai.botbrain.ttcloud.sdk.widget.NewsContentPager;
import ai.botbrain.ttcloud.sdk.widget.TsdTabLayout;

/**
 * 仿UC首页。此处测试代码模拟內嵌信息流页面view的情况。
 *
 * @author Kido
 */

public class PreLayoutActivity extends AppCompatActivity implements TsdTabLayout.RenderListener, NewsContentPager.CallBack {

    private KSwipeRefreshLayout mRefreshLayout;
    private UcNewsTitleLayout mTitleLayout;
    private UcNewsBarLayout mBarLayout;
    private TsdTabLayout mTabLayout;
    private NewsContentPager mContentPager;

    private ImageView bottomBar;

    private GraphicFragment mCurrentFragment;

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
        mRefreshLayout = (KSwipeRefreshLayout) findViewById(R.id.root_refresh_layout);
        mTitleLayout = (UcNewsTitleLayout) findViewById(R.id.titlebar_layout);
        mBarLayout = (UcNewsBarLayout) findViewById(R.id.news_header_layout);
        mContentPager = (NewsContentPager) findViewById(R.id.news_viewPager);
        mContentPager.setCallBack(this);
        mTabLayout = (TsdTabLayout) findViewById(R.id.news_tabLayout);
        mTabLayout.setCallBack(this);
        bottomBar = (ImageView) findViewById(R.id.bottom_bar);
        bottomBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mBarLayout.isClosed()) {
                    mBarLayout.openBar();
                }
            }
        });

    }

    private void initTitleAndHeader() {
        mTitleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentFragment.scrollToTop(true);
            }
        });
        mBarLayout.setBarStateListener(new UcNewsBarLayout.OnBarStateListener() {
            @Override
            public void onBarStartClosing() {
                mContentPager.setPagingEnabled(true);
                mRefreshLayout.setEnabled(false);
                mCurrentFragment.setOpeningState(false);
            }

            @Override
            public void onBarStartOpening() {
                mContentPager.setCurrentItem(0, false);
                mContentPager.setPagingEnabled(false);
                mRefreshLayout.setEnabled(true);
                mCurrentFragment.setOpeningState(true);
            }

            @Override
            public void onBarClosed() {
                bottomBar.setImageResource(R.drawable.bottom_bar_toutiao);
            }

            @Override
            public void onBarOpened() {
                bottomBar.setImageResource(R.drawable.bottom_bar_home);
            }
        });
    }


    private void initTabsAndPager() {
        String[] newsTabTitles = getResources().getStringArray(R.array.news_tab_titles);
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
        }
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
            mBarLayout.openBar();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void renderTabLayoutSuccess(TopTitleEntity.Data data, String appName) {
        mContentPager.setNewsAdapter(getSupportFragmentManager(), data);
        mContentPager.setupTabLayout(mTabLayout);
        mContentPager.setOffscreenPageLimit(10);
        mContentPager.setPagingEnabled(false);
        mContentPager.setCurrentItem(0);
    }

    @Override
    public void renderTabLayoutFail() {
        // TODO 这里做重试
    }

    @Override
    public void getCurrentFragment(GraphicFragment currentFragment) {
        mCurrentFragment = currentFragment;
        if (mContentPager!=null)
            return;
        mCurrentFragment.setOpeningState(true);
    }
}
