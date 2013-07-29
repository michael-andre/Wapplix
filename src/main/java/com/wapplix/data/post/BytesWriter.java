package com.wapplix.data.post;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Michaël André on 30/05/13.
 */
public abstract class BytesWriter extends BodyWriter {

    private byte[] mBytes;

    private byte[] getBytes() throws IOException {
        if (mBytes == null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            writeBufferedBody(stream);
            mBytes = stream.toByteArray();
        }
        return mBytes;
    }

    @Override
    public final int getContentLength() throws IOException {
        return getBytes().length;
    }

    @Override
    public final void writeBody(OutputStream outputStream) throws IOException {
        outputStream.write(getBytes());
    }

    public abstract  void writeBufferedBody(OutputStream outputStream) throws IOException;

}