package com.wapplix.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 *
 * @author Michaël André
 */
public class CursorAdapter extends android.widget.CursorAdapter {

    private final int mItemLayout;
    private final DataViewBinder<Cursor> mViewBinder;
    
    public CursorAdapter(Context context, Cursor cursor, int itemLayout, DataViewBinder<Cursor> viewBinder) {
        super(context, cursor, false);
        this.mItemLayout = itemLayout;
        this.mViewBinder = viewBinder;
    }
    public CursorAdapter(Context context, Cursor cursor, int itemLayout, String[] from, int[] to) {
        this(context, cursor, itemLayout, new CursorViewBinder(from, to));
    }
    public CursorAdapter(Context context, Cursor cursor, int itemLayout, String from, int to) {
        this(context, cursor, itemLayout, new CursorViewBinder(from, to));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(mItemLayout, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        mViewBinder.setViewValue(view, cursor);
    }
    
}
