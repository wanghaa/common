package com.xuliugen.common.util.serializer;

import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.ObjectSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by yyang on 14-9-16.
 */
public class GsonSerializer implements ObjectSerializer {

    private Gson gson;

    public GsonSerializer() {
        this.gson = new GsonBuilder().create();
    }

    public GsonSerializer(Gson gson) {
        this.gson = gson;
    }

    public String serialize(Object anObject) {
        return gson.toJson(anObject);
    }

    public <T> T deserialize(String serializedString, Class<T> objectClass) {
        return gson.fromJson(serializedString, objectClass);
    }

    public void write(JSONSerializer serializer, Object object, Object fieldName, Type fieldType, int features) throws IOException {

    }
}
