package com.wapplix.adapters;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Michaël André
 */
public class JSONAdapter extends DataAdapter<JSONArray, JSONObject> {

    public JSONAdapter(Context context, JSONArray data, int itemLayout, DataViewBinder<JSONObject> viewBinder) {
        super(context, data, itemLayout, viewBinder);
    }
    public JSONAdapter(Context context, JSONArray data, int itemLayout, String[] from, int[] to) {
        this(context, data, itemLayout, new JSONViewBinder(from, to));
    }
    public JSONAdapter(Context context, JSONArray data, int itemLayout, String from, int to) {
        this(context, data, itemLayout, new JSONViewBinder(from, to));
    }
   
    public int getCount() {
        return (getData() != null) ? getData().length() : 0;
    }

    public JSONObject getItemData(int position) {
        return getData().optJSONObject(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public JSONArray filterData(JSONArray data, String constraint) {
        JSONArray d = new JSONArray();
        for (int i = 0; i < data.length(); i++) {
            if (getItemString(data.optJSONObject(i)).toLowerCase().contains(constraint)) {
                d.put(data.optJSONObject(i));
            }
        }
        return d;
    }
    
    public String getItemString(JSONObject item) {
        return item.toString();
    }

}