package com.example.magicarcade.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.magicarcade.R;
import com.example.magicarcade.adapters.ViewPagerAdapter;
import com.example.magicarcade.fragments.ConnectFragment;
import com.example.magicarcade.fragments.HomeFragment;
import com.example.magicarcade.fragments.QRFragment;
import com.example.magicarcade.fragments.ScoreboardFragment;
import com.example.magicarcade.fragments.VoucherFragment;
import com.example.magicarcade.mqtt.MqttService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPagerAdapter = new ViewPagerAdapter(this);

        viewPagerAdapter.addFragment(new HomeFragment(), "Home");
        viewPagerAdapter.addFragment(new QRFragment(), "QR");
        viewPagerAdapter.addFragment(new ScoreboardFragment(), "HighScore");
        viewPagerAdapter.addFragment(new VoucherFragment(), "Voucher");
        viewPagerAdapter.addFragment(new ConnectFragment(), "Settings");

        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getPageTitle(position))
        ).attach();

        // Start the MQTT service
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        startService(mqttServiceIntent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Stop the MQTT service when the activity is destroyed
        Intent mqttServiceIntent = new Intent(this, MqttService.class);
        stopService(mqttServiceIntent);
    }
}