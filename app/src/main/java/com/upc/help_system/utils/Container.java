package com.upc.help_system.utils;

/**
 * Created by Administrator on 2017/5/10.
 */

public class Container {
    public static int EXPRESS = 1;
    public static int TAKEFOOD = 2;
    public static int HOMEWORK = 3;
    public static int BUY = 4;
    public static int OTHER = 5;

    public enum volume {
        买卖,租赁
    }

    public enum weight {
        很轻, 一般, 较重, 特别重
    }

    public enum catagory {
        快递, 带饭, 作业帮, 代买, 其他
    }

    public enum state {
        发布未被接收, 发布已经接收, 已经完成
    }
}
