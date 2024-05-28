package com.example.magicarcade;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.example.magicarcade.fragments.ConnectFragment;
import com.example.magicarcade.fragments.HomeFragment;
import com.example.magicarcade.fragments.QRFragment;
import com.example.magicarcade.fragments.ScoreboardFragment;
import com.example.magicarcade.fragments.VoucherFragment;
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

        setupAttractionListeners();

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
    }


    @SuppressLint("WrongViewCast")
    private void setupAttractionListeners() {
        FrameLayout attraction1Layout = findViewById(R.id.attraction1);
        FrameLayout attraction2Layout = findViewById(R.id.attraction2);
        FrameLayout attraction3Layout = findViewById(R.id.attraction3);
        FrameLayout attraction4Layout = findViewById(R.id.attraction4);
        FrameLayout attraction5Layout = findViewById(R.id.attraction5);
        FrameLayout attraction6Layout = findViewById(R.id.attraction6);

        attraction1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 1 clicked");
            }
        });

        attraction2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 2 clicked");
            }
        });

        attraction3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 3 clicked");
            }
        });

        attraction4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 4 clicked");
            }
        });

        attraction5Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 5 clicked");
            }
        });

        attraction6Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Attraction 6 clicked");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}