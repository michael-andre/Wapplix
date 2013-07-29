package com.wapplix;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;

public class FragmentTabListener<T extends Fragment> implements ActionBar.TabListener {
	
    private T fragment;
    private final Context context;
    private final String tag;
    private final Class<T> fragmentClass;
    
    public static <TFragment extends Fragment> FragmentTabListener<TFragment> getInstance(Context context, String tag, Class<TFragment> fragmentClass) {
    	return new FragmentTabListener<TFragment>(context, tag, fragmentClass);
    }

    public FragmentTabListener(Context context, String tag, Class<T> fragmentClass) {
        this.context = context;
        this.tag = tag;
        this.fragmentClass = fragmentClass;
    }

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // Check if the fragment is already initialized
        if (fragment == null) {
            // If not, instantiate and add it to the activity
            fragment = (T) Fragment.instantiate(context, fragmentClass.getName());
            onFragmentCreated(fragment);
            ft.add(android.R.id.content, fragment, tag);
        } else {
            // If it exists, simply attach it in order to show it
            ft.attach(fragment);
        }
    }
    
    protected void onFragmentCreated(T fragment) { }

    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        if (fragment != null) {
            // Detach the fragment, because another one is being attached
            ft.detach(fragment);
        }
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}