/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.wapplix;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 *
 * @author Michaël André <michael.andre@live.fr>
 */
public class DialogHelper {
    
    public static void showDialog(final Dialog dialog, final FragmentManager manager, final String tag, boolean cancellable) {
        final DialogFragment fragment = new DialogFragment() {

            @Override
            public Dialog onCreateDialog(Bundle savedInstanceState) {
                return dialog;
            }
            
        };
        fragment.setCancelable(cancellable);
        new Handler() {
            @Override
            public void handleMessage(Message msg) { fragment.show(manager, tag); }
        }.sendEmptyMessage(0);
    }
    
    public static void showDialog(final Dialog dialog, final FragmentManager manager, final String tag) {
        showDialog(dialog, manager, tag, true);
    }

}