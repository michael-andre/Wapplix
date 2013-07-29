package com.wapplix;

import java.util.List;

/**
 *
 * @author Michaël André
 */
public class StringHelper {
    
    public static String ucFirst(String string) {
        if (string == null)
            return null;
        else if (string.length() > 0)
            return string.substring(0, 1).toUpperCase() + string.substring(1);
        else
            return "";
    }
    
    public static String join(String glue, String... parts) {
        if (parts.length == 0)
            return "";
        StringBuilder out = new StringBuilder();
        out.append(parts[0]);
        for (int i = 1; i < parts.length; i++)
            if (parts[i] != null && parts[i].length() > 0)
                out.append(glue).append(parts[i]);
        return out.toString();
    }
    
    public static String join(String glue, List<String> parts) {
        return join(glue, parts.toArray(new String[parts.size()]));
    }
}