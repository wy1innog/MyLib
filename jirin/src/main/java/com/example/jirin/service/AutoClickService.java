package com.example.jirin.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.content.Intent;
import android.graphics.Path;
import android.os.Handler;
import android.view.accessibility.AccessibilityEvent;

import com.example.jirin.utils.LogUtil;

/**
 * @ClassName AutoClickService
 * @Author pengfan
 * @Date 2023/5/5
 * @Description 通过无障碍服务的方式实现自动点击服务
 */
public class AutoClickService extends AccessibilityService {
    protected final String TAG = this.getClass().getSimpleName();

    private static AutoClickService mInstance = null;

    public static AutoClickService getInstance() {
        return mInstance;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {

    }

    @Override
    public void onInterrupt() {
        LogUtil.d(TAG, "onInterrupt: -----");
    }

    /**
     * 执行自动点击， 点击屏幕上的位置(x, y)
     * @param x 点击屏幕上的横坐标
     * @param y 点击屏幕上的纵坐标
     */
    public void autoClick(int x, int y) {
        Path path = new Path();
        path.moveTo(x, y);
        GestureDescription gestureDescription;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            gestureDescription = new GestureDescription.Builder()
                    .addStroke(new GestureDescription.StrokeDescription(path,
                            10, 10))
                    .build();
            dispatchGesture(gestureDescription, getGestureResultCallback(), getHandler());
        } else {
            LogUtil.e(TAG, "autoClick: the android version is not support accessibility " +
                    "service");
        }
    }

    /**
     * 需要处理自动点击无障碍服务中的callback， 重写此方法
     * @return 无障碍服务执行的回调监听，默认不设置回调监听返回null
     */
    protected GestureResultCallback getGestureResultCallback() {
        return null;
    }

    /**
     * 需要处理自动点击无障碍服务的Handler，重写此方法
     * @return 返回处理业务的Handler， 默认为null
     */
    protected Handler getHandler() {
        return null;
    }

    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        mInstance = this;
        LogUtil.d(TAG, "onServiceConnected: connected AutoClickService");
    }

    @Override
    public boolean onUnbind(Intent intent) {
        mInstance = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        mInstance = null;
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public void onDestroy() {
        mInstance = null;
        super.onDestroy();
    }
}
