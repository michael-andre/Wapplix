package com.wapplix.data.post;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Michaël André on 30/05/13.
 */
public class UrlEncodedBodyWriter extends BodyWriter {

    private Map<String, String> mValues = new HashMap<String, String>();

    public UrlEncodedBodyWriter(Map<String, String> values) {
        this.mValues = values;
    }

    public final void setValue(String key, String value) {
        mValues.put(key, value);
        mBody = null;
    }

    public String getContentType() {
        return "application/x-www-form-urlencoded";
    }

    private String mBody = null;

    private String getBody() throws UnsupportedEncodingException {
        if (mBody == null) {
            StringBuilder b = new StringBuilder();
            for (Map.Entry<String, String> entry : mValues.entrySet()) {
                b.append("&").append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                b.append("=").append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            b.deleteCharAt(0);
            mBody = b.toString();
        }
        return mBody;
    }

    public int getContentLength() throws UnsupportedEncodingException {
        return getBody().getBytes("UTF-8").length;
    }

    public void writeBody(OutputStream outputStream) throws IOException {
        OutputStreamWriter w = new OutputStreamWriter(outputStream, "UTF-8");
        w.write(getBody());
        w.flush();
    }

}
