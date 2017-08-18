package com.kido.ucmaindemo;

import android.app.Application;
import android.content.Context;

import ai.botbrain.ttcloud.api.TtCloudManager;

/**
 * @author Kido
 */

public class MyApplication extends Application {
    private static Context sContext;

    @Override
    public void onCreate() {
        super.onCreate();
        sContext = this;
        TtCloudManager.init(this);

    }

    public static Context getContext() {
        return sContext;
    }
}
