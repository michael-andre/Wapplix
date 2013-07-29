package com.wapplix.data;

import android.sax.RootElement;
import android.util.Xml;
import android.util.Xml.Encoding;

import com.wapplix.data.xml.NodeBinder;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 *
 * @author Michaël André
 */
public abstract class XMLEndpoint extends Endpoint<JSONObject> {

    private Xml.Encoding mEncoding;

    public XMLEndpoint(String urlBase, String method, Map<String, String> parameters, Encoding encoding) {
        super(urlBase, method, parameters);
        this.mEncoding = encoding;
    }

    @Override
    public HttpURLConnection openConnection() throws Exception {
    	HttpURLConnection connection = super.openConnection();
        connection.setRequestProperty("Accept", "text/xml, application/xml");
        return connection;
    }  
        
    protected abstract NodeBinder getRootNodeAdapter();
    
    @Override
    protected JSONObject getResponseData(InputStream inputStream) throws Exception {
        NodeBinder rootAdapter = getRootNodeAdapter();
        Xml.parse(inputStream, mEncoding, ((RootElement) rootAdapter.getElement()).getContentHandler());
        return rootAdapter.getData();
    }

}