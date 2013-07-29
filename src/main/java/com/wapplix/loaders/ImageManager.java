package com.wapplix.loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.content.Loader;
import android.support.v4.content.Loader.OnLoadCompleteListener;
import android.widget.ImageView;

import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Michaël André <michael.andre@live.fr>
 */
public class ImageManager { 
    
    private Context context;
    private HashMap<URL, ImageViewLoader> loaders = new HashMap<URL, ImageViewLoader>();

    private boolean allowStale = false;

    public void setAllowStale(boolean allowStale) {
        this.allowStale = allowStale;
    }
    
    public ImageManager(Context context) {
        this.context = context;
    }
    
    public void setImage(ImageView view, URL url) {

        if (view.getTag() == url) {
            return;
        }
        view.setImageBitmap(null);
        
        ImageViewLoader loader;
        if (view.getTag() instanceof URL) {
            loader = loaders.get((URL) view.getTag());
            if (loader != null) {
                loader.removeView(view);
            }
        }
        
        loader = loaders.get(url);
        if (loader == null) {
            loader = new ImageViewLoader(context, url);
            loader.setAllowStale(allowStale);
            loader.registerListener(0, loader);
            loaders.put(url, loader);
        }
        loader.addView(view);
        view.setTag(url);
        loader.startLoading();
    }

    private static class ImageViewLoader extends BitmapLoader implements OnLoadCompleteListener<Bitmap> {
        
        private Set<ImageView> views = new HashSet<ImageView>();

        public ImageViewLoader(Context context, URL url) {
            super(context, url);
        }
        
        public void onLoadComplete(Loader<Bitmap> loader, Bitmap data) {
            for (ImageView view : views) {
                view.setImageBitmap(data);
            }
        }
        
        public void addView(ImageView view) { views.add(view); }
        
        public void removeView(ImageView view) {
            views.remove(view);
            if (views.isEmpty()) {
                this.cancelLoad();
            }
        }
        
    }
        
}