package com.example.magicarcade.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.magicarcade.R;
import com.example.magicarcade.adapters.ViewPagerAdapter;
import com.example.magicarcade.fragments.HomeFragment;
import com.example.magicarcade.fragments.QRFragment;
import com.example.magicarcade.fragments.ScoreboardFragment;
import com.example.magicarcade.fragments.VoucherFragment;
import com.example.magicarcade.mqtt.MqttService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private MqttService mqttService;
    private boolean isBound = false;
    private ViewPager2 viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter viewPagerAdapter;
    private ScoreboardFragment scoreboardFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);
        viewPagerAdapter = new ViewPagerAdapter(this);

        viewPagerAdapter.addFragment(new HomeFragment(), "Home");
        viewPagerAdapter.addFragment(new QRFragment(), "QR");
        scoreboardFragment = new ScoreboardFragment();
        viewPagerAdapter.addFragment(scoreboardFragment, "HighScore");
        viewPagerAdapter.addFragment(new VoucherFragment(), "Voucher");
//        viewPagerAdapter.addFragment(new ConnectFragment(), "Settings");

        viewPager.setAdapter(viewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(viewPagerAdapter.getPageTitle(position))
        ).attach();

        bindMqttService();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttService.onDestroy();
        if (isBound) {
            unbindService(serviceConnection);
            isBound = false;
        }
    }

    private void bindMqttService() {
        Intent intent = new Intent(this, MqttService.class);
        bindService(intent, serviceConnection, BIND_AUTO_CREATE);
    }

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "Service connected");
            MqttService.LocalBinder binder = (MqttService.LocalBinder) service;
            mqttService = binder.getService();
            isBound = true;

            // Set the handler for score updates
            if (scoreboardFragment != null) {
                binder.setScoreUpdateHandler(scoreboardFragment.getScoreUpdateHandler());
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "Service disconnected");
            mqttService = null;
            isBound = false;
        }
    };
}
