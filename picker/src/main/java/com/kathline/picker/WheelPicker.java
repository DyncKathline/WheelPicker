package com.kathline.picker;

import android.app.Activity;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.FloatRange;
import androidx.annotation.IntRange;

import com.kathline.picker.base.ConfirmDialog;
import com.kathline.picker.widget.WheelView;

/**
 * 滑轮选择器
 * @author matt
 * blog: addapp.cn
 */
public abstract class WheelPicker extends ConfirmDialog<View> {
    public static final int TEXT_SIZE = 16;//sp
    public static final float TEXT_ALPHA = 0.8f;
    public static final int TEXT_COLOR_FOCUS = 0XFF0288CE;
    public static final int TEXT_COLOR_NORMAL = 0XFFBBBBBB;
    public static final int ITEM_OFF_SET = 2;
    protected int textSize = TEXT_SIZE;
    protected int textColorNormal = TEXT_COLOR_NORMAL;
    protected int textColorFocus = TEXT_COLOR_FOCUS;
    protected int offset = ITEM_OFF_SET;
    //是否是自己布局添加的label   控制分割线是否连接到一起 成一条直线   false的时候 最好分割线是填充 LineConfig.DividerType.WRAP
    protected boolean outerLabelEnable = true;
    protected boolean canLoop = true;//是否循环
    protected boolean onlyCenterLabel = false;//只有中间才显示label
    protected boolean weightEnable = false;//启用权重
    protected boolean canLinkage = false;//是否联动
    protected float lineSpacing = 0;
    //是否开启音频效果
    protected boolean isSoundEffect = false;
    protected @FloatRange(from = 0.0, to = 1.0) float playVolume = 1.0f;
    //可见的item条数
    protected int mVisibleItems = 3;
    protected boolean isShowDivider;
    @WheelView.DividerType int dividerType;
    private View contentView;

    public WheelPicker(Activity activity) {
        super(activity);
    }
    public boolean isOuterLabelEnable() {
        return outerLabelEnable;
    }

    public void setOuterLabelEnable(boolean outerLabelEnable) {
        this.outerLabelEnable = outerLabelEnable;
    }
    /**
     * 设置文字大小
     */
    public void setTextSize(int textSize) {
        this.textSize = textSize;
    }

    /**
     * 设置未选中文字颜色
     */
    public void setUnSelectedTextColor(@ColorInt int unSelectedTextColor) {
        this.textColorNormal = unSelectedTextColor;
    }
    /**
     * 设置选中文字颜色
     */
    public void setSelectedTextColor(@ColorInt int selectedTextColor) {
        this.textColorFocus = selectedTextColor;
    }

    public boolean isOnlyCenterLabel() {
        return onlyCenterLabel;
    }

    public void setOnlyCenterLabel(boolean onlyCenterLabel) {
        this.onlyCenterLabel = onlyCenterLabel;
    }

    /**
     * 设置是否自动联动
     * */
    public void setCanLinkage(boolean canLinkage) {
        this.canLinkage = canLinkage;
    }

    /**
     * 设置选项偏移量，可用来要设置显示的条目数，范围为1-3。
     * 1显示3条、2显示5条、3显示7条
     */
    public void setOffset(@IntRange(from = 1, to = 3) int offset) {
        this.offset = offset;
    }

    /**
     * 设置是否禁用循环
     * true 循环 false 不循环
     */
    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }

    /**
     *
     * 线性布局设置是否启用权重
     * true 启用 false 自适应width
     */
    public void setWeightEnable(boolean weightEnable) {
        this.weightEnable = weightEnable;
    }

    /**
     * 设置item间距
     *
     * @param lineSpacing 行间距值
     */
    public void setLineSpacing(float lineSpacing) {
        this.lineSpacing = lineSpacing;
    }

    /**
     * 设置音效开关
     *
     * @param isSoundEffect 是否开启滚动音效
     */
    public void setSoundEffect(boolean isSoundEffect) {
        this.isSoundEffect = isSoundEffect;
    }
    /**
     * 设置播放音量
     *
     * @param playVolume 播放音量 range 0.0-1.0
     */
    public void setPlayVolume(@FloatRange(from = 0.0, to = 1.0) float playVolume) {
        this.playVolume = playVolume;
    }

    /**
     * 设置WheelView 是否显示分割线
     *
     * @param isShowDivider 是否显示分割线
     */
    public void setShowDivider(boolean isShowDivider) {
        this.isShowDivider = isShowDivider;
    }

    /**
     * 设置WheelView 分割线类型
     *
     * @param dividerType 分割线类型
     */
    public void setDividerType(@WheelView.DividerType int dividerType) {
        this.dividerType = dividerType;
    }

    /**
     * 得到选择器视图，可内嵌到其他视图容器
     */
    @Override
    public View getContentView() {
        if (null == contentView) {
            contentView = makeCenterView();
        }
        return contentView;
    }
}
