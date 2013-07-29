package com.wapplix.adapters;

import android.view.View;

import org.json.JSONObject;

/**
 *
 * @author Michaël André
 */
public class JSONViewBinder extends DataViewBinder<JSONObject> {
    
    private String[] mDataKeys;
    private int[] mViewIds;
    
    public JSONViewBinder() { }

    public JSONViewBinder(String[] dataKeys, int[] viewIds) {
        this.mDataKeys = dataKeys;
        this.mViewIds = viewIds;
    }

    public JSONViewBinder(String dataKey, int viewId) {
        this(new String[] { dataKey }, new int[] { viewId });
    }

    @Override
    public void setViewValue(View view, JSONObject data) {
        if (mDataKeys == null || mViewIds == null) { return; }
        for (int i = 0; i < Math.max(mDataKeys.length, mViewIds.length); i++) {
            setViewText(view, mViewIds[i], data.optString(mDataKeys[i], null));
        }
    }
}