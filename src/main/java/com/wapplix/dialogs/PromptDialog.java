package com.wapplix.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

/**
 *
 * @author Michaël André
 */
public class PromptDialog extends DialogFragment implements DialogInterface.OnClickListener {

    public interface Listener {
        public void onValidate(String value);
    }

    private int mTitleId;
    private String mTitle;
    private String mValue;
    private Listener mListener;

    private EditText mEditText;


    public PromptDialog(int titleId, String value, Listener listener) {
        this.mTitleId = titleId;
        this.mValue = value;
        this.mListener = listener;
    }
    public PromptDialog(String title, String value, Listener listener) {
        this.mTitle = title;
        this.mValue = value;
        this.mListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setInverseBackgroundForced(true);
        if (mTitle != null) builder.setTitle(mTitle);
        if (mTitleId > 0) builder.setTitle(mTitleId);
        mEditText = new EditText(getActivity());
        mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
        if (mValue != null) mEditText.setText(mValue);
        builder.setView(mEditText);
        builder.setPositiveButton(android.R.string.ok, this);
        builder.setNegativeButton(android.R.string.cancel, this);
        return builder.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        mEditText.requestFocus();
    }

    public void setValue(String value) {
        this.mValue = value;
        if (mEditText != null) {
            mEditText.setText(mValue);
        }
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (mListener != null) {
            mListener.onValidate(which == DialogInterface.BUTTON_POSITIVE ? mEditText.getText().toString() : null);
        }
        dialog.dismiss();
    }

}
