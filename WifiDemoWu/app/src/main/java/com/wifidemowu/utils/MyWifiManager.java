package com.wifidemowu.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.util.List;

/**
 * Name: 吴庆森
 * Date: 2019/9/5
 * Mailbox: 1243411677@qq.com
 * Describe:WIFI管理
 */
public class MyWifiManager {


    /**
     * 开始扫描wifi
     */
    public static void startScanWifi(WifiManager manager) {
        if (manager != null) {
            manager.startScan();
        }
    }


    /**
     * 获取wifi列表
     */
    public static List<ScanResult> getWifiList(WifiManager mWifiManager) {
        return mWifiManager.getScanResults();
    }


    /**
     * 保存网络
     */
    public static void saveNetworkByConfig(WifiManager manager, WifiConfiguration config) {
        if (manager == null) {
            return;
        }
        try {
            Method save = manager.getClass().getDeclaredMethod("save", WifiConfiguration.class, Class.forName("android.net.wifi.WifiManager$ActionListener"));
            if (save != null) {
                save.setAccessible(true);
                save.invoke(manager, config, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 忘记网络
     */
    public static void forgetNetwork(WifiManager manager, int networkId) {
        if (manager == null) {
            return;
        }
        try {
            Method forget = manager.getClass().getDeclaredMethod("forget", int.class, Class.forName("android.net.wifi.WifiManager$ActionListener"));
            if (forget != null) {
                forget.setAccessible(true);
                forget.invoke(manager, networkId, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 断开连接
     */
    public static boolean disconnectNetwork(WifiManager manager) {
        return manager != null && manager.disconnect();
    }


    /**
     * 获取当前wifi名字
     * @return
     */
    public static String getWiFiName(WifiManager manager) {
        WifiInfo wifiInfo = manager.getConnectionInfo();
        return wifiInfo.getSSID();
    }

    /**
     * 获取wifi加密方式
     */
    public static String getEncrypt(WifiManager mWifiManager, ScanResult scanResult) {
        if (mWifiManager != null) {
            String capabilities = scanResult.capabilities;
            if (!TextUtils.isEmpty(capabilities)) {
                if (capabilities.contains("WPA") || capabilities.contains("wpa")) {
                    return "WPA";
                } else if (capabilities.contains("WEP") || capabilities.contains("wep")) {
                    return "WEP";
                } else {
                    return "没密码";
                }
            }
        }
        return "获取失败";
    }

    /**
     * 是否开启wifi，没有的话打开wifi
     */
    public static boolean openWifi(WifiManager mWifiManager) {
        boolean bRet = true;
        if (!mWifiManager.isWifiEnabled()) {
            bRet = mWifiManager.setWifiEnabled(true);
        }
        return bRet;
    }


    @SuppressLint("WifiManagerLeak")
    public static void connectWifi(WifiManager wifiManager, String wifiName, String password, String type) {
        // 1、注意热点和密码均包含引号，此处需要需要转义引号
        String ssid = "\"" + wifiName + "\"";
        String psd = "\"" + password + "\"";

        //2、配置wifi信息
        WifiConfiguration conf = new WifiConfiguration();
        conf.SSID = ssid;
        switch (type) {
            case "WEP":
                // 加密类型为WEP
                conf.wepKeys[0] = psd;
                conf.wepTxKeyIndex = 0;
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                conf.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
                break;
            case "WPA":
                // 加密类型为WPA
                conf.preSharedKey = psd;
                break;
            default:
                //无密码
                conf.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
        }
        //3、链接wifi
        wifiManager.addNetwork(conf);
        List<WifiConfiguration> list = wifiManager.getConfiguredNetworks();
        for (WifiConfiguration i : list) {
            if (i.SSID != null && i.SSID.equals(ssid)) {
                wifiManager.disconnect();
                wifiManager.enableNetwork(i.networkId, true);
                wifiManager.reconnect();
                break;
            }
        }
    }
}
