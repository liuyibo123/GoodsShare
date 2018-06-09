package com.upc.help_system.utils;

public class Converter {
    private static String[] trade_type = {"租","买"};
    private  static String[] goods_type = {"食物","日用品","学习用品","电子产品","体育器材","其他"};
    public static String convertTradeType(int a){
        return trade_type[a-1];
    }
    public static String convertGoodsType(int a){
        return goods_type[a-1];
    }
}
