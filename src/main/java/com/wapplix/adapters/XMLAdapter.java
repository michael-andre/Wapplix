/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wapplix.adapters;

import android.content.Context;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michaël André
 */
public class XMLAdapter extends DataAdapter<NodeList, Element> {
    
    public XMLAdapter(Context context, NodeList data, int itemLayout, DataViewBinder<Element> viewBinder) {
        super(context, data, itemLayout, viewBinder);
    }
    public XMLAdapter(Context context, NodeList data, int itemLayout, String[] from, int[] to) {
        this(context, data, itemLayout, new XMLViewBinder(from, to));
    }
    public XMLAdapter(Context context, NodeList data, int itemLayout, String from, int to) {
        this(context, data, itemLayout, new XMLViewBinder(from, to));
    }
    
    public int getCount() {
        return (getData() != null) ? getData().getLength() : 0;
    }

    public Element getItemData(int position) {
        return (Element) getData().item(position);
    }

    public long getItemId(int position) {
        return position;
    }

    @Override
    public NodeList filterData(NodeList data, String constraint) {
        return null;
    }
    
    public String getItemString(Node item) {
        return item.toString();
    }
    
}