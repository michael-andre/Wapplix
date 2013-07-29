package com.wapplix.adapters;

import android.view.View;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Michaël André
 */
public class XMLViewBinder extends DataViewBinder<Element> {

    private String[] mDataAttributes;
    private int[] mViewIds;

    public XMLViewBinder(String[] dataAttributes, int[] viewIds) {
        this.mDataAttributes = dataAttributes;
        this.mViewIds = viewIds;
    }
    public XMLViewBinder(String dataAttributes, int viewId) {
        this(new String[] { dataAttributes }, new int[] { viewId });
    }
    
    @Override
    public void setViewValue(View view, Element data) {
        for (int i = 0; i < Math.max(mDataAttributes.length, mViewIds.length); i++) {
            View v = view.findViewById(mViewIds[i]);
            String value = data.getAttribute(mDataAttributes[i]);
            if (value.equals("")) {
                Node child = data.getElementsByTagName(mDataAttributes[i]).item(0);
                if (child != null) {
                    value = getNodeText(child);
                }
            }
            setViewText(view, mViewIds[i], value);
        }
    }
    
    private static String getNodeText(Node node) {
        if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) {
            return node.getNodeValue();
        }
        else {
            StringBuilder sb = new StringBuilder();
            NodeList children = node.getChildNodes();
            for (int i = 0; i < children.getLength(); i++) {
                sb.append(getNodeText(children.item(i)));
            }
            return sb.toString();
        }
    }
    
}
