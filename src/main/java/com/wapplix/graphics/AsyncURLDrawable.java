package com.wapplix.graphics;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.util.LruCache;
import android.util.Log;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Michaël André on 19/06/13.
 */
public class AsyncURLDrawable extends AsyncDrawable {

    private static LruCache<URL, BitmapDrawable> cache;

    static {
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;
        cache = new LruCache<URL, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(URL key, BitmapDrawable drawable) {
                // The cache size will be measured in kilobytes rather than number of items.
                return drawable.getBitmap().getRowBytes() * drawable.getBitmap().getHeight() / 1024;
            }
        };
    }

    private Resources mResources;
    private String mUrl;

    public AsyncURLDrawable(Drawable placeholder, Resources resources, String url) {
        super(placeholder);
        this.mResources = resources;
        this.mUrl = url;
    }

    public AsyncURLDrawable(Resources resources, String url) {
        this.mResources = resources;
        this.mUrl = url;
    }

    public String getUrl(int width, int height) {
        return mUrl;
    }

    public BitmapDrawable createBitmapDrawable(Resources Resources, Bitmap rawBitmap, int width, int height) {
        return new BitmapDrawable(mResources, BitmapHelper.getScaledBitmapFillCrop(rawBitmap, width, height));
    }

    @Override
    public Drawable getBoundedDrawable(int width, int height) {
        try {
            URL url = new URL(getUrl(width, height));
            BitmapDrawable drawable = cache.get(url);
            if (drawable == null) {
                URLConnection connection = url.openConnection();
                Bitmap raw = BitmapFactory.decodeStream(connection.getInputStream());
                drawable = createBitmapDrawable(mResources, raw, width, height);
                if (raw != null && drawable.getBitmap() != null) {
                    cache.put(url, drawable);
                }
            } else {
                drawable.mutate();
                setDuration(0);
            }
            return drawable;
        } catch (IOException e) {
            Log.e("AsyncURLDrawable", "Failed to create thumbnail of " + mUrl, e);
        }
        return null;
    }
}