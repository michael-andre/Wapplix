package com.wapplix.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;

import java.io.InputStream;

/**
 * Created by Michaël André on 19/06/13.
 */
public class TintedBitmapDrawable extends BitmapDrawable {

    int mTintColor;

    public TintedBitmapDrawable(Resources res, int mTintColor) {
        super(res);
        this.mTintColor = mTintColor;
    }

    public TintedBitmapDrawable(Resources res, Bitmap bitmap, int mTintColor) {
        super(res, bitmap);
        this.mTintColor = mTintColor;
    }

    public TintedBitmapDrawable(Resources res, String filepath, int mTintColor) {
        super(res, filepath);
        this.mTintColor = mTintColor;
    }

    public TintedBitmapDrawable(Resources res, InputStream is, int mTintColor) {
        super(res, is);
        this.mTintColor = mTintColor;
    }

    boolean mTinted = false;

    @Override
    public boolean isStateful() {
        return true;
    }

    @Override
    protected boolean onStateChange(int[] state) {
        boolean tint = false;
        for (int s : state) {
            switch (s) {
                case android.R.attr.state_pressed:
                case android.R.attr.state_focused:
                case android.R.attr.state_selected:
                    tint = true;
                    break;
            }
        }
        boolean changed = false;
        if (mTinted != tint) {
            if (tint) setColorFilter(mTintColor, PorterDuff.Mode.SRC_ATOP);
            else clearColorFilter();
            invalidateSelf();
            mTinted = tint;
            changed = true;
        }
        if (super.onStateChange(state)) {
            changed = true;
        }
        return changed;
    }

}