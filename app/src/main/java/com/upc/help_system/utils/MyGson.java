package com.upc.help_system.utils;

import com.google.gson.Gson;

/**
 * Created by Liuyibo on 2017/3/30.
 * 对gson基于项目进行简单封装
 * <p>
 * toJson 把对象转换成json字符串
 * fromJson 把字符串转换成指定类型的对象
 *
 *
 */
public class MyGson {
    private static Gson gson = null;

    //单例模式
    private static Gson getGson() {
        if (gson == null) {
            gson = new Gson();
        }
        return gson;
    }

    public static <T> String toJson(T t) {
        return getGson().toJson(t);
    }

    public static <T> T fromJson(String json, Class<T> t) {
        return getGson().fromJson(json, t);
    }
}
