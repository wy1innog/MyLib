package com.example.jirin.activity.base;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jirin.utils.LogUtil;

import java.lang.ref.WeakReference;

public abstract class BaseActivity extends Activity {
    protected final String TAG = this.getClass().getSimpleName();

    private BaseHandler mHandler;

    /**
     * 用于判断是否连续双击，用于防用户连续点击
     */
    private long lastClickTime = 0;

    /**
     * 用于判断是否是同一个控件被连续双击， 一般使用view的id作为标识
     */
    private String lastClickTag = "";

    /**
     * 连击间隔时间，单位毫秒
     */
    public static final long LIMIT_TIME_BETWEEN_CLICKS_SHORT = 300;
    public static final long LIMIT_TIME_BETWEEN_CLICKS = 500;
    public static final long LIMIT_TIME_BETWEEN_CLICKS_LONG = 1000;

    /**
     * 发送消息时，正常更新消息
     */
    public static final int MESSAGE_FLAG_UPDATE = 0;
    /**
     * 发送消息时，取消上一条消息
     */
    public static final int MESSAGE_FLAG_CANCEL = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initHandler();
    }

    private void initHandler() {
        if (mHandler == null) {
            mHandler = new BaseHandler(this);
        }
    }

    protected void sendMessage(Message message) {
        sendMessage(message, MESSAGE_FLAG_UPDATE);
    }

    /**
     * 发送消息
     * @param message 消息
     * @param flag MESSAGE_FLAG_CANCEL-表示发送消息之前，移除之前的消息
     *             MESSAGE_FLAG_UPDATE-表示正常发送消息
     */
    protected void sendMessage(Message message, int flag) {
        if (mHandler != null) {
            if (flag == MESSAGE_FLAG_CANCEL && message != null) {
                mHandler.removeMessages(message.what);
            }
            mHandler.sendMessage(message);
        } else {
            LogUtil.d(TAG, "sendMessage: not initHandler, mHandler is null");
        }
    }

    protected void sendMessageDelay(Message message, long delayMillis) {
        sendMessageDelay(message, delayMillis, MESSAGE_FLAG_UPDATE);
    }

    /**
     * 延迟发送消息
     * @param message 消息
     * @param delayMillis 延迟多长时间再发送消息
     * @param flag MESSAGE_FLAG_CANCEL-表示发送消息之前，移除之前的消息
     *             MESSAGE_FLAG_UPDATE-表示正常发送消息
     */
    protected void sendMessageDelay(Message message, long delayMillis, int flag) {
        if (mHandler != null) {
            if (flag == MESSAGE_FLAG_CANCEL && message != null) {
                mHandler.removeMessages(message.what);
            }
            mHandler.sendMessageDelayed(message, delayMillis);
        } else {
            LogUtil.d(TAG, "sendMessageDelay: not initHandler, mHandler is null");
        }
    }

    protected void sendMessageAtTime(Message message, long uptimeMillis) {
        sendMessageAtTime(message, uptimeMillis, MESSAGE_FLAG_UPDATE);
    }

    /**
     * 周期性发送消息
     * @param message 消息
     * @param uptimeMillis 周期
     * @param flag MESSAGE_FLAG_CANCEL-表示发送消息之前，移除之前的消息
     *             MESSAGE_FLAG_UPDATE-表示正常发送消息
     */
    protected void sendMessageAtTime(Message message, long uptimeMillis, int flag) {
        if (mHandler != null) {
            if (flag == MESSAGE_FLAG_CANCEL && message != null) {
                mHandler.removeMessages(message.what);
            }
            mHandler.sendMessageAtTime(message, uptimeMillis);
        } else {
            LogUtil.d(TAG, "sendMessageAtTime: not initHandler, mHandler is null");
        }
    }

    protected void sendEmptyMessage(int msgId) {
        sendEmptyMessage(msgId, MESSAGE_FLAG_UPDATE);
    }

    /**
     * 发送空消息
     * @param msgId 消息Id
     * @param flag MESSAGE_FLAG_CANCEL-表示发送消息之前，移除之前的消息
     *             MESSAGE_FLAG_UPDATE-表示正常发送消息
     */
    protected void sendEmptyMessage(int msgId, int flag) {
        if (mHandler != null) {
            if (flag == MESSAGE_FLAG_CANCEL) {
                mHandler.removeMessages(msgId);
            }
            mHandler.sendEmptyMessage(msgId);
        } else {
            LogUtil.d(TAG, "sendEmptyMessage: not initHandler, mHandler is null");
        }
    }

    protected void sendEmptyMessageDelay(int msgId, long delayMillis) {
        sendEmptyMessageDelay(msgId, delayMillis, MESSAGE_FLAG_UPDATE);
    }

    /**
     * 延迟发送空消息
     * @param msgId 消息Id
     * @param delayMillis 延迟多长时间再发送消息
     * @param flag MESSAGE_FLAG_CANCEL-表示发送消息之前，移除之前的消息
     *             MESSAGE_FLAG_UPDATE-表示正常发送消息
     */
    protected void sendEmptyMessageDelay(int msgId, long delayMillis, int flag) {
        if (mHandler != null) {
            if (flag == MESSAGE_FLAG_CANCEL) {
                mHandler.removeMessages(msgId);
            }
            mHandler.sendEmptyMessageDelayed(msgId, delayMillis);
        } else {
            LogUtil.d(TAG, "sendEmptyMessageDelay: not initHandler, mHandler is null");
        }
    }

    protected void sendEmptyMessageAtTime(int msgId, long uptimeMillis) {
        sendEmptyMessageAtTime(msgId, uptimeMillis, MESSAGE_FLAG_CANCEL);
    }

    /**
     * 周期性发送空消息
     * @param msgId 消息Id
     * @param uptimeMillis 周期
     * @param flag MESSAGE_FLAG_CANCEL-表示发送消息之前，移除之前的消息
     *             MESSAGE_FLAG_UPDATE-表示正常发送消息
     */
    protected void sendEmptyMessageAtTime(int msgId, long uptimeMillis, int flag) {
        if (mHandler != null) {
            if (flag == MESSAGE_FLAG_CANCEL) {
                mHandler.removeMessages(msgId);
            }
            mHandler.sendEmptyMessageAtTime(msgId, uptimeMillis);
        } else {
            LogUtil.d(TAG, "sendEmptyMessageAtTime: not initHandler, mHandler is null");
        }
    }

    /**
     * 移除消息
     * @param msgId 要移除的消息Id
     */
    protected void removeMessage(int msgId) {
        if (mHandler != null) {
            mHandler.removeMessages(msgId);
        } else {
            LogUtil.d(TAG, "removeMessage: not initHandler, mHandler is null");
        }
    }

    protected void postRunnable(Runnable runnable) {
        postRunnable(runnable, MESSAGE_FLAG_UPDATE);
    }

    /**
     * 执行任务Runnable
     * @param runnable 任务
     * @param flag MESSAGE_FLAG_CANCEL-表示发送消息之前，移除之前的消息
     *             MESSAGE_FLAG_UPDATE-表示正常发送消息
     */
    protected void postRunnable(Runnable runnable, int flag) {
        if (mHandler != null) {
            if (flag == MESSAGE_FLAG_CANCEL) {
                mHandler.removeCallbacks(runnable);
            }
            mHandler.post(runnable);
        } else {
            LogUtil.d(TAG, "postRunnable: not initHandler, mHandler is null");
        }
    }

    protected void postRunnableDelay(Runnable runnable, long delayMillis) {
        postRunnableDelay(runnable, delayMillis, MESSAGE_FLAG_UPDATE);
    }

    /**
     * 延迟执行任务Runnable
     * @param runnable 任务
     * @param delayMillis 延迟多长时间再发送消息
     * @param flag MESSAGE_FLAG_CANCEL-表示发送消息之前，移除之前的消息
     *             MESSAGE_FLAG_UPDATE-表示正常发送消息
     */
    protected void postRunnableDelay(Runnable runnable, long delayMillis, int flag) {
        if (mHandler != null) {
            if (flag == MESSAGE_FLAG_CANCEL) {
                mHandler.removeCallbacks(runnable);
            }
            mHandler.postDelayed(runnable, delayMillis);
        } else {
            LogUtil.d(TAG, "postRunnableDelay: not initHandler, mHandler is null");
        }
    }

    protected void postRunnableAtTime(Runnable runnable, long uptimeMillis) {
        postRunnableAtTime(runnable, uptimeMillis, MESSAGE_FLAG_UPDATE);
    }

    /**
     * 周期性执行任务Runnable
     * @param runnable 任务
     * @param uptimeMillis 周期
     * @param flag MESSAGE_FLAG_CANCEL-表示发送消息之前，移除之前的消息
     *             MESSAGE_FLAG_UPDATE-表示正常发送消息
     */
    protected void postRunnableAtTime(Runnable runnable, long uptimeMillis, int flag) {
        if (mHandler != null) {
            if (flag == MESSAGE_FLAG_CANCEL) {
                mHandler.removeCallbacks(runnable);
            }
            mHandler.postAtTime(runnable, uptimeMillis);
        } else {
            LogUtil.d(TAG, "postRunnableAtTime: not initHandler, mHandler is null");
        }
    }

    /**
     * 移除任务Runnable
     * @param runnable 之前发送执行的任务
     */
    protected void removeRunnable(Runnable runnable) {
        if (mHandler != null) {
            mHandler.removeCallbacks(runnable);
        } else {
            LogUtil.d(TAG, "removeRunnable: not initHandler, mHandler is null");
        }
    }

    /**
     * 是否连续双击， 默认连击间隔时间500ms
     * @return ture-连续双击，false-非连续双击
     */
    protected boolean isDoubleClick() {
        return isDoubleClick(LIMIT_TIME_BETWEEN_CLICKS);
    }

    /**
     * 是否连续双击
     * @param limitTime 判定是否连击的间隔时间
     * @return ture-连续双击，false-非连续双击
     */
    protected boolean isDoubleClick(long limitTime) {
        long now = System.currentTimeMillis();
        if (now - lastClickTime > limitTime) {
            lastClickTime = now;
            return false;
        } else {
            lastClickTime = now;
            return true;
        }
    }

    /**
     * 是否同一个控件连续双击， 默认连击间隔时间500ms
     * @param tag 控件的tag, 此处可以使用控件的id做为tag
     * @return ture-连续双击，false-非连续双击
     */
    protected boolean isDoubleClick(String tag) {
        return isDoubleClick(tag, LIMIT_TIME_BETWEEN_CLICKS);
    }

    /**
     * 是否同一个控件连续双击
     * @param tag 控件的tag, 此处可以使用控件的id做为tag
     * @param limitTime 判定是否连击的间隔时间
     * @return ture-连续双击，false-非连续双击
     */
    protected boolean isDoubleClick(String tag, long limitTime) {
        long now = System.currentTimeMillis();
        if (!lastClickTag.equals(tag)) {
            lastClickTag = tag;
            lastClickTime = now;
            return false;
        } else {
            lastClickTag = tag;
            if (now - lastClickTime > limitTime) {
                lastClickTime = now;
                return false;
            } else {
                lastClickTime = now;
                return true;
            }
        }
    }

    /**
     * 如果需要使用mHandler的功能，Message的回调在此函数中执行
     * @param activity 当前activity
     * @param message 消息对象
     */
    protected void handleMessage(Activity activity, Message message) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

    private static class BaseHandler extends Handler {
        private static final String TAG = "MyHandler";
        private WeakReference<Activity> activityWeakReference;

        public BaseHandler(Activity activity) {
            activityWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (activityWeakReference != null) {
                Activity activity = activityWeakReference.get();
                if (activity != null) {
                    if (activity instanceof BaseActivity) {
                        ((BaseActivity) activity).handleMessage(activity, msg);
                    } else {
                        LogUtil.e(TAG, "handleMessage: activity isn't BaseActivity instance");
                    }
                } else {
                    LogUtil.e(TAG, "handleMessage: activity is null");
                }
            } else {
                LogUtil.e(TAG, "handleMessage: activityWeakReference is null");
            }
        }
    }
}
