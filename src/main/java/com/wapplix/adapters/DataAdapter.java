package com.wapplix.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;

/**
 *
 * @author Michaël André
 */
public abstract class DataAdapter<TData, TItemData> extends BaseAdapter implements Filterable {

    private Context mContext;
    private TData mData;
    private int mItemLayout;
    private DataViewBinder<TItemData> mViewBinder;
    
    public DataAdapter(Context context, TData data, int itemLayout, DataViewBinder<TItemData> viewBinder) {
        this.mContext = context;
        this.mData = data;
        this.mItemLayout = itemLayout;
        this.mViewBinder = viewBinder;
    }
    
    public Object getItem(int position) {
        return getItemData(position);
    }
    public abstract TItemData getItemData(int position);

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(mItemLayout, parent, false);
        }
        mViewBinder.setViewValue(convertView, getItemData(position), parent, position);
        return convertView;
    }
    
    public TData getData() {
        return mData;
    }
    
    public void setData(TData data) {
        boolean changed = data != this.mData;
        this.mData = data;
        if (changed) notifyDataSetChanged();
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
                        TData filteredData = filterData(mOriginalData, constraint.toString().toLowerCase());
                        results.values = filteredData;
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
