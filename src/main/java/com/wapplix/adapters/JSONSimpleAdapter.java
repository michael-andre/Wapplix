package com.wapplix.adapters;

import android.content.Context;

import org.json.JSONArray;

/**
 *
 * @author Michaël André
 */
public class JSONSimpleAdapter extends DataAdapter<JSONArray, String> {

    public JSONSimpleAdapter(Context context, JSONArray data, int itemLayout, int textViewId) {
        super(context, data, itemLayout, new StringViewBinder(textViewId));
    }
    public JSONSimpleAdapter(Context context, JSONArray data) {
        this(context, data, android.R.layout.simple_list_item_1, android.R.id.text1);
    }
    
    @Override
    public String getItemData(int position) {
        return getData().optString(position);
    }

    @Override
    public JSONArray filterData(JSONArray data, String constraint) {
        JSONArray d = new JSONArray();
        for (int i = 0; i < data.length(); i++) {
            if (data.optString(i).toLowerCase().contains(constraint)) {
                d.put(data.optString(i));
            }
        }
        return d;
    }

    public int getCount() {
        return (getData() != null) ? getData().length() : 0;
    }

    public long getItemId(int position) {
        return position;
    }

}
