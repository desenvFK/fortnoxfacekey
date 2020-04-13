package com.anyvision.facekeyexample.utils;

import com.google.gson.Gson;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by igal on 30/11/2017.
 */

public class SerializationUtils {
    public static String toJson(Object object){
        Gson gson =  new Gson();
        return gson.toJson(object);
    }

    public static <T> T fromJson(String json, Class<T> classOfT){
        Gson gson =  new Gson();
        return gson.fromJson(json,classOfT);
    }

    public static <T> T clone(T object,Class<T> classOfT){
        return fromJson(toJson(object),classOfT);
    }

    public static <T> List<T> toList(String json, Class<T> classOfT) {
        T[] arrayResult = (T[]) fromJson(json, Array.newInstance(classOfT, 0).getClass());
        return new LinkedList<>(Arrays.asList(arrayResult));
    }
}
