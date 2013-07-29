package com.wapplix.adapters;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Michaël André
 */
public class JSONExpandableAdapter extends DataExpandableAdapter<JSONArray, JSONObject, JSONObject> {

    private String mChildrenKey;
    
    public JSONExpandableAdapter(Context context, JSONArray data, String childrenKey, int groupLayout, DataViewBinder<JSONObject> groupViewBinder, int rowLayout, DataViewBinder<JSONObject> childViewBinder) {
        super(context, data, groupLayout, groupViewBinder, rowLayout, childViewBinder);
        this.mChildrenKey = childrenKey;
    }
    public JSONExpandableAdapter(Context context, JSONArray data, String childrenKey, int groupLayout, String groupKey, int groupViewId, int rowLayout, JSONViewBinder childViewBinder) {
        this(context, data, childrenKey, groupLayout, new JSONViewBinder(groupKey, groupViewId), rowLayout, childViewBinder);
    }
    public JSONExpandableAdapter(Context context, JSONArray data, String childrenKey, int groupLayout, String groupKey, int groupViewId, int rowLayout, String[] rowFrom, int[] rowTo) {
        this(context, data, childrenKey, groupLayout, new JSONViewBinder(groupKey, groupViewId), rowLayout, new JSONViewBinder(rowFrom, rowTo));
    }
    public JSONExpandableAdapter(Context context, JSONArray data, String childrenKey, int groupLayout, String groupKey, int groupViewId, int rowLayout, String rowFrom, int rowTo) {
        this(context, data, childrenKey, groupLayout, new JSONViewBinder(groupKey, groupViewId), rowLayout, new JSONViewBinder(rowFrom, rowTo));
    }
    
    public int getGroupCount() {
        return (getData() != null) ? getData().length() : 0;
    }

    public int getChildrenCount(int groupPosition) {
        JSONArray children = getData().optJSONObject(groupPosition).optJSONArray(mChildrenKey);
        return (children != null) ? children.length() : 0;
    }

    public JSONObject getGroupData(int groupPosition) {
        return getData().optJSONObject(groupPosition);
    }

    public JSONObject getChildData(int groupPosition, int childPosition) {
        return getData().optJSONObject(groupPosition).optJSONArray(mChildrenKey).optJSONObject(childPosition);
    }

    @Override
    public JSONArray filterData(JSONArray data, String constraint) {
        JSONArray d = new JSONArray();
        for (int i = 0; i < data.length(); i++) {
            JSONObject groupData = data.optJSONObject(i);
            if (getGroupString(groupData).toLowerCase().contains(constraint)) {
                d.put(groupData);
            } else {
                JSONArray children = groupData.optJSONArray(mChildrenKey);
                JSONArray c = new JSONArray();
                for (int j = 0; j < children.length(); j++) {
                    if (getChildString(children.optJSONObject(j)).toLowerCase().contains(constraint)) {
                        c.put(children.optJSONObject(j));
                    }
                }
                if (c.length() > 0) {
                    try {
                        JSONObject g = new JSONObject(groupData.toString());
                        g.put(mChildrenKey, c);
                        d.put(g);
                    } catch (JSONException ex) {
                        return new JSONArray();
                    }
                }
            }
        }
        return d;
    }
    
    public String getGroupString(JSONObject groupData) {
        return groupData.toString();
    }

    public String getChildString(JSONObject childData) {
        return childData.toString();
    }
}