package com.wapplix.loaders;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author Michaël André
 */
public class BitmapLoader extends ConnectionLoader<Bitmap> {

    private URL url;
    
    public BitmapLoader(Context context, URL url) {
        super(context);
        this.url = url;
    }

    @Override
    protected HttpURLConnection getConnection() throws Exception {
        return (HttpURLConnection) url.openConnection();
    }

    @Override
    public Bitmap getResponseData(HttpURLConnection connection) throws Exception {
        try {
            return BitmapFactory.decodeStream(connection.getInputStream());
        } finally {
            connection.disconnect();
        }
    }
    
}