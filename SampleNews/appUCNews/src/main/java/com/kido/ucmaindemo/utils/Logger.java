package com.kido.ucmaindemo.utils;

import android.util.Log;

import com.kido.ucmaindemo.BuildConfig;

/**
 * @author Kido
 */

public class Logger {

    private static final boolean IS_LOG_ENABLED = BuildConfig.DEBUG;

    public static void d(String tag, String message, Object... args) {
        if (IS_LOG_ENABLED) {
            Log.d(tag, String.format(message, args));
        }
    }

    public static void i(String tag, String message, Object... args) {
        if (IS_LOG_ENABLED) {
            Log.i(tag, String.format(message, args));
        }
    }


    public static void e(String tag, String message, Object... args) {
        if (IS_LOG_ENABLED) {
            Log.e(tag, String.format(message, args));
        }
    }
}
