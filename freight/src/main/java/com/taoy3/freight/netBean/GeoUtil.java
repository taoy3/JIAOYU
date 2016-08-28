/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.taoy3.freight.netBean;

/**
 * @author Andrew
 */

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Liang Wang
 */
public class GeoUtil {
    private final Gson gson;

    public GeoUtil() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(BigDecimal.class, new BigDecimalGeoDeserializer());
        gsonBuilder.registerTypeAdapter(BigDecimal.class, new BigDecimalGeoSerializer());
        gson = gsonBuilder.create();
    }

    public <T> String serializer(T result) {
        return gson.toJson(result);
    }

    public <T extends Object> T deserializer(String result, Class<T> c) {

        return gson.fromJson(result, c);
    }

    public <T> List<T> deserializerArray(String result, Class<T[]> c) {
        T[] arr = new Gson().fromJson(result, c);
        return Arrays.asList(arr);
    }

    public <T extends Object> T deserializer(String result, Type t) {
        return gson.fromJson(result, t);
    }
}

