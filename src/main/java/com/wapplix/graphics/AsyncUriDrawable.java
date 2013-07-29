package com.wapplix.graphics;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;

import java.io.IOException;

/**
 * Created by Michaël André on 19/06/13.
 */
public class AsyncUriDrawable extends AsyncDrawable {

    private Context mContext;
    private Uri mUri;

    public AsyncUriDrawable(Drawable placeholder, Context context, Uri uri) {
        super(placeholder);
        this.mContext = context;
        this.mUri = uri;
    }

    public AsyncUriDrawable(Context context, Uri uri) {
        this.mContext = context;
        this.mUri = uri;
    }

    public BitmapDrawable createBitmapDrawable(Context context, Uri uri, int width, int height) throws IOException {
        return new BitmapDrawable(mContext.getResources(), BitmapHelper.getScaledBitmapFillCrop(context, uri, width, height));
    }

    @Override
    public Drawable getBoundedDrawable(int width, int height) {
        try {
            return createBitmapDrawable(mContext, mUri, width, height);
        } catch (IOException e) {
            Log.e("AsyncUriDrawable", "Failed to create thumbnail of " + mUri, e);
        }
        return null;
    }
}