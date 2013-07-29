package com.wapplix.data.post;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michaël André on 30/05/13.
 */
public class MultipartBodyWriter extends BodyWriter {

    private HashMap<String, BodyWriter> mParts = new HashMap<String, BodyWriter>();
    private String mBoundary;

    public void setPart(String key, BodyWriter part) {
        mParts.put(key, part);
    }

    public void setPart(String key, String value) {
        setPart(key, new StringWriter(value));
    }

    private String getBoundary() {
        if (mBoundary == null) {
            mBoundary = java.util.UUID.randomUUID().toString();
        }
        return mBoundary;
    }

    @Override
    public String getContentType() {
        return "multipart/form-data; charset=utf-8; boundary=" + getBoundary();
    }

    @Override
    public int getContentLength() throws IOException {
        int length = getBoundary().getBytes("UTF-8").length + 6;
        for (Map.Entry<String, BodyWriter> part : mParts.entrySet()) {
            int contentLength = part.getValue().getContentLength();
            if (contentLength == -1) return -1;
            String contentType = part.getValue().getContentType();
            if (contentType != null) {
                length += 34 + contentType.getBytes("UTF-8").length + Integer.toString(contentLength).getBytes("UTF-8").length;
            }
            String fileName = part.getValue().getFilename();
            if (fileName != null) {
                length += 13 + fileName.getBytes("UTF-8").length;
            }
            length += 49 + getBoundary().getBytes("UTF-8").length + part.getKey().getBytes("UTF-8").length + contentLength;
        }
        return length;
    }

    @Override
    public void writeBody(OutputStream outputStream) throws IOException {
        OutputStreamWriter w = new OutputStreamWriter(outputStream, "UTF-8");
        for (Map.Entry<String, BodyWriter> part : mParts.entrySet()) {
            w.write("--" + getBoundary() + "\r\n");
            w.write("Content-Disposition: form-data; name=\"" + part.getKey() + "\"");
            String fileName = part.getValue().getFilename();
            if (fileName != null) {
                w.write("; fileName=\"" + fileName + "\"");
            }
            w.write("\r\n");
            String contentType = part.getValue().getContentType();
            if (contentType != null) {
                w.write("Content-Type: " + contentType + "\r\n");
                int contentLength = part.getValue().getContentLength();
                if (contentLength > -1) {
                    w.write("Content-Length: " + part.getValue().getContentLength() + "\r\n");
                }
            }
            w.write("\r\n");
            w.flush();
            part.getValue().writeBody(outputStream);
            w.write("\r\n");
        }
        w.write("--" + getBoundary() + "--\r\n");
        w.flush();
    }
}