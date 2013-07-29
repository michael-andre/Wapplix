package com.wapplix.data.xml;

import android.sax.Element;
import android.sax.EndElementListener;
import android.sax.EndTextElementListener;
import android.sax.RootElement;
import android.sax.StartElementListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.Attributes;

/**
 *
 * @author Michaël André
 */
public class NodeBinder {

    private Element mElement;
    private JSONObject mData;

    public NodeBinder(String rootName) {
        this(new RootElement(rootName));
    }
    
    protected NodeBinder(Element element) {
        this.mElement = element;
        this.mData = new JSONObject();
    }

    public JSONObject getData() { return mData; }
    public Element getElement() { return mElement; }
    
    public NodeBinder setChild(String uri, final String name) {
        final NodeBinder childNode = new NodeBinder(mElement.getChild(uri, name));
        mElement.getChild(uri, name).setEndElementListener(new EndElementListener() {
            public void end() {
                try {
                    mData.put(name, childNode.mData);
                } catch (JSONException ex) { }
            }
        });
        return childNode;
    }
    public NodeBinder setChild(final String name) {
        return setChild("", name);
    }
    
    public NodeBinder setChildren(String uri, final String name) {
        final NodeBinder childNode = new NodeBinder(mElement.getChild(uri, name));
        try {
            mData.put(name, new JSONArray());
        } catch (JSONException ex) { }
        mElement.getChild(uri, name).setEndElementListener(new EndElementListener() {
            public void end() {
                try {
                    mData.getJSONArray(name).put(childNode.mData);
                    childNode.mData = new JSONObject();
                } catch (JSONException ex) { }
            }
        });
        return childNode;
    }
    public NodeBinder setChildren(final String name) {
        return setChildren("", name);
    }
    
    public void setTextChild(String uri, final String name) {
        mElement.getChild(uri, name).setEndTextElementListener(new EndTextElementListener() {
            public void end(String text) {
                try {
                    mData.put(name, text);
                } catch (Exception ex) { }
            }
        });
    }
    public void setTextChild(final String name) {
        setTextChild("", name);
    }        
    
    public void setAttributes(final String[] names) {
        mElement.setStartElementListener(new StartElementListener() {
            public void start(Attributes attributes) {
                for (String name : names) {
                    try {
                        mData.put(name, attributes.getValue(name));
                    } catch (Exception ex) {
                    }
                }
            }
        });
    }

}
