package com.example.mtoolkit;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MyWifiActivity extends AppCompatActivity {
    WifiManager mWifiManager;
    WifiInfo mWifiInfo;
    TextView mMyWifiDetailsText, mMyWifiNameText, mMyWifiDHCPText;
    Button mRefreshButton, mSaveButton, mCopyButton;
    LineChart mLineChart;
    ArrayList<Entry> dataVals;
    LineDataSet lineDataSet;
    ArrayList<ILineDataSet> dataSets;
    int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wifi);
        mRefreshButton = findViewById(R.id.refresh);
        mSaveButton = findViewById(R.id.save);
        mCopyButton = findViewById(R.id.copy);
        mLineChart = findViewById(R.id.lineChart);
        dataVals = new ArrayList<Entry>();
        time = 0;

        getDetails();
        final Handler handler1 = new Handler();
        final Runnable r1 = new Runnable() {
            @Override
            public void run() {
                getDetails();
                handler1.postDelayed(this, 5000);
            }
        };
        handler1.postDelayed(r1, 5000);

        mRefreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDetails();
            }
        });

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nearby_network_details;
                Date currentTime = Calendar.getInstance().getTime();
                nearby_network_details = currentTime.toString();

                mMyWifiNameText = findViewById(R.id.my_wifi_name);
                nearby_network_details += "\n\n" + mMyWifiNameText.getText().toString();

                mMyWifiDetailsText = findViewById(R.id.my_wifi_details);
                nearby_network_details += "\n\nWIFI DETAILS:\n" + mMyWifiDetailsText.getText().toString();

                mMyWifiDHCPText = findViewById(R.id.my_wifi_dhcp_details);
                nearby_network_details += "\n\nDHCP INFORMATION:\n" + mMyWifiDHCPText.getText().toString();

                File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "mToolKit");
                dir.mkdirs();
                try {
                    SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy::hh:mm-a");
                    String formattedDate = df.format(currentTime);
                    String filename = "MyWifi-" + formattedDate + ".txt";
                    Log.d("dsfsgfdsfjkkj", filename);
                    File myFile = new File(dir, filename);
                    PrintWriter pw = new PrintWriter(myFile);
                    pw.print(nearby_network_details);
                    Toast.makeText(MyWifiActivity.this, "File Saved!!!", Toast.LENGTH_SHORT).show();
                    pw.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        mCopyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCopyButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                        String my_network_details;
                        Date currentTime = Calendar.getInstance().getTime();
                        my_network_details = currentTime.toString();

                        mMyWifiNameText = findViewById(R.id.my_wifi_name);
                        my_network_details += "\n\n" + mMyWifiNameText.getText().toString();

                        mMyWifiDetailsText = findViewById(R.id.my_wifi_details);
                        my_network_details += "\n\nWIFI DETAILS:\n" + mMyWifiDetailsText.getText().toString();

                        mMyWifiDHCPText = findViewById(R.id.my_wifi_dhcp_details);
                        my_network_details += "\n\nDHCP INFORMATION:\n" + mMyWifiDHCPText.getText().toString();

                        ClipData clipData = ClipData.newPlainText("My WIFI", my_network_details);
                        clipboardManager.setPrimaryClip(clipData);
                        Toast.makeText(MyWifiActivity.this, "Copied!!!", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void getDetails() {
        if (getNetworkClass(getApplicationContext()).equals("WIFI")) {
            getMyWifiDetails();
        }else if(getNetworkClass(getApplicationContext()).equals("-")) {
            notConnected();
        }else{
            mobileNetworkConnected();
        }
    }

    private void notConnected() {
        mMyWifiNameText = findViewById(R.id.my_wifi_name);
        mMyWifiNameText.setText("Not Connected");

        mMyWifiDetailsText = findViewById(R.id.my_wifi_details);
        mMyWifiDetailsText.setText("N.A.");

        mMyWifiDHCPText = findViewById(R.id.my_wifi_dhcp_details);
        mMyWifiDHCPText.setText("N.A.");
    }

    private void mobileNetworkConnected() {
        mMyWifiNameText = findViewById(R.id.my_wifi_name);
        mMyWifiNameText.setText("Connected to mobile data");

        mMyWifiDetailsText = findViewById(R.id.my_wifi_details);
        mMyWifiDetailsText.setText(getNetworkClass(getApplicationContext()));

        mMyWifiDHCPText = findViewById(R.id.my_wifi_dhcp_details);
        mMyWifiDHCPText.setText("N.A.");
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
        int level = WifiManager.calculateSignalLevel(mWifiInfo.getRssi(), 100);
        detail.append("\nSIGNAL STRENGTH:\t").append(level + 1).append(" %   (").append(mWifiInfo.getRssi()).append(" dBm )\n");


        mMyWifiDetailsText = findViewById(R.id.my_wifi_details);
        mMyWifiDetailsText.setText(detail);

        // adding data to graph
        dataVals.add(new Entry(time, mWifiInfo.getRssi()));
        time += 5;
        lineDataSet = new LineDataSet(dataVals, "Wifi RSSI level");
        lineDataSet.setColor(Color.RED);
        dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData data = new LineData(dataSets);
        mLineChart.setData(data);
        mLineChart.getXAxis().setTextColor(Color.WHITE);
        mLineChart.getXAxis().setAxisLineColor(Color.WHITE);
        mLineChart.getAxisLeft().setAxisLineColor(Color.WHITE);
        mLineChart.getAxisLeft().setTextColor(Color.WHITE);
        mLineChart.getAxisRight().setAxisLineColor(Color.WHITE);
        mLineChart.getAxisRight().setTextColor(Color.WHITE);
        mLineChart.getLegend().setTextColor(Color.WHITE);
        mLineChart.invalidate();

        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "mToolKit");
        dir.mkdirs();
        try {
            Date currentTime = Calendar.getInstance().getTime();
            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yy::hh:mm:ss-a");
            String formattedDate = df.format(currentTime);
            File myFile = new File(dir, "SignalStrengthLogs.txt");
            if (myFile.length() < 102400) {
                FileWriter fw = new FileWriter(myFile, true);
                BufferedWriter bw = new BufferedWriter(fw);
                PrintWriter pw = new PrintWriter(bw);
                String printString = mWifiInfo.getSSID() + "\t\t" + mWifiInfo.getRssi() + "\t\t" + formattedDate + "\n";
                pw.print(printString);
                pw.close();
            }else{
                PrintWriter pw = new PrintWriter(myFile);
                pw.print("");
                pw.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                    return "4G LTE";
                default:
                    return "?";
            }
        }
        return "?";
    }
}
