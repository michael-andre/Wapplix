package com.wapplix.data;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.util.Log;

import java.net.HttpURLConnection;

/**
 *
 * @author Michaël André
 */
public class EndpointTask<TResponse> extends AsyncTask<Void, Void, TResponse> {

    public static interface Listener<TResponse> {
        public void onConnectionFinish(Endpoint<TResponse> endpoint, TResponse data, Exception exception);
    }

    public EndpointTask(Endpoint<TResponse> endpoint, Listener<TResponse> listener) {
        this.endpoint = endpoint;
        this.listener = listener;
    }

    Endpoint<TResponse> endpoint;
    Listener<TResponse> listener;
    ProgressDialog mProgressDialog;

    Exception exception;

    public final void execute() {
    	execute((Void[]) null);
    }

    public void setProgressDialog(Context context, String title, String message, boolean cancelable) {
        mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle(title);
        mProgressDialog.setMessage(message);
        mProgressDialog.setCancelable(cancelable);
        if (cancelable) {
            mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    cancel(true);
                }
            });
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (mProgressDialog != null) {
            mProgressDialog.show();
        }
    }

    @Override
    protected TResponse doInBackground(Void... params) {
        try {
            HttpURLConnection connection = endpoint.openConnection();
            Log.i("EndpointTask", "Loading " + connection.getURL().toString());
            return endpoint.readResponse(connection);
        } catch (Exception ex) {
            Log.e("EndpointTask", "Endpoint failed", ex);
            exception = ex;
        }
        return null;
    }

    @Override
    protected void onPostExecute(TResponse result) {
        if (listener != null) {
            listener.onConnectionFinish(endpoint, result, exception);
        }
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }
    
    @Override
    protected void onCancelled(TResponse result) {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    	if (listener != null) {
            listener.onConnectionFinish(endpoint, null, exception);
        }
    }

}
