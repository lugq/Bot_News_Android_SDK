package com.kido.ucmaindemo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

/**
 * @author Kido
 */

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    private Button mUcNewsBarButton;
    private Button mOnlyUcNewsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        bindViews();
    }

    private void bindViews() {
        mUcNewsBarButton = (Button) findViewById(R.id.addView_button);
        mOnlyUcNewsButton = (Button) findViewById(R.id.preLayout_button);

        mUcNewsBarButton.setOnClickListener(this);
        mOnlyUcNewsButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addView_button:
                startActivity(new Intent(this, AddViewActivity.class));
                break;
            case R.id.preLayout_button:
                startActivity(new Intent(this, PreLayoutActivity.class));
                break;
        }
    }
}
