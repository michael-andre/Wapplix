package com.wapplix.adapters;

import android.view.View;

/**
 *
 * @author Michaël André
 */
public class StringViewBinder extends DataViewBinder<String> {

    private int mTextViewId;

    public StringViewBinder(int textViewId) {
        this.mTextViewId = textViewId;
    }
    
    @Override
    public void setViewValue(View view, String data) {
        setViewText(view, mTextViewId, data);
    }
}