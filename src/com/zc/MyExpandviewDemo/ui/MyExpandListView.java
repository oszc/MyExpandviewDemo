package com.zc.MyExpandviewDemo.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.zc.MyExpandviewDemo.BuildConfig;
import com.zc.MyExpandviewDemo.R;

/**
 * 1/19/15  4:48 PM
 * Created by JustinZhang.
 */
public class MyExpandListView extends ExpandableListView implements AbsListView.OnScrollListener {
    /**
     * 该控件作用是header stick
     * <p/>
     * ----------分类----------  （分类永远在屏幕顶端）
     * ----------条目----------
     * ----------条目----------
     * ----------条目----------
     * ----------条目----------
     * ----------分类----------   (当上升到屏幕顶端时，替换最上面的分类）
     * ----------条目----------
     * ----------条目----------
     * ----------条目----------
     * <p/>
     * 思路：
     * 判断现在分类的组是哪个？顶部绘制该组，怎么绘制 ondraw?
     */

    private static final String TAG = MyExpandListView.class.getSimpleName();

    public interface OnHeaderUpdateListener {
        public View getPinnedHeader();
        public void updatePinnedHeader(View headerView, int firstVisibleGroupPos);
    }

    private View mHeaderView;
    private int mHeaderWidth;
    private int mHeaderHeight;

    private View mTouchTarget;

    private OnScrollListener mScrollerListener;
    private OnHeaderUpdateListener mHeaderUpdateListener;

    private boolean mActionDownHappened = false;
    protected boolean mIsHeaderGroupClickable = true;
    private LayoutInflater mLayoutInflater;

    public MyExpandListView(Context context) {
        super(context);
        initView(context);
    }

    public MyExpandListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public MyExpandListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context);
    }

    private void initView(Context context) {
        mLayoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setFadingEdgeLength(0);
        setOnScrollListener(this);
        post(new Runnable() {
            @Override
            public void run() {
                setOnHeaderUpdateListener(new OnHeaderUpdateListener() {
                    @Override
                    public View getPinnedHeader() {
                        View view =mLayoutInflater.inflate(R.layout.group_layout,null);
                        view.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        return view;
                    }

                    @Override
                    public void updatePinnedHeader(View headerView, int firstVisibleGroupPos) {

                        TextView tv = (TextView) headerView.findViewById(R.id.tv_grop);
                        ExpandableListAdapter expandableListAdapter = getExpandableListAdapter();
                        tv.setText(expandableListAdapter.getGroup(firstVisibleGroupPos).toString());


                    }
                });
            }
        });

    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        if (l != this) {
            mScrollerListener = l;
        } else {
            mScrollerListener = null;
        }
        super.setOnScrollListener(this);
    }


    public void setOnGroupClickListener(OnGroupClickListener onGroupClickListener, boolean isHeaderGroupClickable) {
        mIsHeaderGroupClickable = isHeaderGroupClickable;
        super.setOnGroupClickListener(onGroupClickListener);
    }

    public void setOnHeaderUpdateListener(OnHeaderUpdateListener listener) {
        mHeaderUpdateListener = listener;
        if (listener == null) {
            mHeaderView = null;
            mHeaderWidth = mHeaderHeight = 0;
            return;
        }

        mHeaderView = listener.getPinnedHeader();
        int firstVisiblePos = getFirstVisiblePosition();
        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
        listener.updatePinnedHeader(mHeaderView, firstVisibleGroupPos);
        requestLayout();
        postInvalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mHeaderView == null) {
            return;
        }
        measureChild(mHeaderView, widthMeasureSpec, heightMeasureSpec);
        mHeaderWidth = mHeaderView.getMeasuredWidth();
        mHeaderHeight = mHeaderView.getMeasuredHeight();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mHeaderView == null) {
            return;
        }
        int delta = mHeaderView.getTop();
        mHeaderView.layout(0, delta, mHeaderWidth, mHeaderHeight + delta);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //viewGroup 如果没有背景，不会调用onDraw,所以如果画子view只能在dispatchDraw中
        super.dispatchDraw(canvas);
        if (mHeaderView != null) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }

    /*
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mHeaderView != null) {
            drawChild(canvas, mHeaderView, getDrawingTime());
        }
    }
    */

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //主要作用是：点击最顶部的view还能折叠group
        //
        int x = (int) ev.getX();
        int y = (int) ev.getY();

        int pos = pointToPosition(x, y);

        if (mHeaderView != null && y >= mHeaderView.getTop() && y <= mHeaderView.getBottom()) {

            if (ev.getAction() == MotionEvent.ACTION_DOWN) {

                mTouchTarget = getTouchTarget(mHeaderView, x, y);
                mActionDownHappened = true;
            } else if (ev.getAction() == MotionEvent.ACTION_UP) {

                View touchTarget = getTouchTarget(mHeaderView, x, y);

                if (touchTarget == mTouchTarget && mTouchTarget.isClickable()) {
                    mTouchTarget.performClick();
                    invalidate(new Rect(0, 0, mHeaderWidth, mHeaderHeight));
                } else if (mIsHeaderGroupClickable) {
                    int groupPosition = getPackedPositionGroup(getExpandableListPosition(pos));
                    if (groupPosition != INVALID_POSITION && mActionDownHappened) {
                        if (isGroupExpanded(groupPosition)) {
                            collapseGroup(groupPosition);
                        } else {
                            expandGroup(groupPosition);
                        }
                    }
                }
                mActionDownHappened = false;
            }
            return true;
        }


        return super.dispatchTouchEvent(ev);
    }

    /**
     * 获得被点击的view.如果是viewgroup，那么寻找viewgroup中的被点击view
     *
     * @param view 被查找的空间viewgroup
     * @param x    点击的x坐标
     * @param y    点击的y坐标
     * @return
     */
    public View getTouchTarget(View view, int x, int y) {
        if (!(view instanceof ViewGroup)) {
            return view;
        }

        ViewGroup parent = (ViewGroup) view;
        int childrenCount = parent.getChildCount();
        final boolean customOrder = isChildrenDrawingOrderEnabled();
        View target = null;

        for (int i = childrenCount - 1; i >= 0; i--) {
            final int childIndex = customOrder ? getChildDrawingOrder(childrenCount, i) : i;
            final View child = parent.getChildAt(childIndex);
            if (isTouchPointInView(child, x, y)) {
                target = child;
                break;
            }
        }
        if (target == null) {
            target = parent;
        }
        return target;
    }

    private boolean isTouchPointInView(View view, int x, int y) {
        if (view.isClickable() && y >= view.getTop() && y <= view.getBottom() && x >= view.getLeft() && x <= view.getRight()) {
            return true;
        }
        return false;
    }

    public void requestRefreshHeader() {
        refreshHeader();
        invalidate(new Rect(0, 0, mHeaderWidth, mHeaderHeight));
    }

    private void refreshHeader() {
        if (mHeaderView == null) {
            return;
        }

        int firstVisiblePos = getFirstVisiblePosition();
        int pos = firstVisiblePos + 1;
        int firstVisibleGroupPos = getPackedPositionGroup(getExpandableListPosition(firstVisiblePos));
        int group = getPackedPositionGroup(getExpandableListPosition(pos));

        if (BuildConfig.DEBUG) {
            Log.e(TAG, "refreshHeader firstVisibleGroupPos=" + firstVisibleGroupPos);
        }

        if (group == firstVisibleGroupPos + 1) {
            View view = getChildAt(1);
            if (view == null) {
                Log.w(TAG, "Warning : refreshHeader getChildAt(1)=null");
                return;
            }
            if (view.getTop() <= mHeaderHeight) {
                int delta = mHeaderHeight - view.getTop();
                mHeaderView.layout(0, -delta, mHeaderWidth, mHeaderHeight - delta);
            } else {
                mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
            }
        } else {
            mHeaderView.layout(0, 0, mHeaderWidth, mHeaderHeight);
        }

        if (mHeaderUpdateListener != null) {
            mHeaderUpdateListener.updatePinnedHeader(mHeaderView, firstVisibleGroupPos);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(mScrollerListener!=null){
            mScrollerListener.onScrollStateChanged(view,scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if(totalItemCount > 0 ){
            refreshHeader();
        }
        if(mScrollerListener != null){
            mScrollerListener.onScroll(view,firstVisibleItem,visibleItemCount,totalItemCount);
        }
    }



}
