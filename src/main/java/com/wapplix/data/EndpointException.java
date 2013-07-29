package com.wapplix.data;

import com.wapplix.StreamHelper;

import java.io.IOException;
import java.io.InputStream;

public class EndpointException extends Exception {

	private static final long serialVersionUID = 7157628972182455120L;
	
	private String responseString;
	private int statusCode;
	
	public EndpointException(int statusCode, Throwable throwable) {
        super("Endpoint error " + statusCode, throwable);
		this.statusCode = statusCode;
    }

	public EndpointException(int statusCode, String responseString, Throwable throwable) {
        this(statusCode, throwable);
        this.responseString = responseString;
    }
	
	public EndpointException(int statusCode, InputStream errorStream, Throwable throwable) throws IOException {
        this(statusCode, throwable);
        this.responseString = StreamHelper.readStringContent(errorStream);
    }
	
	public String getResponseString() {
		return responseString;
	}
	
	public int getStatusCode() {
		return statusCode;
	}

}