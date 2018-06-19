package com.beyondphysics.network.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by xihuan22 on 2017/10/14.
 */
public class JsonTool {

    public static boolean getBooleanFromJsonObject(JSONObject jsonObject, String key) {
        boolean result = false;
        if (jsonObject == null || key == null) {
            return result;
        }
        try {
            result = jsonObject.getBoolean(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }


    public static int getIntFromJsonObject(JSONObject jsonObject, String key) {
        int result = 0;
        if (jsonObject == null || key == null) {
            return result;
        }
        try {
            result = jsonObject.getInt(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static long getLongFromJsonObject(JSONObject jsonObject, String key) {
        long result = 0;
        if (jsonObject == null || key == null) {
            return result;
        }
        try {
            result = jsonObject.getLong(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static double getDoubleFromJsonObject(JSONObject jsonObject, String key) {
        double result = 0.0;
        if (jsonObject == null || key == null) {
            return result;
        }
        try {
            result = jsonObject.getDouble(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String getStringFromJsonObject(JSONObject jsonObject, String key) {
        String result = null;
        if (jsonObject == null || key == null) {
            return result;
        }
        try {
            result = jsonObject.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

}
