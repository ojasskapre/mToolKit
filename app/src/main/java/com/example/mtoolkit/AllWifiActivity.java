package com.example.mtoolkit;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class AllWifiActivity extends AppCompatActivity {
    WifiManager mWifiManager;
    List<ScanResult> mScanResultList;
    StringBuilder detail;
    LinearLayout mLinearLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_wifi);
        getAllWifiResult();
    }

    private void getAllWifiResult() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        mLinearLayout = findViewById(R.id.linearLayout);
        mScanResultList = mWifiManager.getScanResults();
        TextView mNuberWifiText = findViewById(R.id.number_wifi);
        mNuberWifiText.setText("Number of networks available: " + mScanResultList.size());
        for (int itr = 0; itr < mScanResultList.size(); itr++) {
            detail = new StringBuilder();
            detail.append("\n");
            detail.append("SSID:\t").append(mScanResultList.get(itr).SSID).append("\n");
            detail.append("BSSID:\t").append(mScanResultList.get(itr).BSSID).append("\n");
            detail.append("CAPABILITIES:\t").append(mScanResultList.get(itr).capabilities).append("\n");
            int level = WifiManager.calculateSignalLevel(mScanResultList.get(itr).level, 100);
            detail.append("SIGNAL LEVEL:\t").append(level).append(" %    (").append(mScanResultList.get(itr).level).append(" dBm )\n");
            detail.append("FREQUENCY:\t").append(mScanResultList.get(itr).frequency).append("\n");
            detail.append("DESCRIBE CONTENTS:\t").append(mScanResultList.get(itr).describeContents()).append("\n");

            TextView mDetailsText = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 10, 20, 10);
            mDetailsText.setLayoutParams(params);
            mDetailsText.setTextSize(16);
            mDetailsText.setPadding(20, 0, 0, 0);
            mDetailsText.setBackgroundResource(R.color.coloTextView);
            mDetailsText.setText(detail);
            mLinearLayout.addView(mDetailsText);
        }
    }
}
