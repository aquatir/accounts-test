package com.revolute.test.example.util;

import com.google.gson.Gson;
import com.revolute.test.example.dto.TransferRequest;
import spark.Request;

public class JsonMapper {
    private final Gson gson = new Gson();

    public String toJson(Object obj) {
        return gson.toJson(obj);
    }

    public <T> T toObject(String json, Class<T> objClass) {
        return gson.fromJson(json, objClass);
    }

}
