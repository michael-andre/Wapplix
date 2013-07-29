package com.wapplix.data;

import android.content.Context;
import android.util.Log;

import com.integralblue.httpresponsecache.HttpResponseCache;

import java.io.File;

/**
 *
 * @author Michaël André
 */
public class CacheManager {

    public static void installHttpCache(Context context, long size) {
        
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir == null) { cacheDir = context.getCacheDir(); }
        if (!cacheDir.exists()) { cacheDir.mkdirs(); }
        
        try {
        	Class<?> cacheClass = Class.forName("android.net.http.HttpResponseCache");
            Object cache = cacheClass.getMethod("getInstalled", (Class<?>[]) null).invoke(null, (Object[]) null);
            if (cache == null) {
            	cacheClass.getMethod("install", File.class, long.class).invoke(null, cacheDir, size);
                Log.d("CacheManager", "android.net.http.HttpResponseCache installed");
            }
        } catch (Exception ex) {
            try {
                if (HttpResponseCache.getInstalled() == null) {
                    HttpResponseCache.install(cacheDir, size);
                    Log.d("CacheManager", "com.integralblue.httpresponsecache.HttpResponseCache installed");
                }
            } catch(Exception e) {
                Log.e("CacheManager", "Failed to install com.integralblue.httpresponsecache.HttpResponseCache", ex);
            }
        }
    }
    
    public static void flushHttpCache() {
        try {
        	Class<?> cacheClass = Class.forName("android.net.http.HttpResponseCache");
            Object cache = cacheClass.getMethod("getInstalled", (Class<?>[]) null).invoke(null, (Object[]) null);
            if (cache != null) {
            	cacheClass.getMethod("flush", (Class<?>[]) null).invoke(cache, (Object[]) null);
                Log.d("CacheManager", "android.net.http.HttpResponseCache flushed");
            }
        } catch (Exception ex) {
            HttpResponseCache cache = HttpResponseCache.getInstalled();
            if (cache != null) {
                cache.flush();
                Log.d("CacheManager", "com.integralblue.httpresponsecache.HttpResponseCache flushed");
            }
        }
    }

}