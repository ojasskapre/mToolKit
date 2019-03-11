package com.example.mtoolkit;

import android.content.IntentFilter;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class SavedWifiActivity extends AppCompatActivity {
    WifiManager mWifiManager;
    List<WifiConfiguration> mScanResultList;
    StringBuilder detail;
    LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_wifi);
        getSavedNetworks();
    }

    private void getSavedNetworks() {
        mWifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        mScanResultList = mWifiManager.getConfiguredNetworks();

        mLinearLayout = findViewById(R.id.savedLinearLayout);
        TextView mNuberWifiText = findViewById(R.id.number_saved_wifi);
        mNuberWifiText.setText("Number of saved networks: " + mScanResultList.size());
        for (int itr = 0; itr < mScanResultList.size(); itr++) {
            detail = new StringBuilder();
            detail.append("\n");
            detail.append("NETWORK ID:\t").append(mScanResultList.get(itr).networkId).append("\n");
            detail.append("SSID:\t").append(mScanResultList.get(itr).SSID).append("\n");
            int status = mScanResultList.get(itr).status;
            if (status == 0){
                detail.append("STATUS:\t").append("CURRENT").append("\n");
            }else if (status == 1) {
                detail.append("STATUS:\t").append("DISABLED").append("\n");
            }else if (status == 2) {
                detail.append("STATUS:\t").append("ENABLED").append("\n");
            }


            TextView mDetailsText = new TextView(getApplicationContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(20, 10, 20, 10);
            mDetailsText.setLayoutParams(params);
            mDetailsText.setTextSize(16);
            mDetailsText.setPadding(20, 0, 0, 0);
            mDetailsText.setBackgroundResource(R.color.colorTextView);
            mDetailsText.setTextColor(Color.WHITE);
            mDetailsText.setText(detail);
            mLinearLayout.addView(mDetailsText);
        }
    }
}

