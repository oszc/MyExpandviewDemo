package com.zc.MyExpandviewDemo.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * 1/19/15  4:48 PM
 * Created by JustinZhang.
 *
 */
public class MyExpandListView extends ExpandableListView{
    /**
     *  该控件作用是header stick
     *
     *  ----------分类----------  （分类永远在屏幕顶端）
     *  ----------条目----------
     *  ----------条目----------
     *  ----------条目----------
     *  ----------条目----------
     *  ----------分类----------
     *  ----------条目----------
     *  ----------条目----------
     *  ----------条目----------
     *
     * 思路：
     *      判断现在分类的组是哪个？顶部绘制改组，
     */
    public MyExpandListView(Context context) {
        super(context);
    }

    public MyExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyExpandListView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }



}
