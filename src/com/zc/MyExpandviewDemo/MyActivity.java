package com.zc.MyExpandviewDemo;

import android.app.Activity;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.zc.MyExpandviewDemo.ui.MyExpandListView;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyActivity extends Activity {

    @InjectView(R.id.my_expand_listview)
    MyExpandListView mMyExpandListview;
    private Map<String, List<String>> mData;

    /**
     * 数据
     * 体育项目：田径,体操,篮球,排球,足球,乒乓球,羽毛球,举重,游泳,自行车
     * 国产汽车：长城,中华,红旗,解放,金杯,江淮,江铃,哈飞,东风,昌河,长安,红岩,陕汽,福田,春兰,华菱,锐雁,中顺
     * 日系车：丰田TOYOTA,日产NISSAN,本田HONDA,雷克萨斯LEXUS,英菲尼迪INFINITI,讴歌ACURA,斯巴鲁SUBARU,马自达MAZDA,三菱MITSUBISI,五十铃ISUZU,铃木SUZUKI
     * 德系车：欧宝,奔驰,奥迪,BMW,保时捷,大众
     * 美系车：别克：荣誉,君威,君越,凯越,GL8,昂克雷,林荫大道,英朗
     */


    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ButterKnife.inject(this);
        initData();
        mMyExpandListview.setAdapter(new MyExpandableListviewAdapter(this,mData));
        for (int i = 0; i < mData.size(); i++) {
            mMyExpandListview.expandGroup(i);

        }
    }

    private void initData() {
        mData = new HashMap<String, List<String>>();

        List<String> sports = Arrays.asList("田径", "体操", "篮球", "排球", "足球", "乒乓球", "羽毛球", "举重", "游泳", "自行车");
        mData.put("体育项目：", sports);

        List<String> domesticCar = Arrays.asList("长城", "中华", "红旗", "解放", "金杯", "江淮", "江铃", "哈飞", "东风", "昌河", "长安", "红岩", "陕汽", "福田", "春兰", "华菱", "锐雁", "中顺");
        mData.put("国产汽车", domesticCar);

        List<String> japanMadeCar = Arrays.asList("丰田TOYOTA", "日产NISSAN", "本田HONDA", "雷克萨斯LEXUS", "英菲尼迪INFINITI", "讴歌ACURA", "斯巴鲁SUBARU", "马自达MAZDA", "三菱MITSUBISI", "五十铃ISUZU", "铃木SUZUKI");
        mData.put("日系车", japanMadeCar);

        List<String> germanMadeCar = Arrays.asList("欧宝", "奔驰", "奥迪", "BMW", "保时捷", "大众");
        mData.put("德系车", germanMadeCar);

        List<String> usaMadeCar = Arrays.asList("别克", "荣誉", "君威", "君越", "凯越", "GL8", "昂克雷", "林荫大道", "英朗");
        mData.put("美系车", usaMadeCar);

    }

}
