package com.example.mtoolkit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Button mMyWifi;
    Button mNetworks;
    Button mSavedWifi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMyWifi = findViewById(R.id.my_wifi);
        mNetworks = findViewById(R.id.networks);
        mSavedWifi = findViewById(R.id.saved_wifi);

        mMyWifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, MyWifiActivity.class);
                startActivity(intent);
            }
        });

        mNetworks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(MainActivity.this, AllWifiActivity.class);
                startActivity(intent);
            }
        });
    }
}
