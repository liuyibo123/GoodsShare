package com.upc.help_system.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.upc.help_system.MyApplication;


/**
 * Created by Administrator on 2017/6/18.
 */

public class SharedPreferenceUtil {

    static SharedPreferences sharedPreferences ;
    public static String getString(String spname,String key){
        sharedPreferences = MyApplication.getContext().getSharedPreferences(spname, Context.MODE_PRIVATE);
        String value = sharedPreferences.getString(key,"");
        return value;
    }
    public static void setString(String spname,String key,String value){
        sharedPreferences = MyApplication.getContext().getSharedPreferences(spname, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    public static int getInt(String spname,String key){
        sharedPreferences = MyApplication.getContext().getSharedPreferences(spname, Context.MODE_PRIVATE);
        int value = sharedPreferences.getInt(key,0);
        return value;
    }

    public static void clearString(String spname){
        sharedPreferences = MyApplication.getContext().getSharedPreferences(spname, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
