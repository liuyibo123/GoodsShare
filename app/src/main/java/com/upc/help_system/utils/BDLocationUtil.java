package com.upc.help_system.utils;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.upc.help_system.MyApplication;
import com.upc.help_system.view.activity.MainActivity;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Administrator on 2017/5/15.
 */

public class BDLocationUtil {

    public BDLocationUtil() {

    }

    public static void getCurrentLoc() {
        LocationClient mLocationClient = new LocationClient(MyApplication.getContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        int span = 5000;
        option.setScanSpan(span);
        option.setOpenGps(true);
        option.setIsNeedAltitude(true);
        option.setCoorType("bd0911");
        option.setIsNeedAddress(true);
        option.setLocationNotify(true);
        option.setIsNeedLocationDescribe(true);
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                MyLoction myLoction = new MyLoction();
                myLoction.setTime(bdLocation.getTime());
                myLoction.setLatitude(bdLocation.getLatitude());
                myLoction.setLongitude(bdLocation.getLongitude());

                myLoction.setAddr(bdLocation.getAddrStr());
                if (bdLocation.getAddrStr() == null || bdLocation.getAddrStr().equals("")) {
                    myLoction.setAddr("未获取");
                    Toast.makeText(MyApplication.getContext(), "请检查网络", Toast.LENGTH_SHORT).show();
                }
                String myloc = MyGson.toJson(myLoction);
                String bdloc = MyGson.toJson(bdLocation);
                Log.d("MainActivity", "bdloction:" + bdloc);
                SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("location", MODE_PRIVATE).edit();
                editor.putString("location", myloc);
                editor.commit();
                Log.d("MainActivity", "获取位置信息为：" + myloc);
                mLocationClient.stop();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });
        mLocationClient.start();
    }

}
