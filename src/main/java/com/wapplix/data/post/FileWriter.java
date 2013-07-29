package com.wapplix.data.post;

import android.webkit.MimeTypeMap;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Michaël André on 30/05/13.
 */
public class FileWriter extends BodyWriter {

    private File mFile;

    public FileWriter(File file) {
        this.mFile = file;
    }

    public void setFile(File file) {
        this.mFile = file;
    }

    @Override
    public String getContentType() {
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(mFile.getAbsolutePath()));
    }

    @Override
    public int getContentLength() throws IOException {
        return (int) mFile.length();
    }

    @Override
    public String getFilename() {
        return mFile.getName();
    }

    public static final int BUFFER_SIZE = 1024 * 2;

    @Override
    public void writeBody(OutputStream outputStream) throws IOException {
        new FileInputStream(mFile);
        byte[] buffer = new byte[BUFFER_SIZE];

        BufferedInputStream in = new BufferedInputStream(new FileInputStream(mFile), BUFFER_SIZE);
        BufferedOutputStream out = new BufferedOutputStream(outputStream, BUFFER_SIZE);
        int n = 0;
        try {
            while ((n = in.read(buffer, 0, BUFFER_SIZE)) != -1) {
                out.write(buffer, 0, n);
            }
            out.flush();
        } finally {
            in.close();
        }
    }
}
