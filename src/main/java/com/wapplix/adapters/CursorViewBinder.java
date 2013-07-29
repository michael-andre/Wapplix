package com.wapplix.adapters;

import android.database.Cursor;
import android.view.View;

/**
 *
 * @author Michaël André
 */
public class CursorViewBinder extends DataViewBinder<Cursor> {

    private String[] mDataColumns;
    private int[] mViewIds;
    
    public CursorViewBinder(String[] dataColumns, int[] viewIds) {
        this.mDataColumns = dataColumns;
        this.mViewIds = viewIds;
    }
    public CursorViewBinder(String dataColumn, int viewId) {
        this(new String[] { dataColumn }, new int[] { viewId });
    }
    
    @Override
    public void setViewValue(View view, Cursor data) {
        for (int i = 0; i < Math.max(mDataColumns.length, mViewIds.length); i++) {
            View v = view.findViewById(mViewIds[i]);
            setViewText(v, data.getString(data.getColumnIndex(mDataColumns[i])));
        }
    }
    
}
