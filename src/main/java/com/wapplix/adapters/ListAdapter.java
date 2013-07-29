package com.wapplix.adapters;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Michaël André
 */
public class ListAdapter<T> extends DataAdapter<List<T>, T> {

    public ListAdapter(Context context, List<T> data, int itemLayout, DataViewBinder<T> itemMapper) {
        super(context, data, itemLayout, itemMapper);
    }   
    
    @Override
    public T getItemData(int position) {
        return getData().get(position);
    }

    @Override
    public List<T> filterData(List<T> data, String constraint) {
        ArrayList<T> d = new ArrayList();
        for (int i = 0; i < data.size(); i++) {
            if (getItemString(data.get(i)).toLowerCase().contains(constraint)) {
                d.add(data.get(i));
            }
        }
        return d;
    }

    public int getCount() {
        List<T> data = getData();
        return data != null ? data.size() : 0;
    }

    public long getItemId(int position) {
        return position;
    }

    public String getItemString(T item) {
        return item.toString();
    }
}