package com.example.jirin.utils;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

/**
 * @ClassName PermissionUtil
 * @Author pengfan
 * @Date 2023/5/6
 * @Description 权限或一些功能的检测工具类
 */
public class PermissionUtil {
    private static final String AUTO_CLICK_SERVICE_ID
            = "com.example.jirin.service.AutoClickService";

    /**
     * 检查AutoClickService是否已经开启
     * @param context 上下文对象
     * @param packageName 使用此lib库的包名
     * @return 如果已经开启，返回true；如果还没有开启，返回false。
     */
    public static boolean checkAutoClickServiceEnable(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        String serviceId = packageName + "/" + AUTO_CLICK_SERVICE_ID;
        return checkAccessibilityServiceEnable(context, serviceId);
    }

    /**
     * 检查无障碍服务是否已开启
     * @param context 上下文对象
     * @param serviceId 无障碍服务id
     * @return 如果无障碍服务已开启，返回true；如果无障碍服务未开启。返回false
     */
    public static boolean checkAccessibilityServiceEnable(Context context, String serviceId) {
        if (TextUtils.isEmpty(serviceId)) {
            return false;
        }
        AccessibilityManager manager =
                (AccessibilityManager) context.getSystemService(Context.ACCESSIBILITY_SERVICE);
        List<AccessibilityServiceInfo> list = manager
                .getEnabledAccessibilityServiceList(AccessibilityServiceInfo.FEEDBACK_GENERIC);
        for (AccessibilityServiceInfo info : list) {
            if (serviceId.equals(info.getId())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 跳转到无障碍服务设置页面
     * @param context 上下文对象
     */
    public static void setAccessibilitySettings(Context context) {
        context.startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }
}
