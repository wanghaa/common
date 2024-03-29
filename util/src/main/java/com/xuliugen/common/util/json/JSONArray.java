package com.xuliugen.common.util.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * JSONArray.
 */
public class JSONArray implements JSONNode {

    private List<Object> mArray = new ArrayList<Object>();

    /**
     * get.
     * @param index index.
     * @return boolean or long or double or String or JSONArray or JSONObject or null.
     */
    public Object get(int index) {
        return mArray.get(index);
    }

    /**
     * get boolean value.
     * @param index index.
     * @param def   default value.
     * @return value or default value.
     */
    public boolean getBoolean(int index, boolean def) {
        Object tmp = mArray.get(index);
        return tmp != null && tmp instanceof Boolean ? ((Boolean) tmp).booleanValue() : def;
    }

    /**
     * get int value.
     * @param index index.
     * @param def   default value.
     * @return value or default value.
     */
    public int getInt(int index, int def) {
        Object tmp = mArray.get(index);
        return tmp != null && tmp instanceof Number ? ((Number) tmp).intValue() : def;
    }

    /**
     * get long value.
     * @param index index.
     * @param def   default value.
     * @return value or default value.
     */
    public long getLong(int index, long def) {
        Object tmp = mArray.get(index);
        return tmp != null && tmp instanceof Number ? ((Number) tmp).longValue() : def;
    }

    /**
     * get float value.
     * @param index index.
     * @param def   default value.
     * @return value or default value.
     */
    public float getFloat(int index, float def) {
        Object tmp = mArray.get(index);
        return tmp != null && tmp instanceof Number ? ((Number) tmp).floatValue() : def;
    }

    /**
     * get double value.
     * @param index index.
     * @param def   default value.
     * @return value or default value.
     */
    public double getDouble(int index, double def) {
        Object tmp = mArray.get(index);
        return tmp != null && tmp instanceof Number ? ((Number) tmp).doubleValue() : def;
    }

    /**
     * get string value.
     * @param index index.
     * @return value or default value.
     */
    public String getString(int index) {
        Object tmp = mArray.get(index);
        return tmp == null ? null : tmp.toString();
    }

    /**
     * get JSONArray value.
     * @param index index.
     * @return value or default value.
     */
    public JSONArray getArray(int index) {
        Object tmp = mArray.get(index);
        return tmp == null ? null : tmp instanceof JSONArray ? (JSONArray) tmp : null;
    }

    /**
     * get JSONObject value.
     * @param index index.
     * @return value or default value.
     */
    public JSONObject getObject(int index) {
        Object tmp = mArray.get(index);
        return tmp == null ? null : tmp instanceof JSONObject ? (JSONObject) tmp : null;
    }

    /**
     * get array length.
     * @return length.
     */
    public int length() {
        return mArray.size();
    }

    /**
     * add item.
     */
    public void add(Object ele) {
        mArray.add(ele);
    }

    /**
     * add items.
     */
    public void addAll(Object[] eles) {
        for (Object ele : eles)
            mArray.add(ele);
    }

    /**
     * add items.
     */
    public void addAll(Collection<?> c) {
        mArray.addAll(c);
    }

    /**
     * write json.
     * @param jc json converter
     * @param jb json builder.
     */
    public void writeJSON(JSONConverter jc, JSONWriter jb, boolean writeClass) throws IOException {
        jb.arrayBegin();
        for (Object item : mArray) {
            if (item == null)
                jb.valueNull();
            else
                jc.writeValue(item, jb, writeClass);
        }
        jb.arrayEnd();
    }
}