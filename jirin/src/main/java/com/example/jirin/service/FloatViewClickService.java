package com.example.jirin.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.example.jirin.R;
import com.example.jirin.utils.LogUtil;
import com.example.jirin.utils.PermissionUtil;
import com.example.jirin.widget.listener.FloatViewTouchListener;

import org.apache.commons.lang3.concurrent.BasicThreadFactory;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @ClassName FloatViewClickService
 * @Author pengfan
 * @Date 2023/5/4
 * @Description 悬浮按钮点击Service，通过这个服务，可以创建可移动位置和点击的悬浮按钮。
 * 被创建的悬浮按钮可以显示在其他应用的上面。
 * [注意：使用FloatViewClickService服务需要给应用申请android.permission.SYSTEM_ALERT_WINDOW权限，
 * 如果是在Android 6.0的版本上，需要动态申请android.permission.SYSTEM_ALERT_WINDOW权限]
 */
public class FloatViewClickService extends Service implements
        FloatViewTouchListener.OnFloatViewOnClickListener,
        FloatViewTouchListener.OnFloatViewOnDragListener {
    protected final String TAG = this.getClass().getSimpleName();

    private static final String THREAD_NAME_AUTO_CLICK = "auto_click_thread";

    /**
     * 自动点击启动的延迟， 默认0ms
     */
    private static final int INIT_DELAY = 0;

    /**
     * 自动点击的频率， 默认200ms
     */
    private static final int DEFAULT_PERIOD = 200;

    private WindowManager mWindowManager;

    private View mView;

    private WindowManager.LayoutParams mParams;

    private int mRecordX;

    private int mRecordY;

    private FloatViewTouchListener mFloatViewTouchListener;

    /**
     * 用来记录mView在屏幕上的left和top位置， left = location[0], top = location[1]
     */
    private int[] location = new int[2];

    private ScheduledExecutorService mExecutorService;

    /**
     * float view是否被点击开启
     */
    private boolean isOn = false;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d(TAG, "onCreate: create the FloatViewClickService");
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        initView();
    }

    protected void initView() {
        if (mView == null) {
            if (getFloatViewLayoutId() != -1) {
                mView = LayoutInflater.from(this).inflate(getFloatViewLayoutId(),
                        null);
            } else {
                mView = LayoutInflater.from(this).inflate(R.layout.defalut_float_view,
                        null);
            }
        }
        if (getLayoutParams() != null) {
            mParams = getLayoutParams();
        } else {
            int viewType;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                viewType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                viewType = WindowManager.LayoutParams.TYPE_PHONE;
            }
            mParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT,
                    WindowManager.LayoutParams.WRAP_CONTENT, viewType,
                    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        }

        mWindowManager.addView(mView, mParams);
        mFloatViewTouchListener = new FloatViewTouchListener(mParams);
        mFloatViewTouchListener.setOnFloatViewOnDragListener(this);
        mFloatViewTouchListener.setOnFloatViewOnClickListener(this);
        mView.setOnTouchListener(mFloatViewTouchListener);
    }


    @Override
    public void onDrag(View view, WindowManager.LayoutParams params, MotionEvent event) {
        mParams = params;
        mWindowManager.updateViewLayout(mView, mParams);
    }

    @Override
    public void onClick(View view) {
        if (isOn) {
            stopAutoClick();
            isOn = false;
        } else {
            startAutoClick();
            isOn = true;
        }
        String text = isOn ? getResources().getString(R.string.on) :
                getResources().getString(R.string.off);
        ((TextView) mView.findViewById(R.id.tv_btn)).setText(text);
    }

    private final Runnable autoClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (mView != null) {
                mView.getLocationOnScreen(location);
                if (AutoClickService.getInstance() == null) {
                    LogUtil.d(TAG, "autoClickRunnable: AutoClickService unable");
                    PermissionUtil.setAccessibilitySettings(FloatViewClickService.this);
                } else {
                    //实际自动点击的位置在悬浮按钮的最右边位置
                    int x = location[0] + mView.getRight() + 10;
                    int y = location[1] + ((mView.getTop() + mView.getBottom()) / 2) + 10;
                    AutoClickService.getInstance().autoClick(x, y);
                }
            }
        }
    };

    private void startAutoClick() {
        if (mExecutorService == null) {
            ThreadFactory threadFactory = new BasicThreadFactory.Builder()
                    .namingPattern(THREAD_NAME_AUTO_CLICK).daemon(true).build();
            mExecutorService = new ScheduledThreadPoolExecutor(1, threadFactory);
        }
        int delay = INIT_DELAY;
        if (getInitDelay() >= 0) {
            delay = getInitDelay();
        }
        int period = DEFAULT_PERIOD;
        if (getPeriod() >= 0) {
            period = getPeriod();
        }
        mExecutorService.scheduleAtFixedRate(autoClickRunnable, delay, period,
                TimeUnit.MILLISECONDS);
    }

    private void stopAutoClick() {
        if (mExecutorService != null) {
            mExecutorService.shutdown();
            mExecutorService = null;
        }
    }

    /**
     * 需要自定义FloatView的时候，可以重写此方法
     * @return 自定义FloatView的layout id
     */
    protected int getFloatViewLayoutId() {
        return -1;
    }

    /**
     * 需要自定义FloatView的Layout Params的时候可以重写此方法
     * @return 自定义FloatView的Layout Params
     */
    protected WindowManager.LayoutParams getLayoutParams() {
        return null;
    }

    /**
     * 当要自定义点击开始延迟的时候，重写此方法
     * @return 自动点击启动延迟，单位ms
     */
    protected int getInitDelay() {
        return INIT_DELAY;
    }

    /**
     * 当要自定义点击频率时，重写此方法
     * @return 自动点击的频率， 单位ms
     */
    protected int getPeriod() {
        return DEFAULT_PERIOD;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MyServiceBinder();
    }

    public class MyServiceBinder extends Binder {
        public FloatViewClickService getService() {
            return FloatViewClickService.this;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        resizeView();
    }

    private void resizeView() {
        int x = mParams.x;
        int y = mParams.y;
        mParams.x = mRecordX;
        mParams.y = mRecordY;
        mRecordX = x;
        mRecordY = y;

        if (mView != null) {
            mWindowManager.updateViewLayout(mView, mParams);
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        stopAutoClick();
        mWindowManager.removeView(mView);
        LogUtil.d(TAG, "onDestroy: stop AutoClick, because task removed");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopAutoClick();
        mWindowManager.removeView(mView);
        LogUtil.d(TAG, "onDestroy: stop AutoClick, and destroy FloatViewClickService");
    }
}
