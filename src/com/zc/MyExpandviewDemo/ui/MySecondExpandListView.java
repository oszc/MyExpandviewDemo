package com.zc.MyExpandviewDemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.TextView;
import com.zc.MyExpandviewDemo.R;

/**
 * 1/20/15  7:23 PM
 * Created by JustinZhang.
 */
public class MySecondExpandListView extends ExpandableListView implements AbsListView.OnScrollListener {
    private static final String TAG =MySecondExpandListView.class.getSimpleName() ;
    private Context mContext;
    private View mHeaderView;
    private LayoutInflater mLayoutInflater;

    public MySecondExpandListView(Context context) {
        super(context);
        shareConstructor(context);
    }

    public MySecondExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        shareConstructor(context);
    }

    public MySecondExpandListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        shareConstructor(context);
    }

    private void shareConstructor(Context context){
        mContext = context;
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mHeaderView = mLayoutInflater.inflate(R.layout.group_layout,null);
        mHeaderView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        setOnScrollListener(this);
    }

    public void refreshHeader(){

        int firstVisiblePosition = getFirstVisiblePosition();
        long expandableListPosition = getExpandableListPosition(firstVisiblePosition);

        int packedPositionGroup = getPackedPositionGroup(expandableListPosition);

        TextView tv = (TextView) mHeaderView.findViewById(R.id.tv_grop);

        mHeaderView.layout(0,0,mHeaderWidth,mHeaderHeight);
        String group = (String) getExpandableListAdapter().getGroup(packedPositionGroup);
        Log.e(TAG,"packedPositionGroup:"+ packedPositionGroup+"   group string:"+group);
        tv.setText(group);



    }

    private int mHeaderWidth;
    private int mHeaderHeight;
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChild(mHeaderView,widthMeasureSpec,heightMeasureSpec);

        mHeaderWidth = mHeaderView.getMeasuredWidth();
        mHeaderHeight = mHeaderView.getMeasuredHeight();

    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(mHeaderView != null){

            mHeaderView.draw(canvas);

        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        if(totalItemCount > 0){
            refreshHeader();
        }
    }
}
