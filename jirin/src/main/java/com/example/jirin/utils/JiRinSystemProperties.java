package com.example.jirin.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @ClassName JiRinSystemProperties
 * @Author pengfan
 * @Date 2023/4/7
 * @Description 通过反射访问android.os.SystemProperties
 */
public class JiRinSystemProperties {

    /**
     * 获取属性值
     * @param key 属性key
     * @return 返回属性值, 获取失败返回null
     */
    public static String get(String key) {
        return get(key, null);
    }

    /**
     * 获取属性值
     * @param key 属性key
     * @param def 获取失败返回的默认值
     */
    public static String get(String key, String def) {
        try {
            Class className = Class.forName("android.os.SystemProperties");
            Method method = className.getDeclaredMethod("get",
                    new Class[] {String.class, String.class});
            return String.valueOf(method.invoke(className, new Object[] {key, def}));
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 设置属性
     * @param key 属性key
     * @param value 要设置的属性值
     */
    public static void set(String key, String value) {
        try {
            Class className = Class.forName("android.os.SystemProperties");
            Method method = className.getDeclaredMethod("set",
                    new Class[] {String.class, String.class});
            method.invoke(className, new Object[] {key, value});
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取boolean属性值
     * @param key 属性key
     * @param def 获取失败返回的默认值
     * @return 返回属性值
     */
    public static boolean getBoolean(String key, boolean def) {
        try {
            Class className = Class.forName("android.os.SystemProperties");
            Method method = className.getDeclaredMethod("getBoolean",
                    new Class[] {String.class, Boolean.class});
            return Boolean.valueOf((method.invoke(className, new Object[] {key, def})).toString());
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 获取int属性值
     * @param key 属性key
     * @param def 获取失败返回的默认值
     * @return 返回属性值
     */
    public static int getInt(String key, int def) {
        try {
            Class className = Class.forName("android.os.SystemProperties");
            Method method = className.getDeclaredMethod("getInt",
                    new Class[] {String.class, Integer.class});
            return Integer.valueOf(method.invoke(className, new Object[] {key, def}).toString());
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return def;
    }

    /**
     * 获取long属性值
     * @param key 属性key
     * @param def 获取失败返回的默认值
     * @return 返回属性值
     */
    public static long getLong(String key, long def) {
        try {
            Class className = Class.forName("android.os.SystemProperties");
            Method method = className.getDeclaredMethod("getLong",
                    new Class[] {String.class, Long.class});
            return Long.valueOf(method.invoke(className, new Object[] {key, def}).toString());
        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return def;
    }
}
