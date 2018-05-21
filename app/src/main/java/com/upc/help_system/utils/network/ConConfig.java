package com.upc.help_system.utils.network;

import com.upc.help_system.utils.SharedPreferenceUtil;

/**
 * Created by Administrator on 2017/5/22.
 */

public class ConConfig {

    public static String ip ;
    public static String url;
    public ConConfig(){
        ip =  SharedPreferenceUtil.getString("network","ip");
        if(ip==null||ip.equals("")){
            url="http://180.201.175.73";
        }else{
            url="http://"+ip;
        }
    }
    //public static String url = "http://180.201.158.155";
    //  public static String url = "http://172.25.208.125";
}
