package com.wapplix.data;

import android.text.TextUtils;

import com.wapplix.StreamHelper;
import com.wapplix.data.JSONObjectEndpoint.JSONEndpointException;

import org.json.JSONArray;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 *
 * @author Michaël André
 */
public class JSONArrayEndpoint extends Endpoint<JSONArray> {

    public JSONArrayEndpoint(String urlBase, String method, Map<String, String> parameters) {
        super(urlBase, method, parameters);
    }

    @Override
    public HttpURLConnection openConnection() throws Exception {
    	HttpURLConnection connection = super.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        return connection;
    }
        
    @Override
    protected JSONArray getResponseData(InputStream inputStream) throws Exception {
        String string = StreamHelper.readStringContent(inputStream);
        if (TextUtils.isEmpty(string)) return null;
        JSONArray data = new JSONArray(string);
        return data;
    }

    @Override
    protected EndpointException getResponseException(int statusCode, InputStream errorStream, IOException cause) throws Exception {
        return new JSONEndpointException(statusCode, errorStream, cause);
    }

}
