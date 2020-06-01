package com.kathline.picker.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.annotation.FloatRange;
import androidx.annotation.StyleRes;

import com.kathline.picker.utils.ScreenUtils;

/**
 * 弹窗基类
 *
 * @param <V> 弹窗的内容视图类型
 * @author matt
 * blog: addapp.cn
 */
public abstract class BaseDialog<V extends View> implements DialogInterface.OnKeyListener,
        DialogInterface.OnDismissListener {
    private static final String TAG = BaseDialog.class.getSimpleName();
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;
    protected Activity activity;
    protected int screenWidthPixels;
    protected int screenHeightPixels;
    private Dialog dialog;
    private int gravity = Gravity.CENTER;
    private @StyleRes int animRes;
    private float dimAmount = 0.5f;
    private DialogInterface.OnDismissListener onDismissListener;
    private DialogInterface.OnKeyListener onKeyListener;
    private int width = WRAP_CONTENT;
    private int height = WRAP_CONTENT;
    private ViewGroup decorView = null;
    private FrameLayout contentLayout;
    private boolean isPrepared = false;
    private boolean outCancel = false;

    public BaseDialog(Activity activity) {
        this.activity = activity;
        DisplayMetrics metrics = ScreenUtils.displayMetrics(activity);
        screenWidthPixels = metrics.widthPixels;
        screenHeightPixels = metrics.heightPixels;
    }

    private void initDialog() {
        contentLayout = new FrameLayout(activity);
        contentLayout.setLayoutParams(new ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
        contentLayout.setFocusable(true);
        contentLayout.setFocusableInTouchMode(true);
        //contentLayout.setFitsSystemWindows(true);
        dialog = new Dialog(activity);
        dialog.setCancelable(outCancel);//按返回键取消窗体
        dialog.setCanceledOnTouchOutside(outCancel);//触摸屏幕取消窗体
        dialog.setOnKeyListener(this);
        dialog.setOnDismissListener(this);
        Window window = dialog.getWindow();
        if (window != null) {
            //AndroidRuntimeException: requestFeature() must be called before adding content
            window.requestFeature(Window.FEATURE_NO_TITLE);
            window.setContentView(contentLayout);
            WindowManager.LayoutParams params = window.getAttributes();
            params.width = width;
            params.height = height;
            if (dimAmount < 0f || dimAmount > 1f) {
                throw new RuntimeException("透明度必须在0~1之间");
            } else {
                params.dimAmount = dimAmount;
            }
            window.setAttributes(params);
            window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            window.getDecorView().setPadding(0, 0, 0, 0);

            window.setGravity(gravity);
            if(animRes != 0) {
                window.setWindowAnimations(animRes);
            }
            if(onDismissListener != null) {
                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        BaseDialog.this.onDismiss(dialogInterface);
                        onDismissListener.onDismiss(dialogInterface);
                    }
                });
            }
            if(onKeyListener != null) {
                dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        BaseDialog.this.onKey(dialog, keyCode, event);
                        return onKeyListener.onKey(dialog, keyCode, event);
                    }
                });
            }
        }
    }

    public int getScreenWidthPixels() {
        return screenWidthPixels;
    }

    public int getScreenHeightPixels() {
        return screenHeightPixels;
    }

    public void setCanceledOnTouchOutside(boolean canceled){
        this.outCancel = canceled;
    }

    /**
     * 创建弹窗的内容视图
     *
     * @return the view
     */
    protected abstract V makeContentView();

    /**
     * 位于屏幕何处
     * 可以调整宽度和高度
     * @see Gravity
     */
    public void setGravity(int gravity) {
        this.gravity = gravity;
    }

    /**
     * 设置弹窗的内容视图之前执行
     */
    protected void setContentViewBefore() {
    }

    /**
     * 设置弹窗的内容视图之后执行
     *
     * @param contentView 弹窗的内容视图
     */
    protected void setContentViewAfter(V contentView) {
    }

    public void setContentView(View view) {
        contentLayout.removeAllViews();
        contentLayout.addView(view);
    }

    public void setDecorView(ViewGroup decorView) {
        this.decorView = decorView;
    }

    public void setAnimationStyle(@StyleRes int animRes) {
        this.animRes = animRes;
    }

    /**
     * 设置Dialog之外的背景透明度，0~1之间，默认值 0.5f，半透明，越小也透明
     *
     * @param dimAmount
     * @return
     */
    public void setDimAmount(@FloatRange(from = 0, to = 1.0) float dimAmount) {
        this.dimAmount = dimAmount;
    }

    public void setOnDismissListener(final DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
    }

    public void setOnKeyListener(final DialogInterface.OnKeyListener onKeyListener) {
        this.onKeyListener = onKeyListener;
    }

    /**
     * 设置弹窗的宽和高
     *
     * @param width  宽
     * @param height 高
     */
    public void setSize(int width, int height) {
        if (width == MATCH_PARENT) {
            //360奇酷等手机对话框MATCH_PARENT时两边还会有边距，故强制填充屏幕宽
            this.width = screenWidthPixels;
            this.height = height;
        }
        if (width == 0 && height == 0) {
            this.width = WRAP_CONTENT;
            this.height = WRAP_CONTENT;
        } else if (width == 0) {
            this.width = WRAP_CONTENT;
            this.height = height;
        } else if (height == 0) {
            this.width = width;
            this.height = WRAP_CONTENT;
        }else {
            this.width = width;
            this.height = height;
        }
    }

    /**
     * 设置弹窗的宽
     *
     * @param width 宽
     * @see #setSize(int, int)
     */
    public void setWidth(int width) {
        setSize(width, 0);
    }

    /**
     * 设置弹窗的高
     *
     * @param height 高
     * @see #setSize(int, int)
     */
    public void setHeight(int height) {
        setSize(0, height);
    }

    /**
     * 设置是否需要重新初始化视图，可用于数据刷新
     */
    public void setPrepared(boolean prepared) {
        isPrepared = prepared;
    }

    public boolean isShowing() {
        if(dialog == null) {
            Log.e(TAG, "current mode is not dialog");
            return false;
        }
        return dialog.isShowing();
    }

    public final void show() {
        if(decorView == null) {
            if (isPrepared) {
                dialog.show();
                showAfter();
                return;
            }
            initDialog();
            setContentViewBefore();
            V view = makeContentView();
            setContentView(view);// 设置弹出窗体的布局
            setContentViewAfter(view);
            isPrepared = true;
            dialog.show();
            showAfter();
        }else {
            if(isPrepared) {
                showAfter();
                return;
            }
            setContentViewBefore();
            V view = makeContentView();
            decorView.removeAllViews();
            decorView.addView(view);
            setContentViewAfter(view);
            isPrepared = true;
            showAfter();
        }
    }

    protected void showAfter() {
    }

    public void dismiss() {
        dismissImmediately();
    }

    protected final void dismissImmediately() {
        if(dialog == null) {
            Log.e(TAG, "current mode is not dialog");
            return;
        }
        dialog.dismiss();
    }

    public boolean onBackPress() {
        dismiss();
        return false;
    }

    @Override
    public final boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPress();
        }
        return false;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        dismiss();
    }

    public Context getContext() {
        if(decorView != null) {
            return decorView.getContext();
        }
        return dialog.getContext();
    }

    public Window getWindow() {
        if(decorView != null) {
            return null;
        }
        return dialog.getWindow();
    }

    /**
     * 弹框的内容视图
     */
    public View getContentView() {
        // IllegalStateException: The specified child already has a parent.
        // You must call removeView() on the child's parent first.
        return contentLayout.getChildAt(0);
    }

    /**
     * 弹框的根视图
     */
    public ViewGroup getRootView() {
        return contentLayout;
    }

}
