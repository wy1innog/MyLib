package com.example.jirin.utils;

import android.content.Context;

/**
 * @ClassName CommonUtil
 * @Author pengfan
 * @Date 2023/5/4
 * @Description 通用的工具类
 */
public class CommonUtil {

    /**
     * 将dp数值转换为px
     * @param context 上下文对象
     * @param dp dp单位的数值
     * @return 返回dp对应的px值
     */
    public static int dp2px(Context context, float dp) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
