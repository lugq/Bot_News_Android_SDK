package com.peter.newssdkdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import ai.botbrain.ttcloud.sdk.activity.TsdH5ReaderOnWvActivity;
import ai.botbrain.ttcloud.sdk.fragment.IndexFragment;

public class MainActivity extends FragmentActivity {

    private IndexFragment mNewsIndexFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewsIndexFragment = new IndexFragment();
        initSchema();

        if (!mNewsIndexFragment.isAdded()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, mNewsIndexFragment);
            fragmentTransaction.commit();
        }
    }

    private void initSchema() {
        Intent intent = getIntent();
        String action = intent.getAction();
        if (Intent.ACTION_VIEW.equals(action)) {
            Uri uri = intent.getData();
            if (uri != null) {
                String queryString = uri.getQuery();
                String url = "https://bkd.botbrain.ai" + uri.getPath() + "?" + queryString + "&platform=android&type=2";
                intent.setClass(this, TsdH5ReaderOnWvActivity.class);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        }
    }


}
