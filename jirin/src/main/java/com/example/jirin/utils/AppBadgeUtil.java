package com.example.jirin.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.core.app.NotificationCompat;

import com.example.jirin.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName AppBadgeUtil
 * @Author pengfan
 * @Date 2023/4/6
 * @Description 用于设置应用提示角标类
 */
public class AppBadgeUtil {
    private static final String TAG = "AppBadgeUtil";

    private static final String DEVICE_UNKNOWN = "unknown";
    private static final String DEVICE_HUAWEI = "huawei";
    private static final String DEVICE_HONER = "honer";
    private static final String DEVICE_XIAOMI = "xiaomi";
    private static final String DEVICE_OPPO = "oppo";
    private static final String DEVICE_VIVO = "vivo";
    private static final String DEVICE_SAMSUNG = "samsung";

    public static final String CHANNEL_ID_BADGE = "id_badge";
    public static final String CHANNEL_NAME_BADGE = "badge";
    public static final int BADGE_NOTIFICATION_ID = 1023;

    private static final String RO_MIUI_UI_VERSION_NAME = "ro.miui.ui.version.name";
    private static final int MIUI_12 = 12;

    /**
     * 设置应用角标
     * @param context 上下文对象
     * @param count 角标数量，当角标数量大于99时，都显示99
     */
    public static void setBadgeCount(Context context, int count) {
        if (count <= 0) {
            count = 0;
        } else {
            count = Math.min(count, 99);
        }
        if (isHuawei() || isHonor()) {
            setHuaweiBadge(context, count);
        } else if (isXiaomi()) {
            setXiaomiBadge(context, count);
        } else if (isOPPO()) {
            setOppoBadge(context, count);
        } else if (isVivo()) {
            setVivoBadge(context, count);
        } else if (isSamsung()) {
            setSamsungBadge(context, count);
        } else {
            LogUtil.i(TAG, "setBadgeCount: device " + DEVICE_UNKNOWN);
        }
    }

    /**
     * 通过notification的方式设置小米/oppo手机的角标
     * @param context 上下文对象，需要是能转换为activity的实例
     * @param id notification id
     * @param notification 设置角标的notification，如何创建此notification
     *                     可以参考本类中的createBadgeNotification(Context context, int count)方法
     */
    public static void sendBadgeNotification(Context context, int id, Notification notification) {
        NotificationManager notificationManager = getBadgeNotificationManager(context);
        if (notificationManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(CHANNEL_ID_BADGE,
                        CHANNEL_NAME_BADGE, NotificationManager.IMPORTANCE_DEFAULT);
                channel.setShowBadge(true);
                channel.enableLights(false);
                channel.enableVibration(false);
                channel.setSound(null, null);
                notificationManager.createNotificationChannel(channel);
            }
            notificationManager.notify(id, notification);
        }
    }

    /**
     * 华为/荣耀手机的角标设置
     * @param context 上下文对象
     * @param count 角标数量
     */
    private static void setHuaweiBadge(Context context, int count) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("package", context.getPackageName());
            String launchClassName = context.getPackageManager()
                    .getLaunchIntentForPackage(context.getPackageName())
                    .getComponent().getClassName();
            bundle.putString("class", launchClassName);
            bundle.putInt("badgenumber", count);
            String uriString = "content://com.huawei.android.launcher.settings/badge/";
            context.getContentResolver().call(Uri.parse(uriString), "change_badge",
                    null, bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 小米手机的角标设置
     * @param context 上下文对象
     * @param count 角标数量
     */
    private static void setXiaomiBadge(Context context, int count) {
        NotificationManager notificationManager = getBadgeNotificationManager(context);
        Notification notification = createXiaomiBadgeNotification(context, count);
        if (notificationManager != null && notification != null) {
            notificationManager.cancel(BADGE_NOTIFICATION_ID);
            notificationManager.notify(BADGE_NOTIFICATION_ID, notification);
        }
    }

    private static NotificationManager getBadgeNotificationManager(Context context) {
        NotificationManager notificationManager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            LogUtil.e(TAG, "sendBadgeNotification: notificationManager is null");
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID_BADGE,
                    CHANNEL_NAME_BADGE, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setShowBadge(true);
            channel.enableLights(false);
            channel.enableVibration(false);
            channel.setSound(null, null);
            notificationManager.createNotificationChannel(channel);
        }
        return notificationManager;
    }

    private static Notification createXiaomiBadgeNotification(Context context, int count) {
        Notification notification = createBadgeNotification(context, count);

        if (notification != null) {
            //如果MIUI版本低于12的话，需要反射进行处理
            String miuiVersionName = JiRinSystemProperties.get(RO_MIUI_UI_VERSION_NAME);
            LogUtil.d(TAG, "createXiaomiBadgeNotification: miuiVersionName = "
                    + miuiVersionName);

            //如果未获取到MIUI的版本号，按照MIUI版本号大于12的逻辑处理
            if (!TextUtils.isEmpty(miuiVersionName)) {
                int miuiVersionCode = MIUI_12;
                try {
                    miuiVersionCode = Integer
                            .parseInt(miuiVersionName.trim().replace("V", ""));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (miuiVersionCode < MIUI_12) {
                    try {
                        Field field = notification.getClass()
                                .getDeclaredField("extraNotification");
                        Object extraNotification = field.get(notification);
                        Method method = extraNotification.getClass()
                                .getDeclaredMethod("setMessageCount", Integer.class);
                        method.invoke(extraNotification, count);
                    } catch (NoSuchFieldException | NoSuchMethodException
                            | IllegalAccessException | InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return notification;
    }

    /**
     * 生成设置应用角标数的Notification
     * @param context 上下文对象，需要是能转换为activity的实例
     * @param count 角标数量
     * @return 返回配置好的notification
     */
    private static Notification createBadgeNotification(Context context, int count) {
        if (!(context instanceof Activity)) {
            LogUtil.e(TAG, "createBadgeNotification: context not instanceof Activity, " +
                    "can't set the badge");
            return null;
        }
        Activity activity = (Activity) context;
        Intent intent = new Intent(context, activity.getClass());
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        String content;
        if (count == 1) {
            content = String.format(context.getString(R.string.badge_notification_content), count);
        } else if (count <= 0) {
            content = context.getString(R.string.badge_notification_empty);
        } else  {
            content = String.format(context.getString(R.string.badge_notification_contents), count);
        }
        Notification notification;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notification = new NotificationCompat.Builder(context, CHANNEL_ID_BADGE)
                    .setContentTitle(context.getString(R.string.badge_notification_title))
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(CHANNEL_ID_BADGE)
                    .setNumber(count)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .build();
        } else {
            notification = new NotificationCompat.Builder(context)
                    .setContentTitle(context.getString(R.string.badge_notification_title))
                    .setContentText(content)
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setChannelId(CHANNEL_ID_BADGE)
                    .setNumber(count)
                    .setBadgeIconType(NotificationCompat.BADGE_ICON_SMALL)
                    .build();
        }
        return notification;
    }

    /**
     * Oppo手机的角标设置
     * @param context 上下文对象
     * @param count 角标数量
     */
    private static void setOppoBadge(Context context, int count) {
        NotificationManager notificationManager = getBadgeNotificationManager(context);
        Notification notification = createBadgeNotification(context, count);
        if (notificationManager != null && notification != null) {
            notificationManager.cancel(BADGE_NOTIFICATION_ID);
            notificationManager.notify(BADGE_NOTIFICATION_ID, notification);
        }
    }

    /**
     * Vivo手机的应用角标设置
     * @param context 上下文对象
     * @param count 角标数量
     */
    private static void setVivoBadge(Context context, int count) {
        try {
            Intent intent = new Intent();
            intent.setAction("launcher.action.CHANGE_APPLICATION_NOTIFICATION_NUM");
            intent.putExtra("packageName", context.getPackageName());
            String launchClassName = context.getPackageManager()
                    .getLaunchIntentForPackage(context.getPackageName())
                    .getComponent().getClassName();
            intent.putExtra("className", launchClassName);
            intent.putExtra("notificationNum", count);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Samsung手机的应用角标设置
     * @param context 上下文对象
     * @param count 角标数量
     */
    private static void setSamsungBadge(Context context, int count) {
        try {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.BADGE_COUNT_UPDATE");
            intent.putExtra("badge_count_package_name", context.getPackageName());
            String launchClassName = context.getPackageManager()
                    .getLaunchIntentForPackage(context.getPackageName())
                    .getComponent().getClassName();
            intent.putExtra("badge_count_class_name", launchClassName);
            intent.putExtra("badge_count", count);
            context.sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取设备制造上名称
     * @return
     */
    private static String getManufacturer() {
        String manufacturer = DEVICE_UNKNOWN;
        try {
            manufacturer = Build.MANUFACTURER;
            if (!TextUtils.isEmpty(manufacturer)) {
                manufacturer = manufacturer.toLowerCase();
            }
            LogUtil.d(TAG, "getManufacturer: manufacturer = " + manufacturer);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return manufacturer;
    }

    private static boolean isHuawei() {
        return DEVICE_HUAWEI.equals(getManufacturer());
    }

    private static boolean isHonor() {
        return DEVICE_HONER.equals(getManufacturer());
    }

    private static boolean isXiaomi() {
        return DEVICE_XIAOMI.equals(getManufacturer());
    }

    private static boolean isOPPO() {
        return DEVICE_OPPO.equals(getManufacturer());
    }

    private static boolean isVivo() {
        return DEVICE_VIVO.equals(getManufacturer());
    }

    private static boolean isSamsung() {
        return DEVICE_SAMSUNG.equals(getManufacturer());
    }
}
