package com.wapplix.data.post;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Michaël André on 30/05/13.
 */
public abstract class BodyWriter {

    public String getContentType() {
        return null;
    }

    public int getContentLength() throws IOException {
        return -1;
    }

    public String getFilename() {
        return null;
    }

    public abstract void writeBody(OutputStream outputStream) throws IOException;

}