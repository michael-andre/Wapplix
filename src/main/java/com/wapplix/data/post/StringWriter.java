package com.wapplix.data.post;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Michaël André on 30/05/13.
 */
public class StringWriter extends BodyWriter {

    private String mValue;

    public StringWriter(String value) {
        this.mValue = value;
    }

    public void setValue(String value) {
        this.mValue = value;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public int getContentLength() throws IOException {
        return mValue.getBytes("UTF-8").length;
    }

    @Override
    public void writeBody(OutputStream outputStream) throws IOException {
        outputStream.write(mValue.getBytes("UTF-8"));
    }
}
