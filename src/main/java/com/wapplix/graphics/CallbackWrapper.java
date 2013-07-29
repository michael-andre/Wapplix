package com.wapplix.graphics;

import android.graphics.drawable.Drawable;

/**
 * Created by Michaël André on 19/06/13.
 */
public class CallbackWrapper implements Drawable.Callback {

    Drawable.Callback mCallback;

    public CallbackWrapper(Drawable.Callback mCallback) {
        this.mCallback = mCallback;
    }

    @Override
    public void invalidateDrawable(Drawable who) {
        mCallback.invalidateDrawable(who);
    }

    @Override
    public void scheduleDrawable(Drawable who, Runnable what, long when) {
        mCallback.scheduleDrawable(who, what, when);
    }

    @Override
    public void unscheduleDrawable(Drawable who, Runnable what) {
        mCallback.unscheduleDrawable(who, what);
    }
}
