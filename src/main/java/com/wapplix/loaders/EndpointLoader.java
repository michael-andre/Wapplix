package com.wapplix.loaders;

import android.content.Context;

import com.wapplix.data.Endpoint;

import java.net.HttpURLConnection;

/**
 *
 * @author Michaël André
 */
public class EndpointLoader<TData> extends ConnectionLoader<TData> {

    private Endpoint<TData> mEndpoint;
    
    public EndpointLoader(Context context, Endpoint<TData> endpoint) {
        super(context);
        this.mEndpoint = endpoint;
    }

    @Override
    protected HttpURLConnection getConnection() throws Exception {
        return mEndpoint.openConnection();
    }

    @Override
    public TData getResponseData(HttpURLConnection connection) throws Exception {
        return mEndpoint.readResponse(connection);
    }
    
}