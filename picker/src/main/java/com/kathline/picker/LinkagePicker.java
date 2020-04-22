package com.kathline.picker;

import android.app.Activity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Size;

import com.kathline.picker.listener.OnOptionsChangedListener;
import com.kathline.picker.listener.OnOptionsSelectedListener;
import com.kathline.picker.listener.OnPickerScrollStateChangedListener;
import com.kathline.picker.widget.WheelView;

import java.util.List;

/**
 * 两级、三级联动选择器。默认只初始化第一级数据，第二三级数据由联动获得。
 * <p/>
 * @author matt
 * blog: addapp.cn
 */
public class LinkagePicker<T> extends WheelPicker {

    private static final int ID_OPTIONS_WV_1 = 1;
    private static final int ID_OPTIONS_WV_2 = 2;
    private static final int ID_OPTIONS_WV_3 = 3;
    //WheelView
    private WheelView<T> mOptionsWv1;
    private WheelView<T> mOptionsWv2;
    private WheelView<T> mOptionsWv3;

    //联动数据
    private List<T> mOptionsData1;
    private List<List<T>> mOptionsData2;
    private List<List<List<T>>> mOptionsData3;
    //是否联动
    private boolean isLinkage;
    private boolean isResetSelectedPosition;
    protected OnOptionsSelectedListener<T> mOnOptionsSelectedListener;
    protected OnOptionsChangedListener<T> mOnOptionsChangedListener;
    protected OnPickerScrollStateChangedListener mOnPickerScrollStateChangedListener;
    
    protected String firstLabel = "", secondLabel = "", thirdLabel = "";
    protected int selectedFirstIndex = 0, selectedSecondIndex = 0, selectedThirdIndex = 0;
    private float firstColumnWeight = 1;//第一级显示的宽度比
    private float secondColumnWeight = 1;//第二级显示的宽度比
    private float thirdColumnWeight = 1;//第三级显示的宽度比

    private boolean[] showStatus = new boolean[]{true, true, true};//对应三个WheelView显示隐藏

    public LinkagePicker(Activity activity) {
        super(activity);
    }

    public void setShowStatus(@Size(3) boolean[] status) {
        this.showStatus = status;
    }

    /**
     * 设置不联动数据
     *
     * @param data 数据
     */
    public void setData(List<T> data) {
        setData(data, null, null);
    }

    /**
     * 设置不联动数据
     *
     * @param data1 数据1
     * @param data2 数据2
     */
    public void setData(List<T> data1, List<T> data2) {
        setData(data1, data2, null);
    }

    /**
     * 设置不联动数据
     *
     * @param data1 数据1
     * @param data2 数据2
     * @param data3 数据3
     */
    public void setData(List<T> data1, List<T> data2, List<T> data3) {
        isLinkage = false;
        setDataOrGone(data1, mOptionsWv1);
        setDataOrGone(data2, mOptionsWv2);
        setDataOrGone(data3, mOptionsWv3);
    }

    /**
     * 设置数据，如果数据为null隐藏对应的WheelView
     *
     * @param data      数据
     * @param wheelView WheelView
     */
    private void setDataOrGone(List<T> data, WheelView<T> wheelView) {
        if (data != null) {
            wheelView.setData(data);
        } else {
            wheelView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置联动数据
     *
     * @param linkageData1 联动数据1
     * @param linkageData2 联动数据2
     */
    public void setLinkageData(List<T> linkageData1, List<List<T>> linkageData2) {
        setLinkageData(linkageData1, linkageData2, null);
    }

    /**
     * 设置联动数据
     *
     * @param linkageData1 联动数据1
     * @param linkageData2 联动数据2
     * @param linkageData3 联动数据3
     */
    public void setLinkageData(List<T> linkageData1, List<List<T>> linkageData2, List<List<List<T>>> linkageData3) {
        if (linkageData1 == null || linkageData1.size() == 0 || linkageData2 == null || linkageData2.size() == 0) {
            return;
        }
        //数据限制，联动需保持 最外层List size一致，及linkageData1.size()==linkageData2.size()==linkageData3.size()
        //理论上第二层 linkageData2每一项的size及get(i).size()都要和linkageData3.get(i).size()一致
        if (linkageData1.size() != linkageData2.size()) {
            throw new IllegalArgumentException("linkageData1 and linkageData3 are not the same size.");
        }
        isLinkage = true;
        mOptionsData1 = linkageData1;
        mOptionsData2 = linkageData2;
        if (linkageData3 == null) {
            mOptionsData3 = null;
            mOptionsWv3.setVisibility(View.GONE);
            //两级联动
            mOptionsWv1.setData(linkageData1);
            mOptionsWv2.setData(linkageData2.get(0));
        } else {
            mOptionsWv3.setVisibility(View.VISIBLE);
            if (linkageData1.size() != linkageData3.size()) {
                throw new IllegalArgumentException("linkageData1 and linkageData3 are not the same size.");
            }

            for (int i = 0; i < linkageData1.size(); i++) {
                if (linkageData2.get(i).size() != linkageData3.get(i).size()) {
                    throw new IllegalArgumentException("linkageData2 index " + i + " List and linkageData3 index "
                            + i + " List are not the same size.");
                }
            }

            mOptionsData3 = linkageData3;
            //三级联动
            mOptionsWv1.setData(linkageData1);
            mOptionsWv2.setData(linkageData2.get(0));
            mOptionsWv3.setData(linkageData3.get(0).get(0));
            if (isResetSelectedPosition) {
                mOptionsWv1.setSelectedItemPosition(0);
                mOptionsWv2.setSelectedItemPosition(0);
                mOptionsWv3.setSelectedItemPosition(0);
            }
        }
    }

    public void setSelectedIndex(int firstIndex, int secondIndex) {
        setSelectedIndex(firstIndex, secondIndex, 0);
    }

    public void setSelectedIndex(int firstIndex, int secondIndex, int thirdIndex) {
        selectedFirstIndex = firstIndex;
        selectedSecondIndex = secondIndex;
        selectedThirdIndex = thirdIndex;
    }

    public void setLabel(String firstLabel, String secondLabel) {
        setLabel(firstLabel, secondLabel, "");
    }

    public void setLabel(String firstLabel, String secondLabel, String thirdLabel) {
        this.firstLabel = firstLabel;
        this.secondLabel = secondLabel;
        this.thirdLabel = thirdLabel;
    }

    public int getSelectedFirstIndex() {
        return selectedFirstIndex;
    }

    public int getSelectedSecondIndex() {
        return selectedSecondIndex;
    }

    public int getSelectedThirdIndex() {
        return selectedThirdIndex;
    }

    /**
     * 设置每列的宽度比例，将屏幕分为三列，每列范围为0.0～1.0，如0.3333表示约占屏幕的三分之一。
     */
    public void setColumnWeight(@FloatRange(from = 0, to = 1) float firstColumnWeight,
                                @FloatRange(from = 0, to = 1) float secondColumnWeight,
                                @FloatRange(from = 0, to = 1) float thirdColumnWeight) {
        this.firstColumnWeight = firstColumnWeight;
        this.secondColumnWeight = secondColumnWeight;
        this.thirdColumnWeight = thirdColumnWeight;
    }

    /**
     * 设置每列的宽度比例，将屏幕分为两列，每列范围为0.0～1.0，如0.5表示占屏幕的一半。
     */
    public void setColumnWeight(@FloatRange(from = 0, to = 1) float firstColumnWeight,
                                @FloatRange(from = 0, to = 1) float secondColumnWeight) {
        this.firstColumnWeight = firstColumnWeight;
        this.secondColumnWeight = secondColumnWeight;
        this.thirdColumnWeight = 0;
    }

    /**
     * 获取联动状态下当数据变化时，是否重置选中下标到第一个
     *
     * @return 是否重置选中下标到第一个
     */
    public boolean isResetSelectedPosition() {
        return isResetSelectedPosition;
    }

    /**
     * 设置联动状态下当数据变化时，是否重置选中下标到第一个
     *
     * @param isResetSelectedPosition 当数据变化时,是否重置选中下标到第一个
     */
    public void setResetSelectedPosition(boolean isResetSelectedPosition) {
        this.isResetSelectedPosition = isResetSelectedPosition;
        mOptionsWv1.setResetSelectedPosition(isResetSelectedPosition);
        mOptionsWv2.setResetSelectedPosition(isResetSelectedPosition);
        mOptionsWv3.setResetSelectedPosition(isResetSelectedPosition);
    }

    /**
     * 根据比例计算，获取每列的实际宽度。
     * 三级联动默认每列宽度为屏幕宽度的三分之一，两级联动默认每列宽度为屏幕宽度的一半。
     */
    @Size(3)
    protected int[] getColumnWidths() {
        int[] widths = new int[3];
        widths[0] = (int) (screenWidthPixels * firstColumnWeight);
        widths[1] = (int) (screenWidthPixels * secondColumnWeight);
        widths[2] = (int) (screenWidthPixels * thirdColumnWeight);
        return widths;
    }

    protected int setVisibility(boolean flag) {
        return flag ? View.VISIBLE : View.GONE;
    }

    @NonNull
    @Override
    protected View makeCenterView() {
        int[] widths = getColumnWidths();
        LinearLayout layout = new LinearLayout(activity);
        layout.setLayoutParams(new ViewGroup.LayoutParams(MATCH_PARENT, WRAP_CONTENT));
        layout.setOrientation(LinearLayout.HORIZONTAL);
        layout.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams wheelParams = new LinearLayout.LayoutParams(widths[0], WRAP_CONTENT);
        LinearLayout.LayoutParams wheelParams1 = new LinearLayout.LayoutParams(widths[1], WRAP_CONTENT);
        LinearLayout.LayoutParams wheelParams2 = new LinearLayout.LayoutParams(widths[2], WRAP_CONTENT);
        if(weightEnable){
            wheelParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            wheelParams1 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            wheelParams2 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            wheelParams.weight = 1;
            wheelParams1.weight = 1;
            wheelParams2.weight = 1;
        }else {
            wheelParams = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            wheelParams1 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            wheelParams2 = new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
            wheelParams.weight = firstColumnWeight;
            wheelParams1.weight = secondColumnWeight;
            wheelParams2.weight = thirdColumnWeight;
        }
        //判断是选择ios滚轮模式还是普通模式
        mOptionsWv1 = new WheelView<>(activity);
        mOptionsWv1.setId(ID_OPTIONS_WV_1);
        mOptionsWv1.setCyclic(canLoop);
        mOptionsWv1.setTextSize(textSize);
        mOptionsWv1.setSelectedItemTextColor(textColorFocus);
        mOptionsWv1.setNormalItemTextColor(textColorNormal);
        mOptionsWv1.setLineSpacing(lineSpacing);
        mOptionsWv1.setSoundEffect(isSoundEffect);
        mOptionsWv1.setPlayVolume(playVolume);
        mOptionsWv1.setVisibleItems(mVisibleItems);
        mOptionsWv1.setLayoutParams(wheelParams);
        layout.addView(mOptionsWv1);
        if (!TextUtils.isEmpty(firstLabel)){
            if(isOuterLabelEnable()){
                TextView labelView = new TextView(activity);
                labelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                labelView.setTextSize(textSize);
                labelView.setTextColor(textColorFocus);
                labelView.setText(firstLabel);
                layout.addView(labelView);
            }
        }

        mOptionsWv2 = new WheelView<>(activity);
        mOptionsWv2.setId(ID_OPTIONS_WV_2);
        mOptionsWv2.setCyclic(canLoop);
        mOptionsWv2.setTextSize(textSize);
        mOptionsWv2.setSelectedItemTextColor(textColorFocus);
        mOptionsWv2.setNormalItemTextColor(textColorNormal);
        mOptionsWv2.setLineSpacing(lineSpacing);
        mOptionsWv2.setSoundEffect(isSoundEffect);
        mOptionsWv2.setPlayVolume(playVolume);
        mOptionsWv2.setVisibleItems(mVisibleItems);
        mOptionsWv2.setLayoutParams(wheelParams1);
        layout.addView(mOptionsWv2);
        if (!TextUtils.isEmpty(secondLabel)){
            if(isOuterLabelEnable()){
                TextView labelView = new TextView(activity);
                labelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                labelView.setTextSize(textSize);
                labelView.setTextColor(textColorFocus);
                labelView.setText(secondLabel);
                layout.addView(labelView);
            }
        }
        mOptionsWv3 = new WheelView<>(activity);
        mOptionsWv3.setId(ID_OPTIONS_WV_3);
        mOptionsWv3.setCyclic(canLoop);
        mOptionsWv3.setTextSize(textSize);
        mOptionsWv3.setSelectedItemTextColor(textColorFocus);
        mOptionsWv3.setNormalItemTextColor(textColorNormal);
        mOptionsWv3.setLineSpacing(lineSpacing);
        mOptionsWv3.setSoundEffect(isSoundEffect);
        mOptionsWv3.setPlayVolume(playVolume);
        mOptionsWv3.setVisibleItems(mVisibleItems);
        mOptionsWv3.setLayoutParams(wheelParams2);
        layout.addView(mOptionsWv3);
        if (!TextUtils.isEmpty(thirdLabel)){
            if(isOuterLabelEnable()){
                TextView labelView = new TextView(activity);
                labelView.setLayoutParams(new LinearLayout.LayoutParams(WRAP_CONTENT, WRAP_CONTENT));
                labelView.setTextSize(textSize);
                labelView.setTextColor(textColorFocus);
                labelView.setText(thirdLabel);
                layout.addView(labelView);
            }
        }

        mOptionsWv1.setVisibility(setVisibility(showStatus[0]));
        mOptionsWv2.setVisibility(setVisibility(showStatus[1]));
        mOptionsWv3.setVisibility(setVisibility(showStatus[2]));

        mOptionsWv1.setOnItemSelectedListener(mOnItemSelectedListener);
        mOptionsWv2.setOnItemSelectedListener(mOnItemSelectedListener);
        mOptionsWv3.setOnItemSelectedListener(mOnItemSelectedListener);
        mOptionsWv1.setAutoFitTextSize(true);
        mOptionsWv2.setAutoFitTextSize(true);
        mOptionsWv3.setAutoFitTextSize(true);
        mOptionsWv1.setOnWheelChangedListener(mOnWheelChangedListener);
        mOptionsWv2.setOnWheelChangedListener(mOnWheelChangedListener);
        mOptionsWv3.setOnWheelChangedListener(mOnWheelChangedListener);
        return layout;
    }

    private WheelView.OnWheelChangedListener mOnWheelChangedListener = new WheelView.OnWheelChangedListener() {
        @Override
        public void onWheelScroll(int scrollOffsetY) {

        }

        @Override
        public void onWheelItemChanged(int oldPosition, int newPosition) {

        }

        @Override
        public void onWheelSelected(int position) {

        }

        @Override
        public void onWheelScrollStateChanged(int state) {
            if (mOnPickerScrollStateChangedListener != null) {
                mOnPickerScrollStateChangedListener.onScrollStateChanged(state);
            }
        }
    };

    private WheelView.OnItemSelectedListener<T> mOnItemSelectedListener = new WheelView.OnItemSelectedListener<T>() {
        @Override
        public void onItemSelected(WheelView<T> wheelView, T data, int position) {
            if (isLinkage) {
                //联动
                if (wheelView.getId() == ID_OPTIONS_WV_1) {
                    //第一个
                    mOptionsWv2.setData(mOptionsData2.get(position));
                    if (mOptionsData3 != null) {
                        mOptionsWv3.setData(mOptionsData3.get(position).get(mOptionsWv2.getSelectedItemPosition()));
                    }
                } else if (wheelView.getId() == ID_OPTIONS_WV_2) {
                    //第二个
                    if (mOptionsData3 != null) {
                        mOptionsWv3.setData(mOptionsData3.get(mOptionsWv1.getSelectedItemPosition()).get(position));
                    }
                }

                if (mOnOptionsChangedListener != null) {
                    int opt1Pos = mOptionsWv1.getSelectedItemPosition();
                    int opt2Pos = mOptionsWv2.getSelectedItemPosition();
                    int opt3Pos = mOptionsData3 == null ? -1 : mOptionsWv3.getSelectedItemPosition();
                    T opt1Data = mOptionsData1.get(opt1Pos);
                    T opt2Data = mOptionsData2.get(opt1Pos).get(opt2Pos);
                    T opt3Data = null;
                    if (mOptionsData3 != null) {
                        opt3Data = mOptionsData3.get(opt1Pos).get(opt2Pos).get(opt3Pos);
                    }
                    mOnOptionsChangedListener.onOptionsSelected(opt1Pos, opt1Data, opt2Pos, opt2Data, opt3Pos, opt3Data);
                }

            } else {
                //不联动
                if (mOnOptionsChangedListener != null) {
                    boolean isOpt1Shown = mOptionsWv1.getVisibility() == View.VISIBLE;
                    int opt1Pos = isOpt1Shown ? mOptionsWv1.getSelectedItemPosition() : -1;
                    boolean isOpt2Shown = mOptionsWv2.getVisibility() == View.VISIBLE;
                    int opt2Pos = isOpt2Shown ? mOptionsWv2.getSelectedItemPosition() : -1;
                    boolean isOpt3Shown = mOptionsWv3.getVisibility() == View.VISIBLE;
                    int opt3Pos = isOpt3Shown ? mOptionsWv3.getSelectedItemPosition() : -1;
                    T opt1Data = isOpt1Shown ? mOptionsWv1.getSelectedItemData() : null;
                    T opt2Data = isOpt2Shown ? mOptionsWv2.getSelectedItemData() : null;
                    T opt3Data = isOpt3Shown ? mOptionsWv3.getSelectedItemData() : null;
                    mOnOptionsChangedListener.onOptionsSelected(opt1Pos, opt1Data, opt2Pos, opt2Data, opt3Pos, opt3Data);
                }
            }
        }
    };

    @Override
    public void onSubmit() {
        if (mOnOptionsSelectedListener != null) {
            mOnOptionsSelectedListener.onOptionsSelected(getOpt1SelectedPosition(),getOpt1SelectedData(),
                    getOpt2SelectedPosition(), getOpt2SelectedData(),
                    getOpt3SelectedPosition(), getOpt3SelectedData());
        }
    }

    /**
     * 设置选项选中回调监听器
     *
     * @param onOptionsSelectedListener 选项选中回调监听器
     */
    public void setOnOptionsSelectedListener(OnOptionsSelectedListener<T> onOptionsSelectedListener) {
        mOnOptionsSelectedListener = onOptionsSelectedListener;
    }

    /**
     * 设置选项选中改变回调监听器
     *
     * @param onOptionsChangedListener 选项选中回调监听器
     */
    public void setOnOptionsChangedListener(OnOptionsChangedListener<T> onOptionsChangedListener) {
        mOnOptionsChangedListener = onOptionsChangedListener;
    }

    /**
     * 设置滚动状态变化监听
     *
     * @param listener 滚动状态变化监听器
     */
    public void setOnPickerScrollStateChangedListener(OnPickerScrollStateChangedListener listener) {
        mOnPickerScrollStateChangedListener = listener;
    }

    /**
     * 获取选项1WheelView 选中下标
     *
     * @return 选中下标
     */
    public int getOpt1SelectedPosition() {
        return mOptionsWv1.getSelectedItemPosition();
    }

    /**
     * 设置选项1WheelView 选中下标
     *
     * @param position 选中下标
     */
    public void setOpt1SelectedPosition(int position) {
        setOpt1SelectedPosition(position, false);
    }

    /**
     * 设置选项1WheelView 选中下标
     *
     * @param position       选中下标
     * @param isSmoothScroll 是否平滑滚动
     */
    public void setOpt1SelectedPosition(int position, boolean isSmoothScroll) {
        setOpt1SelectedPosition(position, isSmoothScroll, 0);
    }

    /**
     * 设置选项1WheelView 选中下标
     *
     * @param position       选中下标
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    public void setOpt1SelectedPosition(int position, boolean isSmoothScroll, int smoothDuration) {
        mOptionsWv1.setSelectedItemPosition(position, isSmoothScroll, smoothDuration);
    }

    /**
     * 获取选项2WheelView 选中下标
     *
     * @return 选中下标
     */
    public int getOpt2SelectedPosition() {
        return mOptionsWv2.getSelectedItemPosition();
    }

    /**
     * 设置选项2WheelView 选中下标
     *
     * @param position 选中下标
     */
    public void setOpt2SelectedPosition(int position) {
        setOpt2SelectedPosition(position, false);
    }

    /**
     * 设置选项2WheelView 选中下标
     *
     * @param position       选中下标
     * @param isSmoothScroll 是否平滑滚动
     */
    public void setOpt2SelectedPosition(int position, boolean isSmoothScroll) {
        setOpt2SelectedPosition(position, isSmoothScroll, 0);
    }

    /**
     * 设置选项2WheelView 选中下标
     *
     * @param position       选中下标
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    public void setOpt2SelectedPosition(int position, boolean isSmoothScroll, int smoothDuration) {
        mOptionsWv2.setSelectedItemPosition(position, isSmoothScroll, smoothDuration);
    }

    /**
     * 获取选项3WheelView 选中下标
     *
     * @return 选中下标
     */
    public int getOpt3SelectedPosition() {
        return mOptionsWv3.getSelectedItemPosition();
    }

    /**
     * 设置选项3WheelView 选中下标
     *
     * @param position 选中下标
     */
    public void setOpt3SelectedPosition(int position) {
        setOpt3SelectedPosition(position, false);
    }

    /**
     * 设置选项3WheelView 选中下标
     *
     * @param position       选中下标
     * @param isSmoothScroll 是否平滑滚动
     */
    public void setOpt3SelectedPosition(int position, boolean isSmoothScroll) {
        setOpt3SelectedPosition(position, isSmoothScroll, 0);
    }

    /**
     * 设置选项3WheelView 选中下标
     *
     * @param position       选中下标
     * @param isSmoothScroll 是否平滑滚动
     * @param smoothDuration 平滑滚动持续时间
     */
    public void setOpt3SelectedPosition(int position, boolean isSmoothScroll, int smoothDuration) {
        mOptionsWv3.setSelectedItemPosition(position, isSmoothScroll, smoothDuration);
    }

    /**
     * 获取选项1WheelView 选中的数据
     *
     * @return 选中的数据
     */
    public T getOpt1SelectedData() {
        if (isLinkage) {
            return mOptionsData1.get(mOptionsWv1.getSelectedItemPosition());
        } else {
            return mOptionsWv1.getSelectedItemData();
        }
    }

    /**
     * 获取选项2WheelView 选中的数据
     *
     * @return 选中的数据
     */
    public T getOpt2SelectedData() {
        if (isLinkage) {
            return mOptionsData2.get(mOptionsWv1.getSelectedItemPosition())
                    .get(mOptionsWv2.getSelectedItemPosition());
        } else {
            return mOptionsWv2.getSelectedItemData();
        }
    }

    /**
     * 获取选项3WheelView 选中的数据
     *
     * @return 选中的数据
     */
    public T getOpt3SelectedData() {
        if (isLinkage) {
            return mOptionsData3 == null ? null : mOptionsData3.get(mOptionsWv1.getSelectedItemPosition())
                    .get(mOptionsWv2.getSelectedItemPosition())
                    .get(mOptionsWv3.getSelectedItemPosition());
        } else {
            return mOptionsWv3.getSelectedItemData();
        }
    }

    /**
     * 获取选项1WheelView
     *
     * @return 选项1WheelView
     */
    public WheelView<T> getOptionsWv1() {
        return mOptionsWv1;
    }

    /**
     * 获取选项2WheelView
     *
     * @return 选项2WheelView
     */
    public WheelView<T> getOptionsWv2() {
        return mOptionsWv2;
    }

    /**
     * 获取选项3WheelView
     *
     * @return 选项3WheelView
     */
    public WheelView<T> getOptionsWv3() {
        return mOptionsWv3;
    }

}
