package com.wapplix;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StreamHelper {

    public static String readStringContent(InputStream stream) throws IOException {
        BufferedReader rd = new BufferedReader(new InputStreamReader(stream), 8192);
        String line;
        StringBuilder sb = new StringBuilder();
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        return sb.toString();
    }

}