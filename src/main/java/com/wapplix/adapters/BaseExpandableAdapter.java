package com.wapplix.adapters;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

/**
 *
 * @author Michaël André
 */
public abstract class BaseExpandableAdapter extends android.widget.BaseExpandableListAdapter implements ListAdapter {

    public int getCount() {
        int count = this.getGroupCount();
        int l = count;
        for (int i = 0; i < l; i++) {
            count += this.getChildrenCount(i);
        }
        return count;
    }

    public Object getItem(int position) {
        IndexPath ip = this.getIndexPath(position);
        if (ip.childPosition == -1) { return getGroup(ip.groupPosition); }
        else { return getChild(ip.groupPosition, ip.childPosition); }
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        IndexPath ip = this.getIndexPath(position);
        if (ip.childPosition == -1) {
    		return getGroupView(ip.groupPosition, true, convertView, parent);
		} else {
			return getChildView(ip.groupPosition, ip.childPosition, ip.isLastChild, convertView, parent);
		}
    }

    public int getItemViewType(int position) {
        return this.getIndexPath(position).childPosition == -1 ? 0 : 1;
    }

    public int getViewTypeCount() {
    	return 2;
	}
    
    @Override
    public boolean areAllItemsEnabled() {
    	return false;
	}
    
    public boolean isEnabled(int position) {
        return this.getIndexPath(position).childPosition != -1;
    }

    public int getPosition(int groupPosition, int childPosition) {
        int position = groupPosition;
        for (int i = 0; i < groupPosition; i++) {
            position += getChildrenCount(i);
        }
        return position + childPosition;
    }
    
    public int getChildPosition(int position) {
    	return getIndexPath(position).childPosition;
    }
    
    public int getGroupPosition(int position) {
    	return getIndexPath(position).groupPosition;
    }
    
    protected static class IndexPath {
		int groupPosition;
		int childPosition;
		boolean isLastChild;
	}

	protected IndexPath getIndexPath(int position) {
        IndexPath ip = new IndexPath();
        int offset = 0;
        int gc = this.getGroupCount();
        for (int gp = 0; gp < gc; gp++) {
            if (position == offset) {
                ip.childPosition = -1;
                ip.groupPosition = gp;
                return ip;
            } else {
                offset += 1;
            }
            int cc = this.getChildrenCount(gp);
            if (position < offset + cc) {
                ip.childPosition = position - offset;
                ip.isLastChild = ip.childPosition == cc - 1;
                ip.groupPosition = gp;
                return ip;
            } else {
                offset += cc;
            }
        }
        return null;
    }
}
