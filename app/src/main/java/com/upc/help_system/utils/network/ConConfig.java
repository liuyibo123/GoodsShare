package com.upc.help_system.utils.network;

import com.upc.help_system.utils.SharedPreferenceUtil;

/**
 * Created by Administrator on 2017/5/22.
 */

public class ConConfig {

    public static String ip ;
    public static String url;
    public static void setIp(String ip1){
        ip = ip1;
        url = "http://"+ip;
    }
    public ConConfig(){

    }
    //public static String url = "http://180.201.158.155";
    //  public static String url = "http://172.25.208.125";
}
