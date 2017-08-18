package com.kido.ucmaindemo.adapter;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.List;

import ai.botbrain.ttcloud.sdk.fragment.GraphicFragment;

/**
 * 新闻标签fragment适配器
 *
 * @author Kido
 */

public class TagFragmentAdapter extends FragmentStatePagerAdapter {

    private List<GraphicFragment> mFragments;

    public TagFragmentAdapter(FragmentManager fm, List<GraphicFragment> fragments) {
        super(fm);
        this.mFragments = fragments;
    }


    @Override
    public GraphicFragment getItem(int position) {
        return mFragments == null ? null : mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments == null ? 0 : mFragments.size();
    }
}
