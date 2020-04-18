package com.kathline.demo.utils;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.StringRes;

/**
 * Toast相关工具类
 */
public class ToastUtils {

    private ToastUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static Toast sToast;

    /**
     * 短吐司
     *
     * @param context
     * @param msg
     */
    public static void showShortToast(Context context, String msg) {
        sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        sToast.show();
    }

    /**
     * 短吐司
     *
     * @param context
     * @param msg
     */
    public static void showShortToast(Context context, @StringRes int msg) {
        sToast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
        sToast.show();
    }

    /**
     * 长吐司
     *
     * @param context
     * @param msg
     */
    public static void showLongToast(Context context, String msg) {
        sToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        sToast.show();
    }

    /**
     * 长吐司
     *
     * @param context
     * @param msg
     */
    public static void showLongToast(Context context, @StringRes int msg) {
        sToast = Toast.makeText(context, msg, Toast.LENGTH_LONG);
        sToast.show();
    }

    /**
     * 取消吐司显示
     */
    public static void cancelToast() {
        if (sToast != null) {
            sToast.cancel();
            sToast = null;
        }
    }
}