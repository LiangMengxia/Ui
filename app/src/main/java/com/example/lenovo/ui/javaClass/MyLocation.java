package com.example.lenovo.ui.javaClass;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClientOption;
import com.example.lenovo.ui.activity.activity.CameraActivity;

/**
 * Created by lenovo on 2017/5/25.
 */
public class MyLocation {
    public static String message="正在定位";
    //获取定位信息
    public static void getLocationDetails() {
        //注册位置监听器
        CameraActivity.locationClient.registerLocationListener(new BDLocationListener() {
            @Override
            public void onReceiveLocation(BDLocation bdLocation) {
                if (bdLocation == null) {
                    return;
                }
                StringBuilder sb = new StringBuilder(256);
                sb.append("定位时间 : ");
                sb.append(bdLocation.getTime());    //获取定位时间
                if (bdLocation.getLocType() == BDLocation.TypeGpsLocation) {

                    // GPS定位结果
                    sb.append("\n地址信息 : ");
                    sb.append(bdLocation.getAddrStr());    //获取地址信息

                    sb.append("\n定位类型 : ");
                    sb.append("gps定位成功");

                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkLocation) {

                    // 网络定位结果
                    sb.append("\n地址信息 : ");
                    sb.append(bdLocation.getAddrStr());    //获取地址信息

                    sb.append("\n定位类型 : ");
                    sb.append("网络定位成功");

                } else if (bdLocation.getLocType() == BDLocation.TypeOffLineLocation) {

                    // 离线定位结果
                    sb.append("\n定位类型 : ");
                    sb.append("离线定位成功，离线定位结果也是有效的");

                } else if (bdLocation.getLocType() == BDLocation.TypeServerError) {

                    sb.append("\n定位类型 : ");
                    sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

                } else if (bdLocation.getLocType() == BDLocation.TypeNetWorkException) {

                    sb.append("\n定位类型 : ");
                    sb.append("网络不同导致定位失败，请检查网络是否通畅");

                } else if (bdLocation.getLocType() == BDLocation.TypeCriteriaException) {

                    sb.append("\n定位类型 : ");
                    sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

                }
                sb.append("\n定位描述 : ");
                sb.append(bdLocation.getLocationDescribe());    //位置语义化信息
                message = sb.toString();
            }

            @Override
            public void onConnectHotSpotMessage(String s, int i) {

            }
        });

         /*第二步：
          * 设置定位SDK参数
          * */
        initLocation();
       /* 第三步：
       * 开始定位，超过1000ms后，定位SDK内部使用定时定位模式。
       * 调用requestLocation( )后，每隔设定的时间1000ms，定位SDK就会进行一次定位。
       * */
        CameraActivity.locationClient.start();
        CameraActivity.locationClient.requestLocation();
    }

    //设置定位SDK参数
    private static void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll");
        //可选，默认gcj02，设置返回的定位结果坐标系

        int span = 1000;
        option.setScanSpan(span);
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true);
        //可选，设置是否需要地址信息，默认不需要

        option.setOpenGps(true);
        //可选，默认false,设置是否使用gps

        option.setLocationNotify(true);
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true);
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true);
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false);
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.SetIgnoreCacheException(false);
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false);
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

        CameraActivity.locationClient.setLocOption(option);
    }

}
