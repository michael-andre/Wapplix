package com.wapplix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 *
 * @author Michaël André
 */
public abstract class DataExpandableAdapter<TData, TGroupData, TChildData> extends BaseExpandableAdapter implements ExpandableListAdapter, Filterable {

    private Context mContext;
    private TData mData;
    private int mGroupLayout;
    private DataViewBinder<TGroupData> mGroupViewBinder;
    private int mChildLayout;
    private DataViewBinder<TChildData> mChildViewBinder;
    
    public DataExpandableAdapter(Context context, TData data, int groupLayout, DataViewBinder<TGroupData> groupViewBinder, int rowLayout, DataViewBinder<TChildData> childViewBinder) {
        this.mContext = context;
        this.mData = data;
        this.mGroupLayout = groupLayout;
        this.mGroupViewBinder = groupViewBinder;
        this.mChildLayout = rowLayout;
        this.mChildViewBinder = childViewBinder;
    }
    
    @Override
    public Object getGroup(int position) {
        return getGroupData(position);
    }
    public abstract TGroupData getGroupData(int position);
    
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return getChildData(groupPosition, childPosition);
    }
    public abstract TChildData getChildData(int groupPosition, int childPosition);
    
    public TChildData getChildData(int position) {
        IndexPath ip = getIndexPath(position);
        return getChildData(ip.groupPosition, ip.childPosition);
    }
    
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public boolean hasStableIds() {
        return false;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = onCreateGroupView(groupPosition, parent);
        }
        mGroupViewBinder.setViewValue(convertView, getGroupData(groupPosition), parent, groupPosition, isExpanded);
        return convertView;
    }

    protected View onCreateGroupView(int groupPosition, ViewGroup parent) {
        return ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(mGroupLayout, parent, false);
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = onCreateChildView(groupPosition, childPosition, parent);
        }
        mChildViewBinder.setViewValue(convertView, getChildData(groupPosition, childPosition), parent, groupPosition, childPosition);
        return convertView;
    }

    protected View onCreateChildView(int groupPosition, int childPosition, ViewGroup parent) {
        return ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(mChildLayout, parent, false);
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public TData getData() {
        return mData;
    }    
    
    public void setData(TData data) {
        this.mData = data;
        notifyDataSetChanged();
    }
    
    public abstract TData filterData(TData data, String constraint);

    private Filter mFilter = null;

    @Override
    public Filter getFilter() {
        if (mFilter == null) {
            mFilter = new Filter() {

                private final TData mOriginalData = getData();

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();
                    if (constraint == null) {
                        results.values = mOriginalData;
                    } else {
                        results.values = filterData(mOriginalData, constraint.toString().toLowerCase());
                    }
                    return results;
                }

				@Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    setData((TData) results.values);
                }
            };
        }
        return mFilter;
    }
}