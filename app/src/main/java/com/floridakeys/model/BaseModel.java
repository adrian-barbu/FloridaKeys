package com.floridakeys.model;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @description Base Data Model
 *
 * @author      Adrian
 */
public class BaseModel {
    /**
     * Get Boolean Value
     *
     * @param object
     * @param key
     * @return
     */
    protected boolean getBooleanValue(JSONObject object, String key) {
        try {
            return object.getBoolean(key);
        } catch (JSONException e) {
            return false;
        }
    }

    /**
     * Get String Value
     *
     * @param object
     * @param key
     * @return
     */
    protected String getStringValue(JSONObject object, String key) {
        try {
            return object.getString(key);
        } catch (JSONException e) {
            return "";
        }
    }

    /**
     * Get Double Value
     *
     * @param object
     * @param key
     * @return
     */
    protected double getDoubleValue(JSONObject object, String key) {
        try {
            return object.getDouble(key);
        } catch (JSONException e) {
            return 0.0;
        }
    }

    /**
     * Get Int Value
     *
     * @param object
     * @param key
     * @return
     */
    protected int getIntValue(JSONObject object, String key) {
        try {
            return object.getInt(key);
        } catch (JSONException e) {
            return 0;
        }
    }

    public boolean doParseJson(JSONObject jsonObject) {
        return false;
    }

    protected void onDestroy() {
        System.gc();
    }
}
