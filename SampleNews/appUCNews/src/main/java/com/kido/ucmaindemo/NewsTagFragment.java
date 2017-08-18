package com.kido.ucmaindemo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.kido.ucmaindemo.adapter.ListViewAdapter;
import com.kido.ucmaindemo.utils.Logger;
import com.kido.ucmaindemo.widget.listView.NestedListView;
import com.kido.ucmaindemo.widget.refresh.KSwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * 新闻标签对应的fragment
 *
 * @author Kido
 */
public class NewsTagFragment extends Fragment {
    private static final String KEY_TITLE = "title";
    private static final String KEY_OPENING = "opening";

    private NestedScrollView mNestedScrollView;
    private NestedListView mListView;
    private KSwipeRefreshLayout mRefreshLayout;

    private String mTitle = "";
    private boolean mIsOpeningState;
    private List<KSwipeRefreshLayout.OnRefreshListener> mOnRefreshListeners = new ArrayList<>();

    final ArrayList<String> dataList = new ArrayList<>();
    ListViewAdapter adapter;

    public static NewsTagFragment newInstance() {
        return newInstance("");
    }

    public static NewsTagFragment newInstance(String title) {
        return newInstance(title, true);
    }

    public static NewsTagFragment newInstance(String title, boolean isOpeningState) {
        Bundle args = new Bundle();
        args.putString(KEY_TITLE, title);
        args.putBoolean(KEY_OPENING, isOpeningState);
        NewsTagFragment fragment = new NewsTagFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_news_tag, container, false);
        initView(rootView);
        initData();
        return rootView;
    }

    private void initView(View rootView) {
        mTitle = getArguments().getString(KEY_TITLE);
        mIsOpeningState = getArguments().getBoolean(KEY_OPENING);
//        mNestedScrollView = (NestedScrollView) rootView.findViewById(R.id.nested_scrollView);
        mListView = (NestedListView) rootView.findViewById(R.id.recyclerView);
        mRefreshLayout = (KSwipeRefreshLayout) rootView.findViewById(R.id.refresh_layout);
//        mListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        mRefreshLayout.setOnRefreshListener(new KSwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                triggerOnRefresh();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRefreshLayout.setRefreshing(false);
                    }
                }, 1500); //模拟下拉耗时
            }

            @Override
            public void onTerminal() {
                triggerOnTerminal();
            }
        });
        setOpeningState(mIsOpeningState);
    }

    public void setOpeningState(boolean isOpening) {
        if (isOpening) {
            scrollToTop(false);
            setRefreshEnable(false);
            setTouchScrollable(false);
        } else {
            scrollToTop(false);
            setRefreshEnable(true);
            setTouchScrollable(true);
        }
    }

    public void setTouchScrollable(boolean scrollable) {
        if (mListView != null) {
            mListView.setTouchScrollable(scrollable);
        }
    }


    public void setRefreshEnable(boolean refreshEnable) {
        if (mRefreshLayout != null) {
            mRefreshLayout.setEnabled(refreshEnable);
        }
    }

    public void scrollToTop(boolean smooth) {
//        if (mNestedScrollView != null) {
//            mNestedScrollView.scrollTo(0, 0);
//        }
        if (mListView != null) {
            if (smooth) {
                mListView.smoothScrollToPosition(0);
            } else {
                mListView.setSelection(0);
            }
        }
    }

    public void addOnRefreshListener(KSwipeRefreshLayout.OnRefreshListener listener) {
        mOnRefreshListeners.add(listener);
    }

    private void triggerOnRefresh() {
        for (KSwipeRefreshLayout.OnRefreshListener listener : mOnRefreshListeners) {
            if (listener != null) {
                listener.onRefresh();
            }
        }
    }

    private void triggerOnTerminal() {
        for (KSwipeRefreshLayout.OnRefreshListener listener : mOnRefreshListeners) {
            if (listener != null) {
                listener.onTerminal();
            }
        }
    }

    private void initData() {

        for (int i = 0; i < 40; i++) {
            dataList.add("This is the title. (" + mTitle + i + ")");
        }
//        RecyclerViewAdapter adapter = new RecyclerViewAdapter(dataList);
//        adapter.setOnItemClickListener(new RecyclerViewAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(getContext(), dataList.get(position), Toast.LENGTH_LONG).show();
//            }
//        });
        adapter = new ListViewAdapter(getContext(), dataList);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position % 2 == 0) {
                    addData();
                } else {
                    Logger.e("kido", "mListView.getMeasureHeight->" + mListView.getMeasuredHeight());
                }
            }
        });

    }

    public void addData() {
        int size = dataList.size();
        for (int i = size; i < size + 5; i++) {
            dataList.add("This is the title. (" + mTitle + i + ")");
        }
        Toast.makeText(getContext(), "You added 5 data.", Toast.LENGTH_SHORT).show();
        adapter.notifyDataSetChanged();

//        mListView.setAdapter(new ListViewAdapter(getContext(), dataList));

    }

}
