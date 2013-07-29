package com.wapplix.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

/**
 * Created by Michaël André
 */
public class ConfirmDialogFragment extends DialogFragment implements DialogInterface.OnClickListener {

    public static interface Listener {
        void onConfirm(boolean confirm);
    }

    public static ConfirmDialogFragment show(FragmentManager fragmentManager, int titleId, int messageId, Listener listener, String tag) {
        ConfirmDialogFragment dialog = new ConfirmDialogFragment(titleId, messageId, listener);
        dialog.show(fragmentManager, tag);
        return dialog;
    }

    public static ConfirmDialogFragment show(FragmentManager fragmentManager, String title, String message, Listener listener, String tag) {
        ConfirmDialogFragment dialog = new ConfirmDialogFragment(title, message, listener);
        dialog.show(fragmentManager, tag);
        return dialog;
    }

    private int mTitleId;
    private String mTitle;
    private int mMessageId;
    private String mMessage;
    private Listener mListener;

    public ConfirmDialogFragment(int titleId, int messageId, Listener listener) {
        this.mTitleId = titleId;
        this.mMessageId = messageId;
        this.mListener = listener;
    }
    public ConfirmDialogFragment(String title, String message, Listener listener) {
        this.mTitle = title;
        this.mMessage = message;
        this.mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (mTitle != null) builder.setTitle(mTitle);
        if (mTitleId > 0) builder.setTitle(mTitleId);
        if (mMessage != null) builder.setMessage(mMessage);
        if (mMessageId > 0) builder.setMessage(mMessageId);
        builder.setPositiveButton(android.R.string.ok, this);
        builder.setNegativeButton(android.R.string.cancel, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mListener != null) {
            mListener.onConfirm(which == DialogInterface.BUTTON_POSITIVE);
        }
        dialog.dismiss();
    }
}
