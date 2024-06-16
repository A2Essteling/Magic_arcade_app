package com.example.magicarcade.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.magicarcade.R;
import com.example.magicarcade.objects.Profile;

public class HomeFragment extends Fragment {

    private TextView pointsTextView;
    private TextView tv1;
    private TextView tv2;
    private TextView tv3;
    private TextView tv4;
    private TextView tv5;
    private TextView tv6;

    public HomeFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        pointsTextView = rootView.findViewById(R.id.textViewPoints);
        setupAttractionListeners(rootView);
        updatePointsText();

         tv1= rootView.findViewById(R.id.attraction1Multiplier);
         tv2= rootView.findViewById(R.id.attraction2Multiplier);
         tv3= rootView.findViewById(R.id.attraction3Multiplier);
         tv4= rootView.findViewById(R.id.attraction4Multiplier);
         tv5= rootView.findViewById(R.id.attraction5Multiplier);
         tv6= rootView.findViewById(R.id.attraction6Multiplier);

         tv1.setText("X" + Math.round(Math.random() * 7+ 1));
         tv2.setText("X" + Math.round(Math.random() * 7+ 1));
         tv3.setText("X" + Math.round(Math.random() * 7+ 1));
         tv4.setText("X" + Math.round(Math.random() * 7+ 1));
         tv5.setText("X" + Math.round(Math.random() * 7+ 1));
         tv6.setText("X" + Math.round(Math.random() * 7+ 1));

        return rootView;
    }

    @SuppressLint("WrongViewCast")
    private void setupAttractionListeners(View rootView) {
        FrameLayout attraction1Layout = rootView.findViewById(R.id.attraction1);
        FrameLayout attraction2Layout = rootView.findViewById(R.id.attraction2);
        FrameLayout attraction3Layout = rootView.findViewById(R.id.attraction3);
        FrameLayout attraction4Layout = rootView.findViewById(R.id.attraction4);
        FrameLayout attraction5Layout = rootView.findViewById(R.id.attraction5);
        FrameLayout attraction6Layout = rootView.findViewById(R.id.attraction6);

        attraction1Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Jonkheer 1897 clicked");
            }
        });

        attraction2Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Astrolica clicked");
            }
        });

        attraction3Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("De Zwevende Belg clicked");
            }
        });

        attraction4Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Droomreis clicked");
            }
        });

        attraction5Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Johan & de Eenhoorn clicked");
            }
        });

        attraction6Layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast("Cobra clicked");
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updatePointsText() {
        if (pointsTextView != null) {
            pointsTextView.setText(getString(R.string.points_on_account) + " " + Profile.getPoints());
        }
    }
}
