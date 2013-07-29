package com.wapplix.data;

import android.text.TextUtils;

import com.wapplix.StreamHelper;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 *
 * @author Michaël André
 */
public class JSONObjectEndpoint extends Endpoint<JSONObject> {

    private String responseErrorKey;

    public void setResponseErrorKey(String errorKey) {
        this.responseErrorKey = errorKey;
    }

    public JSONObjectEndpoint(String urlBase, String method, Map<String, String> parameters) {
        super(urlBase, method, parameters);
    }
    
    @Override
    public HttpURLConnection openConnection() throws Exception {
    	HttpURLConnection connection = super.openConnection();
        connection.setRequestProperty("Accept", "application/json");
        return connection;
    }
        
    @Override
    protected JSONObject getResponseData(InputStream inputStream) throws Exception {
        String string = StreamHelper.readStringContent(inputStream);
        if (TextUtils.isEmpty(string)) return null;
        JSONObject data = new JSONObject(string);
        if (responseErrorKey != null && data.has(responseErrorKey)) {
            throw new JSONEndpointException(-1, data, null);
        }
        return data;
    }

    @Override
    protected EndpointException getResponseException(int statusCode, InputStream errorStream, IOException cause) throws Exception {
        return new JSONEndpointException(statusCode, errorStream, cause);
    }
    
    public static class JSONEndpointException extends EndpointException {
        
		/**
		 * 
		 */
		private static final long serialVersionUID = 6879622758360757130L;

		JSONObject errorData;

		public JSONEndpointException(int statusCode, InputStream errorStream, Throwable throwable) throws Exception {
			super(statusCode, errorStream, throwable);
			this.errorData = new JSONObject(getResponseString());
		}
		
		public JSONEndpointException(int statusCode, JSONObject errorData, Throwable throwable) throws Exception {
			super(statusCode, throwable);
			this.errorData = errorData;
		}		
		
        public JSONObject getErrorData() {
            return errorData;
        } 
                
    }
    
}
