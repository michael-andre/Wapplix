package com.wapplix.data;

import android.util.Xml.Encoding;

import com.wapplix.data.xml.NodeBinder;

import java.net.HttpURLConnection;
import java.util.Map;

/**
 *
 * @author Michaël André
 */
public class RSSEndpoint extends XMLEndpoint {

    public RSSEndpoint(String urlBase, String method, Map<String, String> parameters, Encoding encoding) {
        super(urlBase, method, parameters, encoding);
    }
    
    @Override
    public HttpURLConnection openConnection() throws Exception {
    	HttpURLConnection connection = super.openConnection();
        connection.setRequestProperty("Accept", "application/rss+xml");
        return connection;
    }
        
    @Override
    protected NodeBinder getRootNodeAdapter() {
        NodeBinder rss = new NodeBinder("rss");
        NodeBinder channel = rss.setChild("channel");
        channel.setTextChild("title");
        channel.setTextChild("link");
        channel.setTextChild("description");
        NodeBinder item = channel.setChildren("item");
        item.setTextChild("title");
        item.setTextChild("link");
        item.setTextChild("description");
        item.setTextChild("http://purl.org/rss/1.0/modules/content/", "encoded");
        item.setTextChild("category");
        item.setTextChild("pubDate");
        return rss;
    }

}