package com.kathline.demo;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.kathline.picker.widget.WheelView;

import java.util.ArrayList;
import java.util.List;

public class AttrsActivity extends AppCompatActivity {

    private static final String TAG = "Main2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attrs);

        final WheelView<Integer> wheelView = findViewById(R.id.wheelview);
        List<Integer> list = new ArrayList<>(1);
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        wheelView.setData(list);

        final Button showDataBtn=findViewById(R.id.btn_showSelectedData);
        showDataBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AttrsActivity.this,"选中的值为："+wheelView.getSelectedItemData(),Toast.LENGTH_SHORT).show();
            }
        });

        TextView textSizeTv=findViewById(R.id.tv_text_size);
        textSizeTv.setText(getString(R.string.textSizeValue,wheelView.getTextSize()+""));
        TextView textAlignTv=findViewById(R.id.tv_text_align);
        String textAlign="Center";
        if (wheelView.getTextAlign()== WheelView.TEXT_ALIGN_LEFT) {
            textAlign="Left";
        }else if (wheelView.getTextAlign()==WheelView.TEXT_ALIGN_RIGHT){
            textAlign="right";
        }
        textAlignTv.setText(getString(R.string.textAlignValue,textAlign));
        TextView textBoundaryMarginTv=findViewById(R.id.tv_textBoundaryMargin);
        textBoundaryMarginTv.setText(getString(R.string.textBoundaryMarginValue,wheelView.getTextBoundaryMargin()+""));
        TextView normalItemTextColorTv=findViewById(R.id.tv_normalItemTextColor);
        normalItemTextColorTv.setText(getString(R.string.normalItemTextColorValue,"#"+Integer.toHexString(wheelView.getNormalItemTextColor()).toUpperCase()));
        TextView selectedItemTextColorTv=findViewById(R.id.tv_selectedItemTextColor);
        selectedItemTextColorTv.setText(getString(R.string.selectedItemTextColorValue,"#"+Integer.toHexString(wheelView.getSelectedItemTextColor()).toUpperCase()));
        TextView lineSpaceTv=findViewById(R.id.tv_lineSpace);
        lineSpaceTv.setText(getString(R.string.lineSpaceValue,wheelView.getLineSpacing()+""));
        TextView integerNeedFormatTv=findViewById(R.id.tv_integerNeedFormat);
        integerNeedFormatTv.setText(getString(R.string.integerNeedFormatValue,wheelView.isIntegerNeedFormat()+""));
        TextView integerFormatTv=findViewById(R.id.tv_integerFormat);
        integerFormatTv.setText(getString(R.string.integerFormatValue,wheelView.getIntegerFormat()));
        TextView visibleItemsTv=findViewById(R.id.tv_visibleItems);
        visibleItemsTv.setText(getString(R.string.visibleItemsValue,wheelView.getVisibleItems()+""));
        final TextView currentItemPositionTv=findViewById(R.id.tv_currentItemPosition);
        currentItemPositionTv.setText(getString(R.string.currentItemPositionValue,wheelView.getSelectedItemPosition()+""));
        final TextView currentItemDataTv=findViewById(R.id.tv_currentItemData);
        currentItemDataTv.setText(getString(R.string.currentItemDataValue,wheelView.getSelectedItemData()+""));
        TextView showDividerTv=findViewById(R.id.tv_showDivider);
        showDividerTv.setText(getString(R.string.showDividerValue,wheelView.isShowDivider()+""));
        TextView dividerTypeTv=findViewById(R.id.tv_dividerType);
        String dividerType="fill";
        if (wheelView.getDividerType()== WheelView.DIVIDER_TYPE_WRAP) {
            dividerType="wrap";
        }
        dividerTypeTv.setText(getString(R.string.dividerTypeValue,dividerType));
        TextView dividerColorTv=findViewById(R.id.tv_dividerColor);
        dividerColorTv.setText(getString(R.string.dividerColorValue,"#"+Integer.toHexString(wheelView.getDividerColor()).toUpperCase()));
        TextView dividerHeightTv=findViewById(R.id.tv_dividerHeight);
        dividerHeightTv.setText(getString(R.string.dividerHeightValue,wheelView.getDividerHeight()+""));
        TextView dividerPaddingForWrapTv=findViewById(R.id.tv_dividerPaddingForWrap);
        dividerPaddingForWrapTv.setText(getString(R.string.dividerPaddingForWrapValue,wheelView.getDividerPaddingForWrap()+""));
        TextView curvedTv=findViewById(R.id.tv_curved);
        curvedTv.setText(getString(R.string.curvedValue,wheelView.isCurved()+""));
        TextView curvedArcDirectionTv=findViewById(R.id.tv_curvedAlign);
        String curvedArcDirection="Center";
        if (wheelView.getCurvedArcDirection()==WheelView.CURVED_ARC_DIRECTION_LEFT){
            curvedArcDirection="Left";
        }else if (wheelView.getCurvedArcDirection()==WheelView.CURVED_ARC_DIRECTION_RIGHT){
            curvedArcDirection="Right";
        }
        curvedArcDirectionTv.setText(getString(R.string.curvedArcDirectionValue,curvedArcDirection));
        TextView curvedArcDirectionBiasTv=findViewById(R.id.tv_curvedAlignBias);
        curvedArcDirectionBiasTv.setText(getString(R.string.curvedArcDirectionBiasValue,wheelView.getCurvedArcDirectionFactor()+""));
        TextView curvedRefractXTv=findViewById(R.id.tv_curvedRefractRatio);
        curvedRefractXTv.setText(getString(R.string.curvedRefractXValue,wheelView.getRefractRatio()+""));

        wheelView.setOnItemSelectedListener(new WheelView.OnItemSelectedListener<Integer>() {
            @Override
            public void onItemSelected(WheelView<Integer> wheelView, Integer data, int position) {
                currentItemPositionTv.setText(getString(R.string.currentItemPositionValue,position+""));
                currentItemDataTv.setText(getString(R.string.currentItemDataValue,wheelView.getSelectedItemData()+""));
            }
        });

        wheelView.setOnWheelChangedListener(new WheelView.OnWheelChangedListener() {
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
                showDataBtn.setEnabled(state==WheelView.SCROLL_STATE_IDLE);
            }
        });
        wheelView.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/pingfang_medium.ttf"));
    }
}
