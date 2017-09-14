package com.peter.newssdkdemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;

import ai.botbrain.ttcloud.api.TtCloudManager;
import ai.botbrain.ttcloud.sdk.view.activity.TsdH5ReaderOnWvActivity;
import ai.botbrain.ttcloud.sdk.view.fragment.IndexFragment;

/**
 * 在线文档地址：https://lugq.gitbooks.io/bot_news_document/content/
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private IndexFragment mNewsIndexFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNewsIndexFragment = new IndexFragment();

        initView();
        initSchema();

        if (!mNewsIndexFragment.isAdded()) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, mNewsIndexFragment);
            fragmentTransaction.commit();
        }

    }

    private void initView() {
        AppCompatButton btn_day = (AppCompatButton) findViewById(R.id.btn_day);
        AppCompatButton btn_night = (AppCompatButton) findViewById(R.id.btn_night);
        btn_day.setOnClickListener(this);
        btn_night.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_day) {
            TtCloudManager.setDayTheme();
            return;
        }
        if (id == R.id.btn_night) {
            TtCloudManager.setNightTheme();
        }
    }
}
