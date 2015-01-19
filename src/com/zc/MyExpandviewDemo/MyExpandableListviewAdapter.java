package com.zc.MyExpandviewDemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.zc.MyExpandviewDemo.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * 1/19/15  5:05 PM
 * Created by JustinZhang.
 */
public class MyExpandableListviewAdapter extends BaseExpandableListAdapter {
    private Context mContext;
    private Map<String, List<String>> mData;
    private LayoutInflater mLayoutInflater;

    public MyExpandableListviewAdapter(Context mContext, Map<String, List<String>> mData) {
        this.mContext = mContext;
        this.mData = mData;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getGroupCount() {
        return mData.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return Utils.getValueFromMap(mData, groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return Utils.getKeyFromMap(mData, groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Utils.getValueFromMap(mData, groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {

        View view = null;
        GroupViewHolder groupViewHolder = null;

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.group_layout, parent, false);
            groupViewHolder = new GroupViewHolder(view);
            view.setTag(groupViewHolder);
        } else {

            view = convertView;
            groupViewHolder = (GroupViewHolder) view.getTag();

        }
        String s = (String) getGroup(groupPosition);
        groupViewHolder.mTvGrop.setText(s);
        return view;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        View view = null;
        ChildViewHolder vh;

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.child_layout, parent, false);
            vh = new ChildViewHolder(view);
            view.setTag(vh);
        } else {
            view = convertView;
            vh = (ChildViewHolder) view.getTag();
        }

        String data =(String)getChild(groupPosition,childPosition);
        vh.mTvChild.setText(data);
        return view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    static class GroupViewHolder {
        @InjectView(R.id.tv_grop)
        TextView mTvGrop;

        GroupViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    /**
     * This class contains all butterknife-injected Views & Layouts from layout file 'child_layout.xml'
     * for easy to all layout elements.
     *
     * @author ButterKnifeZelezny, plugin for Android Studio by Inmite Developers (http://inmite.github.io)
     */
    static class ChildViewHolder {
        @InjectView(R.id.tv_child)
        TextView mTvChild;

        ChildViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }
}
