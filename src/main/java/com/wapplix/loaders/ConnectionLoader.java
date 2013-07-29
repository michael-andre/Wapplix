package com.wapplix.loaders;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.wapplix.data.CacheManager;

import java.io.FileNotFoundException;
import java.net.HttpURLConnection;

/**
 *
 * @author Michaël André
 */
public abstract class ConnectionLoader<TData> extends AsyncTaskLoader<TData> {

    private TData result;
    
    public ConnectionLoader(Context context) {
        super(context);
    }
    
    private boolean useCache = false;
    public void setUseCaches(boolean useCaches) {
        this.useCache = useCaches;
    }
    
    private boolean allowStale = false;
    public void setAllowStale(boolean allowStale) {
        this.allowStale = allowStale;
    }
    
    private boolean forceRefresh = false;
    public void setForceRefresh(boolean forceRefresh) {
        this.forceRefresh = forceRefresh;
    }

    @Override
    public void deliverResult(TData result) {
        if (isReset()) {
            // An async query came in while the loader is stopped
            return;
        }
        this.result = result;
        super.deliverResult(result);
        if (allowStale) {
            allowStale = false;
            forceLoad();
        }
    }

    @Override
    protected void onStartLoading() {
        if (useCache) {
            CacheManager.installHttpCache(this.getContext(), 10 * 1024 * 1024);
        }
        if (result != null) {
            deliverResult(result);
        }
        if (takeContentChanged() || result == null) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
         // Attempt to cancel the current load task if possible.
        cancelLoad();
        if (useCache) {
            CacheManager.flushHttpCache();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();
        // Ensure the loader is stopped
        onStopLoading();
        result = null;
    }

    protected abstract HttpURLConnection getConnection() throws Exception;
    public abstract TData getResponseData(HttpURLConnection connection) throws Exception;
    
    @Override
    public TData loadInBackground() {
        HttpURLConnection connection = null;
        try {
            connection = getConnection();
            
            connection.setUseCaches(useCache);
            if (forceRefresh) {
                connection.setRequestProperty("Cache-Control", "max-age=0");
            } else if (allowStale) {
                connection.setRequestProperty("Cache-Control", "max-stale=" + 3600 * 24 * 365);
            }
            TData data = getResponseData(connection);
            allowStale = "110 HttpURLConnection \"Response is stale\"".equals(connection.getHeaderField("Warning"));
            Log.i("DataLoader", (allowStale ? "Loaded stale data" : "Loaded data") + " for " + connection.getURL().toString());
            return data;
        } catch (FileNotFoundException ex) {
            if (allowStale) {
                Log.i("DataLoader", "No stale data for " + connection.getURL().toString());
                allowStale = false;
                return loadInBackground();
            }
            Log.e("DataLoader", "Failed to load data for " + connection.getURL().toString(), ex);
        } catch (Exception ex) {
            Log.e("DataLoader", "Failed to load data for " + connection.getURL().toString(), ex);
        }
        return null;
    }
    
}