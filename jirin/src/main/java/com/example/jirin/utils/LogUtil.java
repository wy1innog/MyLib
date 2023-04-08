package com.example.jirin.utils;

import android.util.Log;

public class LogUtil {
    private static final String TAG = "JiRin-";
    private static boolean IS_DEBUG = false;

    /**
     * 默认日志的debug模式是关闭
     */
    public static void init() {
        init(false);
    }

    public static void init(boolean isDebug) {
        IS_DEBUG = isDebug;
    }

    public static void d(String msg) {
        if (IS_DEBUG) {
            d("", msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(TAG + tag, msg);
        }
    }

    public static void i(String msg) {
        i("", msg);
    }

    public static void i(String tag, String msg) {
        Log.i(TAG + tag, msg);
    }

    public static void v(String msg) {
        v("", msg);
    }

    public static void v(String tag, String msg) {
        Log.v(TAG + tag, msg);
    }

    public static void w(String msg) {
        w("", msg);
    }

    public static void w(String tag, String msg) {
        Log.w(TAG + tag, msg);
    }

    public static void e(String msg) {
        e("", msg);
    }

    public static void e(String tag, String msg) {
        Log.e(TAG + tag, msg);
    }
}
