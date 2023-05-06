package com.example.mylib;

import android.app.Application;

import com.example.jirin.utils.LogUtil;

/**
 * @ClassName MyLibApplication
 * @Author pengfan
 * @Date 2023/5/6
 * @Description
 */
public class MyLibApplication extends Application {
    private static final String TAG = "MyLibApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.init(BuildConfig.DEBUG);
        LogUtil.d(TAG, "onCreate: -----");
    }
}
