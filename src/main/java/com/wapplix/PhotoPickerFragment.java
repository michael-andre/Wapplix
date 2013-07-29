package com.wapplix;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Michaël André on 05/06/13.
 */
public class PhotoPickerFragment extends Fragment {

    public static interface PhotoPickerListener {
        public void onPhotoPick(Uri photoUri);
    }

    private static final int REQUEST_CODE = 65535;

    public void show(FragmentManager manager, String tag, String chooserTitle, String captureAlbum, PhotoPickerListener listener) {
        mChooserTitle = chooserTitle;
        mCaptureAlbum = captureAlbum;
        mListener = listener;
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commit();
    }

    private String mChooserTitle;
    private String mCaptureAlbum;
    private PhotoPickerListener mListener;

    private boolean mStarted = false;
    private Uri mCaptureUri;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCaptureUri = savedInstanceState.getParcelable("captureUri");
            mStarted = savedInstanceState.getBoolean("started");
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        if (mStarted) return;

        Intent pickerIntent = new Intent(Intent.ACTION_PICK);
        pickerIntent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(pickerIntent, mChooserTitle);

        if (!TextUtils.isEmpty(mCaptureAlbum)) {
            Intent captureIntent = createTakePhotoIntent();
            if (captureIntent != null) {
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { captureIntent } );
            }
        }

        startActivityForResult(chooserIntent, REQUEST_CODE);
        mStarted = true;
    }

    private Intent createTakePhotoIntent() {
        // Check that capture intent is supported
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (getActivity().getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).isEmpty()) {
            return null;
        }

        // Check that external storage is available
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            return null;
        }

        // Create temporary capture file
        try {
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), mCaptureAlbum);
            String timeStamp = new SimpleDateFormat("yyyy-MM-dd", Locale.US).format(new Date());
            String imageFileName = timeStamp + "-";
            storageDir.mkdirs();
            mCaptureUri = Uri.fromFile(File.createTempFile(imageFileName, ".jpg", storageDir));
        } catch (IOException e) {
            return null;
        }

        // Create intent
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mCaptureUri);
        return intent;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putParcelable("captureUri", mCaptureUri);
        outState.putBoolean("started", mStarted);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    Uri intentUri = intent.getData();
                    if (intentUri != null) {
                        sendResult(intentUri);
                        return;
                    }
                }
                if (mCaptureUri != null) {
                    File file = new File(mCaptureUri.getPath());
                    if (file.exists()) {
                        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        mediaScanIntent.setData(mCaptureUri);
                        getActivity().sendBroadcast(mediaScanIntent);
                        sendResult(mCaptureUri);
                        return;
                    }
                }
            } else if (mCaptureUri != null) {
                File file = new File(mCaptureUri.getPath());
                file.delete();
            }
            sendResult(null);
            return;
        }
        super.onActivityResult(requestCode, resultCode, intent);
    }

    private void sendResult(Uri imageUri) {
        if (mListener != null) {
            mListener.onPhotoPick(imageUri);
        }
        getFragmentManager().beginTransaction().remove(this).commit();
    }

}
