/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wapplix;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;

import java.util.ArrayList;

/**
 *
 * @author Mike
 */
public class MenuViewAdapter extends BaseAdapter implements ListAdapter {

    public MenuViewAdapter(Context context) {
        this.context = context;
    }
    
    public void addMenuItem(int titleId, int iconId, Class<? extends Activity> activity) {
        MenuDefinition md = new MenuDefinition();
        md.titleId = titleId;
        md.iconId = iconId;
        md.activity = activity;
        items.add(md);
        notifyDataSetChanged();
    }

    private Context context;
    private ArrayList<MenuDefinition> items = new ArrayList<MenuDefinition>();
    
    private class MenuDefinition {
        int titleId;
        int iconId;
        Class<? extends Activity> activity;
    }
        
    public int getCount() {
        return items.size();
    }

    public Object getItem(int index) {
        return items.get(index);
    }

    public long getItemId(int index) {
        return index;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = new Button(context);
        }
        final MenuDefinition md = (MenuDefinition) getItem(position);
        Button btn = (Button) convertView;
        btn.setText(md.titleId);
        btn.setCompoundDrawablesWithIntrinsicBounds(0, md.iconId, 0, 0);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View sender) {
                context.startActivity(new Intent(context, md.activity));
            }
        });
        return btn;
    }

}