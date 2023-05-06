package com.example.jirin.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * @author PengFan
 */
public class ToastUtil {
    private static Toast mToast;
    public static void showShortToast(Context context, String msg) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showLongToast(Context context, String msg) {
        if (mToast == null){
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }

    public static void showShortToast(Context context, int msgId) {
        if (mToast == null) {
            mToast = Toast.makeText(context, msgId, Toast.LENGTH_SHORT);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(context, msgId, Toast.LENGTH_SHORT);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    public static void showLongToast(Context context, int msgId) {
        if (mToast == null){
            mToast = Toast.makeText(context, msgId, Toast.LENGTH_LONG);
        } else {
            mToast.cancel();
            mToast = Toast.makeText(context, msgId, Toast.LENGTH_LONG);
            mToast.setDuration(Toast.LENGTH_LONG);
        }
        mToast.show();
    }
}
