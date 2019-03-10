package com.example.mtoolkit;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MyWifiActivity extends AppCompatActivity {
    WifiManager mWifiManager;
    WifiInfo mWifiInfo;
    TextView mMyWifiDetailsText;
    TextView mMyWifiNameText;
    TextView mMyWifiDHCPText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wifi);
        getMyWifiDetails();
    }

    @SuppressLint({"HardwareIds", "SetTextI18n"})
    private void getMyWifiDetails() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        mWifiInfo = mWifiManager.getConnectionInfo();

        mMyWifiNameText = findViewById(R.id.my_wifi_name);
        String wifiName = mWifiInfo.getSSID();
        wifiName = wifiName.substring(1, wifiName.length() - 1);

        mMyWifiNameText.setText("Connected to :\t" + wifiName.toUpperCase());

        StringBuilder detail = new StringBuilder();
        detail.append("\nSSID:\t").append(wifiName);
        if (mWifiInfo.getBSSID() == null) {
            detail.append("\nBSSID:\t").append(mWifiInfo.getBSSID());
        }else {
            detail.append("\nBSSID:\t").append(mWifiInfo.getBSSID().toUpperCase());
        }

        int ip = mWifiInfo.getIpAddress();
        String result = convertIPformat(ip);

        detail.append("\nIP ADDRESS:\t").append(result);
        detail.append("\nMAC ADDRESS:\t").append(mWifiInfo.getMacAddress().toUpperCase());
        detail.append("\nLINK SPEED:\t").append(mWifiInfo.getLinkSpeed());
        detail.append("\nFREQUENCY:\t").append(mWifiInfo.getFrequency());
        int level = WifiManager.calculateSignalLevel(mWifiInfo.getRssi(), 5);
        detail.append("\nSIGNAL STRENGTH:\t").append(level * 20).append("\n");


        mMyWifiDetailsText = findViewById(R.id.my_wifi_details);
        mMyWifiDetailsText.setText(detail);

        DhcpInfo info = mWifiManager.getDhcpInfo();

        StringBuilder dhcp_info = new StringBuilder();
        dhcp_info.append("\nNETMASK:\t").append(info.netmask);

        ip = info.gateway;
        result = convertIPformat(ip);
        dhcp_info.append("\nGATEWAY:\t").append(result);

        ip = info.dns1;
        result = convertIPformat(ip);
        dhcp_info.append("\nDNS(1) ADDRESS:\t").append(result);

        ip = info.dns2;
        result = convertIPformat(ip);
        dhcp_info.append("\nDNS(2) ADDRESS:\t").append(result);

        ip = info.serverAddress;
        result = convertIPformat(ip);
        dhcp_info.append("\nSERVER ADDRESS:\t").append(result);
        dhcp_info.append("\nLEASE DURATION:\t").append(info.leaseDuration);
        dhcp_info.append("\nNETWORK ID:\t").append(mWifiInfo.getNetworkId());
        dhcp_info.append("\nNETWORK TYPE:\t").append(getNetworkClass(getApplicationContext())).append("\n");

        mMyWifiDHCPText = findViewById(R.id.my_wifi_dhcp_details);
        mMyWifiDHCPText.setText(dhcp_info);
    }

    private String convertIPformat(int ip) {
        @SuppressLint("DefaultLocale") String result = String.format("%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
        return result;
    }

    public static String getNetworkClass(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if(info==null || !info.isConnected())
            return "-"; //not connected
        if(info.getType() == ConnectivityManager.TYPE_WIFI)
            return "WIFI";
        if(info.getType() == ConnectivityManager.TYPE_MOBILE){
            int networkType = info.getSubtype();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                    return "2G";
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                case TelephonyManager.NETWORK_TYPE_TD_SCDMA:  //api<25 : replace by 17
                    return "3G";
                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                case TelephonyManager.NETWORK_TYPE_IWLAN:  //api<25 : replace by 18
                case 19:  //LTE_CA
                    return "4G";
                default:
                    return "?";
            }
        }
        return "?";
    }
}
