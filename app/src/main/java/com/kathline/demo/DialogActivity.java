package com.kathline.demo;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import com.kathline.demo.entities.CityEntity;
import com.kathline.demo.utils.ParseHelper;
import com.kathline.demo.utils.ToastUtils;
import com.kathline.picker.DatePicker;
import com.kathline.picker.DateTimePicker;
import com.kathline.picker.LinkagePicker;
import com.kathline.picker.SinglePicker;
import com.kathline.picker.TimePicker;
import com.kathline.picker.listener.OnItemPickListener;
import com.kathline.picker.listener.OnOptionsChangedListener;
import com.kathline.picker.listener.OnOptionsSelectedListener;
import com.kathline.picker.listener.OnSingleWheelListener;
import com.kathline.picker.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DialogActivity extends AppCompatActivity {

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        context = this;
    }

    public void onSignPicker(View view) {
        ArrayList<String> list = new ArrayList<>();
        for(int i = 0;i<10; i++){
            String s = "";
            if(i<10){
                s = "0"+i;
            }else{
                s = i+"";
            }
            list.add(s);
        }
//        String[] ss = (String[]) list.toArray();
        SinglePicker<String> picker = new SinglePicker<>(this, list);
        picker.setCanLoop(false);//不禁用循环
        picker.setTopLineVisible(false);
        picker.setTextSize(18);
        picker.setLineSpacing(30);
        picker.setSelectedIndex(2);
        picker.setLabel("分");
        picker.setOuterLabelEnable(true);
        picker.setTitleText("得分");
        picker.setItemWidth(100);
        //启用权重 setWeightWidth 才起作用
//        picker.setWeightEnable(true);
//        picker.setWeightWidth(1);
        picker.setSelectedTextColor(Color.GREEN);//前四位值是透明度
        picker.setUnSelectedTextColor(Color.BLACK);
        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int index, String item) {
                ToastUtils.showShortToast(context, "index=" + index + ", item=" + item);
            }
        });
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                ToastUtils.showShortToast(context, "index=" + index + ", item=" + item);
            }
        });
        picker.show();
    }

    public void onLinkagePicker(View view) {
        LinkagePicker<String> picker = new LinkagePicker<>(this);
        picker.setCanLoop(false);
        picker.setLabel("小时制", "点");
        picker.setSelectedIndex(0, 8);
        //picker.setSelectedItem("12", "9");
        picker.setOnOptionsSelectedListener(new OnOptionsSelectedListener<String>() {
            @Override
            public void onOptionsSelected(int opt1Pos, @Nullable String opt1Data, int opt2Pos,
                                          @Nullable String opt2Data, int opt3Pos, @Nullable String opt3Data) {
                ToastUtils.showShortToast(context, opt1Data + "-" + opt2Data + "-" + opt3Data);
            }
        });
        picker.show();
        List<String> firstList = new ArrayList<>(1);
        firstList.add("12");
        firstList.add("24");
        List<List<String>> secondList = new ArrayList<>(1);
        for (int index = 0; index < firstList.size(); index++) {
            ArrayList<String> list = new ArrayList<>(1);
            for (int i = 1; i <= (index == 0 ? 12 : 24); i++) {
                String str = DateUtils.fillZero(i);
                list.add(str);
            }
            secondList.add(list);
        }
        picker.setLinkageData(firstList, secondList);
    }

    /*
     * 年月日时间选择
     * */
    public void onYearMonthDayTimePicker(View view) {
        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
        picker.setActionButtonTop(false);
        picker.setDateRangeStart(2017, 1, 1);
        picker.setDateRangeEnd(2025, 11, 11);
        picker.setSelectedItem(2018,6,16,0,0);
        picker.setTimeRangeStart(9, 0);
        picker.setTimeRangeEnd(20, 30);
        picker.setCanLinkage(false);
        picker.setTitleText("请选择");
        picker.setStepMinute(5);
        picker.setWeightEnable(true);
        picker.setCanceledOnTouchOutside(true);
//        LineConfig config = new LineConfig();
//        config.setColor(Color.BLUE);//线颜色
//        config.setAlpha(120);//线透明度
//        config.setVisible(true);//线不显示 默认显示
//        picker.setLineConfig(config);
        picker.setOuterLabelEnable(true);
//        picker.setLabel(null,null,null,null,null);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                ToastUtils.showShortToast(context, year + "-" + month + "-" + day + " " + hour + ":" + minute);
            }
        });
        picker.show();
    }

    public void onTimePicker(View view) {
        TimePicker picker = new TimePicker(this, TimePicker.HOUR_24);
        picker.setRangeStart(9, 0);//09:00
        picker.setRangeEnd(18, 0);//18:30
        picker.setTopLineVisible(false);
        picker.setOnTimePickListener(new TimePicker.OnTimePickListener() {
            @Override
            public void onTimePicked(String hour, String minute) {
                ToastUtils.showShortToast(context, hour + ":" + minute);
            }
        });
        picker.show();
    }

    public void onMonthDayPicker(View view) {
        DatePicker picker = new DatePicker(this, DatePicker.MONTH_DAY);
        picker.setGravity(Gravity.CENTER);//弹框居中
        picker.setCanLoop(false);
        picker.setWeightEnable(true);
        picker.setCanLinkage(true);
//        LineConfig lineConfig = new LineConfig();
//        lineConfig.setColor(Color.GREEN);
//        picker.setLineConfig(lineConfig);
        picker.setTopLineColor(Color.RED);
        picker.setRangeStart(5, 1);
        picker.setRangeEnd(12, 31);
        picker.setSelectedItem(10, 14);
        picker.setOnDatePickListener(new DatePicker.OnMonthDayPickListener() {
            @Override
            public void onDatePicked(String month, String day) {
                ToastUtils.showShortToast(context, month + "-" + day);
            }
        });
        picker.show();
    }

    public void onAddressPicker(View view) {
        LinkagePicker<CityEntity> picker = new LinkagePicker<>(this);
        picker.setCanLoop(false);
//        picker.setShowStatus(new boolean[]{true, false, true});
//        picker.setWeightEnable(true);
        picker.setGravity(Gravity.BOTTOM);
//        if (hideCounty) {
//            picker.setColumnWeight(1 / 3.0f, 2 / 3.0f);//将屏幕分为3份，省级和地级的比例为1:2
//        } else {
//            picker.setColumnWeight(2 / 8.0f, 3 / 8.0f, 3 / 8.0f);//省级、地级和县级的比例为2:3:3
//        }
        picker.setOnOptionsSelectedListener(new OnOptionsSelectedListener<CityEntity>() {
            @Override
            public void onOptionsSelected(int opt1Pos, @Nullable CityEntity opt1Data, int opt2Pos,
                                          @Nullable CityEntity opt2Data, int opt3Pos, @Nullable CityEntity opt3Data) {
                ToastUtils.showShortToast(context, "submit: " + (opt1Data != null ? opt1Data.getWheelText() : "null") + "-" + (opt2Data != null ? opt2Data.getWheelText() : "null") + "-" + (opt3Data != null ? opt3Data.getWheelText() : "null"));
            }
        });
        picker.setOnOptionsChangedListener(new OnOptionsChangedListener<CityEntity>() {
            @Override
            public void onOptionsSelected(int opt1Pos, @Nullable CityEntity opt1Data, int opt2Pos,
                                          @Nullable CityEntity opt2Data, int opt3Pos, @Nullable CityEntity opt3Data) {
                ToastUtils.showShortToast(context, "changed: " + (opt1Data != null ? opt1Data.getWheelText() : "null") + "-" + (opt2Data != null ? opt2Data.getWheelText() : "null") + "-" + (opt3Data != null ? opt3Data.getWheelText() : "null"));
            }
        });
        picker.show();
        List<CityEntity> p3List = new ArrayList<>(1);
        List<List<CityEntity>> c3List = new ArrayList<>(1);
        List<List<List<CityEntity>>> d3List = new ArrayList<>(1);
        ParseHelper.initThreeLevelCityList(context, p3List, c3List, d3List);
        picker.setLinkageData(p3List, c3List, d3List);
    }

    public void onNestView(View view) {
    }

    public void onDateRangePicker(View view) {
    }

    public void onYearMonthDayPicker(View view) {
        final DatePicker picker = new DatePicker(this);
        picker.setTopPadding(15);
        picker.setRangeStart(2016, 8, 29);
        picker.setRangeEnd(2111, 1, 11);
        picker.setSelectedItem(2050, 10, 14);
        picker.setWeightEnable(true);
        picker.setTopLineColor(Color.BLACK);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                ToastUtils.showLongToast(context, year + "-" + month + "-" + day);
            }
        });
        picker.setOnWheelListener(new DatePicker.OnWheelListener() {
            @Override
            public void onYearWheeled(int index, String year) {
                picker.setTitleText(year + "-" + picker.getSelectedMonth() + "-" + picker.getSelectedDay());
            }

            @Override
            public void onMonthWheeled(int index, String month) {
                picker.setTitleText(picker.getSelectedYear() + "-" + month + "-" + picker.getSelectedDay());
            }

            @Override
            public void onDayWheeled(int index, String day) {
                picker.setTitleText(picker.getSelectedYear() + "-" + picker.getSelectedMonth() + "-" + day);
            }
        });
        picker.show();
    }

    public void onYearMonthPicker(View view) {
        DatePicker picker = new DatePicker(this, DatePicker.YEAR_MONTH);
        picker.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL);
        picker.setWidth((int) (picker.getScreenWidthPixels() * 0.6));
        picker.setRangeStart(2016, 10, 14);
        picker.setRangeEnd(2020, 11, 11);
        picker.setSelectedItem(2017, 9);
        picker.setCanLinkage(true);
        picker.setWeightEnable(true);
        picker.setOnDatePickListener(new DatePicker.OnYearMonthPickListener() {
            @Override
            public void onDatePicked(String year, String month) {
                ToastUtils.showLongToast(context, year + "-" + month);
            }
        });
        picker.show();
    }

    public void onConstellationPicker(View view) {
        boolean isChinese = Locale.getDefault().getDisplayLanguage().contains("中文");
        SinglePicker<String> picker = new SinglePicker<>(this,
                isChinese ? new String[]{
                        "水瓶座", "双鱼座", "白羊", "金牛座", "双子座", "巨蟹座",
                        "狮子座", "处女座", "天秤座", "天蝎座", "射手", "摩羯座"
                } : new String[]{
                        "Aquarius", "Pisces", "Aries", "Taurus", "Gemini", "Cancer",
                        "Leo", "Virgo", "Libra", "Scorpio", "Sagittarius", "Capricorn"
                });
        picker.setCanLoop(false);//不禁用循环
        picker.setTopBackgroundColor(0xFFEEEEEE);
        picker.setTopHeight(50);
        picker.setTopLineColor(0xFF33B5E5);
        picker.setTopLineHeight(1);
        picker.setTitleText(isChinese ? "请选择" : "Please pick");
        picker.setTitleTextColor(0xFF999999);
        picker.setTitleTextSize(12);
        picker.setCancelTextColor(0xFF33B5E5);
        picker.setCancelTextSize(13);
        picker.setSubmitTextColor(0xFF33B5E5);
        picker.setSubmitTextSize(13);
        picker.setSelectedTextColor(0xFFEE0000);
        picker.setUnSelectedTextColor(0xFF999999);
//        LineConfig config = new LineConfig();
//        config.setColor(Color.BLUE);//线颜色
//        config.setAlpha(120);//线透明度
////        config.setRatio(1);//线比率
//        picker.setLineConfig(config);
        picker.setItemWidth(200);
        picker.setBackgroundColor(0xFFE1E1E1);
        picker.setSelectedItem(isChinese ? "处女座" : "Virgo");
        picker.setSelectedIndex(7);
        picker.setOnSingleWheelListener(new OnSingleWheelListener() {
            @Override
            public void onWheeled(int index, String item) {
                ToastUtils.showShortToast(context, "index=" + index + ", item=" + item);
            }
        });
        picker.setOnItemPickListener(new OnItemPickListener<String>() {
            @Override
            public void onItemPicked(int index, String item) {
                ToastUtils.showShortToast(context, "index=" + index + ", item=" + item);
            }
        });
        picker.show();
    }

    public void onNumberPicker(View view) {
    }

    public void onAddress3Picker(View view) {
        LinkagePicker<CityEntity> picker = new LinkagePicker<>(this);
        picker.setCanLoop(false);
        picker.setShowStatus(new boolean[]{true, true, false});
//        picker.setWeightEnable(true);
        picker.setGravity(Gravity.BOTTOM);
//        if (hideCounty) {
//            picker.setColumnWeight(1 / 3.0f, 2 / 3.0f);//将屏幕分为3份，省级和地级的比例为1:2
//        } else {
//            picker.setColumnWeight(2 / 8.0f, 3 / 8.0f, 3 / 8.0f);//省级、地级和县级的比例为2:3:3
//        }
        picker.setOnOptionsSelectedListener(new OnOptionsSelectedListener<CityEntity>() {
            @Override
            public void onOptionsSelected(int opt1Pos, @Nullable CityEntity opt1Data, int opt2Pos,
                                          @Nullable CityEntity opt2Data, int opt3Pos, @Nullable CityEntity opt3Data) {
                ToastUtils.showShortToast(context, "submit: " + (opt1Data != null ? opt1Data.getWheelText() : "null") + "-" + (opt2Data != null ? opt2Data.getWheelText() : "null") + "-" + (opt3Data != null ? opt3Data.getWheelText() : "null"));
            }
        });
        picker.setOnOptionsChangedListener(new OnOptionsChangedListener<CityEntity>() {
            @Override
            public void onOptionsSelected(int opt1Pos, @Nullable CityEntity opt1Data, int opt2Pos,
                                          @Nullable CityEntity opt2Data, int opt3Pos, @Nullable CityEntity opt3Data) {
                ToastUtils.showShortToast(context, "changed: " + (opt1Data != null ? opt1Data.getWheelText() : "null") + "-" + (opt2Data != null ? opt2Data.getWheelText() : "null") + "-" + (opt3Data != null ? opt3Data.getWheelText() : "null"));
            }
        });
        picker.show();
        List<CityEntity> p3List = new ArrayList<>(1);
        List<List<CityEntity>> c3List = new ArrayList<>(1);
        List<List<List<CityEntity>>> d3List = new ArrayList<>(1);
        ParseHelper.initThreeLevelCityList(context, p3List, c3List, d3List);
        picker.setLinkageData(p3List, c3List, null);
    }
}
