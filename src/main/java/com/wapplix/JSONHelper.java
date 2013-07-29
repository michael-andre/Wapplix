package com.wapplix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONHelper {

	public static JSONObject optObject(String string) {
		try {
			return new JSONObject(string);
		} catch (JSONException e) {
			return null;
		}
	}
	
	public static JSONArray optArray(String string) {
		try {
			return new JSONArray(string);
		} catch (JSONException e) {
			return null;
		}
	}
}
