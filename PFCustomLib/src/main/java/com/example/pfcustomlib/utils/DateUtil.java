package com.example.pfcustomlib.utils;

import android.content.Context;
import android.provider.Settings;
import android.text.TextUtils;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtil {
    private static final String TAG = "DateUtil";

    /**
     * 时间格式12小时制和24小时制
     */
    public static final String TIME_FORMAT_12 = "12";
    public static final String TIME_FORMAT_24 = "24";

    /**
     * 日期格式：年-月-日 时：分：秒
     */
    public static final String DATA_FORMAT_HOUR_MIN_SEC_24 = "yyyy-MM-dd HH:mm:ss";
    public static final String DATA_FORMAT_HOUR_MIN_SEC_12 = "yyyy-MM-dd hh:mm:ss";
    /**
     * 12小时制并且带PM或AM单位
     */
    public static final String DATA_FORMAT_HOUR_MIN_SEC_12_UNIT = "yyyy-MM-dd aa hh:mm:ss";

    /**
     * 日期格式：年-月-日 时：分
     */
    public static final String DATA_FORMAT_HOUR_MIN_24 = "yyyy-MM-dd HH:mm";
    public static final String DATA_FORMAT_HOUR_MIN_12 = "yyyy-MM-dd hh:mm";
    /**
     * 12小时制并且带PM或AM单位
     */
    public static final String DATA_FORMAT_HOUR_MIN_12_UNIT = "yyyy-MM-dd aa hh:mm";

    /**
     * 日期格式：年-月-日 时：分：秒.毫秒
     */
    public static final String DATA_FORMAT_HOUR_MIN_SEC_S_24 = "yyyy-MM-dd HH:mm:ss.SSS";
    public static final String DATA_FORMAT_HOUR_MIN_SEC_S_12 = "yyyy-MM-dd hh:mm:ss.SSS";
    /**
     * 12小时制并且带PM或AM单位
     */
    public static final String DATA_FORMAT_HOUR_MIN_SEC_S_12_UNIT = "yyyy-MM-dd aa hh:mm:ss.SSS";

    /**
     * 判断当前系统时间格式
     * @param context 上下文对象
     * @return true-当前系统是24小时制， false-当前系统是12小时制
     */
    public static boolean is24HourTimeFormat(Context context) {
        String timeFormat = Settings.System.getString(context.getContentResolver(),
                Settings.System.TIME_12_24);
        return TIME_FORMAT_24.equals(timeFormat);
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static String getCurrentData(Context context) {
        return getCurrentDate(context, false);
    }

    /**
     * 获取当前时间，并且转换为日期格式返回
     * @param context 上下文对象
     * @param hasUnit 如果系统为12小时制的时候，是否显示AM/PM单位
     * @return 返回当前日期格式
     */
    public static String getCurrentDate(Context context, boolean hasUnit) {
        if (is24HourTimeFormat(context)) {
            return getCurrentDataByFormat(DATA_FORMAT_HOUR_MIN_SEC_24);
        } else {
            if (hasUnit) {
                return getCurrentDataByFormat(DATA_FORMAT_HOUR_MIN_SEC_12_UNIT);
            } else {
                return getCurrentDataByFormat(DATA_FORMAT_HOUR_MIN_SEC_12);
            }
        }
    }

    public static String getCurrentDataByFormat(String format) {
        return getDataByFormat(format, getCurrentTime());
    }

    /**
     * 将时间转换为指定格式的字符串
     * @param format 时间显示格式
     * @param date 时间
     * @return 指定格式时间字符串
     */
    public static String getDataByFormat(String format, long date) {
        if (TextUtils.isEmpty(format)) {
            LogUtil.e(TAG, "getCurrentDataByFormat: format is null");
            return null;
        }

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
            return dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtil.e(TAG, "getCurrentDataByFormat: format is illegal");
        }
        return null;
    }

    public static String changeDataTo_HOUR_MIN_SEC(Context context, long date) {
        return changeDataTo_HOUR_MIN_SEC(context, date, false);
    }

    /**
     * 将时间转换为年-月-日 时：分：秒格式
     * @param context 上下文对象
     * @param date 时间
     * @param hasUnit 系统时间为12小时制时是否带PM或AM单位， ture-带单位， false-不带单位
     * @return 时间字符串
     */
    public static String changeDataTo_HOUR_MIN_SEC(Context context, long date, boolean hasUnit) {
        if (is24HourTimeFormat(context)) {
            return getDataByFormat(DATA_FORMAT_HOUR_MIN_SEC_24, date);
        } else {
            if (hasUnit) {
                return getDataByFormat(DATA_FORMAT_HOUR_MIN_SEC_12_UNIT, date);
            } else {
                return getDataByFormat(DATA_FORMAT_HOUR_MIN_SEC_12, date);
            }
        }
    }

    public static String changeDataTo_HOUR_MIN(Context context, long date) {
        return changeDataTo_HOUR_MIN(context, date, false);
    }

    /**
     * 将时间转换为年-月-日 时：分格式
     * @param context 上下文对象
     * @param date 时间
     * @param hasUnit 系统时间为12小时制时是否带PM或AM单位， ture-带单位， false-不带单位
     * @return 时间字符串
     */
    public static String changeDataTo_HOUR_MIN(Context context, long date, boolean hasUnit) {
        if (is24HourTimeFormat(context)) {
            return getDataByFormat(DATA_FORMAT_HOUR_MIN_24, date);
        } else {
            if (hasUnit) {
                return getDataByFormat(DATA_FORMAT_HOUR_MIN_12_UNIT, date);
            } else {
                return getDataByFormat(DATA_FORMAT_HOUR_MIN_12, date);
            }
        }
    }

    public static String changeDataTo_HOUR_MIN_SEC_S(Context context, long date) {
        return changeDataTo_HOUR_MIN_SEC_S(context, date, false);
    }

    /**
     * 将时间转换为年-月-日 时：分：秒.毫秒格式
     * @param context 上下文对象
     * @param date 时间
     * @param hasUnit 系统时间为12小时制时是否带PM或AM单位， ture-带单位， false-不带单位
     * @return 时间字符串
     */
    public static String changeDataTo_HOUR_MIN_SEC_S(Context context, long date, boolean hasUnit) {
        if (is24HourTimeFormat(context)) {
            return getDataByFormat(DATA_FORMAT_HOUR_MIN_SEC_S_24, date);
        } else {
            if (hasUnit) {
                return getDataByFormat(DATA_FORMAT_HOUR_MIN_SEC_S_12_UNIT, date);
            } else {
                return getDataByFormat(DATA_FORMAT_HOUR_MIN_SEC_S_12, date);
            }
        }
    }

}
